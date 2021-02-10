package com.example.somiti.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somiti.AccountDetailsActivity;
import com.example.somiti.MainActivity;
import com.example.somiti.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainAdapter  extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private ProgressDialog progressDialog;

    private  Activity activity;
    private  Context context;
    private List<Users> dataList=new ArrayList<>();

    public MainAdapter(Activity activity,Context context, List<Users> dataList) {
        this.activity=activity;
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item_layout, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Users data=dataList.get(position);




        if (data.getName().length() > 14)
        {
            String sname = data.getName().substring(0, 14);
            holder.nameTextview.setText((position+1)+". "+sname+"...");

        }else{
            holder.nameTextview.setText((position+1)+". "+data.getName());
        }




            holder.paidButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showcustomdioloage(data.getName(),data.getId());
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, AccountDetailsActivity.class);
                    intent.putExtra("id",data.getId());
                    intent.putExtra("name",data.getName());
                    context.startActivity(intent);
                }
            });



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder  extends  RecyclerView.ViewHolder{

        TextView nameTextview;
         Button paidButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            nameTextview=itemView.findViewById(R.id.userlist_nameTextViewid);
            paidButton=itemView.findViewById(R.id.userList_PaidButtonid);




        }
    }




    public void showcustomdioloage(final String name, final String id) {


        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");





        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view=activity.getLayoutInflater().inflate(R.layout.paid_custom_diolouge,null);
        builder.setView(view);
        final EditText   amountEdittext=view.findViewById(R.id.diolouge_AmountEdittextid);
        TextView titleTextview=view.findViewById(R.id.paiddiolougeTextview);
        Button paidButton=view.findViewById(R.id.diolougePaidButtonid);

        final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        final String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        titleTextview.setText("Name: "+name+"  \n"+currentDate+" At  "+currentTime);





        final AlertDialog dialog=builder.create();
        dialog.show();
        paidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String amount=amountEdittext.getText().toString();

                if(amount.isEmpty()){
                    Toast.makeText(context, "Please Enter Some Amount", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                 final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("assosiation").child("Account");
                 final DatabaseReference totalAmountRef= FirebaseDatabase.getInstance().getReference().child("assosiation").child("TotalAmount");


                    HashMap<String, Object> userMap=new HashMap<>();


                    String currentid=databaseReference.push().getKey();
                    userMap.put("name",name);
                    userMap.put("amount",amount);
                    userMap.put("date",currentDate);
                    userMap.put("time",currentTime);
                    userMap.put("mid",id);
                    userMap.put("id",currentid);



                 databaseReference.child(id).child(currentid).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                totalAmountRef.child(id).child("TotalAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                          if(snapshot.exists()){

                                                    String totalAmount=snapshot.child("totalAmount").getValue().toString();

                                                    int ptA=Integer.parseInt(totalAmount);
                                                    int cpA=Integer.parseInt(amount);

                                                    int total=ptA+cpA;

                                                final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                                HashMap<String, Object> hashMap=new HashMap<>();
                                                    hashMap.put("totalAmount",Integer.toString(total));
                                                    hashMap.put("updatedTime",currentDate);




                                              totalAmountRef.child(id).child("TotalAmount")
                                                      .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(context, "Successfully Paid", Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                            dialog.dismiss();

                                                        }
                                                    }
                                                });







                                           }else{



                                                final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                                HashMap<String, Object> hashMap=new HashMap<>();
                                                hashMap.put("totalAmount",amount);
                                                hashMap.put("updatedTime",currentDate);




                                              totalAmountRef.child(id).child("TotalAmount")
                                                        .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            progressDialog.dismiss();
                                                            dialog.dismiss();
                                                            Toast.makeText(context, "Successfully Paid", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });








                                            }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                     }
                 });






                }



            }
        });









    }






}

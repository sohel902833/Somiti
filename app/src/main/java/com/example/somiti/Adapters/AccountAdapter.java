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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.somiti.AccountDetailsActivity;
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

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    private ProgressDialog progressDialog;

    private  Activity activity;
    private  Context context;
    private List<AccountDetails> dataList=new ArrayList<>();

    public AccountAdapter(Activity activity, Context context, List<AccountDetails> dataList) {
        this.activity=activity;
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_list_item_layout, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final AccountDetails data=dataList.get(position);

            holder.textview.setText((position+1)+". Date : "+data.getDate()+"    Time : "+data.getTime()+"    Paid = "+data.getAmount()+"tk.");

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    showcustomdioloage(data.getDate(),data.getAmount(),data.getMid(),data.getId());


                    return false;
                }
            });




    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder  extends  RecyclerView.ViewHolder{

        TextView textview;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            textview=itemView.findViewById(R.id.paymentList_Textviewid);




        }
    }




    public void showcustomdioloage(String date,final String currentAmount, final String id,final String currentId) {

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("assosiation").child("Account");
        final DatabaseReference totalAmountRef= FirebaseDatabase.getInstance().getReference().child("assosiation").child("TotalAmount");



        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Updating");





        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view=activity.getLayoutInflater().inflate(R.layout.paid_custom_diolouge,null);
        builder.setView(view);
        final EditText   amountEdittext=view.findViewById(R.id.diolouge_AmountEdittextid);
        TextView titleTextview=view.findViewById(R.id.paiddiolougeTextview);
        Button paidButton=view.findViewById(R.id.diolougePaidButtonid);

         titleTextview.setText("Date: "+date+"Update: ");
         paidButton.setText("Update");
        amountEdittext.setText(currentAmount);





        final AlertDialog dialog=builder.create();
        dialog.show();
        paidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String amount=amountEdittext.getText().toString();

                if(amount.isEmpty()){
                    Toast.makeText(context, "Please Enter Some Amount", Toast.LENGTH_SHORT).show();
                }else{

                    final int newAmount=Integer.parseInt(amount);
                    final int pastAmount=Integer.parseInt(currentAmount);

                    if(newAmount==pastAmount){
                        Toast.makeText(context, "Update Not Needed", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    }
                   else /*if(newAmount>pastAmount){*/
                       progressDialog.show();

                        totalAmountRef.child(id).child("TotalAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){

                                    String tAmount=snapshot.child("totalAmount").getValue().toString();

                                    int totalAmount=Integer.parseInt(tAmount);
                                    int newTotalAmount=(totalAmount-pastAmount)+newAmount;

                                    totalAmountRef.child(id).child("TotalAmount")
                                            .child("totalAmount").setValue(newTotalAmount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                databaseReference.child(id).child(currentId).child("amount").setValue(newAmount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            progressDialog.dismiss();
                                                            dialog.dismiss();
                                                            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }) ;

                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                   /* }else{

                        progressDialog.show();

                        totalAmountRef.child(id).child("TotalAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){

                                    String tAmount=snapshot.child("totalAmount").getValue().toString();

                                    int totalAmount=Integer.parseInt(tAmount);
                                    int newTotalAmount=(totalAmount-pastAmount)+newAmount;

                                    totalAmountRef.child(id).child("TotalAmount")
                                            .child("totalAmount").setValue(newTotalAmount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                databaseReference.child(id).child(currentId).child("amount").setValue(newAmount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            progressDialog.dismiss();
                                                            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }) ;

                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }*/












                }












            }
        });









    }






}

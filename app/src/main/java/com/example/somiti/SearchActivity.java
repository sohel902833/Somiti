package com.example.somiti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.somiti.Adapters.MainAdapter;
import com.example.somiti.Adapters.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private EditText searchET;
    private  String str="";


    private DatabaseReference databaseReference;

    private  ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().hide();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("assosiation").child("AllMember");
        databaseReference.keepSynced(true);

        searchET=findViewById(R.id.searchEdittextid);
        recyclerView=findViewById(R.id.search_Recyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(searchET.getText().toString().equals("")){
                    Toast.makeText(SearchActivity.this, "Write Name For search", Toast.LENGTH_SHORT).show();
                }else{
                    str= s.toString();
                    onStart();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Users> options=null;
        if(str.equals("")){
            options=new FirebaseRecyclerOptions.Builder<Users>()
                    .setQuery(databaseReference,Users.class)
                    .build();
        }else{
            options=new FirebaseRecyclerOptions.Builder<Users>()
                    .setQuery(databaseReference.orderByChild("name")
                            .startAt(str)
                            .endAt(str+"\uf8ff"),Users.class)
                    .build();
        }


        FirebaseRecyclerAdapter<Users,MyViewHolder> adapter=new FirebaseRecyclerAdapter<Users, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final Users dataList) {




                if (dataList.getName().length() > 14)
                {
                    String sname = dataList.getName().substring(0, 14);
                    holder.nameTextview.setText(sname+"...");

                }else{
                    holder.nameTextview.setText(dataList.getName());
                }





                holder.paidButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showcustomdioloage(dataList.getName(),dataList.getId());
                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(SearchActivity.this, AccountDetailsActivity.class);
                        intent.putExtra("id",dataList.getId());
                        intent.putExtra("name",dataList.getName());
                        startActivity(intent);
                    }
                });




            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item_layout, parent, false);


                return new MyViewHolder(view);



            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();











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


        progressDialog=new ProgressDialog(SearchActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();




        AlertDialog.Builder builder=new AlertDialog.Builder(SearchActivity.this);
        View view=getLayoutInflater().inflate(R.layout.paid_custom_diolouge,null);
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
                    Toast.makeText(SearchActivity.this, "Please Enter Some Amount", Toast.LENGTH_SHORT).show();
                }else{

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
                                                        Toast.makeText(SearchActivity.this, "Successfully Paid", Toast.LENGTH_SHORT).show();
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
                                                        Toast.makeText(SearchActivity.this, "Successfully Paid", Toast.LENGTH_SHORT).show();
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
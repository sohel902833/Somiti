package com.example.somiti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.somiti.Adapters.AccountAdapter;
import com.example.somiti.Adapters.AccountDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountDetailsActivity extends AppCompatActivity {


    String id,name;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private List<AccountDetails> dataList=new ArrayList<>();
    private AccountAdapter adapter;
    String totalAmount;

    private TextView textView;
    int loopcounter=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        getSupportActionBar().hide();

        id=getIntent().getStringExtra("id");
        name=getIntent().getStringExtra("name");


        databaseReference= FirebaseDatabase.getInstance().getReference().child("assosiation").child("Account");


        recyclerView=findViewById(R.id.accountDetailsRecyclerviewid);
        textView=findViewById(R.id.accountDetails_Textviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        adapter=new AccountAdapter(AccountDetailsActivity.this,this,dataList);
        recyclerView.setAdapter(adapter);














    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.keepSynced(true);
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        loopcounter++;
                            String amount=snapshot1.child("amount").getValue().toString();
                            String date=snapshot1.child("date").getValue().toString();
                            String id=snapshot1.child("id").getValue().toString();
                            String mid=snapshot1.child("mid").getValue().toString();
                            String name2=snapshot1.child("name").getValue().toString();
                            String time=snapshot1.child("time").getValue().toString();

                        AccountDetails details=new AccountDetails(amount,date,id,mid,name2,time);
                          dataList.add(details);
                            adapter.notifyDataSetChanged();







                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //getTotal amount

        DatabaseReference totalAmountRef= FirebaseDatabase.getInstance().getReference().child("assosiation").child("TotalAmount");

        totalAmountRef.child(id).child("TotalAmount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    totalAmount=snapshot.child("totalAmount").getValue().toString();
                    if (name.length() > 14)
                    {
                        String sname = name.substring(0, 14);
                        textView.setText(sname+".. Balance: ("+totalAmount+"tk)\n"+"Total Installment: "+loopcounter);

                    }else{
                        textView.setText(name+".. Balance: ("+totalAmount+"tk)\n"+"Total Installment: "+loopcounter);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
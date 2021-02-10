package com.example.somiti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AbouteActivity extends AppCompatActivity {
    private TextView totalBalance,totalMembers;
    private DatabaseReference databaseReference;
    private DatabaseReference totalAmountRef;
    int totalMember=0;
    int totalAmount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboute);
        this.setTitle("About Association");


        databaseReference= FirebaseDatabase.getInstance().getReference().child("assosiation").child("AllMember");
        totalAmountRef= FirebaseDatabase.getInstance().getReference().child("assosiation").child("TotalAmount");




        totalBalance=findViewById(R.id.aboute_TotalBalanceTextviewid);
        totalMembers=findViewById(R.id.aboute_TotalMembersTextviewid);










    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        totalMember++;
                    }


                    totalMembers.setText("Total Member : "+totalMember);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        totalAmountRef.keepSynced(true);
        totalAmountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){

                    String oneMemberBalance=snapshot1.child("TotalAmount").child("totalAmount").getValue().toString();
                    int onb=Integer.parseInt(oneMemberBalance);
                    totalAmount=totalAmount+onb;



                }


                totalBalance.setText("Main Balance: "+totalAmount+"tk");




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










    }














}
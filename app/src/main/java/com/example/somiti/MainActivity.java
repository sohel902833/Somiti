package com.example.somiti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.somiti.Adapters.MainAdapter;
import com.example.somiti.Adapters.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class MainActivity extends AppCompatActivity {




    private  RecyclerView recyclerView;
    private  FirebaseAuth mAuth;


    private MainAdapter adapter;
    List<Users> datalist=new ArrayList<>();


    private  DatabaseReference databaseReference;
    private  ProgressDialog progressDialog;




    //for customdiolouge
    private EditText memberNameEdittext;
    private  EditText memberPhoneEdittext;
    private Button memberAddButton;
    private String name,phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.setTitle("Members");

        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("assosiation").child("AllMember");

        recyclerView=findViewById(R.id.mainRecyclerViewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new MainAdapter(MainActivity.this,this,datalist);

         recyclerView.setAdapter(adapter);

    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser==null){
            startActivity(new Intent(MainActivity.this,StartActivity.class));
        }


        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                            datalist.clear();
                            for (DataSnapshot snapshot1: snapshot.getChildren()){


                                String name=snapshot1.child("name").getValue().toString();
                                String date=snapshot1.child("date").getValue().toString();
                                String time=snapshot1.child("time").getValue().toString();
                                String id=snapshot1.child("id").getValue().toString();
                                String phone=snapshot1.child("phone").getValue().toString();


                                Users users=new Users(name,phone,date,time,id);
                                datalist.add(users);
                                adapter.notifyDataSetChanged();
                            }


                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.main_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.addnewMembermenuButtonid){

            showcustomdioloage();

        }  else   if(item.getItemId()==R.id.searchMenuButtonid){

          startActivity(new Intent(MainActivity.this,SearchActivity.class));

        }else   if(item.getItemId()==R.id.logout){

        mAuth.signOut();
        startActivity(new Intent(MainActivity.this,StartActivity.class));

        }
    else   if(item.getItemId()==R.id.aboutebuttonid){

        startActivity(new Intent(MainActivity.this,AbouteActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }






    public void showcustomdioloage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_member_custom_diolouge,null);
        builder.setView(view);
        memberNameEdittext=view.findViewById(R.id.diolouge_MembernameEdittextid);
        memberPhoneEdittext=view.findViewById(R.id.diolouge_MemberPhoneEdittextid);
        memberAddButton=view.findViewById(R.id.memberSaveButtonid);

        final AlertDialog dialog=builder.create();
        dialog.show();
        memberAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=memberNameEdittext.getText().toString();
                phone=memberPhoneEdittext.getText().toString();


                if(name.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter  Member Name", Toast.LENGTH_SHORT).show();
                }else{
                       saveMemberData(name,phone,dialog);
                }
            }
        });









    }

    private void saveMemberData(String name, String phone, final AlertDialog dialog) {

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String memberKey=databaseReference.push().getKey();


        HashMap <String, Object>   userMap=new HashMap<>();


        userMap.put("name",name);
        userMap.put("phone",phone);
        userMap.put("date",currentDate);
        userMap.put("time",currentTime);
        userMap.put("id",memberKey);


        databaseReference.child(memberKey).setValue(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Member Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                    }
                });













    }


















}
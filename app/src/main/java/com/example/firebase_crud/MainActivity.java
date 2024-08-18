package com.example.firebase_crud;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView rv;
    ArrayList<UsersItem> array;
    Button btn;
    UsersRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        Objects.requireNonNull(getSupportActionBar()).hide();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        array = new ArrayList<>();

        btn = findViewById(R.id.btnadd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                viewDialogAdd.showDialog(MainActivity.this);

            }
        });
        readData();

    }

    private void readData() {
        databaseReference.child("USERS").orderByChild("userName").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                array.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    UsersItem usersItem = dataSnapshot.getValue(UsersItem.class);
                    array.add(usersItem);
                }
                adapter = new UsersRecyclerAdapter(MainActivity.this,array);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ViewDialogAdd{
        public void showDialog(Context context){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);


            EditText txt1,txt2,txt3;
            Button btn1, btn2;
            txt1 = dialog.findViewById(R.id.txtname);
            txt2 = dialog.findViewById(R.id.txtemail);
            txt3 = dialog.findViewById(R.id.txtcountry);
            btn1 = dialog.findViewById(R.id.btnadd);
            btn2 = dialog.findViewById(R.id.btnexit);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = "user" + new Date().getTime();
                    String name = txt1.getText().toString();
                    String email = txt2.getText().toString();
                    String country = txt3.getText().toString();

                    if(name.isEmpty() || email.isEmpty() || country.isEmpty()){
                        Toast.makeText(context, "Please Enter All data ...", Toast.LENGTH_SHORT).show();
                    }else{
                        databaseReference.child("USERS").child(id).setValue(new UsersItem(id,name,email,country));
                        Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


        }
    }
}
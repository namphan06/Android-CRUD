package com.example.firebase_crud;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<UsersItem> usersItemArrayList;
    DatabaseReference databaseReference;


    public UsersRecyclerAdapter(Context context, ArrayList<UsersItem> usersItemArrayList) {
        this.context = context;
        this.usersItemArrayList = usersItemArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater flater = LayoutInflater.from(context);
        View view = flater.inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsersItem users = usersItemArrayList.get(position);

        holder.txt1.setText("Name : " + users.getUserName());
        holder.txt2.setText("Email : " + users.getUserEmail());
        holder.txt3.setText("Country : " + users.getUserCountry());

        holder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, users.getUserID(), users.getUserName(), users.getUserEmail(), users.getUserCountry());
            }
        });

        holder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(context, users.getUserID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersItemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt1,txt2,txt3;
        Button btn1,btn2;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            txt1 = itemView.findViewById(R.id.txtname);
            txt2 = itemView.findViewById(R.id.txtemail);
            txt3 = itemView.findViewById(R.id.txtcountry);
            btn1 = itemView.findViewById(R.id.btndelete);
            btn2 = itemView.findViewById(R.id.btnupdate);

        }
    }
    public class ViewDialogUpdate {
        public void showDialog(Context context, String id, String name, String email, String country) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            EditText textName = dialog.findViewById(R.id.txtname);
            EditText textEmail = dialog.findViewById(R.id.txtemail);
            EditText textCountry = dialog.findViewById(R.id.txtcountry);

            textName.setText(name);
            textEmail.setText(email);
            textCountry.setText(country);


            Button buttonUpdate = dialog.findViewById(R.id.btnadd);
            Button buttonCancel = dialog.findViewById(R.id.btnexit);

            buttonUpdate.setText("UPDATE");

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String newName = textName.getText().toString();
                    String newEmail = textEmail.getText().toString();
                    String newCountry = textCountry.getText().toString();

                    if (name.isEmpty() || email.isEmpty() || country.isEmpty()) {
                        Toast.makeText(context, "Please Enter All data...", Toast.LENGTH_SHORT).show();
                    } else {

                        if (newName.equals(name) && newEmail.equals(email) && newCountry.equals(country)) {
                            Toast.makeText(context, "you don't change anything", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child("USERS").child(id).setValue(new UsersItem(id, newName, newEmail, newCountry));
                            Toast.makeText(context, "User Updated successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }


                    }
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }


    public class ViewDialogConfirmDelete {
        public void showDialog(Context context, String id) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.view_dialog_delete);

            Button buttonDelete = dialog.findViewById(R.id.buttonDelete);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseReference.child("USERS").child(id).removeValue();
                    Toast.makeText(context, "User Deleted successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
         }
        }
}


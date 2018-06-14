package com.tongminhnhut.luanvan.DAL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tongminhnhut.luanvan.BLL.MD5;
import com.tongminhnhut.luanvan.Model.User;

import dmax.dialog.SpotsDialog;

public class SignUp_DAL {
    static DatabaseReference db_User;
    public static void signUp (final SpotsDialog dialog, final Context context, final EditText phone, final EditText pass, final EditText name, final Intent intent){
        db_User = FirebaseDatabase.getInstance().getReference("User");
        dialog.show();
        db_User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phone.getText().toString().trim()).exists()){
                    dialog.dismiss();
                    Toast.makeText(context, "Tài khoản đã tồn tại !", Toast.LENGTH_SHORT).show();
                }else {
                    dialog.dismiss();
                    User user = new User(name.getText().toString().trim(), MD5.md5(pass.getText().toString().trim()));
                    db_User.child(phone.getText().toString().trim()).setValue(user);
                    intent.putExtra("Sign", phone.getText().toString());
                    intent.putExtra("SignIn", pass.getText().toString());
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Toast.makeText(context, "Đăng ký thành công !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

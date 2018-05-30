package com.tongminhnhut.luanvan.DAL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tongminhnhut.luanvan.BLL.MD5;
import com.tongminhnhut.luanvan.HomeActivity;
import com.tongminhnhut.luanvan.Model.User;
import com.tongminhnhut.luanvan.SignInActivity;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class SignIn_DAL {
    public static User curentUser ;
    static DatabaseReference db_User = FirebaseDatabase.getInstance().getReference("User");


    public static void signIn(final AlertDialog dialog, final Context context, final String phone, final String pass, final Intent intent){
        dialog.show();
        db_User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phone).exists()){
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);
                    if (user.getPassword().equals(MD5.md5(pass))){
                        dialog.dismiss();
//                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        curentUser = user;
                        context.startActivity(intent);
                        Toast.makeText(context, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(context, "Sai tên đăng nhập hoặc mật khẩu !", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Tài khoản không tồn tại !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void signUp (final AlertDialog dialog, final Context context, final String phone, final String pass, final String name, final Intent intent){
        dialog.show();
        db_User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phone).exists()){
                    dialog.dismiss();
                    Toast.makeText(context, "Tài khoản đã tồn tại !", Toast.LENGTH_SHORT).show();
                }else {
                    dialog.dismiss();
                    User user = new User(name, MD5.md5(pass));
                    db_User.child(phone).setValue(user);
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

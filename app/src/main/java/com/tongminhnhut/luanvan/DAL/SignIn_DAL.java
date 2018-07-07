package com.tongminhnhut.luanvan.DAL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    static DatabaseReference db_User = FirebaseDatabase.getInstance().getReference("User");


    public static void signIn(final AlertDialog dialog, final Context context, final String phone, final String pass, final Intent intent, final Intent main){
//        dialog.show();
        db_User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phone).exists()){
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);
                    if (user.getPassword().equals(MD5.md5(pass))){
                        dialog.dismiss();
//                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        curentUser = user;
                        context.startActivity(intent);
                        Toast.makeText(context, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                    } else {
                        main.addFlags(main.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(main);
                        Toast.makeText(context, "Sai tên đăng nhập hoặc mật khẩu !", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "Tài khoản không tồn tại !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void updateHomeAddress(final Context context){
        db_User.child(curentUser.getPhone())
                .setValue(curentUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}

package com.tongminhnhut.luanvan;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tongminhnhut.luanvan.DAL.SignIn_DAL;
import com.tongminhnhut.luanvan.Model.User;

import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;

public class SignInActivity extends AppCompatActivity {
    FirebaseDatabase mData ;
    DatabaseReference db_user ;
    FButton btnSignIn;
    EditText edtPhone, edtPass ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mData = FirebaseDatabase.getInstance();
        db_user = mData.getReference("User");

        initView();
        addEvents();

        Intent intent = getIntent() ;
        String phone = intent.getStringExtra("Sign");
        String pass = intent.getStringExtra("SignIn");
        edtPhone.setText(phone);
        edtPass.setText(pass);

    }

    private void addEvents() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString();
                String pass = edtPass.getText().toString();
                if (phone.isEmpty()&&pass.isEmpty()){
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập đầy đủ !", Toast.LENGTH_SHORT).show();
                } else {
                    login(edtPhone.getText().toString().trim(), edtPass.getText().toString().trim());
                }
            }
        });
    }

    private void initView() {
        btnSignIn = findViewById(R.id.btnSignIn_SignIn);
        edtPhone = findViewById(R.id.edtPhonenumber_SignIp);
        edtPass = findViewById(R.id.edtPass_SignIp);
    }

    private void login(final String phone, final String pass) {
        final AlertDialog dialog = new SpotsDialog(SignInActivity.this, "Loading . . .");

        if (SignIn_DAL.isConnectedInternet(getApplicationContext())){
            dialog.show();
            db_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phone).exists()){
                        dialog.dismiss();
                        User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                        user.setPhone(edtPhone.getText().toString());
                        if (user.getPassword().equals(pass)){
                            Toast.makeText(SignInActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                        }else {
                            dialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Sai ten dang nhap hoac mat khau", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(SignInActivity.this, "Tai khoan khong ton tai", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(this, "Vui long kiem tra ket noi", Toast.LENGTH_SHORT).show();
        }
    }
}

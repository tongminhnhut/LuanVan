package com.tongminhnhut.luanvan;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tongminhnhut.luanvan.Model.User;

import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;

public class SignUpActivity extends AppCompatActivity {
    DatabaseReference db_user ;
    EditText edtName, edtPhone, edtPass;
    FButton btnSignUp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        db_user = FirebaseDatabase.getInstance().getReference("User");

        initView();
        addEvents();
    }

    private void addEvents() {
        final AlertDialog dialog = new SpotsDialog(SignUpActivity.this, "Loading . . .");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = edtPhone.getText().toString().trim();
                final String name = edtName.getText().toString().trim();
                final String pass = edtPass.getText().toString().trim();
                dialog.show();
                db_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(phone).exists()){
                            dialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Tài khoản đã tồn tại !", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dialog.dismiss();
                            User user = new User(name,pass);
                            db_user.child(phone).setValue(user);
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            intent.putExtra("Sign", edtPhone.getText().toString());
                            intent.putExtra("SignIn", edtPass.getText().toString());
                            startActivity(intent);
                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void initView() {
        edtName = findViewById(R.id.edtFullName_SignUp);
        edtPass = findViewById(R.id.edtPass_SignUp);
        edtPhone = findViewById(R.id.edtPhonenumber_SignUp);
        btnSignUp = findViewById(R.id.btnSignUp_SignUp);
    }
}

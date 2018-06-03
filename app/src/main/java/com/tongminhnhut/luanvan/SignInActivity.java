package com.tongminhnhut.luanvan;

import android.app.AlertDialog;
import android.content.Context;
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
import com.tongminhnhut.luanvan.BLL.CheckConnection;
import com.tongminhnhut.luanvan.BLL.MD5;
import com.tongminhnhut.luanvan.DAL.SignIn_DAL;
import com.tongminhnhut.luanvan.Model.User;

import java.util.List;

import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignInActivity extends AppCompatActivity {
    FirebaseDatabase mData ;
    DatabaseReference db_user ;
    FButton btnSignIn;
    EditText edtPhone, edtPass ;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set Default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("VNFFutura.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_sign_in);

        Paper.init(this);

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
                final AlertDialog dialog = new SpotsDialog(SignInActivity.this, "Loading . . .");
                String pass = edtPass.getText().toString();
                if (phone.isEmpty()&&pass.isEmpty()){
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập đầy đủ !", Toast.LENGTH_SHORT).show();
                } else {
                    login(dialog,edtPhone.getText().toString().trim(), edtPass.getText().toString().trim());
                    finish();
                }
            }
        });
    }

    private void initView() {
        btnSignIn = findViewById(R.id.btnSignIn_SignIn);
        edtPhone = findViewById(R.id.edtPhonenumber_SignIp);
        edtPass = findViewById(R.id.edtPass_SignIp);
    }

    private void login(AlertDialog dialog, final String phone, final String pass) {
        if (CheckConnection.isConnectedInternet(getApplicationContext())){
            Intent intent1 = new Intent(getApplicationContext(), HomeActivity.class);
            SignIn_DAL.signIn(dialog,getApplicationContext(),phone, pass,intent1 );
            finish();
        }else {
            Toast.makeText(SignInActivity.this, "Kiem tra ket noi !", Toast.LENGTH_SHORT).show();
        }
    }


}

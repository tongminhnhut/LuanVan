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
import com.tongminhnhut.luanvan.DAL.SignUp_DAL;
import com.tongminhnhut.luanvan.Model.User;

import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {
    DatabaseReference db_user ;
    EditText edtName, edtPhone, edtPass;
    FButton btnSignUp ;

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

        setContentView(R.layout.activity_sign_up);

        db_user = FirebaseDatabase.getInstance().getReference("User");

        initView();
        addEvents();
    }

    private void addEvents() {
        final SpotsDialog dialog = new SpotsDialog(SignUpActivity.this, "Loading . . .");
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckConnection.isConnectedInternet(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    SignUp_DAL.signUp(dialog,getApplicationContext(),edtPhone, edtPass,edtName,intent);
                }else {
                    Toast.makeText(SignUpActivity.this, "Vui lòng kiểm tra kết nối !", Toast.LENGTH_SHORT).show();
                }
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

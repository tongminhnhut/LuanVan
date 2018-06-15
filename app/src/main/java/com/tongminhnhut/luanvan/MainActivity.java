package com.tongminhnhut.luanvan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tongminhnhut.luanvan.DAL.SignIn_DAL;

import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    FButton btnSignIn, btnSignUp ;

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

        setContentView(R.layout.activity_main);


        Paper.init(this);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });

        String user = Paper.book().read(SignIn_DAL.USER_KEY);
        String pwd = Paper.book().read(SignIn_DAL.PWD_KEY);
        if (user !=null && pwd != null){
            if (!user.isEmpty() & !pwd.isEmpty()){
                final SpotsDialog dialog = new SpotsDialog(MainActivity.this, "Loading . . .");
                Intent intent1 = new Intent(getApplicationContext(), HomeActivity.class);
                Intent main = new Intent(getApplicationContext(), SignInActivity.class);
                SignIn_DAL.signIn(dialog,getApplicationContext(),user, pwd   ,intent1 , main);
            }
        }

    }
}

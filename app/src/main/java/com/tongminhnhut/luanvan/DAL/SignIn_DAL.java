package com.tongminhnhut.luanvan.DAL;

import android.app.AlertDialog;
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
import com.tongminhnhut.luanvan.Model.User;
import com.tongminhnhut.luanvan.SignInActivity;

import dmax.dialog.SpotsDialog;

public class SignIn_DAL {
    public static User curentUser ;
    public static boolean isConnectedInternet (Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager !=null){
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo() ;
            if (infos !=null){
                for (int i = 0; i<infos.length;i++)
                {
                    if (infos[i].getState()== NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false ;
    }

}

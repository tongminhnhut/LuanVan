package com.tongminhnhut.luanvan;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.luanvan.BLL.CheckConnection;
import com.tongminhnhut.luanvan.DAL.LoadListDongHo;
import com.tongminhnhut.luanvan.DAL.LoadMenuDAL;
import com.tongminhnhut.luanvan.Model.DongHo;
import com.tongminhnhut.luanvan.ViewHolder.DongHoViewHolder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DongHoActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference db_DongHo ;
    String menuId ="";
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseRecyclerAdapter<DongHo, DongHoViewHolder> adapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set Default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fs.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.activity_dong_ho);

        db_DongHo = FirebaseDatabase.getInstance().getReference("DongHo");

        recyclerView = findViewById(R.id.recylerView_DongHo);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //swipeRefresh
        swipeRefreshLayout = findViewById(R.id.swipeRefresh_DongHo);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent() != null)
                    menuId = getIntent().getStringExtra("ID");
                if (!menuId.isEmpty() && menuId!=null){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    LoadListDongHo.loadDongHo(menuId, recyclerView, getApplicationContext(),intent, swipeRefreshLayout);
                }
            }
        });


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (getIntent() != null)
                    menuId = getIntent().getStringExtra("ID");
                if (!menuId.isEmpty() && menuId != null) {
                    if (CheckConnection.isConnectedInternet(getBaseContext())) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        LoadListDongHo.loadDongHo(menuId, recyclerView, getApplicationContext(), intent, swipeRefreshLayout);
                    } else {
                        Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra internet", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            }
        });
    }
}

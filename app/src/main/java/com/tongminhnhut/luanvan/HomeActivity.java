package com.tongminhnhut.luanvan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.SliderLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tongminhnhut.luanvan.BLL.CheckConnection;
import com.tongminhnhut.luanvan.DAL.Database;
import com.tongminhnhut.luanvan.DAL.LoadMenuDAL;
import com.tongminhnhut.luanvan.DAL.SignIn_DAL;
import com.tongminhnhut.luanvan.Model.Token;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtFullname ;
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager ;
    DatabaseReference db_Category ;

    SwipeRefreshLayout swipeRefreshLayout ;

    SliderLayout sliderLayout ;
    CounterFab btnCart;
    EditText edtHomeAddress;

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

        setContentView(R.layout.activity_home);

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Watch Store");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        


        recyclerView = findViewById(R.id.recyler_menu);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Set name for User
        View headerView = navigationView.getHeaderView(0);
        txtFullname = headerView.findViewById(R.id.txtFullName);
        txtFullname.setText(SignIn_DAL.curentUser.getName());

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckConnection.isConnectedInternet(HomeActivity.this)){
                    Intent intent = new Intent(getApplicationContext(), DongHoActivity.class);
                    LoadMenuDAL.loadMenu(recyclerView, getApplicationContext(), swipeRefreshLayout, intent);
                }else {
                    Toast.makeText(HomeActivity.this, "Vui lòng kiểm tra internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (CheckConnection.isConnectedInternet(HomeActivity.this)){
                    Intent intent = new Intent(getApplicationContext(), DongHoActivity.class);
                    LoadMenuDAL.loadMenu(recyclerView, getApplicationContext(), swipeRefreshLayout, intent);
                }else {
                    Toast.makeText(HomeActivity.this, "Vui lòng kiểm tra internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        sliderLayout = findViewById(R.id.slider_banner);


        if (CheckConnection.isConnectedInternet(getApplicationContext())){
            Intent intent = new Intent(getApplicationContext(), DongHoActivity.class);
            LoadMenuDAL.loadMenu(recyclerView, getApplicationContext(), swipeRefreshLayout, intent);
            LoadMenuDAL.loadBanner(sliderLayout, getApplicationContext());
        }else Toast.makeText(this, "Vui lòng kiểm tra kết nối !", Toast.LENGTH_SHORT).show();


        btnCart = findViewById(R.id.btnCart_Home);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });
        btnCart.setCount(new Database(this).getCountCart(SignIn_DAL.curentUser.getPhone()));

//        //register Service
//        Intent intent = new Intent(getApplicationContext(), ListenOrder.class);
//        startService(intent);
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void updateToken(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token, false);
        tokens.child(SignIn_DAL.curentUser.getPhone()).setValue(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnCart.setCount(new Database(this).getCountCart(SignIn_DAL.curentUser.getPhone()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_timkiem) {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        }else if (id == R.id.nav_xemlai) {
            startActivity(new Intent(getApplicationContext(), SaveActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_thuonghieu) {
            Toast.makeText(this, "Sẽ cập nhật sau", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_baohanh) {
            startActivity(new Intent(getApplicationContext(), BaoHanhActivity.class));

        } else if (id == R.id.nav_thongtin) {
            startActivity(new Intent(getApplicationContext(), ContactActivity.class));

        } else if (id==R.id.nav_dangxuat){
            Paper.book().destroy();
            Intent logoutItent = new Intent(getApplicationContext(), SignInActivity.class);
            logoutItent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutItent);
            finish();
        }else if (id == R.id.nav_status) {
            startActivity(new Intent(getApplicationContext(), OrderStatusActivity.class));

        }else if (id == R.id.nav_home_address) {
            showDialogHomeAddress();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogHomeAddress() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Cập nhật địa chỉ nhà");
        alertDialog.setMessage("Điền vào địa chỉ nhà");
        alertDialog.setIcon(R.drawable.ic_home_black_24dp);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_home_address, null);
        alertDialog.setView(view);

        edtHomeAddress = view.findViewById(R.id.edtHomeAddress_dialogHomeAddress);

        alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SignIn_DAL.curentUser.setHomeAddress(edtHomeAddress.getText().toString());
                SignIn_DAL.updateHomeAddress(getApplicationContext());
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }

}

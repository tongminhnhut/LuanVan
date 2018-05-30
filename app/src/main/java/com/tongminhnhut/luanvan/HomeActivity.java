package com.tongminhnhut.luanvan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.luanvan.BLL.CheckConnection;
import com.tongminhnhut.luanvan.DAL.SignIn_DAL;
import com.tongminhnhut.luanvan.Interface.ItemClickListener;
import com.tongminhnhut.luanvan.Model.Category;
import com.tongminhnhut.luanvan.ViewHolder.MenuViewHolder;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtFullname ;
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager ;
    DatabaseReference db_Category ;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter ;

    SwipeRefreshLayout swipeRefreshLayout ;

    HashMap<String, String> list_image ;
    SliderLayout sliderLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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


        //init firebase
        db_Category = FirebaseDatabase.getInstance().getReference("Catergory");

        recyclerView = findViewById(R.id.recyler_menu);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

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
                    loadMenu();
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
                    loadMenu();
                }else {
                    Toast.makeText(HomeActivity.this, "Vui lòng kiểm tra internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        if (CheckConnection.isConnectedInternet(getApplicationContext())){
            loadMenu();
        }else Toast.makeText(this, "Vui lòng kiểm tra kết nối !", Toast.LENGTH_SHORT).show();



    }

    private void loadMenu() {
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(db_Category,Category.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Category model) {
                holder.txtName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(holder.img);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @Override
            public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
                return new MenuViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
        recyclerView.getAdapter().notifyDataSetChanged();

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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_thuonghieu) {
            // Handle the camera action
        } else if (id == R.id.nav_baohanh) {

        } else if (id == R.id.nav_thongtin) {

        } else if (id == R.id.nav_tintuc) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }
}

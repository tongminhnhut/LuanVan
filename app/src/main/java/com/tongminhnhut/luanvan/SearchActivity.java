package com.tongminhnhut.luanvan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.luanvan.BLL.CheckConnection;
import com.tongminhnhut.luanvan.DAL.Database;
import com.tongminhnhut.luanvan.DAL.LoadListDongHo;
import com.tongminhnhut.luanvan.Interface.ItemClickListener;
import com.tongminhnhut.luanvan.Model.DongHo;
import com.tongminhnhut.luanvan.ViewHolder.DongHoViewHolder;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class SearchActivity extends AppCompatActivity {
    // Search Bar
    FirebaseRecyclerAdapter<DongHo, DongHoViewHolder> searchAdapter;
    List<String> list = new ArrayList<>();
    MaterialSearchBar materialSearchBar ;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout ;
    DatabaseReference db_DongHo ;
    MaterialSpinner spPrice, spBrand ;
    ArrayList<String> listPrice = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db_DongHo = FirebaseDatabase.getInstance().getReference("DongHo");
        recyclerView = findViewById(R.id.recyclerVire_Search);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        materialSearchBar = findViewById(R.id.searchBar);
        spPrice = findViewById(R.id.spinner_Price);
        spBrand = findViewById(R.id.spinner_Brand);

        materialSearchBar.setHint("Enter your key");
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for (String search:list){
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recyclerView.setAdapter(LoadListDongHo.adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        loadList();
        loadAllDongHo();




    }

    private void loadAllDongHo() {
        Intent intent = new Intent(getApplicationContext(), DetailDongHoActivity.class);
        LoadListDongHo.loadAllDongHo(recyclerView,getApplicationContext(),intent);
    }

    private void startSearch(CharSequence text){
        Query query = db_DongHo.orderByChild("name").equalTo(text.toString());
        FirebaseRecyclerOptions<DongHo> options = new FirebaseRecyclerOptions.Builder<DongHo>()
                .setQuery(query, DongHo.class)
                .build();

        searchAdapter = new FirebaseRecyclerAdapter<DongHo, DongHoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DongHoViewHolder viewHolder, int position, @NonNull DongHo model) {
                viewHolder.txtTen.setText(model.getName());
                viewHolder.txtGia.setText(model.getGia());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imgHinh);
                DongHo itemClick = model ;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(getApplicationContext(), DetailDongHoActivity.class);
                        intent.putExtra("dongho", searchAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @Override
            public DongHoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dongho, parent, false);

                return new DongHoViewHolder(itemView);
            }
        };


        searchAdapter.notifyDataSetChanged();
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadList() {
        list = new ArrayList<>();
        db_DongHo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    DongHo dongHo = postSnapShot.getValue(DongHo.class);
                    list.add(dongHo.getName());
                }
                materialSearchBar.setLastSuggestions(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

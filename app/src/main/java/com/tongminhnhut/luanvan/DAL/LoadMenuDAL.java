package com.tongminhnhut.luanvan.DAL;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.luanvan.HomeActivity;
import com.tongminhnhut.luanvan.Interface.ItemClickListener;
import com.tongminhnhut.luanvan.Model.Banner;
import com.tongminhnhut.luanvan.Model.Category;
import com.tongminhnhut.luanvan.R;
import com.tongminhnhut.luanvan.ViewHolder.MenuViewHolder;

import java.util.HashMap;

public class LoadMenuDAL extends HomeActivity {
    static DatabaseReference db_Category = FirebaseDatabase.getInstance().getReference("Catergory");
    static DatabaseReference db_Banner = FirebaseDatabase.getInstance().getReference("Banner");
    static FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter ;
    static FirebaseRecyclerOptions<Category> options;

    public static void loadMenu(RecyclerView recyclerView, final Context context, SwipeRefreshLayout swipeRefreshLayout, final Intent intent){
        options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(db_Category, Category.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Category model) {
                holder.txtName.setText(model.getName());
                Picasso.with(context).load(model.getImage())
                        .into(holder.img);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("ID", adapter.getRef(position).getKey());
                        context.startActivity(intent);
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
        recyclerView.getAdapter().notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

    }
    static  HashMap<String, String> list_image ;


    public static void loadBanner(final SliderLayout sliderLayout, final Context context){
        list_image = new HashMap<>();
        db_Banner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postDataSnapShot:dataSnapshot.getChildren()){
                    Banner banner1 = postDataSnapShot.getValue(Banner.class);
                    list_image.put(banner1.getName()+"_"+banner1.getId(), banner1.getImage());

                }


                for (String key:list_image.keySet()){
                    String[] keySplit = key.split("_");
                    String nameOfFood = keySplit[0];
                    String idOfFood = keySplit[1];

//                    TextSliderView

                    final TextSliderView textSliderView = new TextSliderView(context);
                    textSliderView.description(nameOfFood)
                            .image(list_image.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                    ;

                    // Add Extras Bundle
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("IDFood", idOfFood);

                    sliderLayout.addSlider(textSliderView);

                    // remove event after finish
                    db_Banner.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

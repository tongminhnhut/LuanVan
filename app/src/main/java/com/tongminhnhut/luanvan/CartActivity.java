package com.tongminhnhut.luanvan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tongminhnhut.luanvan.DAL.Database;
import com.tongminhnhut.luanvan.Model.Order;
import com.tongminhnhut.luanvan.ViewHolder.CartAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference db_Request;
    public TextView txtTotal;
    Button btnOrder ;
    List<Order> listOrder = new ArrayList<>();

    CartAdapter adapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db_Request = FirebaseDatabase.getInstance().getReference("Request");

        recyclerView = findViewById(R.id.recyclerView_Cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotal = findViewById(R.id.txtTotal_Cart);
        btnOrder = findViewById(R.id.btnOrder_Cart);

        loadFood();

    }

    private void loadFood() {
        listOrder = new Database(this).getCart();
        adapter = new CartAdapter(listOrder, this);
        recyclerView.setAdapter(adapter);

        //total
        int total = 0;
        for (Order order:listOrder){
            total+=(Integer.parseInt(order.getPrice())*Integer.parseInt(order.getQuantity()));
        }

        Locale locale = new Locale("vn", "VN");
        NumberFormat fm = NumberFormat.getCurrencyInstance(locale);
        txtTotal.setText(fm.format(total));


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Delete")){
//            new Database(this).removeItemCart(listOrder.get());
        }
        return super.onContextItemSelected(item);
    }
}

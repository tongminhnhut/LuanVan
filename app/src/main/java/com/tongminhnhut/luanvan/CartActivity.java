package com.tongminhnhut.luanvan;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tongminhnhut.luanvan.BLL.Common;
import com.tongminhnhut.luanvan.DAL.Database;
import com.tongminhnhut.luanvan.DAL.SignIn_DAL;
import com.tongminhnhut.luanvan.Model.MyResponse;
import com.tongminhnhut.luanvan.Model.Notification;
import com.tongminhnhut.luanvan.Model.Order;
import com.tongminhnhut.luanvan.Model.RequestOrder;
import com.tongminhnhut.luanvan.Model.Sender;
import com.tongminhnhut.luanvan.Model.Token;
import com.tongminhnhut.luanvan.Remote.APIService;
import com.tongminhnhut.luanvan.ViewHolder.CartAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference db_Request;
    public TextView txtTotal;
    Button btnOrder ;
    List<Order> listOrder = new ArrayList<>();

    CartAdapter adapter ;

    APIService mService ;
    EditText edtAddress, edtCmt;

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
        setContentView(R.layout.activity_cart);

        //init Service
        mService = Common.getFCMService();

        db_Request = FirebaseDatabase.getInstance().getReference("RequestOrder");

        recyclerView = findViewById(R.id.recyclerView_Cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotal = findViewById(R.id.txtTotal_Cart);
        btnOrder = findViewById(R.id.btnOrder_Cart);

        loadFood();
        addEvents();

    }

    private void addEvents() {
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequestOrderDialog();
            }
        });
    }

    private void showRequestOrderDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Điền thông tin !");
        alertDialog.setMessage("Địa chỉ giao hàng: ");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addcart, null);
        alertDialog.setView(view);

        edtAddress = view.findViewById(R.id.edtAddress_dialogCart);
        edtCmt =view.findViewById(R.id.edtComment_dialogCart);

        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestOrder requestOrder = new RequestOrder(
                        SignIn_DAL.curentUser.getPhone(),
                        SignIn_DAL.curentUser.getName(),
                        edtAddress.getText().toString().trim(),
                        txtTotal.getText().toString(),
                        edtCmt.getText().toString(),
                        listOrder
                );
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat fm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                db_Request.child(String.valueOf(fm.format(currentTime))).setValue(requestOrder);
                //delete cart (Database SQLite)
                new Database(getApplicationContext()).cleanCart();
//                Toast.makeText(CartActivity.this, "Cám ơn, quý khách đã đặt hàng thành công!", Toast.LENGTH_SHORT).show();
//                finish();
                sendNotification(fm.format(currentTime));

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }

    private void sendNotification(final String orderNumber) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        final Query data = tokens.orderByChild("serverToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    Token serverToken = postSnapShot.getValue(Token.class);
                    //Create
                    Notification notification = new Notification("Watch Store","You have new order" + orderNumber);
                    Sender content = new Sender(serverToken.getToken(), notification);
                    mService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.body().success==1){
                                        Toast.makeText(CartActivity.this, "Cám ơn, quý khách đã đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else
                                        Toast.makeText(CartActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        DecimalFormat fm = new DecimalFormat("#,###,###");
        txtTotal.setText(fm.format(total)+" VNĐ");


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Delete")){
//            new Database(this).removeItemCart(listOrder.get());
        }
        return super.onContextItemSelected(item);
    }
}

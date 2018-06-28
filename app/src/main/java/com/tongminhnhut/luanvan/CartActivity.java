package com.tongminhnhut.luanvan;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
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
import com.tongminhnhut.luanvan.Remote.IGoogleService;
import com.tongminhnhut.luanvan.ViewHolder.CartAdapter;
import com.tongminhnhut.luanvan.ViewHolder.ShowCartAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class CartActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference db_Request;
    public TextView txtTotal;
    Button btnOrder ;
    List<Order> listOrder = new ArrayList<>();

    CartAdapter adapter ;
    ShowCartAdapter showCartAdapter;

    APIService mService ;
    EditText edtAddress, edtCmt;
    Place shipAddress ;
    //Location
    private Location mLastLocation ;
    private GoogleApiClient mGoogleApiClient ;
    private LocationRequest mLocationRequest ;
    private static int UPDATE_INTERVAL =1000 ;
    private static int FATEST_INTERVAL =5000 ;
    private static int DISPLACEMENT = 10 ;

    private static final int LOCATION_REQUEST_CODE = 99 ;

    private static int PLAY_SERVICE_REQUEST = 100;


    IGoogleService iGoogleService;
    String address ="";
    String GoogleAPI_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=false";

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
        iGoogleService = Common.getGoogleMapAPI();



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION

                    },LOCATION_REQUEST_CODE);
        }else {
            if (checkPlayService()){
                buildGoogleApiClient();
                createLocationRequest();
            }
        }



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



    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(CartActivity.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayService() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_REQUEST).show();
            }else {
                Toast.makeText(this, "This device is not support", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void addEvents() {
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showRequestOrderDialog();
                showDialogShowCart();
            }
        });
    }

    private void showDialogShowCart() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Đơn hàng của bạn");
        alertDialog.setMessage("Chọn NEXT để tiến hành thanh toán");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);


        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_show_cart, null);
        alertDialog.setView(view);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerVire_DialogShowCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listOrder = new Database(this).getCart();
        showCartAdapter = new ShowCartAdapter(listOrder, this);
        recyclerView.setAdapter(adapter);

        alertDialog.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showRequestOrderDialog();
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();


    }

    private void showRequestOrderDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Điền thông tin !");
        alertDialog.setMessage("Địa chỉ giao hàng: ");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addcart, null);
        alertDialog.setView(view);

        final PlaceAutocompleteFragment edtAddress = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.fragment_address);
//        edtAddress = view.findViewById(R.id.edtAddress_dialogCart);
        edtCmt =view.findViewById(R.id.edtComment_dialogCart);
        edtAddress.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
        ((EditText)edtAddress.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Enter your address");
        ((EditText)edtAddress.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(14);

        //radiobutton Group
        final RadioButton rbShiptoAddress = view.findViewById(R.id.rbShipToAddress_dialogCart);
        final RadioButton rbHomeAddress = view.findViewById(R.id.rbHomeAddress_dialogCart);

        final RadioButton rbCOD = view.findViewById(R.id.rbCOD_dialogCart);
        final RadioButton rbPaypal = view.findViewById(R.id.rbPaypal_dialogCart);

        rbHomeAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (SignIn_DAL.curentUser.getHomeAddress() != null ||!TextUtils.isEmpty(SignIn_DAL.curentUser.getHomeAddress())) {
                        address = SignIn_DAL.curentUser.getHomeAddress();
                        ((EditText)edtAddress.getView().findViewById(R.id.place_autocomplete_search_input)).setText(address);
                    }
                    else{
                        Toast.makeText(CartActivity.this, "Vui lòng nhập cập địa chỉ trong thông tin HOME", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        rbShiptoAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    iGoogleService.getAddressToShip(String.format(GoogleAPI_URL,
                            mLastLocation.getLatitude(),
                            mLastLocation.getLongitude())).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray result = jsonObject.getJSONArray("results");
                                JSONObject firstObject = result.getJSONObject(0);
                                address = firstObject.getString("formatted_address");
//                                txtAddress.setText(address);
                                ((EditText)edtAddress.getView().findViewById(R.id.place_autocomplete_search_input)).setText(address);
                                Toast.makeText(CartActivity.this, address, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }


            }
        });



        edtAddress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                shipAddress = place;
            }

            @Override
            public void onError(Status status) {
                Log.e("ERROR", status.getStatusMessage());
            }
        });
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);




        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!rbHomeAddress.isChecked() && !rbShiptoAddress.isChecked()) {
                    if (shipAddress != null) {
                        address = shipAddress.getAddress().toString();
//                        address = txtAddress.getText().toString();
                    } else {
                        Toast.makeText(CartActivity.this, "Vui lòng nhập địa chỉ hoặc chọn option", Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction()
                                .remove(getFragmentManager().findFragmentById(R.id.fragment_address))
                                .commit();
                        return;
                    }
                }
//
//                    if (TextUtils.isEmpty(address)) {
//                        Toast.makeText(CartActivity.this, "Vui lòng nhập địa chỉ hoặc chọn option", Toast.LENGTH_SHORT).show();
////                        getFragmentManager().beginTransaction()
////                                .remove(getFragmentManager().findFragmentById(R.id.fragment_address))
////                                .commit();
////                        return;
//                    }
                if (!rbCOD.isChecked() && !rbPaypal.isChecked()){
                    Toast.makeText(CartActivity.this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction()
                            .remove(getFragmentManager().findFragmentById(R.id.fragment_address))
                            .commit();
                    return;
                }
                else if (rbPaypal.isChecked()){
                    Toast.makeText(CartActivity.this, "Sẽ cập nhật sau. Mong quý khách thông cảm", Toast.LENGTH_LONG).show();
                }
                else if (rbCOD.isChecked()) {


                    RequestOrder requestOrder = new RequestOrder(
                            SignIn_DAL.curentUser.getPhone(),
                            SignIn_DAL.curentUser.getName(),
                            address,
                            txtTotal.getText().toString(),
                            edtCmt.getText().toString(),
                            "COD",
//                                String.format("%s,%s", shipAddress.getLatLng().latitude, shipAddress.getLatLng().longitude),
//                                "1",
                            listOrder
                    );
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat fm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    db_Request.child(String.valueOf(fm.format(currentTime))).setValue(requestOrder);
                    //delete cart (Database SQLite)
                    new Database(getApplicationContext()).cleanCart();
                    sendNotification(fm.format(currentTime));

                    getFragmentManager().beginTransaction()
                            .remove(getFragmentManager().findFragmentById(R.id.fragment_address))
                            .commit();
                }


            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getFragmentManager().beginTransaction()
                        .remove(getFragmentManager().findFragmentById(R.id.fragment_address))
                        .commit();

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        disPlayLocation();
        startLocationUpdate();
    }

    private void disPlayLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            Log.e("LOCATION", "Your location "+mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
        }
        else {
            Log.e("ERROR", "Could not get location");
        }
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        disPlayLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (checkPlayService()){
                        buildGoogleApiClient();
                        createLocationRequest();

                    }
                }
                break;
        }
    }


}

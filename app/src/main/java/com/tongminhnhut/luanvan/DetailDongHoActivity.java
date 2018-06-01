package com.tongminhnhut.luanvan;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.luanvan.BLL.CheckConnection;
import com.tongminhnhut.luanvan.Model.DongHo;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailDongHoActivity extends AppCompatActivity {
    TextView txtName, txtPrice, txtThuonghieu, txtXuatxu, txtKichthuoc, txtBaohanh, txtMay, txtDaydeo ;
    ImageView imgDongHo ;
    String idDongHo = "";
    DatabaseReference db_DongHo;
    CollapsingToolbarLayout collapsingToolbarLayout ;
    ElegantNumberButton numberButton ;

    CounterFab btnCart ;
    DongHo dongHo ;

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

        setContentView(R.layout.activity_detail_dong_ho);

        db_DongHo = FirebaseDatabase.getInstance().getReference("DongHo");

        initView();
        if (getIntent() != null){
            idDongHo = getIntent().getStringExtra("dongho");
        }
        if (!idDongHo.isEmpty() && idDongHo !=null){
            if (CheckConnection.isConnectedInternet(getApplicationContext())){
                loadDongHo(idDongHo);
            }else {
                Toast.makeText(this, "Vui long kiểm tra kết nối !", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void loadDongHo(String idDongHo) {
        db_DongHo.child(idDongHo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dongHo = dataSnapshot.getValue(DongHo.class);

                Picasso.with(getBaseContext()).load(dongHo.getImage())
                        .into(imgDongHo);
                collapsingToolbarLayout.setTitle(dongHo.getName());
                txtName.setText(dongHo.getName());
                txtPrice.setText(dongHo.getGia());
                txtBaohanh.setText(dongHo.getBaoHanh());
                txtDaydeo.setText(dongHo.getDayDeo());
                txtMay.setText(dongHo.getMay());
                txtThuonghieu.setText(dongHo.getThuongHieu());
                txtXuatxu.setText(dongHo.getXuatXu());
                txtKichthuoc.setText(dongHo.getSize());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initView() {
        imgDongHo = findViewById(R.id.imgDongHo_DetailDongHo);
        txtName = findViewById(R.id.txtName_DetailDongHo);
        txtBaohanh = findViewById(R.id.txtBaoHanh_DetailDongHo);
        txtPrice = findViewById(R.id.txtPrice_DetailDongHo);
        txtDaydeo = findViewById(R.id.txtDaydeo_DetailDongHo);
        txtKichthuoc = findViewById(R.id.txtSize_DetailDongHo);
        txtMay = findViewById(R.id.txtMay_DetailDongHo);
        txtThuonghieu = findViewById(R.id.txtThuonghieu_DetailDongHo);
        txtXuatxu = findViewById(R.id.txtXuatsu_DetailDongHo);
        btnCart = findViewById(R.id.btnCart_DetailDongHo);
        collapsingToolbarLayout = findViewById(R.id.collapsing_DetailDongHo);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);
        numberButton = findViewById(R.id.numer_button);
    }
}

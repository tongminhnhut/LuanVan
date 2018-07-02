package com.tongminhnhut.luanvan.DAL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.tongminhnhut.luanvan.Model.Order;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "sqlite_dongho.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public boolean checkExistDongHo (String IdDongHo, String userPhone){
        boolean flag = false ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null ;
        String query = String.format("SELECT * FROM DongHoStore WHERE UserPhone='%s' AND ProductId='%s'", userPhone, IdDongHo);
        cursor =db.rawQuery(query, null);
        if (cursor.getCount()>0)
            flag= true;
        else
            flag = false;
        cursor.close();
        return flag;

    }

    public List<Order> getCart(String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone","ProductId", "ProductName", "Quantity", "Price", "Discount", "Image"};
        String sqlTable = "DongHoStore" ;

        qb.setTables(sqlTable);

        Cursor c = qb.query(db, sqlSelect, "UserPhone=?", new String[] {userPhone}, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                result.add(new Order(
                        c.getString(c.getColumnIndex("UserPhone")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount")),
                        c.getString(c.getColumnIndex("Image"))
                        ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addCarts (Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT OR REPLACE INTO DongHoStore (UserPhone,ProductId, ProductName, Quantity, Price, Discount, Image) VALUES ('%s','%s','%s','%s','%s','%s','%s');",
                order.getUserPhone(),
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getImage());
        db.execSQL(query);
    }

    public void cleanCart (String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM DongHoStore WHERE UserPhone='%s' ", userPhone);
        db.execSQL(query);
    }

    public void updateCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("Update DongHoStore SET Quantity= '%s' WHERE UserPhone = '%s' AND ProductId='%s'", order.getQuantity(), order.getUserPhone(), order.getProductId());
        db.execSQL(query);

    }

    public void increaseCart(String idDH, String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("Update DongHoStore SET Quantity=Quantity+1 WHERE UserPhone = '%s' AND ProductId='%s'",userPhone, idDH);
        db.execSQL(query);

    }

    public int getCountCart(String userPhone) {
        int count =0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("Select Count(*) from DongHoStore Where UserPhone='%s'", userPhone);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                count=cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        return count;
    }

    public void removeItemCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("Delete from DongHoStore Where UserPhone='%s' AND ProductId='%s'", order.getUserPhone(), order.getProductId());
        db.execSQL(query);

    }
}

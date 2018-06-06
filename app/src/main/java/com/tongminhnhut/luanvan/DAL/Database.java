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

    public List<Order> getCart(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID","ProductId", "ProductName", "Quantity", "Price", "Discount", "Image"};
        String sqlTable = "DongHoStore" ;

        qb.setTables(sqlTable);

        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                result.add(new Order(
                        c.getInt(c.getColumnIndex("ID")),
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
        String query = String.format("INSERT INTO DongHoStore (ProductId, ProductName, Quantity, Price, Discount, Image) VALUES ('%s','%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getImage());
        db.execSQL(query);
    }

    public void cleanCart (){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM DongHo ");
        db.execSQL(query);
    }

    public void updateCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("Update DongHoStore SET Quantity= %s WHERE ID = %d", order.getQuantity(), order.getID());
        db.execSQL(query);

    }

    public int getCountCart() {
        int count =0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("Select Count(*) from DongHoStore");
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
        String query = String.format("Delete from DongHoStore Where ID= %d", order.getID());
        db.execSQL(query);

    }
}

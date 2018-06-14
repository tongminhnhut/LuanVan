package com.tongminhnhut.luanvan.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tongminhnhut.luanvan.Model.RequestOrder;
import com.tongminhnhut.luanvan.OrderStatusActivity;
import com.tongminhnhut.luanvan.R;

public class ListenOrder extends Service implements ChildEventListener {

    DatabaseReference db_request ;

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db_request = FirebaseDatabase.getInstance().getReference("RequestOrder");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db_request.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        RequestOrder requestOrder = dataSnapshot.getValue(RequestOrder.class);
        showNotification(dataSnapshot.getKey(), requestOrder);

    }

    private void showNotification(String key, RequestOrder requestOrder) {
        Intent intent = new Intent(getApplicationContext(), OrderStatusActivity.class);
        intent.putExtra("userPhone", requestOrder.getPhone());
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Watch Store")
                .setContentInfo("Đơn hàng của bạn đã cập nhật")
                .setContentText("Đơn hàng #"+key+" đã cập nhật trạng thái ")
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

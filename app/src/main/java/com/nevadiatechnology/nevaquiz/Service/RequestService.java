package com.nevadiatechnology.nevaquiz.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Model.Request;
import com.nevadiatechnology.nevaquiz.Model.User;


public class RequestService extends Service implements ChildEventListener, ValueEventListener {
    private DatabaseReference requests, user;
    private String uid;
    private SharedPreferences sharedPreferences;
    private User currentUser;
    private String CHANNEL_ID = "online_quiz_app";

    public RequestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        requests = FirebaseDatabase.getInstance().getReference("Player_Request");
        user = FirebaseDatabase.getInstance().getReference("User");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "NAV", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);

            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uid = sharedPreferences.getString("UID", "");
        requests.addChildEventListener(RequestService.this);
        user.addValueEventListener(this);
        return Service.START_STICKY;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (!uid.equals("")) {
            if (dataSnapshot != null) {
                Request request = dataSnapshot.getValue(Request.class);
                showNotification(dataSnapshot.getKey(), request);
            }
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
       /* if (!uid.equals("")) {
            Request request = dataSnapshot.getValue(Request.class);
            if (request.getUid().equals(uid)) {
                if (request.getStatus() == 1) {
                    startActivity(new Intent(this, QuizActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("category", request.getCategory())
                            .putExtra("key", dataSnapshot.getKey())
                            .putExtra("friendUid", request.getRequestPersonUid()));
                }
            }
        }*/
    }

    private void showNotification(String key, Request request) {
        /*if (uid.equals(request.getRequestPersonUid())) {

            Intent intent = new Intent(this, QuizActivity.class)
                    .putExtra("category", request.getCategory())
                    .putExtra("key", key)
                    .putExtra("friendUid", request.getUid());
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID);
            builder.setAutoCancel(false)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("Quiz Request")
                    .setContentText("You want to play with " + request.getName())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());

        }*/
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (!uid.equals("")) {
            if (dataSnapshot.child(uid).exists()) {
                currentUser = dataSnapshot.getValue(User.class);
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

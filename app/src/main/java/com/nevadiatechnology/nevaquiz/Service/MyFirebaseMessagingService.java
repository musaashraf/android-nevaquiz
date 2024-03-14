package com.nevadiatechnology.nevaquiz.Service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nevadiatechnology.nevaquiz.Activity.HelperActivity;
import com.nevadiatechnology.nevaquiz.Activity.QuizActivity;
import com.nevadiatechnology.nevaquiz.Activity.RequestActivity;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.MyToken;

import java.util.Random;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.userToken;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            updateToken(s);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String content = remoteMessage.getData().get("content");
        if (title.equals("1")) {//1 means Play
            quizView(this, remoteMessage);
        } else if (title.equals("0")) {//0 means Not play
            showDialog(this, content);
        } else {
            Common.showNotification(this, new Random().nextInt(),
                    remoteMessage.getData().get("title"),
                    remoteMessage.getData().get("content"), new Intent(this, RequestActivity.class));
        }
    }

    public static void updateToken(final String s) {
        if (s != null) {
            userToken.child(Common.uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                    } else {
                        MyToken myToken = new MyToken();
                        myToken.setToken(s);

                        FirebaseDatabase.getInstance().getReference("Token").child(Common.uid).setValue(myToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void quizView(final Context context, RemoteMessage remoteMessage) {
        if (remoteMessage != null) {
            final String content = remoteMessage.getData().get("content");//key
            final String category = remoteMessage.getData().get("category");
            final String receiverUID = remoteMessage.getData().get("receiverUID");

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(context, QuizActivity.class)
                            .putExtra("categoryName", category)
                            .putExtra("key", content)
                            .putExtra("friendUid", receiverUID)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
        }
    }

    public void showDialog(final Context context, final String content) {
        if (content != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(context, HelperActivity.class).putExtra("content", content)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
        }
    }
}

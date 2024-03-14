package com.nevadiatechnology.nevaquiz.Adpter;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.FCMData;
import com.nevadiatechnology.nevaquiz.Model.FCMResponse;
import com.nevadiatechnology.nevaquiz.Model.MyToken;
import com.nevadiatechnology.nevaquiz.Model.Request;
import com.nevadiatechnology.nevaquiz.Model.User;
import com.nevadiatechnology.nevaquiz.R;
import com.nevadiatechnology.nevaquiz.Retrofit.FCMApi;
import com.nevadiatechnology.nevaquiz.Retrofit.RetrofitClient;
import com.nevadiatechnology.nevaquiz.ViewHolder.OnlineUserViewHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.requests;
import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.userToken;

public class OnlineUserAdapter extends RecyclerView.Adapter<OnlineUserViewHolder> {

    private Context context;
    private List<User> userList;
    private String category;
    private Dialog dialog;

    public OnlineUserAdapter(Context context, List<User> userList, String category) {
        this.context = context;
        this.userList = userList;
        this.category = category;
    }

    @NonNull
    @Override
    public OnlineUserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.active_user_item, viewGroup, false);
        return new OnlineUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineUserViewHolder holder, final int i) {
        if (userList != null && !userList.get(i).getUid().equals(Common.currentUser.getUid())) {
            holder.name.setText(userList.get(i).getName());
            Picasso.get().load(userList.get(i).getImage()).placeholder(R.drawable.boy)
                    .into(holder.profileImage);

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(i, userList.get(i).getName());
                }
            });
        }
    }

    private void showAlertDialog(final int i, final String name) {
        dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.send_request_layout, null);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);
        TextView textView3 = (TextView) view.findViewById(R.id.textView3);
        Button button = (Button) view.findViewById(R.id.button);

        dialog.setContentView(view);
        dialog.show();

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        textView3.setText("Are you sure you want to play with " + name + "?");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request request = new Request(Common.uid, 0, userList.get(i).getName(), userList.get(i).getUid(), category, userList.get(i).getImage());
                Request request = new Request(category, Common.uid, Common.currentUser.getName(), Common.currentUser.getImage(),
                        userList.get(i).getUid(), userList.get(i).getName(), userList.get(i).getImage());
                checkRequest(userList.get(i).getUid(), request);
            }
        });
    }

    private void sendNotification(String token, final Request request) {
        Map<String, String> map = new HashMap<>();

        map.put("title", "Quiz Request");
        map.put("content", "You want to play with " + request.getSenderName());
        FCMData data = new FCMData(token, map);

        FCMApi api = RetrofitClient.getRetrofit().create(FCMApi.class);

        Call<FCMResponse> call = api.sendNotification(data);
        call.enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.isSuccessful()) {
                  //  Toast.makeText(context, "Success " + response.body().getSuccess(), Toast.LENGTH_SHORT).show();
                } else {
                  //  Toast.makeText(context, "Failed " + response.body().getFailure(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {

            }
        });
    }

    private void checkRequest(final String requestUID, final Request request) {
        requests.orderByChild("receiverUID").equalTo(requestUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dialog.dismiss();
                    Toast.makeText(context, "Already Request Done", Toast.LENGTH_SHORT).show();
                } else {
                    userToken.child(requestUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                requests.push().setValue(request);
                                Toast.makeText(context, "Please wait for response", Toast.LENGTH_SHORT).show();
                                sendNotification(dataSnapshot.getValue(MyToken.class).getToken(), request);
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

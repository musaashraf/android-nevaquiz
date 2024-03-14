package com.nevadiatechnology.nevaquiz.Adpter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nevadiatechnology.nevaquiz.Model.FCMData;
import com.nevadiatechnology.nevaquiz.Model.FCMResponse;
import com.nevadiatechnology.nevaquiz.Model.Request;
import com.nevadiatechnology.nevaquiz.R;
import com.nevadiatechnology.nevaquiz.Retrofit.FCMApi;
import com.nevadiatechnology.nevaquiz.Retrofit.RetrofitClient;
import com.nevadiatechnology.nevaquiz.ViewHolder.RequestViewHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestPlayingAdapter extends RecyclerView.Adapter<RequestViewHolder> {

    private Context context;
    private List<Request> requestsList;

    public RequestPlayingAdapter(Context context, List<Request> requestsList) {
        this.context = context;
        this.requestsList = requestsList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.request_player_item, viewGroup, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, final int i) {
        Picasso.get().load(requestsList.get(i).getReceiverImage()).placeholder(R.drawable.boy).into(holder.request_image);
        holder.request_name.setText(requestsList.get(i).getReceiverName());
        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Accept", Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                /*SplashScreenActivity.requests.orderByChild("senderUID").equalTo(requestsList.get(i).getSenderUID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            userToken.child(requestsList.get(i).getSenderUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        sendMessage(dataSnapshot.getValue(MyToken.class).getToken(), requestsList.get(i).getSenderName());
                                        String key = dataSnapshot.getKey();
                                        Toast.makeText(context, "Delete" + key, Toast.LENGTH_SHORT).show();
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
                });*/
            }
        });
    }

    private void sendMessage(String token, String name) {
        Map<String, String> map = new HashMap<>();

        map.put("title", "0");
        map.put("content", name);
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

    @Override
    public int getItemCount() {
        return requestsList.size();
    }
}

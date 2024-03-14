package com.nevadiatechnology.nevaquiz.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.FCMData;
import com.nevadiatechnology.nevaquiz.Model.FCMResponse;
import com.nevadiatechnology.nevaquiz.Model.MyToken;
import com.nevadiatechnology.nevaquiz.Model.Request;
import com.nevadiatechnology.nevaquiz.R;
import com.nevadiatechnology.nevaquiz.Retrofit.FCMApi;
import com.nevadiatechnology.nevaquiz.Retrofit.RetrofitClient;
import com.nevadiatechnology.nevaquiz.ViewHolder.RequestViewHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;
import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.requests;
import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.userToken;

public class RequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    /*private RequestPlayingAdapter adapter;
    private List<Request> requestList;*/
    private FirebaseRecyclerAdapter<Request, RequestViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.bg_color)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //requestList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //loadReuqest();
        loadFirebaseAdapter();
    }

    private void loadFirebaseAdapter() {
        final FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requests.orderByChild("receiverUID").equalTo(Common.uid), Request.class).build();

        adapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder holder, final int position, @NonNull final Request model) {
                Picasso.get().load(model.getSenderImage()).placeholder(R.drawable.boy).into(holder.request_image);
                holder.request_name.setText(model.getSenderName());
                holder.accept_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendPlay(adapter.getRef(position).getKey(), model);
                    }
                });

                holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendNoPlay(adapter.getRef(position).getKey(), model.getSenderUID(), model.getReceiverName());
                    }
                });
            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(RequestActivity.this);
                View view = inflater.inflate(R.layout.request_player_item, viewGroup, false);
                return new RequestViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void sendPlay(final String key, final Request model) {
        userToken.child(model.getSenderUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sendResponse(dataSnapshot.getValue(MyToken.class).getToken(), model, key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendResponse(String token, final Request model, final String key) {
        Map<String, String> map = new HashMap<>();

        map.put("title", "1");
        map.put("content", key);
        map.put("category", model.getCategory());
        map.put("receiverUID", model.getSenderUID());
        FCMData data = new FCMData(token, map);

        FCMApi api = RetrofitClient.getRetrofit().create(FCMApi.class);

        Call<FCMResponse> call = api.sendNotification(data);
        call.enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.isSuccessful()) {

                } else {
                    //  Toast.makeText(context, "Failed " + response.body().getFailure(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {

            }
        });

        startActivity(new Intent(RequestActivity.this, QuizActivity.class)
                .putExtra("categoryName", model.getCategory())
                .putExtra("key", key)
                .putExtra("friendUid", model.getSenderUID()));
    }

    private void sendNoPlay(final String key, String senderUID, final String name) {
        userToken.child(senderUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sendNotification(dataSnapshot.getValue(MyToken.class).getToken(), name);
                    requests.child(key).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String token, String name) {
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

    /*private void loadReuqest() {
        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            requestList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(Request.class).getRequestPersonUid().equals(Common.uid)) {
                        requestList.add(snapshot.getValue(Request.class));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter = new RequestPlayingAdapter(this, requestList);
        recyclerView.setAdapter(adapter);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(true);
        }
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(false);
        }
    }
}

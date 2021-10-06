package com.example.notifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Fragment4 extends Fragment {

    private static String TAG = "Fragment4";
    Button button;
    private Context context;
    List<MainData> dataList = new ArrayList<>();
    RoomDB database;


    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_4,container,false);
        Context context = getContext();
        TextView current = (TextView)view.findViewById(R.id.Interface);
        Button logout = (Button)view.findViewById(R.id.logout);
        Button userBtn = (Button)view.findViewById(R.id.userInterface);
        Button error = (Button)view.findViewById(R.id.errorBtn);
        Button token = (Button)view.findViewById(R.id.Token);

        String getTime = sdf.format(date);
        current.setText(getTime);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-1234-5678" ));
                context.startActivity(myIntent);
            }
        });
        token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                String token = task.getResult();

                                // Log and toast
                                String msg = getString(R.string.msg_token_fmt, token);
                                Log.d(TAG, msg);
                                Token tokenDa = new Token();
                                tokenDa.token = msg;
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        String data1 = postData(tokenDa);
                                        if(data1.equalsIgnoreCase("good")){
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context,
                                                            msg,
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
            }
        });


        Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.app_use);

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

               Button check1 = (Button)dialog.findViewById(R.id.use_btn);
                check1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public class Token{
        String token;
    }

    private String postData(Token tokenData) {

        try {
            OkHttpClient client = new OkHttpClient();

            String url = "http://121.152.234.192:8888/token";

            Gson gson = new Gson();
            String json = gson.toJson(tokenData);

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), json))
                    .build();

            Response response = client.newCall(request).execute();

            //
            String jsonResult = response.body().string();
            Map<String, String> map = new Gson().fromJson(jsonResult, Map.class);
            String result = map.get("code");

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "fail";
    }
}
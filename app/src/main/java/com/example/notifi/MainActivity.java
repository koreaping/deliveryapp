package com.example.notifi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notifi.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;

    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        database = RoomDB.getInstance(MainActivity.this);
        dataList = database.mainDao().getAll();


        EditText userId = (EditText)findViewById(R.id.user_id);
        EditText userPass = (EditText)findViewById(R.id.user_password);

        Button register = (Button)findViewById(R.id.register_btn);
        Button Login = (Button)findViewById(R.id.btn_login);
        Button findPassword = (Button)findViewById(R.id.findPassword);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoginData loginData = new LoginData();
                loginData.userid = userId.getText().toString();
                loginData.userpass = userPass.getText().toString();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        String result = postData(loginData);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if("login_success".equalsIgnoreCase(result)){
                                    Toast.makeText(MainActivity.this,"로그인 성공.",Toast.LENGTH_SHORT).show();
                                    Intent intent2 = new Intent(getApplicationContext(), Fragment4.class);
                                    Intent intent = new Intent(getApplicationContext(), userActivity.class);
                                    intent2.putExtra("result",loginData.userid);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(MainActivity.this,"로그인 실패." ,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),registerActivity.class);
                startActivity(intent);
            }
        });

        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), findPassActivity.class);
                startActivity(intent);
            }
        });


    }

    public class LoginData{
        public String userid;
        public String userpass;


    }

    private String postData(LoginData loginData) {

        try {
            OkHttpClient client = new OkHttpClient();

            String url = "http://121.152.234.192:8888/login";

            Gson gson = new Gson();
            String json = gson.toJson(loginData);

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
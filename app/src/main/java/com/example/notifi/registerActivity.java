package com.example.notifi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class registerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        EditText userName = (EditText)findViewById(R.id.NameText);
        EditText userId = (EditText)findViewById(R.id.IdText);
        EditText userPass = (EditText)findViewById(R.id.PasswordText);
        EditText userPassCheck = (EditText)findViewById(R.id.PasswordCheckTect);

        Button register = (Button)findViewById(R.id.findPassword);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final userInfo userData = new userInfo();
                userData.userid = userId.getText().toString();
                userData.username =userName.getText().toString();
                userData.userpassword = userPass.getText().toString();

                String passwordCheck = userPassCheck.getText().toString();

                if(userData.username.length() == 0){
                    Toast.makeText(registerActivity.this,"이름을 입력해야 합니다.",Toast.LENGTH_LONG).show();
                }else if(userData.userid.length() == 0){
                    Toast.makeText(registerActivity.this,"아이디를 입력해야 합니다.",Toast.LENGTH_LONG).show();
                }else if(userData.userpassword.length() == 0) {
                    Toast.makeText(registerActivity.this,"비밀번호를 입력해야 합니다.",Toast.LENGTH_LONG).show();
                }else{
                    if(userData.userpassword.equals(passwordCheck)){
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                String result = postData(userData);
                                if(result.equalsIgnoreCase("overlap")){
                                    registerActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(registerActivity.this,"아이디가 중복 되었습니다.",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }else{
                                    registerActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(registerActivity.this,"회원가입이 완료 되었습니다.",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }

                            }
                        });
                    }
                    else{
                        Toast.makeText(registerActivity.this,"비밀번호가 맞지 않습니다.",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
       //createNotificationChannel();
    }

    public class  userInfo{
        public  String username;
        public  String userid;
        public  String userpassword;
    }

    private String postData(userInfo data) {

        try {
            OkHttpClient client = new OkHttpClient();

            String url = "http://121.152.234.192:8888/register";

            Gson gson = new Gson();
            String json = gson.toJson(data);

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


package com.example.notifi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class findPassActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        EditText userName = (EditText)findViewById(R.id.findUserName);
        EditText userId = (EditText)findViewById(R.id.findUserId);

        Button findBtn = (Button)findViewById(R.id.findBtn);

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final findData userData= new findData();
                userData.username = userName.getText().toString();
                userData.userid = userId.getText().toString();

                if(userData.username.length() == 0){
                    Toast.makeText(findPassActivity.this,"정보가 입력되지 않았습니다.",Toast.LENGTH_SHORT).show();
                }else if(userData.userid.length() == 0){
                    Toast.makeText(findPassActivity.this,"정보가 입력되지 않았습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            findData result = postData(userData);

                            if(result.result.equalsIgnoreCase("noPeople")){
                                Toast.makeText(findPassActivity.this,"등록되지 않은 정보입니다.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                findPassActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder =new AlertDialog.Builder(findPassActivity.this);
                                        builder.setTitle(result.username+ "님");
                                        builder.setMessage("비밀번호는 "+result.password + "입니다.");
                                        builder.setPositiveButton("로그인 하러가기", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                        builder.setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(getApplicationContext(), findPassActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    public  class findData{
        public String username;
        public String userid;
        public String password;
        public String result;
    }

    private findData postData(findData userData) {
        findData userdata = new findData();

        try {
            OkHttpClient client = new OkHttpClient();

            String url = "http://121.152.234.192:8080/find";

            Gson gson = new Gson();
            String json = gson.toJson(userData);

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), json))
                    .build();

            Response response = client.newCall(request).execute();

            //
            String jsonResult = response.body().string();
            Map<String, String> map = new Gson().fromJson(jsonResult, Map.class);
            userdata.result = map.get("code");
            userdata.userid = map.get("userId");
            userdata.password = map.get("password");
            userdata.username = map.get("userName");


            return userdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userdata;
    }
}

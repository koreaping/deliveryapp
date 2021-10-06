package com.example.notifi;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Fragment2 extends Fragment {

    RecyclerView recyclerView;
    private Spinner spinner;
    ArrayAdapter<String> arrayAdapter;
    TextView spinnerText;
    String[] items = {"CJ대한통운"	,	"한진택배"	,	"롯데택배"	,
            "우체국택배"	,	"로젠택배"	,	"일양로지스"	,
            "한덱스"	,	"대신택배"	,	"경동택배"	,
            "합동택배"	,	"CU 편의점택배"	,	"GS Postbox 택배"	,
            "한의사랑택배"	,	"천일택배"	,	"건영택배"	,
            "굿투럭"	,	"애니트랙"	,	"SLX택배"	,
            "호남택배"	,	"우리한방택배"	,	"농협택배"	,
            "홈픽택배"	,	"IK물류"	,	"성훈물류"	,
            "용마로지스"	,	"원더스퀵"	,	"티피엠코리아㈜ 용달이 특송"	,
            "컬리로지스"	,	"풀앳홈"	,	"삼성전자물류"	,
            "큐런택배"	,	"두발히어로"	,	"위니아딤채"	,
            "지니고 당일배송"	,	"오늘의픽업"	,	"로지스밸리"	,
            "한샘서비스원 택배"	,	"NDEX KOREA"	,	"도도플렉스(dodoflex)"	,
            "LG전자(판토스)"	,	"부릉"	,	"1004홈"	,
            "썬더히어로"	,	"(주)팀프레시"	,	"롯데칠성"	,
            "핑퐁"	,	"발렉스 특수물류"	,
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_2,container,false);

        ArrayList<whereData> list = new ArrayList<>();

        spinner = (Spinner)view.findViewById(R.id.spinner);
        spinnerText = view.findViewById(R.id.spinnerText);
        Button Sbtn = (Button)view.findViewById(R.id.searchBtn);
        TextView Number = (TextView)view.findViewById(R.id.Dnumber);
        TextView name = (TextView)view.findViewById(R.id.Name);
        TextView date = (TextView)view.findViewById(R.id.Date);

        recyclerView = view.findViewById(R.id.diliverList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Activity activity = getActivity();

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                spinnerText.setText(items[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerText.setText("선택 : ");
            }

        });

        Sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                list.clear();
                diliverData Data = new diliverData();
                if(Number.getText().length() == 0){
                    Toast.makeText(getContext(), "운 송장 번호를입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Data.Dcompany = spinnerText.getText().toString();
                    Data.Dnumber = Number.getText().toString();
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run(){
                            Detail NewUser = new Detail();
                            String result = postData(Data);
                            try{
                                JSONObject jsonObject = new JSONObject(result);
                                NewUser.itemName = jsonObject.getString("code");
                                NewUser.Ctime = jsonObject.getString("arrive");
                                String Where = jsonObject.getString("detail");
                                JSONArray jsonArray = new JSONArray(Where);
                                for(int i =0; i< jsonArray.length();i++){
                                    whereData useData = new whereData();
                                    JSONObject subJson = jsonArray.getJSONObject(i);
                                    useData.timeString = subJson.getString("timeString");
                                    useData.whereString = subJson.getString("where");
                                    useData.kindString = subJson.getString("kind");
                                    list.add(useData);
                                }
                                DiliverAdapter adapter = new DiliverAdapter(list);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        name.setText("제품 이름 : " + NewUser.itemName);
                                        date.setText("도착 예정시간 : " + NewUser.Ctime);
                                        recyclerView.setAdapter(adapter);
                                        NewUser.itemName ="";
                                        NewUser.Ctime ="";
                                    }
                                });
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    public class diliverData{
        public String Dcompany;
        public String Dnumber;
    }

    public class whereData{
        public String timeString;
        public String whereString;
        public String kindString;
    }

    private String postData(diliverData data) {
        try {
            OkHttpClient client = new OkHttpClient();

            String url = "http://121.152.234.192:8888/diliver";

            Gson gson = new Gson();
            String json = gson.toJson(data);

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), json))
                    .build();

            Response response = client.newCall(request).execute();



            String jsonResult = response.body().string();

            try{
                JSONObject jsonObject = new JSONObject(jsonResult);
                String Where = jsonObject.getString("detail");
                JSONArray jsonArray = new JSONArray(Where);
                for(int i = 0; i< jsonArray.length(); i++){
                    JSONObject subJsonObject = jsonArray.getJSONObject(i);
                    String arrive = subJsonObject.getString("where");
                    Log.v("태그","위치 : "+arrive);
                }

            }catch(JSONException e){
                e.printStackTrace();
            }




            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "fail";
    }
    public class Detail{
        public String itemName;
        public String Ctime;
        public String WhereData;
        public String kindData;
    }

}

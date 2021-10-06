package com.example.notifi;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class Fragment3 extends Fragment {

    RecyclerView recyclerView;

    List<MainData> dataList = new ArrayList<>();
    RoomDB database;
    recyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3,container,false);
        Activity context = getActivity();
        EditText list_name = (EditText)view.findViewById(R.id.list_name);
        EditText list_num = (EditText)view.findViewById(R.id.list_num);
        Button addBtn = (Button)view.findViewById(R.id.addBtn1);

        recyclerView = view.findViewById(R.id.recycler_view);

        database = RoomDB.getInstance(context);
        dataList = database.mainDao().getAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new recyclerAdapter( context,dataList);

        recyclerView.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sName = list_name.getText().toString().trim();
                String sNum = list_num.getText().toString().trim();
                if(!sName.equals("") && !sNum.equals("")){
                    MainData data = new MainData();
                    data.setName(sName);
                    data.setNum(sNum);
                    database.mainDao().insert(data);

                    list_name.setText("");
                    list_num.setText("");

                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();
                }
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

}
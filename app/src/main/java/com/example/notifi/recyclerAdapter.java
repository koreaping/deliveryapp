package com.example.notifi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder> {

    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;


    public recyclerAdapter(Activity context, List<MainData> dataList)
    {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);

        return new ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MainData data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.name.setText(data.getName());
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData mainData = dataList.get(holder.getAbsoluteAdapterPosition());

                final int sID = mainData.getId();
                String sName = mainData.getName();
                String sNum = mainData.getNum();

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update);

                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setLayout(width,height);
                dialog.show();

                final EditText editText = dialog.findViewById(R.id.dialog_edit_text);
                final EditText editText1 = dialog.findViewById(R.id.dialog_edit_text2);
                Button bt_update = dialog.findViewById(R.id.bt_update);

                editText.setText(sName);
                editText1.setText(sNum);

                bt_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String uName = editText.getText().toString().trim();
                        String uNum = editText1.getText().toString().trim();

                        database.mainDao().update_name(sID, uName);
                        database.mainDao().update_num(sID,uNum);

                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());
                        notifyDataSetChanged();
                    }
                });
            }
        });
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData mainData = dataList.get(holder.getAbsoluteAdapterPosition());
                database.mainDao().delete(mainData);

                int position = holder.getAbsoluteAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,dataList.size());
            }
        });
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData mainData = dataList.get(holder.getAbsoluteAdapterPosition());
                int position = holder.getAbsoluteAdapterPosition();
                String sNum = mainData.getNum();
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + sNum));
                context.startActivity(myIntent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    } // 데이터를 입력 public void setArrayData(String strData) { arrayList.add(strData); }

    public static  class  ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        EditText list_name;
        EditText list_num;
        ImageView addBtn;
        ImageView btDelete;
        ImageView callBtn;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            callBtn = itemView.findViewById(R.id.bt_call);
            btDelete = itemView.findViewById(R.id.bt_delete);
            name = itemView.findViewById(R.id.reText);
            list_name = itemView.findViewById(R.id.list_name);
            list_num = itemView.findViewById(R.id.list_num);
            addBtn = itemView.findViewById(R.id.addBtn);
        }
    }
}

package com.example.notifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiliverAdapter extends RecyclerView.Adapter<DiliverAdapter.ViewHolder>{
    private ArrayList<Fragment2.whereData> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        TextView textView2;
        TextView textView3;
        ViewHolder(View itemView) {
            super(itemView) ;


            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.currentTime) ;
            textView2 = itemView.findViewById(R.id.currentWhere);
            textView3 = itemView.findViewById(R.id.currentState);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    DiliverAdapter(ArrayList<Fragment2.whereData> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public DiliverAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.dilivery, parent, false) ;
        DiliverAdapter.ViewHolder vh = new DiliverAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(DiliverAdapter.ViewHolder holder, int position) {
        Fragment2.whereData text = mData.get(position) ;
        holder.textView1.setText(text.timeString) ;
        holder.textView2.setText(text.whereString);
        holder.textView3.setText(text.kindString);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}

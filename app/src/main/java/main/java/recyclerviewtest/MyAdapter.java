package main.java.recyclerviewtest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import main.java.R;
import main.java.model.SearchHistory;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    List<SearchHistory> modelList=new ArrayList<>();

    @NonNull
    @Override
    // 뷰 홀더가 생성되었을 때
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        // 연결할 레이아웃 설정
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false));
    }

    @Override
    // 뷰와 뷰홀더가 묶였을 때(= 재활용이 됐을 때)
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position){
        holder.bind(modelList.get(position));
    }

    @Override
    // 리사이클러뷰의 item 개수
    public int getItemCount(){
        return modelList.size();
    }

    // 외부에서 데이터 넘기기
    public void submitList(List<SearchHistory> modelList){
        this.modelList = modelList;
    }
}

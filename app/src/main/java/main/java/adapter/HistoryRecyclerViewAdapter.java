package main.java.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import main.java.R;
import main.java.model.SearchHistory;
import main.java.repository.HistoryRepository;
import main.java.repository.LocalHistoryRepository;
import main.java.service.history.HistoryService;
import main.java.service.history.HistoryServiceImpl;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<SearchHistory> historyItemList;
    Context context;

    public HistoryRecyclerViewAdapter(List<SearchHistory> historyItemList, Context context) {
        this.historyItemList = historyItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryRepository historyRepository = new LocalHistoryRepository(context);
        HistoryService historyService = new HistoryServiceImpl(historyRepository);

        int itemIdx = holder.getAdapterPosition();

        HistoryItemViewHolder historyItemViewHolder = (HistoryItemViewHolder) holder;

        historyItemViewHolder.name.setText(historyItemList.get(itemIdx).getRecipeName());
        historyItemViewHolder.createTime.setText(historyItemList.get(itemIdx).getCreateDateTime());

        historyItemViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, (itemIdx + 1)+"번째 텍스트 뷰 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        historyItemViewHolder.removeHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyService.removeHistory(itemIdx + 1);
                deleteItem(itemIdx);
                Toast.makeText(context, (itemIdx + 1)+"번째 텍스트 뷰 삭제", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyItemList.size();
    }

    public static class HistoryItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        TextView name;
        TextView createTime;
        ImageButton removeHistoryBtn;

        public HistoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.history_item_name);
            createTime =  itemView.findViewById(R.id.history_item_createtime);
            itemLayout = itemView.findViewById(R.id.history_item_view);
            removeHistoryBtn = itemView.findViewById(R.id.remove_history_btn);
        }
    }

    // 아이템 삭제 이벤트 처리 메서드
    public void deleteItem(int position) {
        // 아이템 삭제 후 데이터 업데이트
        historyItemList.remove(position);
        // 리사이클러뷰에 아이템 삭제를 알림
        notifyItemRemoved(position);
        // 삭제된 아이템 이후에 있는 아이템들의 뷰 업데이트
        notifyItemRangeChanged(position, historyItemList.size() - position);
    }
}

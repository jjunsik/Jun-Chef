package main.java.recyclerviewtest;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import main.java.R;
import main.java.model.SearchHistory;

public class MyViewHolder extends RecyclerView.ViewHolder {
    private final TextView txtTest = itemView.findViewById(R.id.txt_test);

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    // 데이터와 뷰를 묶는다.
    public void bind(SearchHistory history) {
        txtTest.setText(history.getRecipeName() + " - " + history.getCreateDateTime());
    }
}

package main.java.adapter;

import static main.java.model.constant.ResultConstant.COOKING_ORDER;
import static main.java.model.constant.ResultConstant.INGREDIENTS;
import static main.java.model.constant.ResultConstant.RECIPE_NAME;
import static main.java.util.error.constant.ErrorConstant.getErrorFromMessage;

import android.app.Activity;
import android.content.Intent;
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
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import main.java.R;
import main.java.controller.ResultActivity;
import main.java.model.SearchHistory;
import main.java.model.SearchResult;
import main.java.repository.HistoryRepository;
import main.java.repository.LocalHistoryRepository;
import main.java.service.history.HistoryService;
import main.java.service.history.HistoryServiceImpl;
import main.java.service.recipe.GptRecipeService;
import main.java.service.recipe.RecipeService;
import main.java.util.LoadingDialog;
import main.java.util.error.ErrorFormat;
import main.java.util.error.dialog.ErrorDialog;
import main.java.util.http.HttpService;
import main.java.util.parser.GptResponseParser;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<SearchHistory> historyItemList;
    Activity activity;

    public HistoryRecyclerViewAdapter(List<SearchHistory> historyItemList, Activity activity) {
        this.historyItemList = historyItemList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryRepository historyRepository = new LocalHistoryRepository(activity);
        HistoryService historyService = new HistoryServiceImpl(historyRepository);
        RecipeService recipeService
                = new GptRecipeService(new HttpService(), new GptResponseParser(), historyService);

        final LoadingDialog loadingDialog = new LoadingDialog(activity);

        int itemIdx = holder.getAdapterPosition();

        String recipeName = historyItemList.get(itemIdx).getRecipeName();

        HistoryItemViewHolder historyItemViewHolder = (HistoryItemViewHolder) holder;

        historyItemViewHolder.name.setText(historyItemList.get(itemIdx).getRecipeName());
        historyItemViewHolder.createTime.setText(historyItemList.get(itemIdx).getCreateDateTime());

        historyItemViewHolder.itemLayout.setOnClickListener(v -> {
            loadingDialog.show();
            CompletableFuture<SearchResult> futureResult = recipeService.search(recipeName);

            futureResult.thenAccept(result -> {
                loadingDialog.dismiss();

                Intent goToResultActivity = new Intent(activity, ResultActivity.class);

                goToResultActivity.putExtra(RECIPE_NAME, result.getRecipeName());
                goToResultActivity.putExtra(INGREDIENTS, result.getIngredients());
                goToResultActivity.putExtra(COOKING_ORDER, result.getCookingOrder());

                activity.startActivity(goToResultActivity);
            }).exceptionally(ex -> {
                loadingDialog.dismiss();
                String message = Objects.requireNonNull(ex.getMessage());
                ErrorFormat result = getErrorFromMessage(message);

                // A 클래스에서 발생한 예외 처리
                activity.runOnUiThread(() -> {
                    ErrorDialog errorDialog = new ErrorDialog(activity, result);
                    errorDialog.show();
                });

                return null;
            });
        });

        historyItemViewHolder.removeHistoryBtn.setOnClickListener(v -> {
            historyService.removeHistory(itemIdx + 1);
            deleteItem(itemIdx);
            Toast.makeText(activity, "\"" + recipeName + "\" 삭제", Toast.LENGTH_SHORT).show();
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

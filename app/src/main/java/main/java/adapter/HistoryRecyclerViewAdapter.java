package main.java.adapter;

import static main.java.model.constant.ResultConstant.COOKING_ORDER;
import static main.java.model.constant.ResultConstant.INGREDIENTS;
import static main.java.model.constant.ResultConstant.RECIPE_NAME;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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

import main.java.R;
import main.java.controller.ResultActivity;
import main.java.model.history.SearchHistory;
import main.java.service.junchef.history.JunChefHistoryService;
import main.java.service.junchef.recipe.JunChefRecipeService;
import main.java.util.LoadingDialog;
import main.java.util.error.ErrorFormat;
import main.java.util.error.dialog.ErrorDialog;
import main.java.util.error.junchef.JunChefException;
import main.java.util.http.junchef.history.HistoryHttpService;
import main.java.util.http.junchef.recipe.RecipeHttpService;
import main.java.util.parser.junchef.history.HistoryResponseParser;
import main.java.util.parser.junchef.recipe.RecipeResponseParser;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<SearchHistory> historyItemList;
    private final Activity activity;
    private final Long memberId;

    public HistoryRecyclerViewAdapter(List<SearchHistory> historyItemList, Activity activity, Long memberId) {
        this.historyItemList = historyItemList;
        this.activity = activity;
        this.memberId = memberId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JunChefRecipeService junChefRecipeService = new JunChefRecipeService(new RecipeHttpService(activity), new RecipeResponseParser());
        JunChefHistoryService junChefHistoryService = new JunChefHistoryService(new HistoryHttpService(activity), new HistoryResponseParser());

        final LoadingDialog loadingDialog = new LoadingDialog(activity);

        int itemIdx = holder.getAdapterPosition();

        String recipeName = historyItemList.get(itemIdx).getRecipeName();

        HistoryItemViewHolder historyItemViewHolder = (HistoryItemViewHolder) holder;

        historyItemViewHolder.name.setText(historyItemList.get(itemIdx).getRecipeName());
        historyItemViewHolder.createTime.setText(historyItemList.get(itemIdx).getCreateDateTime());

        historyItemViewHolder.itemLayout.setOnClickListener(v -> {
            loadingDialog.show();

            junChefRecipeService.search(memberId, recipeName)
            .thenAcceptAsync(result -> {
                loadingDialog.dismiss();

                Intent goToResultActivity = new Intent(activity, ResultActivity.class);

                goToResultActivity.putExtra(RECIPE_NAME, result.getRecipeName());
                goToResultActivity.putExtra(INGREDIENTS, result.getIngredients());
                goToResultActivity.putExtra(COOKING_ORDER, result.getCookingOrder());

                activity.startActivity(goToResultActivity);
            })
            .exceptionally(ex -> {
                loadingDialog.dismiss();

                if (ex != null) {
                    Log.d("junchef", "최근 검색어로 레시피 조회 실패(HistoryRecyclerViewAdapter)" + ex.getClass().getName());
                    JunChefException junChefException;

                    if (ex.getCause() instanceof JunChefException) {
                        Log.d("junchef", "준쉐프 예외임");

                        junChefException = (JunChefException) ex.getCause();
                        Log.d("junchef", "예외 잘 받음(HistoryRecyclerViewAdapter)" + Objects.requireNonNull(junChefException).getCode() + junChefException.getTitle() + junChefException.getMessage());

                        ErrorFormat errorFormat = new ErrorFormat(junChefException.getTitle(), junChefException.getMessage());
                        Log.d("junchef", "에러 포맷 완성" + errorFormat.getTitle() + errorFormat.getMessage());

                        activity.runOnUiThread(() -> {
                            Log.d("junchef", "에러 다이얼로그 객체 만들기 전");
                            ErrorDialog errorDialog = new ErrorDialog(activity, errorFormat);

                            Log.d("junchef", "에러 다이얼로그 객체 만들고 시작 전" + errorDialog);
                            errorDialog.show();
                        });
                    }
                }

                return null;
            });
        });

        historyItemViewHolder.removeHistoryBtn.setOnClickListener(v -> {
        // 비동기적으로 historyId를 얻어옴
        junChefHistoryService.getHistoryId(memberId, recipeName)
            .thenAcceptAsync(historyId -> {
            // historyId를 사용하여 비동기적으로 히스토리 삭제
            junChefHistoryService.deleteHistory(historyId)
                .thenAcceptAsync(result -> {
                    // UI 업데이트
                    Log.d("junchef", "최근 검색어 삭제 " + historyId);

                    activity.runOnUiThread(() -> {
                        deleteItem(itemIdx);

                        // 토스트 메시지 표시
                        Toast.makeText(activity, "\"" + recipeName + "\" 삭제", Toast.LENGTH_SHORT).show();
                    });
                })
                .exceptionally(ex -> {
                    // 삭제 실패 시 예외 처리
                    // 예외 처리 코드 추가
                    if (ex != null) {
                        Log.d("junchef", "최근 검색어 삭제 실패 (HistoryRecyclerViewAdapter)" + ex.getClass().getName());

                        JunChefException junChefException;

                        if (ex.getCause() instanceof JunChefException) {
                            Log.d("junchef", "준쉐프 예외임");

                            junChefException = (JunChefException) ex.getCause();
                            Log.d("junchef", "예외 잘 받음(HistoryRecyclerViewAdapter)" + Objects.requireNonNull(junChefException).getCode() + junChefException.getTitle() + junChefException.getMessage());

                            ErrorFormat errorFormat = new ErrorFormat(junChefException.getTitle(), junChefException.getMessage());
                            Log.d("junchef", "에러 포맷 완성" + errorFormat.getTitle() + errorFormat.getMessage());

                            activity.runOnUiThread(() -> {
                                Log.d("junchef", "에러 다이얼로그 객체 만들기 전");
                                ErrorDialog errorDialog = new ErrorDialog(activity, errorFormat);

                                Log.d("junchef", "에러 다이얼로그 객체 만들고 시작 전" + errorDialog);
                                errorDialog.show();
                            });
                        }
                    }

                    return null;
                });
            })
            .exceptionally(ex -> {
                // historyId 얻어오기 실패 시 예외 처리
                // 예외 처리 코드 추가
                if (ex != null) {
                    Log.d("junchef", "historyId 얻지 못함 (HistoryRecyclerViewAdapter)" + ex.getClass().getName());

                    JunChefException junChefException;

                    if (ex.getCause() instanceof JunChefException) {
                        Log.d("junchef", "준쉐프 예외임");

                        junChefException = (JunChefException) ex.getCause();
                        Log.d("junchef", "예외 잘 받음(HistoryRecyclerViewAdapter)" + Objects.requireNonNull(junChefException).getCode() + junChefException.getTitle() + junChefException.getMessage());

                        ErrorFormat errorFormat = new ErrorFormat(junChefException.getTitle(), junChefException.getMessage());
                        Log.d("junchef", "에러 포맷 완성" + errorFormat.getTitle() + errorFormat.getMessage());

                        activity.runOnUiThread(() -> {
                            Log.d("junchef", "에러 다이얼로그 객체 만들기 전");
                            ErrorDialog errorDialog = new ErrorDialog(activity, errorFormat);

                            Log.d("junchef", "에러 다이얼로그 객체 만들고 시작 전" + errorDialog);
                            errorDialog.show();
                        });
                    }
                }

                return null;
            });
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
        Log.d("junchef", "deleteItemPosition: " + position);

        // 아이템 삭제 후 데이터 업데이트
        historyItemList.remove(position);
        Log.d("junchef", "아이템 삭제 후 데이터 업데이트");

        // 리사이클러뷰에 아이템 삭제를 알림
        notifyItemRemoved(position);
        Log.d("junchef", "아이템 삭제 리사클러뷰에 알림");

        // 삭제된 아이템 이후에 있는 아이템들의 뷰 업데이트
        notifyItemRangeChanged(position, historyItemList.size() - position);
        Log.d("junchef", "아이템들의 뷰 업데이트");
    }
}

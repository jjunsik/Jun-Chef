package main.java.service.history;

import java.util.List;

import main.java.model.SearchHistory;
import main.java.repository.HistoryRepository;

public class HistoryServiceImpl implements HistoryService {
    // 로컬 저장소에서 가져올 때 예외가 생긴다면 try-catch 문에서 예외 처리 해주는 클래스
    private final HistoryRepository historyRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<SearchHistory> getSearchHistories(int limit) {
        // 히스토리를 로컬 저장소에서 가져옴
        int searchHistoryCount = historyRepository.getSearchHistoryCount();

        if (searchHistoryCount == 0)
            return null;

        if (limit > searchHistoryCount)
            return historyRepository.getSearchHistoriesByLimit(searchHistoryCount);

        return historyRepository.getSearchHistoriesByLimit(limit);
    }

    @Override
    public void addHistory(SearchHistory searchHistory) {
        // 히스토리를 저장 (Local 저장소에)
        historyRepository.addHistory(searchHistory);
        historyRepository.addSearchHistoryCount();
    }

    @Override
    public void removeHistory(int index) {
        // 히스토리를 삭제 (Local 저장소)
        historyRepository.removeHistory(index);
        historyRepository.subtractHistoryCount();
    }

}

package main.java.service.history;

import java.util.List;

import main.java.model.SearchHistory;
import main.java.repository.HistoryRepository;

public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<SearchHistory> getSearchHistories(int limit) {
        int searchHistoryCount = historyRepository.getSearchHistoryCount();

        if (searchHistoryCount == 0)
            return null;

        if (limit > searchHistoryCount)
            return historyRepository.getSearchHistoriesByLimit(searchHistoryCount);

        return historyRepository.getSearchHistoriesByLimit(limit);
    }

    @Override
    public void addHistory(SearchHistory searchHistory) {
        historyRepository.addHistory(searchHistory);
        historyRepository.addSearchHistoryCount();
    }

    @Override
    public void removeHistory(int index) {
        historyRepository.removeHistory(index);
        historyRepository.subtractHistoryCount();
    }

}

package com.campus.lostfound.util;

import java.util.Stack;

/**
 * 📚 SearchHistoryStack
 * Demonstrates Unit 4: Java Collections Framework (Java Stack)
 * Supports recent keyword tracking, undo, and peek operations.
 */
public class SearchHistoryStack {

    // Java Collections Stack (Unit 4)
    private final Stack<String> searchHistory = new Stack<>();

    public void pushSearch(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            searchHistory.push(keyword.trim());
        }
    }

    public String popLastSearch() {
        if (!searchHistory.isEmpty()) {
            return searchHistory.pop();
        }
        return null;
    }

    public String peekRecentSearch() {
        if (!searchHistory.isEmpty()) {
            return searchHistory.peek();
        }
        return "No recent searches";
    }

    public int getHistorySize() {
        return searchHistory.size();
    }

    public void clearHistory() {
        searchHistory.clear();
    }
}

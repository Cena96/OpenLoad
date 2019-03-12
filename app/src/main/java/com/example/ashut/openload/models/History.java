
package com.example.ashut.openload.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class History {

    @SerializedName("results")
    @Expose
    private List<HistoryResult> historyResults = null;

    public History(List<HistoryResult> results) {
        super();
        this.historyResults = results;
    }

    public List<HistoryResult> getHistoryResults() {
        return historyResults;
    }

    public void setHistoryResults(List<HistoryResult> historyResults) {
        this.historyResults = historyResults;
    }
}

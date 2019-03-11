
package com.example.ashut.openload.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class History {

    @SerializedName("historyResults")
    @Expose
    private List<HistoryResult> historyResults = null;

    public List<HistoryResult> getHistoryResults() {
        return historyResults;
    }

    public void setHistoryResults(List<HistoryResult> historyResults) {
        this.historyResults = historyResults;
    }
    public History(List<HistoryResult> results){
        historyResults=results;
    }
}

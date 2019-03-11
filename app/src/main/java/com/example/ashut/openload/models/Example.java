
package com.example.ashut.openload.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Example {

    @SerializedName("profileResults")
    @Expose
    private List<ProfileResult> profileResults = null;

    public List<ProfileResult> getProfileResults() {
        return profileResults;
    }

    public void setProfileResults(List<ProfileResult> profileResults) {
        this.profileResults = profileResults;
    }

}

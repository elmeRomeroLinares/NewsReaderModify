package com.example.newsreader.model;

import java.util.List;

public class Results_ {

    private List<Results> results;

    Results_(List<Results> results) {
        this.results = results;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }
}

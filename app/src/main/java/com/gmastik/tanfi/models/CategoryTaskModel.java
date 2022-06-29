package com.gmastik.tanfi.models;

public class CategoryTaskModel {
    private String tags;

    public CategoryTaskModel() {
    }

    public CategoryTaskModel(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}

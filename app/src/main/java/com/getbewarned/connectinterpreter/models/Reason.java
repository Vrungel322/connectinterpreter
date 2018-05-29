package com.getbewarned.connectinterpreter.models;

public class Reason {
    private String slug;
    private String label;

    public Reason(String slug, String label) {
        this.slug = slug;
        this.label = label;
    }

    public String getSlug() {
        return slug;
    }

    public String getLabel() {
        return label;
    }
}

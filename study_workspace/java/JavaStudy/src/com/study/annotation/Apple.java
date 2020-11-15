package com.study.annotation;

public class Apple {

    @FruitName
    private String appleName;
    
    @FruitColor
    private String appleColor;
    
    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }
    public String getAppleColor() {
        return appleColor;
    }
    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }
    public String getAppleName() {
        return appleName;
    }
    
    @Override
    public String toString() {
        return "name: " + appleName + ", color: " + appleColor;
    }
}

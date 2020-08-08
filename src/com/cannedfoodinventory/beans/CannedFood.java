package com.cannedfoodinventory.beans;

import java.util.Objects;

public class CannedFood {
    private String id;
    private String description;
    private MyDate expiryDate;

    public CannedFood(String id, String description, int year, int month, int day) {
        this.id = id;
        this.description = description;
        this.expiryDate = new MyDate(day, month, year);
    }

    public boolean isExpired() {
        return expiryDate.isBefore();
    }

    @Override
    public String toString() {
        return ""+id+","+description+","+expiryDate.getYear()+","+expiryDate.getMonth()+","+expiryDate.getDay();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CannedFood that = (CannedFood) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, expiryDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MyDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(MyDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}

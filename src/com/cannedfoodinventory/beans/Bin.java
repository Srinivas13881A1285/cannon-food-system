package com.cannedfoodinventory.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Bin {
    private String location;
    private String description;
    private CannedFood[] stock;

    public static final int size = 10;
    private int stockIndex = -1;

    public Bin(String location, String description) {
        this.stock = new CannedFood[10];
        this.location = location;
        this.description = description;
    }

    public boolean addCannedFood(CannedFood cannedFood) {
        if (stockIndex >= size - 1){
            System.out.println("Already " + cannedFood.getDescription() + " Bin is full");
            return false;
        }

        stock[++stockIndex] = cannedFood;
        System.out.println("Successfully added the food "+cannedFood+"");
        System.out.println("Stock occupied size in " + cannedFood.getDescription() + " Bin now : " + (stockIndex + 1));
        return true;
    }

    public CannedFood getCannedFood() {
        if (stockIndex == -1){
            System.out.println("No stock in this bin");
            return null;
        }
        
        ArrayList<CannedFood> arrList = new ArrayList<>(Arrays.asList(stock));
       
        CannedFood nearestExpireDateFood = getNearestExpireDateFood(stock);

        if (nearestExpireDateFood != null) {
        	arrList.remove(nearestExpireDateFood);
        	System.out.println("Successfully removed nearest expiry canned food "+nearestExpireDateFood);
        	 stockIndex--;
             
        	 System.out.println("Stock size in bin now : " + (stockIndex + 1));
             System.out.println("Stock empty size bin now :"+(size - (stockIndex + 1)));
             for(int i = 0 ; i < arrList.size();i++) {
             	stock[i] = arrList.get(i);
             }
             	return nearestExpireDateFood;
        }else {
        	System.out.println("not present any nearest expiring food so deleting last canned food in stock");
        	CannedFood food = arrList.get(arrList.size() -  1);
        	arrList.remove(food);
        	 stockIndex--;
             
             System.out.println("Stock size in bin now : " + (stockIndex + 1));
             System.out.println("Stock empty size bin now :"+(size - (stockIndex + 1)));
             for(int i = 0 ; i < arrList.size();i++) {
             	stock[i] = arrList.get(i);
             }
             return food;
        }
        
       
    }

    public void removeCannedFood(List<CannedFood> expiryFoods){
       for(CannedFood cannedFood : expiryFoods) {
           System.out.println("Removed the expired canned food "+cannedFood); 
       }
       
       ArrayList<CannedFood> list = new ArrayList<>();
       for(int i = 0; i < size; i++) {
    	   list.add(stock[i]);
       }
       
       list.removeAll(expiryFoods);
       
       stock = list.toArray(stock);
       
       stockIndex = stockIndex - expiryFoods.size();
    }

    private CannedFood getNearestExpireDateFood(CannedFood[] stock) {
        Comparator<CannedFood> yearComparator = (food1, food2) -> food1.getExpiryDate().getYear() - food2.getExpiryDate().getYear();
        Comparator<CannedFood> monthComparator = (food1, food2) -> food1.getExpiryDate().getMonth() - food2.getExpiryDate().getMonth();
        Comparator<CannedFood> dayComparator = (food1, food2) -> food1.getExpiryDate().getDay() - food2.getExpiryDate().getDay();


        Optional<CannedFood> max = Stream.of(stock).filter(food -> food != null).min(yearComparator.thenComparing(monthComparator.thenComparing(dayComparator)));
        if (max.isPresent())
            return max.get();
        else
            return null;
    }


    @Override
    public String toString() {
        return "Bin{" +
                "location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + Arrays.toString(stock) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bin bin = (Bin) o;
        return stockIndex == bin.stockIndex &&
                Objects.equals(location, bin.location) &&
                Objects.equals(description, bin.description) &&
                Arrays.equals(stock, bin.stock);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(location, description);
        result = 31 * result + Arrays.hashCode(stock);
        return result;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CannedFood[] getStock() {
        return stock;
    }

    public void setStock(CannedFood[] stock) {
        this.stock = stock;
    }
}

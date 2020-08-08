package com.cannedfoodinventory.main;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.cannedfoodinventory.beans.Bin;
import com.cannedfoodinventory.beans.CannedFood;

public class CannedFoodWareHouse {

    private Bin[] bins;

    public CannedFoodWareHouse() {
        this.bins = new Bin[3];
        bins[0] = new Bin("A1", "Beans");
        bins[1] = new Bin("A2", "Sardine");
        bins[2] = new Bin("A3", "Soup");
        loadInitialDataFromFile();
    }

    public void loadInitialDataFromFile() {
        System.out.println("Loading data from file \n");

        FileReader fileReader = getFileReader();
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                CannedFood cannedFood = getCannedFood(line);

                addCannedFoodToSuitableBin(cannedFood);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error in reading file \n");
        }
    }

    private boolean addCannedFoodToSuitableBin(CannedFood cannedFood) {
        for (Bin bin : bins) {
            if (bin.getDescription().equals(cannedFood.getDescription())) {
                return bin.addCannedFood(cannedFood);
            }
        }
        return false;
    }

    private CannedFood getCannedFood(String line) {
        String cannonFoodDetails[] = line.split(",");

        String id = cannonFoodDetails[0].trim();
        String description = cannonFoodDetails[1].trim();
        int year = Integer.parseInt(cannonFoodDetails[2].trim());
        int month = Integer.parseInt(cannonFoodDetails[3].trim());
        int day = Integer.parseInt(cannonFoodDetails[4].trim());

        return new CannedFood(id, description, year, month, day);
    }

    private FileReader getFileReader() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(".//src//food.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File is not present\n");
        }
        return fileReader;
    }


    public void insertNewCannedFood() {
        String description = readDescriptionType();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please Enter id        :       ");
        String id = scanner.next();

        while (!isUniqueId(id.trim())){
            System.out.println("Canned Food with id " + id + " already exists ! Please enter new unique Id");
            System.out.println("Please Enter id        :       ");
            id = scanner.next();
        }

        System.out.println("Please enter Expiry date : ");

        System.out.println("year :");
        int year = scanner.nextInt();

        while(!isValidYear(year)){
            System.out.println("Please enter valid year");
            System.out.println("year :");
            year = scanner.nextInt();
        }

        System.out.println("month :");
        int month = scanner.nextByte();

        while(!isValidMonth(month)){
            System.out.println("Please enter valid month");
            System.out.println("month :");
            month = scanner.nextInt();
        }

        System.out.println("day");
        int day = scanner.nextByte();

        while(!isValidDay(day)){
            System.out.println("Please enter valid day");
            System.out.println("day :");
            day = scanner.nextInt();
        }

        CannedFood newCannedFood = new CannedFood(id, description, year, month, day);
        addCannedFoodToSuitableBin(newCannedFood) ;
            
    }

    private boolean isUniqueId(String id) {
        for (Bin bin : bins) {
            CannedFood[] stock = bin.getStock();
            for (CannedFood cannedFood : stock) {
                if (cannedFood != null && cannedFood.getId().equals(id))
                    return false;
            }
        }
        return true;
    }

    public void listCannedFoodInfo() {
        String description = readDescriptionType();
        for (Bin bin : bins) {
            if (bin.getDescription().equals(description)) {
            	System.out.println("Bin Type is : "+bin.getDescription());
            	int occupied = 0;
            	int emptySize = 0;
                for(CannedFood food : bin.getStock()) {
                	if(food != null) {
                		System.out.println(food);
                			occupied++;
                	}
                	else {
                		System.out.println("Empty");
                			emptySize++;
                	}
                }
                System.out.println("Stack Occupied size is "+occupied);
                System.out.println("Stack Empty size is "+emptySize);
                    
            }
        }
    }

    private String readDescriptionType() {
        System.out.println("Select Description type     :   ");
        System.out.println("Enter 1 for Beans");
        System.out.println("Enter 2 for Sardine");
        System.out.println("Enter 3 for Soup");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        while(!(option >= 1 && option <=3)){
            System.out.println("Please enter valid option");
            option = scanner.nextByte();
        }

        String description = "";

        if (option == 1)
            description = "Beans";
        else if (option == 2)
            description = "Sardine";
        else if (option == 3)
            description = "Soup";
        return description;
    }

    public void removeCannedFood() {
        String description = readDescriptionType();
        for (Bin bin : bins) {
            if (bin.getDescription().equals(description)) {
             bin.getCannedFood();    
            }
        }
    }

    public void removeExpiredCannedFood() {   
        for (Bin bin : bins) {
           List<CannedFood> expiryFoods = new ArrayList<>();
           for(CannedFood food : bin.getStock()) {
        	   if(food != null && food.isExpired())
        		   expiryFoods.add(food);
           }
           bin.removeCannedFood(expiryFoods);
        }
    }
    
 
    public void exitAndSave(CannedFoodWareHouse cannedFoodWareHouse) {
        Path path = Paths.get(".//src//food.txt");
        clearOutputFile(path);
        writeToFile(cannedFoodWareHouse,path);
    }

    private void clearOutputFile(Path path) {
    	try {
			FileChannel.open(path, StandardOpenOption.WRITE).truncate(0).close();
		} catch (IOException e) {
			System.out.println("Error while deleting the output file");
		}
    }
    private void writeToFile(CannedFoodWareHouse cannedFoodWareHouse,Path path) {
        try {
            for(Bin bin : cannedFoodWareHouse.bins){
                for(CannedFood food : bin.getStock()){
                    if(food != null){
                        String content = food.toString() + "\n";
                        Files.write(path,content.getBytes(), StandardOpenOption.APPEND);
                    }
                }
            }
        } catch (IOException e) {
           System.out.println("Error in writing to the file "+path);
        }
        System.out.println("All your changes are saved in file output.txt");
    }

    public static void main(String[] args) {
        CannedFoodWareHouse cannedFoodWareHouse = new CannedFoodWareHouse();
        System.out.println("Initial Ware House Data Loaded from the File : \n");
        cannedFoodWareHouse.printCannedWareHouseToConsole(cannedFoodWareHouse);

        System.out.println("Please choose the menu to get started !\n");
        System.out.println("INSERT,LIST,REMOVE,REMOVE EXPIRED OR EXIT AND SAVE");


        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();

        while(!option.trim().equalsIgnoreCase("EXIT AND SAVE")){
            switch (option.trim().toUpperCase()) {
                case "INSERT":
                    cannedFoodWareHouse.insertNewCannedFood();
                    break;
                case "LIST":
                    cannedFoodWareHouse.listCannedFoodInfo();
                    break;
                case "REMOVE":
                    cannedFoodWareHouse.removeCannedFood();
                    break;
                case "REMOVE EXPIRED":
                    cannedFoodWareHouse.removeExpiredCannedFood();
                    break;
                default:
                    System.out.println("Invalid option");
            }
            System.out.println("SELECT YOUR OPTION AGAIN");
            System.out.println("INSERT,LIST,REMOVE,REMOVE EXPIRED OR EXIT AND SAVE");
            option = scanner.nextLine();
        }
        cannedFoodWareHouse.exitAndSave(cannedFoodWareHouse);
        System.out.println("Canned Food Inventory System is terminating !! Bye ");
    }

    @Override
    public String toString() {
        return "CannedFoodWareHouse{" +
                "bins=" + Arrays.toString(bins) +
                '}';
    }

    private boolean isValidYear(int year){
        return year >= 2010 && year <= 2022;
    }

    private boolean isValidMonth(int month){
        return (month >=1 && month <= 12);
    }

    private boolean isValidDay(int day){
        return day >=1 && day <= 30;
    }

    private void printCannedWareHouseToConsole(CannedFoodWareHouse cannedFoodWareHouse){
        for(Bin bin : cannedFoodWareHouse.bins){
        	System.out.println("Bin Type : "+bin.getDescription());
            for(CannedFood food : bin.getStock()){
                if(food != null)
                    System.out.println(food);
                else
                	System.out.println("empty");
            }
            System.out.println();
        }
    }
}

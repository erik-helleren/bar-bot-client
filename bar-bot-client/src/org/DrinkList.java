package org;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.*;
import org.json.simple.parser.*;

public class DrinkList {
	Set<Drink> drinkList;

	public DrinkList() {
		drinkList=new HashSet<>();
	}

	/**
	 * read, line by line in a file of JSON and populate the drink set.
	 * Can be called multiple times, will remove duplicate drinks, by name,
	 * Automatically.  (Most recent drink saved)
	 */
	public void loadFromFile(String filename) {
		try (BufferedReader in = new BufferedReader(new FileReader(filename));) {
			String line=in.readLine();
			JSONParser parser=new JSONParser();
			while(line!=null){
				
				try {
					drinkList.add(new Drink((JSONObject)parser.parse(line)));
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("JSON file curoupted");
				}
				line=in.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("JSON file not found");
		}
	}
	
	
	public void writeToFile(String filename){
		try (PrintWriter out = new PrintWriter(filename);){
			for(Drink d:drinkList){
				out.println(d.toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Failed to create output file");
		}
	}
	
	public void add(Drink drink){
		drinkList.add(drink);
	}
	
	public Set<Drink> getDrinkSet(){
		return drinkList;
	}
}

package org;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ClientModel {

	private DrinkList drinkList;
	private List<String> userIngredients;
	
	private ConfigInterface ci;
	
	private int password;
	
	ClientModel(){
		ci = new ClientConfig();
		DrinkList drinkList = new DrinkList();
		drinkList.loadFromFile("DrinkDatabase");
		this.drinkList = drinkList;
		
		userIngredients = new ArrayList<String>();
		try {
			loadIngredients("ingredients.txt");
		//	loadConfiguration("config");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	Drink getDrink(String name){
		for(Drink d:drinkList.getDrinkSet()){
			if(d.getName().equals(name)){
				return d;
			}
		}
		return null;
	}
	
	void addDrink(Drink d){
		drinkList.getDrinkSet().add(d);
	}
	
	void removeDrink(Drink d){
		drinkList.getDrinkSet().remove(d);
	}
	
	void writeToFile(){
		drinkList.writeToFile("DrinkDatabase");
	}
	
	String getDrinkName(int i){
		int n = 0;
		for(Drink d:drinkList.getDrinkSet()){
			if (n == i){
				return d.getName();
			}
			n++;
		}
		return "";
	}
	
	List<String> getDrinkSubstring(String s){
		List<String> nameList = new ArrayList<String>();
		for(Drink d:drinkList.getDrinkSet()){
			if(d.getName().toLowerCase().indexOf(s.toLowerCase()) != -1){
				nameList.add(d.getName());
			}
		}
		return nameList;
	}
	
	List<String> getIngredientSubstring(String s){
		List<String> ingredientList = new ArrayList<String>();
		for(Drink d:drinkList.getDrinkSet()){
			for(String ingredient:d.getIngredients().keySet()){
				if(ingredient.toLowerCase().indexOf(s.toLowerCase()) != -1){
					ingredientList.add(d.getName());
				}
			}
		}
		return ingredientList;
	}
	
	List<String> getIngredientsList(){
		return userIngredients;
	}
	
	void setIP(String s){
		ci.setArduinoIP(s);
	}
	
	void setPassword(int i){
		password = i;
	}
	
	int getPassword(){
		return password;
	}
	
	void setPumpID(int i, String s){
		ci.setPumpID(i, s);
	}
	
	int getPumpID(String s){
		return ci.getPumpNumber(s);
	}
	
	ConfigInterface getConfigInterface(){
		return ci;
	}
	
	public void loadIngredients(String fileName) throws FileNotFoundException{
		userIngredients.clear();
		File f = new File(fileName);
		if(f.exists() && !f.isDirectory()){
			try {
				FileInputStream fileIn = new FileInputStream(fileName);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				userIngredients = (ArrayList<String>) in.readObject();
				in.close();
				fileIn.close();
				
				
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
	public void saveIngredients (String fileName) throws FileNotFoundException{
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out= new ObjectOutputStream(fileOut);
			out.writeObject(userIngredients);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	/**
	 * Saves the ingredients currently stored in userIngredients
	 * to fileName
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void saveConfiguration (String fileName) throws FileNotFoundException{
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out= new ObjectOutputStream(fileOut);
			out.writeObject(ci);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	/**
	 * Loads the ingredients currently stored in the file fileName
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void loadConfiguration(String fileName) throws FileNotFoundException{
		//ci.clear();
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			ci =  (ConfigInterface) in.readObject();
			in.close();
			fileIn.close();
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

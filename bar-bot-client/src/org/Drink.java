package org;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class Drink {
	private String name;
	private Map<String,Integer> ingredients;
	
	private JSONObject thisJSON;
	
	public Drink(String name,Map<String,Integer> ingerdients){
		this.name=name;
		this.ingredients=ingredients;
		//build JSON object using the inputs.
		thisJSON=new JSONObject();
		thisJSON.put("DrinkName", name);
		thisJSON.put("Ingredients", ingredients);
	}
	
	public Drink(JSONObject input){
		thisJSON=input;
		this.name=(String) thisJSON.get("DrinkName");
		ingredients=(Map) thisJSON.get("Ingredients");
	}
	
	public Map<String,Integer> getIngredients(){
		return this.ingredients;
	}
	public String getName(){
		return this.name;
	}
	/**
	 * checks to see if this Drink can be made using the fluids that are
	 * Available.
	 * @param fluidsAvailable A list of all the fluids available on the server.
	 * @return  true if all ingredients in the drink are also in the
	 * provide list
	 */
	public boolean canMakeDrink(List<String> fluidsAvailable){
		for(String ingredient:this.ingredients.keySet()){
			if(!fluidsAvailable.contains(ingredient))
				return false;
		}
		return true;
	}
	
	/**
	 * get the byte array used to send to the arduino server
	 * @param input An interface for the config screen
	 * @return
	 * @throws Exception if an ingredient in the drink recipe is not configured
	 * as attached to the arduino.  
	 */
	public byte[] getByteArray(ConfigInterface input) throws Exception{
		byte[] out=new byte[this.ingredients.size()+2];
		out[0]=1;
		out[1]=(byte) this.ingredients.size();
		int pos=1;
		for(String ingredient:ingredients.keySet()){
			int pumpID=input.getPumpID(ingredient);
			if(pumpID==-1)
				throw new Exception("Failed to find ingredient attached to server, cannot make drink");
			out[pos++]=(byte) pumpID;
			out[pos++]=(byte)((int)ingredients.get(ingredient));
		}
		return out;
	}
	public JSONObject getJSON(){
		return thisJSON;
	}
	
	public String toString(){
		return thisJSON.toString();
	}
}

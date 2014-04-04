package org;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class Drink implements Comparable<Drink>{
	private String name;
	private Map<String,Integer> ingredients;
	
	private JSONObject thisJSON;
	
	public Drink(String _name,Map<String,Integer> _ingredients){
		name=_name;
		ingredients=_ingredients;
		//build JSON object using the inputs.
		thisJSON=new JSONObject();
		thisJSON.put("DrinkName", name);
		JSONObject ingJSON=new JSONObject();
		for(String ingredient:ingredients.keySet()){
			ingJSON.put(ingredient,ingredients.get(ingredient));
		}
		thisJSON.put("Ingredients", ingJSON);
	}
	
	public Drink(JSONObject jsonObject){
		thisJSON=jsonObject;
		this.name=(String) thisJSON.get("DrinkName");
		JSONObject ingJSON = new JSONObject();
		ingJSON = (JSONObject) thisJSON.get("Ingredients");
		Map<String,Integer> ingredientMap = (Map<String, Integer>)ingJSON;
		ingredients=ingredientMap;//(Map<String, Integer>) thisJSON.get("Ingredients");
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
		byte[] out=new byte[this.ingredients.size()*2+2];
		out[0]=1;
		out[1]=0;
		int pos=2;
		for(String ingredient:ingredients.keySet()){
			int pumpID=input.getPumpID(ingredient);
			if(pumpID==-1)
				throw new Exception("Failed to find ingredient attached to server, cannot make drink");
			out[pos++]=(byte) pumpID;
			out[pos++]=(byte)((int)ingredients.get(ingredient));
			out[1]++;
		}
		return out;
	}
	public JSONObject getJSON(){
		return thisJSON;
	}
	
	public String toString(){
		return thisJSON.toString();
	}

	@Override
	public int compareTo(Drink o) {
		return this.name.compareTo(o.name);
	}
	public int hashCode(){
		return name.hashCode();
	}
	
	public boolean equals(Drink o){
		boolean out=true;
		out=out&&(this.name.equals(o.name));
		for(String ingredient:this.ingredients.keySet()){
			Integer a=this.ingredients.get(ingredient);
			Integer b=o.ingredients.get(ingredient);
			if(b==null) 
				out=false;
			else
				out=out&&(a==b);
		}
		this.ingredients.keySet().equals(o.ingredients.keySet());
		return out;
	}
}

package org;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class dummyMain {
	
	public static void main(String[] args) throws Exception{
		ConfigInterface ci=new dummyConfig("192.168.2.3");
		Map<String,Integer> ingredients=new HashMap<>();
		ingredients.put("Vodka",45);
		ingredients.put("Gin",46);
		ingredients.put("Orange Juice",60);
		Drink vgoj=new Drink("V G and OJ",ingredients);
		System.out.println(vgoj.getJSON().toString());
		//Test serlization
		Drink vgoj2=new Drink((JSONObject)JSONValue.parse(vgoj.getJSON().toJSONString()));
		System.out.println(vgoj2.toString());
		
		ingredients=new HashMap<>();
		ingredients.put("Vodka",42);
		ingredients.put("Vermoth",10);
		Drink martini=new Drink("Martini",ingredients);
		System.out.println(martini.getJSON().toString());
		
		System.out.println("Making Drinks");
		int a=ArduinoComunicator.makeDrink(vgoj,ci);
		System.out.println("Made 1 drink"+a);
		int b=ArduinoComunicator.makeDrink(martini,ci);
		System.out.println("Made 2 drinks"+b);
		
	}
}

package org;

import java.util.HashMap;
import java.util.Map;

public class dummyMain {
	
	public static void main(String[] args) throws Exception{
		ConfigInterface ci=new dummyConfig("192.168.2.20");
		Map<String,Integer> ingredients=new HashMap<>();
		ingredients.put("Vodka",45);
		ingredients.put("Gin",45);
		ingredients.put("Orange Juice",60);
		Drink vgoj=new Drink("V G and OJ",ingredients);
		System.out.println(vgoj.getJSON().toString());
		
		ingredients=new HashMap<>();
		ingredients.put("Vodka",45);
		ingredients.put("Vermoth",10);
		Drink martini=new Drink("Martini",ingredients);
		System.out.println(martini.getJSON().toString());
		
		ArduinoComunicator.makeDrink(vgoj,ci);
		ArduinoComunicator.makeDrink(martini,ci);
		
	}

}

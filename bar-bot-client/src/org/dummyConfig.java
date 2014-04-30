package org;

import java.util.HashMap;
import java.util.Map;

public class dummyConfig implements ConfigInterface {
    int counter=0;
    Map<String, Integer> pairings;
    String arduinoIP;
	
	public dummyConfig(String aIP){
		pairings=new HashMap<>();
		arduinoIP=aIP;
	}
	@Override
	public int getPumpID(String name) {
		Integer out=pairings.get(name);
		if(out==null){
			out=counter++;
			pairings.put(name,out);
		}
		return out;
	}
	
	@Override
	public void setPumpID(int id, String name) {
		pairings.put(name, id);
	}

	@Override
	public String getArduinoIP() {
		return arduinoIP;
	}
	
	public void clear(){
		pairings.clear();
		arduinoIP = "";
		counter = 0;
	}
	
	

}

package org;

import java.util.HashMap;
import java.util.Map;

public class ClientConfig implements ConfigInterface {
	//static final long serialVersionUID = 0l;
	Map<Integer, String> ingredients;
	String ipaddress;
	int password;
	
	public ClientConfig() {
		ipaddress = null;
		ingredients = new HashMap<Integer,String>();
	}
	
	public ClientConfig(String ip) {
		ipaddress = ip;
		ingredients = new HashMap<Integer,String>();
	}
	@Override
	public int getPumpID(String name) {
		for(Integer i : ingredients.keySet()) {
			if(ingredients.get(i).equalsIgnoreCase(name)) return i;
		}
		
		return -1;
	}

	@Override
	public void setPumpID(int id, String name) {
		ingredients.put(id, name);
		// TODO Auto-generated method stub

	}

	@Override
	public String getArduinoIP() {
		// TODO Auto-generated method stub
		return ipaddress;
	}

	public void setArduinoIP(String ip) {
		// TODO Auto-generated method stub
		ipaddress = ip;
	}

	@Override
	public void clear() {
		ingredients.clear();
		ipaddress = "";
	}

	@Override
	public int getPumpNumber(String s) {
		// TODO Auto-generated method stub
		return getPumpID(s);
	}
	
	public byte[] getPassword() {
		byte[] output = new byte[4];
		output[0] = (byte)(password>>24);
		output[1] = (byte)(password>>16);
		output[2] = (byte)(password>>8);
		output[3] = (byte)(password>>0);
		return output;
	}
	
	public void setPassword(int pw) {
		password = pw;
	}

}

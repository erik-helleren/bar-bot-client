//package org;

public interface ConfigInterface {
	/**
	 * Get the pump ID that corasponsds to
	 * the fluid in the name. 
	 * @param name the name of the fluid to get the ID of
	 * @return the pump id or -1 if entry not found
	 */
	public int getPumpID(String name);
	
	/**
	 * return the arduino's ip address.
	 * @return
	 */
	public String getArduinoIP();
	
}

package org;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * class to comunicat with the arduino
 * @author erik
 *
 */
public class ArduinoComunicator {
	private static final int port=1234;
	private static final int timeout=1000;
	
	/**
	 * Used to send a make drink command to the arduino.  Will throw if the drink's byte array
	 * can not be created because an ingredient is not configured.
	 * @param toMake
	 * @param ci
	 * @return the code returned by the arduino, currently a single byte to ack or deny the drink order.
	 * @throws Exception 
	 */
	public static int makeDrink(Drink toMake,ConfigInterface ci) throws Exception{
		int returned=-1;
		byte[] toSend=toMake.getByteArray(ci);
		try (   Socket kkSocket = new Socket(ci.getArduinoIP(), port);)
	    {
			kkSocket.setSoTimeout(timeout);
			kkSocket.getOutputStream().write(toSend);
			
	        try{
	        	returned=kkSocket.getInputStream().read();//will block untill a byte is read or
	        		//timeout is reached
	        }catch(SocketTimeoutException e){
	        	e.printStackTrace();
	        }
	    } catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Unable to connect to arduino");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returned;
	}
	
}

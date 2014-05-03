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
	private static final int timeout=500;
	public static final Object commLock = new Object();
	
	/**
	 * Used to send a make drink command to the arduino.  Will throw if the drink's byte array
	 * can not be created because an ingredient is not configured.
	 * @param toMake
	 * @param ci
	 * @return the code returned by the arduino, currently a single byte to ack or deny the drink order.
	 * @throws Exception 
	 */
	public static byte[] makeDrink(Drink toMake,ConfigInterface ci) throws Exception{
		//synchronized(commLock) {
			byte[] returned= new byte[100];
			byte[] toSend=toMake.getByteArray(ci);
			try (   Socket kkSocket = new Socket(ci.getArduinoIP(), port);)
		    {
				kkSocket.setSoTimeout(timeout);

				if(!authenticate(kkSocket,ci)) return null;
				
				kkSocket.getOutputStream().write(toSend);
				
		        try{
		        	kkSocket.getInputStream().read(returned);//will block untill a byte is read or
		        		//timeout is reached
		        }catch(java.net.SocketTimeoutException e){
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
		//}
	}
	
	public static byte[] checkDrinkStatus(short id, ConfigInterface ci) throws Exception{
		//synchronized(commLock) {
			byte[] returned= new byte[100];
			byte[] toSend;
			toSend = new byte[3];
			toSend[0] = 0x03;
			toSend[1] = (byte)(id >> 8);
			toSend[2] = (byte)(id);
			
			try (   Socket kkSocket = new Socket(ci.getArduinoIP(), port);)
		    {
				kkSocket.setSoTimeout(timeout);
				
				if(!authenticate(kkSocket,ci)) return null;
				
				kkSocket.getOutputStream().write(toSend);
				
		        try{
		        	kkSocket.getInputStream().read(returned);//will block untill a byte is read or
		        		//timeout is reached
		        }catch(java.net.SocketTimeoutException e){
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
		//}
	}
	
	public static byte[] checkStatus(ConfigInterface ci) throws Exception {
		//synchronized(commLock) {
			
			byte[] toSend = new byte[1];
			toSend[0] = 0x00;
			byte[] out=null;
			try (Socket kkSocket = new Socket(ci.getArduinoIP(), port);){
				kkSocket.setSoTimeout(timeout);
				
				if(!authenticate(kkSocket,ci)) return null;
				
				kkSocket.getOutputStream().write(toSend);
				System.out.printf("%2x ----- ", toSend[0]);
	        	int b;
	        	int i = 0;
	        	b=kkSocket.getInputStream().read();
				System.out.printf("%2x", b);
	        	if(b!=0)
	        		return null;
	        	int size=kkSocket.getInputStream().read();
				System.out.printf("%2x ", size);
	        	out=new byte[size];
	        	kkSocket.getInputStream().read(out);
	        	for(byte by: out)
	        		System.out.printf("%2x", by);
	        	System.out.printf("\n\n");
	        	
		    } catch (UnknownHostException e) {
				e.printStackTrace();
				System.err.println("Unable to connect to arduino");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw e;
			}
			return out;
		//}
	}
	
	public static boolean authenticate(Socket s, ConfigInterface ci) throws Exception {
		//synchronized(commLock) {
		byte[] pw = ci.getPassword();
		s.getOutputStream().write(pw);
		int b = s.getInputStream().read();
		System.out.printf("%02x%02x%02x%02x ----- %02x\n", pw[0], pw[1], pw[2], pw[3], b);
		return s.getInputStream().read() != 0xfe;
		//}
	}
	
	
	//public static int transmit
}

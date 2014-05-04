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
			byte[] returned= new byte[3];
			byte[] toSend=toMake.getByteArray(ci);
			try (   Socket kkSocket = new Socket(ci.getArduinoIP(), port);)
		    {
				kkSocket.setSoTimeout(timeout);

				System.out.printf("\n\n!DRINK!\n");
				//if(!authenticate(kkSocket,ci)) return null;
				for(byte b: toSend)
					System.out.printf("%02x", b);
				kkSocket.getOutputStream().write(toSend);
				System.out.printf(" ----- ");
				
		        try{
		        	int ret;
		        	int i = 0;
		        	while((ret=kkSocket.getInputStream().read()) != -1)
		        		returned[i++] = (byte)ret;
		        	for(byte b: returned)
						System.out.printf("%02x", b);
		        		
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
			byte[] returned= new byte[1];
			byte[] toSend;
			toSend = new byte[3];
			toSend[0] = 0x03;
			toSend[1] = (byte)(id >> 8);
			toSend[2] = (byte)(id);
			
			try (   Socket kkSocket = new Socket(ci.getArduinoIP(), port);)
		    {
				kkSocket.setSoTimeout(timeout);
				
				System.out.printf("\n\n!!DRINKCHECKING!!\n");
				//if(!authenticate(kkSocket,ci)) return null;
				for(byte b: toSend) {
					System.out.printf("%02x", b);
				}
				kkSocket.getOutputStream().write(toSend);
				System.out.printf(" ----- ");
				
		        try{
		        	kkSocket.getInputStream().read(returned);//will block untill a byte is read or
		        		//timeout is reached
		        	for(byte b: returned)
		        		System.out.printf("%02x", b);
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
				
				System.out.printf("\n\n");
				//if(!authenticate(kkSocket,ci)) return null;
				
				kkSocket.getOutputStream().write(0x00);
				System.out.printf("%02xxxxxxx ----- ", toSend[0]);
	        	int b;
	        	int i = 0;
	        	b=kkSocket.getInputStream().read();
	        	if(b != -1)
	        		System.out.printf("%02x", (byte)b);
	        	else
	        		System.out.printf("xx");
	        	if(b!=0)
	        		return null;
	        	int size=kkSocket.getInputStream().read();
				System.out.printf("%02x ", size);
	        	out=new byte[size];
	        	kkSocket.getInputStream().read(out);
	        	for(byte by: out)
	        		System.out.printf("%02x", by);
	        	
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
		return b == 0xff;
		//}
	}
	
	
	//public static int transmit
}

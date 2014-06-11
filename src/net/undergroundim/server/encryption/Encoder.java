package net.undergroundim.server.encryption;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author Troy
 *
 */
public class Encoder {
	private static BASE64Encoder b64e = new BASE64Encoder();
	private static BASE64Decoder b64d = new BASE64Decoder();
	
	/**
	 * Encode a packet to Base64.
	 * 
	 * @param packet
	 * @return String
	 */
	public static String encode(String packet){
		return b64e.encode(packet.getBytes()).replaceAll("\r\n", "");
	}
	
	/**
	 * Encode a packet to Base64.
	 * 
	 * @param packet
	 * @return byte[]
	 */
	public static String encode(byte[] packet){
		return b64e.encode(packet).replaceAll("\r\n", "");
	}
	
	/**
	 * Decode a packet from Base64.
	 * 
	 * @param packet
	 * @return String
	 */
	public static String decode(String packet){
		try{
			return new String(b64d.decodeBuffer(packet));
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Decode a string from Base64.
	 * 
	 * @param packet
	 * @return byte[]
	 */
	public static byte[] decodeB(String packet){
		try{
			return b64d.decodeBuffer(packet);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
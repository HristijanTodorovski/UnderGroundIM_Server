package net.undergroundim.server;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.undergroundim.server.networking.JDBC;


/**
 * 
 * @author Troy
 *
 */
public class Constants {
	private static double version = 1.011;
	private static String buildDate = "09/Oct/2012";
	
	/**
	 * Configure these settings through
	 * preferences.ini
	 */
	private static boolean guiEnabled = false;
	private static boolean fileTransferEnabled = true;
	private static int portNumber; 
	private static String password = ""; 
	private static double minVersionAllowed = version;
	//SQL Config
	private static String mysqlHost;
	private static int mysqlPort;
	private static String mysqlDatabase;
	private static String mysqlUsername;
	private static String mysqlPassword;
	
	private static Console console;
	
	/**
	 * Do not touch below this.
	 */
	public static ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	public static Random random = new Random();
	private static JDBC jdbc;
	
	private static Timer timeout = new Timer(1200000);
	
	/**
	 * This will convert the bytes to a string.
	 * 
	 * @param b
	 * @return String
	 */
	public static String bytesToString(byte[] b) {
	    byte[] b2 = new byte[b.length + 1];
	    b2[0] = 1;
	    System.arraycopy(b, 0, b2, 1, b.length);
	    return new BigInteger(b2).toString(36);
	}
	
    /**
     * Convert the string back to bytes.
     * 
     * @param s
     * @return byte[]
     */
	public static byte[] stringToBytes(String s) {
	    byte[] b2 = new BigInteger(s, 36).toByteArray();
	    return Arrays.copyOfRange(b2, 1, b2.length);
	}
	
	/**
	 * In the future we may let the server generate a secret key which
	 * will then be passed to the client as well to encrypt and decrypt
	 * packets to keep peering eyes confused.
	 * 
	 * @return SecretKeySpec
	 */
	public static SecretKeySpec generateSecretKey(){
		try{
	   	 	KeyGenerator kgen = KeyGenerator.getInstance("AES");
	        kgen.init(128);
	        SecretKey skey = kgen.generateKey();
	        byte[] raw = skey.getEncoded();
	
	        return new SecretKeySpec(raw, "AES");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Hash the password...
	 * 
	 * @param password
	 * @return byte[]
	 * @throws NoSuchAlgorithmException
	 */
	public static String getHash(String password){
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			byte[] input = digest.digest(password.getBytes("UTF-8"));

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < input.length; i++) {
				sb.append(Integer.toString((input[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<ClientThread> getClients() {
		return clients;
	}

	public static void setClients(ArrayList<ClientThread> clients) {
		Constants.clients = clients;
	}
	
	/**
	 * Remove a client and stop the thread running.
	 * 
	 * @param client
	 */
	public static void removeClient(ClientThread client){
		while(!client.isInterrupted())
			client.interrupt();
		
		clients.remove(client);
	}
	
	/**
	 * Get a client by user name.
	 * 
	 * @param username
	 * @return Client
	 */
	public static ClientThread getClient(String username){
		for(ClientThread c : clients)
			if(c.client != null && c.client.getUsername().equals(username))
				return c;
		
		return null;
	}
	
	/**
	 * Get a client by user id.
	 * 
	 * @param user_id
	 * @return Client
	 */
	public static ClientThread getClient(int user_id){
		for(ClientThread c : clients)
			if(c.client != null && c.client.getUser_id() == user_id)
				return c;
		
		return null;
	}
	
	/**
	 * Check if a user is online.
	 * 
	 * @param user_id
	 * @return boolean
	 */
	public static boolean isClientOnline(int user_id){
		for(ClientThread c : clients)
			if(c.client != null && c.client.getUser_id() == user_id)
				return true;
		
		return false;
	}
	
	/**
	 * Check if a user is online.
	 * 
	 * @param username
	 * @return boolean
	 */
	public static boolean isClientOnline(String username){
		for(ClientThread c : clients)
			if(c.client != null && c.client.getUsername().equals(username))
				return true;
		
		return false;
	}

	public static boolean isGuiEnabled() {
		return guiEnabled;
	}

	public static void setGuiEnabled(boolean guiEnabled) {
		Constants.guiEnabled = guiEnabled;
	}

	public static boolean isFileTransferEnabled() {
		return fileTransferEnabled;
	}

	public static void setFileTransferEnabled(boolean fileTransferEnabled) {
		Constants.fileTransferEnabled = fileTransferEnabled;
	}

	public static int getPortNumber() {
		return portNumber;
	}

	public static void setPortNumber(int portNumber) {
		Constants.portNumber = portNumber;
	}

	public static String getPassword() {
		return getHash(password);
	}

	public static void setPassword(String password) {
		Constants.password = password;
	}

	public static double getMinVersionAllowed() {
		return minVersionAllowed;
	}

	public static void setMinVersionAllowed(double minVersionAllowed) {
		Constants.minVersionAllowed = minVersionAllowed;
	}

	public static String getMysqlHost() {
		return mysqlHost;
	}

	public static void setMysqlHost(String mysqlHost) {
		Constants.mysqlHost = mysqlHost;
	}

	public static int getMysqlPort() {
		return mysqlPort;
	}

	public static void setMysqlPort(int mysqlPort) {
		Constants.mysqlPort = mysqlPort;
	}

	public static String getMysqlDatabase() {
		return mysqlDatabase;
	}

	public static void setMysqlDatabase(String mysqlDatabase) {
		Constants.mysqlDatabase = mysqlDatabase;
	}

	public static String getMysqlUsername() {
		return mysqlUsername;
	}

	public static void setMysqlUsername(String mysqlUsername) {
		Constants.mysqlUsername = mysqlUsername;
	}

	public static String getMysqlPassword() {
		return mysqlPassword;
	}

	public static void setMysqlPassword(String mysqlPassword) {
		Constants.mysqlPassword = mysqlPassword;
	}

	public static Random getRandom() {
		return random;
	}

	public static void setRandom(Random random) {
		Constants.random = random;
	}

	public static double getVersion() {
		return version;
	}

	public static void setVersion(double version) {
		Constants.version = version;
	}

	public static String getBuildDate() {
		return buildDate;
	}

	public static void setBuildDate(String buildDate) {
		Constants.buildDate = buildDate;
	}

	public static JDBC getJdbc() {
		return jdbc;
	}
	
	public static void setJdbc(JDBC jdbc) {
		Constants.jdbc = jdbc;
	}

	public static Console getConsole() {
		return console;
	}

	public static void setConsole(Console console) {
		Constants.console = console;
	}

	public static Timer getTimeout() {
		return timeout;
	}

	public static void setTimeout(Timer timeout) {
		Constants.timeout = timeout;
	}

}

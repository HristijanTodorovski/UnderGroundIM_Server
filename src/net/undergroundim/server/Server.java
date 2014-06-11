package net.undergroundim.server;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.undergroundim.server.networking.JDBC;


/**
 * 
 * @author Troy
 *
 */
public class Server implements WindowListener{
	private static Date date;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy - HH:mm:ss");
	
	private int clientNumber;
	
	private ArrayList<String> settings = new ArrayList<String>();
	
	private ServerSocket serverSocket = null;
	private boolean listening = true;
	
	/**
	 * Construct a new server.
	 */
	public Server(){
		readSettings();
		
		if(Constants.isGuiEnabled()){
			Constants.setConsole(new Console());
		}
		
		Constants.setJdbc(new JDBC());
		new TimeOut();
		
		try{
			ServerLoop();
		}catch(Exception e){
			log("Failed to start the server: " + e.getMessage(),null);
		};
	}
	
	/**
	 * The server loop
	 */
	private void ServerLoop() throws IOException{
		log("The server has started on port: " + Constants.getPortNumber(),null);
	     
		try {
			serverSocket = new ServerSocket(Constants.getPortNumber());
	    }catch (IOException e) {
	        log("Could not listen on port: " + Constants.getPortNumber(),null);
	        System.exit(-1);
	    }

	    /**
	     * While listening for connections
	     * accept new clients.
	     */
	    while(listening){
	    	Constants.getClients().add(new ClientThread(serverSocket.accept(), clientNumber++));
	    	 
			/**
			 * Sleep
			 */
	    	try {
	    		Thread.sleep(100);
	    	}catch (InterruptedException e) {
	    		e.printStackTrace();
	    	}
	    }
	     
	     /**
	      * Time to close the server.
	      */
	     serverSocket.close();
	     Constants.getJdbc().disconnect();
	     log("Server shut down.",null);
	}
	
	/**
	 * Log a message with different coloured text.
	 * 
	 * @param msg
	 * @param lineColour
	 */
	public static void log(String msg, Color lineColour){
		if(Constants.isGuiEnabled()){
			Constants.getConsole().log(msg, lineColour);
		}else{
			System.out.println(getDate() + ": " + msg);
		}
	}
	
	/**
	 * Get the date.
	 * 
	 * @return String
	 */
	public static String getDate(){
		date = new Date();
		return sdf.format(date);
	}
	
	/**
	 * Read in the settings.
	 */
	public void readSettings(){
		try{
			BufferedReader in = new BufferedReader(new FileReader("preferences.ini"));
			
			 String str;
			 while((str = in.readLine()) != null) {
				 settings.add(str);
			 }
			 
			 in.close();
			 in = null;
			 
			Constants.setGuiEnabled(Boolean.parseBoolean(settings.get(0).split(";")[1].replaceAll("\t", "")));
			Constants.setFileTransferEnabled(Boolean.parseBoolean(settings.get(1).split(";")[1].replaceAll("\t", "")));
			Constants.setPortNumber(Integer.parseInt(settings.get(2).split(";")[1].replaceAll("\t", "")));
			Constants.setPassword(settings.get(3).split(";")[1].replaceAll("\t", ""));
			Constants.setMinVersionAllowed(Double.parseDouble(settings.get(4).split(";")[1].replaceAll("\t", "")));
			Constants.setMysqlHost(settings.get(5).split(";")[1].replaceAll("\t", ""));
			Constants.setMysqlPort(Integer.parseInt(settings.get(6).split(";")[1].replaceAll("\t", "")));
			Constants.setMysqlDatabase(settings.get(7).split(";")[1].replaceAll("\t", ""));
			Constants.setMysqlUsername(settings.get(8).split(";")[1].replaceAll("\t", ""));
			Constants.setMysqlPassword(settings.get(9).split(";")[1].replaceAll("\t", ""));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This will check the setup and fix
	 * any error's it finds.
	 */
	public static void checkSetup(){
		File file = new File("preferences.ini");
		
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			FileWriter.writeToFile("preferences.ini", "GUI Enabled;		false", false, false);
			FileWriter.writeToFile("preferences.ini", "File Transfer Enabled;	true", true, true);
			FileWriter.writeToFile("preferences.ini", "Server Port;		5632", true, true);
			FileWriter.writeToFile("preferences.ini", "Server Password;	", true, true);
			FileWriter.writeToFile("preferences.ini", "Min Version;		" + Constants.getVersion(), true, true);
			FileWriter.writeToFile("preferences.ini", "MySQL Host;		localhost", true, true);
			FileWriter.writeToFile("preferences.ini", "MySQL Port;		3306", true, true);
			FileWriter.writeToFile("preferences.ini", "MySQL Database;		undergroundim", true, true);
			FileWriter.writeToFile("preferences.ini", "MySQLUsername;		root", true, true);
			FileWriter.writeToFile("preferences.ini", "MySQLPassword;		", true, true);
		}
	}
	
	/**
	 * Start the server
	 */
	public static void main(String[] args){
		checkSetup();
		new Server();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		listening = false;
	}

	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	
}
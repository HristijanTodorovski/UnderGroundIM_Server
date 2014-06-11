package net.undergroundim.server;

/**
 * 
 * @author Troy
 *
 */
public class TimeOut extends Thread{
	private final int UPDATE_RATE = 1;
	private final long UPDATE_PERIOD = 300000L / UPDATE_RATE;
	private long beginTime, timeTaken, timeLeft;
	
	public boolean running = false;
	
	/**
	 * This class will timeout the MySQL connection if
	 * needed, this way if the server is inactive for long
	 * times it doesn't maintain a connection.
	 * 
	 * This way seems more logical rather then opening and
	 * closing a connection after every call to the database
	 * as this could happen a lot...
	 */
	public TimeOut(){
		running = true;
		this.start();
	}
	
	public void run(){
		while(running){
			try{		
				beginTime = System.currentTimeMillis();

				//Check if we need to DC from MySQL
				if(Constants.getTimeout().isUp() && !Constants.getJdbc().connection.isClosed()){
					Constants.getJdbc().disconnect();
					Server.log("MySQL Timed out and has been closed until it is needed again.", null);
				}
					
				timeTaken = System.currentTimeMillis() - beginTime;
				timeLeft = (UPDATE_PERIOD - timeTaken);
		
				if(timeLeft < 10) timeLeft = 10;
				
				try {
					Thread.sleep(timeLeft);
				}catch(InterruptedException ex){break;}
			}catch(Exception e){}
		}
	}

}
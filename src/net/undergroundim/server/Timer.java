package net.undergroundim.server;

/**
 * 
 * @author Troy
 *
 */
public class Timer {
  public long startTime;
  public long endTime;
  public long timeLimit;
  
  public Timer(int timeLimit){
	  this.timeLimit = timeLimit;
	  startTime = System.currentTimeMillis();
	  reset();
  }
  
  public void reset(){
	  endTime = ((startTime = System.currentTimeMillis()) + timeLimit);
  }

  public long getTimeElapsed(){
    return System.currentTimeMillis() - startTime;
  }
  
  public long getTimeLeft(){
	  return endTime - System.currentTimeMillis();
  }
  
  public boolean isUp(){
	  return System.currentTimeMillis() > endTime;
  }
  
  public boolean isNotUp(){
	  return System.currentTimeMillis() < endTime;
  }
  
  public long getTimeRemaining(){
	  return endTime - System.currentTimeMillis();
  }
  
  public long setTimerToEndIn(long endTime){
	  return this.endTime = System.currentTimeMillis() + endTime;
  }
  
  public String toStringTimeElapsed(){
	  return toStringTime(getTimeElapsed());
  }
  
  public String toStringTimeLeft(){
	  return toStringTime(getTimeLeft());
  }
  
  public long getTotalMins(){
	  long TotalSecs = getTimeElapsed() / 1000;
	  return TotalSecs / 60;
  }
  
  public static String toStringTime(long time){
	  StringBuilder b = new StringBuilder();
	  long runtime = time;
	  long TotalSecs = runtime / 1000;
	  long TotalMins = TotalSecs / 60;
	  long TotalHours = TotalMins / 60;
	  int seconds = (int) TotalSecs % 60;
	  int minutes = (int) TotalMins % 60;
	  int hours = (int) TotalHours % 60;
	  if (hours < 10)
		  b.append("0");
	  b.append(hours);
	  b.append(" : ");
	  if (minutes < 10)
		  b.append("0");
	  b.append(minutes);
	  b.append(" : ");
	  if (seconds < 10)
		  b.append("0");
	  b.append(seconds);
	  
	  return String.valueOf(b.toString());
  }
}
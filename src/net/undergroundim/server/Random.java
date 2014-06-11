package net.undergroundim.server;

/**
 * 
 * @author Troy
 *
 */
public class Random {
	private java.util.Random r = new java.util.Random();
	
	public int random(int min, int max) {
		if(min > max)
			throw new IllegalArgumentException("Maximun is less then minimun.");
		else if(min == max)
			return min;
		else
			return min + r.nextInt(max - min);
	}

	public boolean nextBoolean() {
		return r.nextBoolean();
	}
	
	public void nextBytes(byte[] bytes) {
		r.nextBytes(bytes);
	}
	
	public double nextDouble() {
		return r.nextDouble();
	}
	
	public float nextFloat() {
		return r.nextFloat();
	}
	
	public double nextGaussian() {
		return r.nextGaussian();
	}
	
	public int nextInt() {
		return r.nextInt();
	}
	
	public int nextInt(int i) {
		return r.nextInt(i);
	}
}
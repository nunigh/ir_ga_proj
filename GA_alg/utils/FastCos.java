package utils;

public class FastCos {

	private final static double half_pi = (double)(Math.PI / 2.0);
	
	final public static double cos(double ang)
	{
	  return FastSin.sin(half_pi - ang);
	}
}

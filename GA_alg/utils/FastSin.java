package utils;


public class FastSin {

	static double[]             table = null;
	static double               step;
	static double               invStep;
	static int                  size  = 0;

	static
	{
		size = 10000;
		table = new double[size];
		step = 2d * Math.PI / size;
		invStep = 1.0f / step;
		for (int i = 0; i < size; ++i)
		{
		    table[i] = Math.sin(step * i);
		}
	}

	/** Find a linear interpolation from the table
	 * 
	 * @param ang
	 *            angle in radians
	 * @return sin of angle a
	 */
	private final static double pi2 = Math.PI * 2;

	final public static double sin(double ang)
	{
	    double t = ang % pi2;
	    if (t < 0) 
    	{
	    	if (t > -0.0001)
	    		t = 0;
	    	else
	    		t = pi2 + t;
	    }
	    int indexA = (int) (t / step);
	    int indexB = indexA + 1;
	    if (indexB >= size) return table[indexA];
	    double a = table[indexA];
	    return a + (table[indexB] - a) * (t - (indexA * step)) * invStep;
	}
}

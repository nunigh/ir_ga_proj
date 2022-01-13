package utils;

public final class GAUtils {

	public static float MIN_TRANSLATION = 0;
	public static float MAX_TRANSLATION = 0;
	public static float RESET_TRANSLATION = 0;
	
	public static float MIN_ROTATION = 0;
	public static float MAX_ROTATION = 0;
	public static float RESET_ROTATION = 0;
	
	public static float MIN_SCALING = 0;
	public static float MAX_SCALING = 0;
	public static float RESET_SCALING = 1;
	
	public static float MIN_SHEAR = 0;
	public static float MAX_SHEAR = 0;
	public static float RESET_SHEAR = 0;
	
	public static void init()
	{
		MAX_TRANSLATION = (float)(0.8 * Utils.size);
		MIN_TRANSLATION = -1*MAX_TRANSLATION;
		RESET_TRANSLATION = 0;
		
		MIN_ROTATION = -100;
		MAX_ROTATION = 100;
		RESET_ROTATION = 0;
		
		MIN_SCALING = 0.8f;
		MAX_SCALING = 1.2f;
		RESET_SCALING = 1f;
		
		MIN_SHEAR = -0.3f;
		MAX_SHEAR = 0.3f;
		RESET_SHEAR = 0;
	}

	
}

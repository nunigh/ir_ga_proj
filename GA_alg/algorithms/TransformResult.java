package algorithms;


import java.util.ArrayList;
import java.util.Arrays;

import org.opencv.core.Point;

public class TransformResult {

	// TODO: local variable to support parallel, threading, etc.
	public short[] _tempSensedFeaturesTransformed;
	public short[] _tempSensedTransformed;
	public boolean[] _tempOverlap;
	public ArrayList<Point> _tempOverlapList = new ArrayList<Point>();
	public double[] _tempDiffList = null;
	public int[] _tempDiffCount = null;
	public ArrayList<Point> _tempAllList = new ArrayList<Point>();
	public int _tempOverlapCount;
	public int _fails;

	public double[][] _tempTList = null;
			
	public TransformResult(int refSize, int sensedSize)
	{
		Init(refSize, sensedSize);
	}
	
	private void Init(int refSize, int sensedSize) {
					
		_fails = 0;
		_tempOverlapCount = 0;
		
		_tempSensedFeaturesTransformed = new short[refSize];
		_tempSensedTransformed = new short[refSize];
		_tempOverlap = new boolean[refSize];
		_tempDiffList = new double[sensedSize];
		_tempDiffCount = new int[sensedSize];
		_tempTList = new double[sensedSize][2];
		
		Arrays.fill(_tempDiffList, 0);
		Arrays.fill(_tempDiffCount, 0);
		Arrays.fill(_tempOverlap, false);
		Arrays.fill(_tempTList, null);
		
	}
	
	
}

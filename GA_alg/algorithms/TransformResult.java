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
		if (CONST.PROFILING_MODE == false) {
			_tempSensedFeaturesTransformed = new short[refSize];
			_tempSensedTransformed = new short[refSize];
			_tempOverlap = new boolean[refSize];
			//_tempDiffList = new double[sensedSize]; // hadar: unused
			//_tempDiffCount = new int[sensedSize]; //hadar: unused
			_tempTList = new double[sensedSize][2];
			Arrays.fill(_tempTList, null); // hadar: it does not initalize it with null/..
		} else
		{
			// i splitted that as a test to see which is the expinsive one
			arraysInit1(refSize);
			arraysInit2(refSize);
			arraysInit3(refSize);
			//arraysInit4(sensedSize); // result: this is the expensive one
		}


		//Arrays.fill(_tempDiffList, 0); // hadar: 0/false is the default, redundant
		//Arrays.fill(_tempDiffCount, 0);
		//Arrays.fill(_tempOverlap, false);
		//Arrays.fill(_tempTList, null); // hadar: it does not initalize it with null/..

	}
	public void arraysInit1(int refSize)
	{_tempSensedFeaturesTransformed = new short[refSize];}
	public void arraysInit2(int refSize)
	{_tempSensedTransformed = new short[refSize];}
	public void arraysInit3(int refSize)
	{_tempOverlap = new boolean[refSize];}
	public void arraysInit4(int sensedSize)
	{_tempTList = new double[sensedSize][2];}

}

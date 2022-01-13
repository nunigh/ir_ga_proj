package Fitness;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.opencv.core.Point;

import com.sun.org.apache.xpath.internal.operations.Bool;

import utils.InternalMode;
import utils.Utils;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.extensions.MatlabNumericArray;
import algorithms.CONST;
import algorithms.TransformResult;
import Interfaces.Chromosome;

public class CalculateFitnessTask implements Runnable {

	InternalMode _internalMode;
	short[] _referenced;
	short[] _sensed;
	short[] _referencedFeatures;
	short[] _sensedFeatures;
	int _twidth1;
	int _theight1;
	int _twidth2;
	int _theight2;
	int _maxImageSize;
	int _maxSamples;
	ArrayList<Point> _refPoints = null;
	ArrayList<Point> _sensedPoints = null;
	boolean _isCentered;
	int _height2;
	ArrayList<Point> _points1;
	ArrayList<Point> _points2;
	
	Chromosome _ch;
	boolean _forceFix;
	int _popNum;
	
	public CalculateFitnessTask(Chromosome ch, boolean forceFix, int popNum, InternalMode mode, 
			short[] ref, short[] sensed, short[] refF, short[] sensedF,
			int twidth1, 	int theight1, 	int twidth2,	int theight2, 
			ArrayList<Point> refPoints, ArrayList<Point> sensedPoints, int maxSize, int maxSamples, boolean isCentered, int height2,
			ArrayList<Point> points1, ArrayList<Point> points2)
	{
		_ch = ch;
		_forceFix = forceFix;
		_popNum = popNum;
		_internalMode = mode;
		_referenced = ref;
		_sensed = sensed;
		_referencedFeatures = refF;
		_sensedFeatures = sensedF;
		_twidth1 = twidth1;
		_theight1 = theight1;
		_twidth2 = twidth2;
		_theight2 = theight2;
		_refPoints = refPoints;
		_sensedPoints = sensedPoints;
		_maxImageSize = maxSize;
		_isCentered = isCentered;
		_maxSamples = maxSamples;
		_height2 = height2;
		_points1 = points1;
		_points2 = points2;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			CalculateFitness(_ch, _forceFix, _popNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean CalculateFitness(Chromosome ch, boolean forceFix, int popNum)
			throws Exception {
		double[] fitness = null;

		if (CONST.data != null) {
			fitness = new double[] { CalculateFitnessTest(ch) };
		} else {

			if (_internalMode == InternalMode.MultiObjective
					|| _internalMode == InternalMode.MultiObjectiveMI
					|| _internalMode == InternalMode.MultiObjectiveHD)
				fitness = new double[] { Double.MAX_VALUE, Double.MAX_VALUE };
			else
			{
				if (_internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) 
					fitness = new double[] { Double.MAX_VALUE, Double.MAX_VALUE };
				else if (_internalMode == InternalMode.SIFT_MOO2) 
					fitness = new double[] { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};//,  Double.MAX_VALUE };
				else
					fitness = new double[] { Double.MAX_VALUE };
			}
			
			if (ch.IsValid())
			{
				
				double overlapFitness = 0;
				TransformResult res = null;
				if (_internalMode != InternalMode.SIFT && _internalMode != InternalMode.SIFT_MOO)
				{
					res = calculateTransform(ch, forceFix);
					overlapFitness = validateOverlap(ch, res);				
				}
	
				if (_internalMode != InternalMode.HD && 
					_internalMode != InternalMode.HD_ALL && 
					overlapFitness > 0)
				{
					fitness[0] = overlapFitness;
				}
				else if (_internalMode == InternalMode.MI) {
				{

					double f1 = CalculateFitnessNC(ch, res);
					fitness = new double[] { f1 };

				}
				} else if (_internalMode == InternalMode.HD) {
					double f2 = CalculateFitnessHausdorff(ch, res);
					fitness = new double[] { f2 };
				} else if (_internalMode == InternalMode.HD_ALL) {
					double f2 = CalculateFitnessHausdorffAll(ch, res);
					fitness = new double[] { f2 };
				} else if (_internalMode == InternalMode.MultiObjective) {
					double f1 = CalculateFitnessNC(ch, res);
					double f2 = CalculateFitnessHausdorff(ch, res);
					fitness = new double[] { f1, f2 };
				} else if (_internalMode == InternalMode.MultiObjectiveMI) {
	
					{
						double f2 = CalculateFitnessMI(ch, res);
						double f1 = CalculateFitnessNC(ch, res);
						
						fitness = new double[] { f1, f2 };
					}
				} else if (_internalMode == InternalMode.MultiObjectiveHD) {
	
					{
						double f1 = CalculateFitnessHausdorff(ch, res);
						double f2 = CalculateFitnessNC(ch, res);
						fitness = new double[] { f1, f2 };
					}
				} 
				else if (_internalMode == InternalMode.SIFT_MOO ||
						_internalMode == InternalMode.SIFT_MOO2 ||
								_internalMode == InternalMode.SIFT) {
					
					fitness = SetSIFTFitness(ch, res, true);
	
				}
			}

		}

		base.Fitness f = new base.Fitness(fitness);
		ch.set_fitness(f);

		return true;
	}
	
	public double CalculateFitnessDist(Chromosome ch) {

		return 11;
		//double v = validateOverlap(ch);
		//if (v > 0)
		//	return v;

		// int diffX = Math.abs(ch._minX - ch._maxX);
		// int diffY = Math.abs(ch._minY - ch._maxY);

		/*
		 * if (diffX <= 2 * Math.max(_width2,_width1) && diffY <= 2 *
		 * Math.max(_height2,_height1)) return 0;
		 * 
		 * return Math.sqrt(Math.pow(diffX,2) + Math.pow(diffY,2));
		 */
		
		/*
		int diff = 0;
		int internalDist = 0;
		int i = 0;
		int j = 0;
		//if (ch.get_root(0).getDescription() != "x" ||
		//		ch.get_root(1).getDescription() != "y")
		{
			try {

				for (i = 0; i < _twidth2; i++) {
					for (j = 0; j < _theight2; j++) {
						double[] newPos = _tempTList[(int) Utils.getIndex(_height2,
								i, j)];
						if (newPos != null) {
							
						
						internalDist += (int) Math.max(Math.abs(newPos[0] - i),
								Math.abs(newPos[1] - j));
						// diff += NeighbourhoodDistance(ch, i, j);
						diff += NeighbourhoodDistance(ch, i, j);
						// if (diff > 3)
						// count ++;
						
						}
					}
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());

			}
		}
		
		if (internalDist < 2) {
			diff += 10;
		}
		return diff;
		// return count;
		 
		 */
	}

	public static double avgRef  = 0;
	public static double avgSensed  = 0;
	public double CalculateFitnessNC(Chromosome ch, TransformResult res)
	{
		
		
		double v = validateOverlap(ch, res);
		if (v > 0)
			return v;
		
		if (avgRef == 0)
		{
			for (int i = 0; i < _referenced.length; i++) {
				avgRef += _referenced[i];
			}
			avgRef /= _referenced.length;
			
			for (int i = 0; i < _sensed.length; i++) {
				avgSensed += _sensed[i];
			}
			avgSensed /= _sensed.length;
		}
		
		double up = 0;
		double downRef = 0; 
		double downSensed = 0;
		for (int i = 0; i < _twidth1; i++) {
			for (int j = 0; j < _theight1; j++) {
				
				if (res._tempOverlap[(int) Math.round(Utils.getIndex(_theight1, i, j))])
				{
					short oX = _referenced[(int) Math.round(Utils.getIndex(_theight1,i, j))];
					short oY = res._tempSensedTransformed[(int) Math.round(Utils.getIndex(_theight1, i, j))];
					up += ((oX - avgRef)*(oY - avgSensed));
					downRef += Math.pow((oX - avgRef),2);
					downSensed +=  Math.pow((oY - avgSensed),2);
				}
				
			}
		}
		
		double val = up / Math.sqrt(downRef * downSensed);
		return -1*val;
	}
		
	public double CalculateFitnessMI(Chromosome ch, TransformResult res) {
		/*
		 * if (ch._maxDiff > 2) return 200+ch._maxDiff;
		 */

		/*if (_fromSIFT && _SIFTDist != null) {
			if (!ch.ValidateDist(_SIFTDist))
				return 200;
		} else {
			if (_internalMode != InternalMode.MultiObjectiveMI)
			{
				double dist = CalculateFitnessDist(ch);
				if (dist > 3)
					return 200 + dist;
			}
		}
*/
		double v = validateOverlap(ch, res);
		if (v > 0)
			return v;

		int bins = 26;

		int[][] jointHist = new int[bins][bins];
		int[] histA = new int[bins];
		int[] histB = new int[bins];
		// ///int[][] test = new int[bins][bins];

		// init joint hist
		for (int i = 0; i < _twidth1; i++) {
			for (int j = 0; j < _theight1; j++) {
				// for (int k =0;k<_tempOverlapList.size();k++)
				// {

				if (res._tempOverlap[(int) Math.round(Utils.getIndex(_theight1, i, j))])
				// Point p = _tempOverlapList.get(k);
				// double i = p.x;
				// double j = p.y;

				{
					short oX = _referenced[(int) Math.round(Utils.getIndex(_theight1,i, j))];

					short oY = res._tempSensedTransformed[(int) Math.round(Utils.getIndex(_theight1, i, j))];

					int grayX = (int) Math.round(Math.floor(oX / 10.0));
					int grayY = (int) Math.round(Math.floor(oY / 10.0));

					if (grayX != grayY && Math.abs(oX - oY) <= 10.0) {
						double av = (oX + oY) / 2.0;
						grayX = (int) Math.round(Math.floor(av / 10.0));
						grayY = grayX;
					}

					// int x = _original[i][j];
					// int y = ch._transformed[i][j];
					histA[grayX]++;
					histB[grayY]++;
					jointHist[grayX][grayY]++;
				}
				// }
			}
		}

		// init joint hist gray level
		/*
		 * //////// for (int i=0;i<bins;i++) { for (int j=0;j<bins;j++) {
		 * test[i][j]=(int) (((double)jointHist[i][j]/ch.overlapCount)*255); } }
		 */// //

		// calculate entropy
		double entropy = 0;
		// double test = 0;
		// if (ch.overlapCount == 0 || ch.overlapPercent <
		// CONST.MIN_OVERLAP_PERCENT)
		// {
		// entropy = (1-ch.overlapPercent) * 10;
		// }
		// else
		{
			for (int i = 0; i < bins; i++) {
				for (int j = 0; j < bins; j++) {
					if (jointHist[i][j] > 0) {
						{
							double probAB = (double) jointHist[i][j]
									/ (double) res._tempOverlapCount;// ch.overlapCount;
							double probA = (double) histA[i]
									/ res._tempOverlapCount;// ch.overlapCount;
							double probB = (double) histB[j]
									/ res._tempOverlapCount;// ch.overlapCount;
							// 1
							entropy -= probAB
									* Math.log10(probAB / (probA * probB));

							// 10.7.2015
							// 2 //entropy -= probAB * Math.log((probA *
							// probB)/(probAB * probAB));
							// test += probAB * Math.log(probAB);

							// 3 // entropy += probAB * Math.log(probAB / (probA
							// * probB));

						}
					}
				}
			}

			// ///////////////////double p = 1 / (double)_tempOverlapCount;
			// /////////////entropy /= bins*bins*(1*Math.log(1/(p*p)));
			// ///////////////////////entropy = 1-entropy;
			entropy += 10;
			// entropy *=7;

			// 10.7.2015
			if (entropy < 0)
				entropy = 0;
			// if (entropy>1)
			// s entropy = 1;

			// entropy += (double)(1-ch.overlapPercent)/10.0;
		}

		double fitness = entropy;

		// penalty on deep of the chromosome
		// int deep = ch.GetDeep();
		// if (deep > CONST.MAX_DEEP_CHROMOSOME)
		// {
		// fitness += fitness*0.1*deep;
		// }

		
		fitness = ((int)(fitness*1000.0))/1000.0;  
		return fitness;
		// TODO: const by image size ??
	}

	public double CalculateFitnessHausdorff(Chromosome ch, TransformResult res)
			throws MatlabInvocationException {

		if (ch.overlapPercent < CONST.MIN_OVERLAP_PERCENT)
			return (_twidth1 * _theight1) * _theight1 + (_twidth1 * _theight1)
					+ (1 - ch.overlapPercent);

		// Set a variable, add to it, retrieve it, and print the result
		/*
		 * double[][] A = new
		 * double[ch._transformed.length][ch._transformed[0].length]; double[][]
		 * B = new double[_original.length][_original[0].length]; for (int
		 * i=0;i<ch._transformed.length;i++) { for (int
		 * j=0;j<ch._transformed[0].length;j++) { if(ch.overlap[i][j]!=1) {
		 * A[i][j] =0; B[i][j] =0; } else { A[i][j] =
		 * (double)ch._transformed[i][j]; B[i][j] = (double)_original[i][j]; } }
		 * } }
		 */

		ArrayList<Point> A = new ArrayList<Point>();
		ArrayList<Point> B = new ArrayList<Point>();
		
		if (1==1)
		{
			for (int i = 0; i < _refPoints.size(); i++) {
				//if(res._tempOverlap[(int) Math.round(Utils.getIndex(_theight1, _refPoints.get(i).x,_refPoints.get(i).y))])
				{
					A.add(new Point(_refPoints.get(i).x, _refPoints.get(i).y));
				}
			}
			
			for (int i = 0; i < _sensedPoints.size(); i++) {
				int index = (int) Utils.getIndex(_height2, _sensedPoints.get(i).x, _sensedPoints.get(i).y);
				if (res._tempTList[index] != null)
				{
					//B.add(new Point(_sensedPoints.get(i).x, _sensedPoints.get(i).y));
					B.add(new Point(res._tempTList[index][0], res._tempTList[index][1]));
				}
			}
		}
		else
		{
			for (int i = 0; i < _twidth1; i++) {
				for (int j = 0; j < _theight1; j++) {
					if (_referencedFeatures[(int) Math.round(Utils.getIndex(_theight1, i,
							j))] > 100
							&& res._tempOverlap[(int) Math.round(Utils.getIndex(_theight1, i,
									j))])
					// i >= ch._minX && i<= ch._maxX &&
					// j >= ch._minY && j<= ch._maxY)
					{
						A.add(new Point(i, j));
					}
				}
			}
			if (A.size() <= 10)
				return 10000000;
			

			for (int i = 0; i < _twidth1; i++) {
				for (int j = 0; j < _theight1; j++) {
					if (res._tempSensedFeaturesTransformed[(int) Math.round(Utils.getIndex(
							_theight1, i, j))] > 100) // &&
					// i >= ch._minX && i<= ch._maxX &&
					// j >= ch._minY && j<= ch._maxY)
					{
						B.add(new Point(i, j));
					}
				}
			}
			if (B.size() <= 10)
				return 10000000;
			// if (B.size()<=50)
			// return 1000 * (1 - ch.overlapPercent);

		}

		double[][] a2 = new double[A.size()][2];
		for (int i = 0; i < A.size(); i++) {
			a2[i][0] = A.get(i).x;
			a2[i][1] = A.get(i).y;
		}
		// System.out.println("A: "+A.size());

		double[][] b2 = new double[B.size()][2];
		for (int i = 0; i < B.size(); i++) {
			b2[i][0] = B.get(i).x;
			b2[i][1] = B.get(i).y;
		}
		// System.out.println("B: "+B.size());

		double result = CalcHDDist(a2, b2);
		//		/ (double) Math.max(_width2, _height2);

		// #27.1.16 ch._maxDiff = CalculateFitnessDist(ch)
		//		/ ((double) _width2 * (double) _height2 * (double) Math.max(
		//				_width2, _height2));
		//return result + 2 * ch._maxDiff;
		
		return result;
	}

	double[][] _refMat = null;
	public double CalculateFitnessHausdorffAll(Chromosome ch, TransformResult res)
			throws MatlabInvocationException {
		double defaultfitness = 999999;
		
		if (!ch.IsValid())
			return defaultfitness;

		if (_refMat == null) {
			ArrayList<Point> A = new ArrayList<Point>();
			for (int i = 0; i < _twidth1; i++) {
				for (int j = 0; j < _theight1; j++) {
					if (_referencedFeatures[(int) Math.round(Utils.getIndex(
							_theight1, i, j))] > 0) {
						A.add(new Point(i, j));
					}
				}
			}

			_refMat = new double[A.size()][2];
			for (int i = 0; i < A.size(); i++) {
				_refMat[i][0] = A.get(i).x;
				_refMat[i][1] = A.get(i).y;
			}
		}

		if (res._tempAllList.size() < 10)
			return defaultfitness;

		double[][] b2 = new double[res._tempAllList.size()][2];
		for (int i = 0; i < res._tempAllList.size(); i++) {
			b2[i][0] = res._tempAllList.get(i).x;
			b2[i][1] = res._tempAllList.get(i).y;
		}

		// count how much each point matching the model
		double result = CalcHDDistAll(_refMat, b2);
		return Math.min(result, defaultfitness);
	}

	private double CalcHDDistAll(double[][] a2, double[][] b2)
			throws MatlabInvocationException {

		double result = -1;
		int num = 1000;
		if (b2.length > num) {
			double maxResult = -1;
			double[][] temp = new double[num][b2[0].length];
			for (int i = 0; i < 1; i++) {
				int ind;
				for (int j = 0; j < num; j++) {
					ind = Utils.rand.nextInt(b2.length);
					temp[j][0] = b2[ind][0];
					temp[j][1] = b2[ind][1];
				}

				result = CalcHDDist(a2, temp);
				maxResult = Math.max(result, maxResult);
			}

			result = maxResult;
		} else {
			result = CalcHDDist(a2, b2);
		}

		return result;
	}

	private double CalcHDDist(double[][] a2, double[][] b2)
			throws MatlabInvocationException {
		MatlabNumericArray arrA = new MatlabNumericArray(a2, null);
		CONST.processor.setNumericArray("A", arrA);
		MatlabNumericArray arrB = new MatlabNumericArray(b2, null);
		CONST.processor.setNumericArray("B", arrB);
		// CONST.Myproxy.eval("hd = HausdorffDist(A,B)");
		CONST.Myproxy.eval("[hd] = MedianHausdorffDist(A,B)");

		double result = ((double[]) CONST.Myproxy.getVariable("hd"))[0];
		return result;
	}

	
	private double validateOverlap(Chromosome ch, TransformResult res) {
		if (res._fails > res._tempOverlapCount / 4.0
				|| ch.overlapPercent < CONST.MIN_OVERLAP_PERCENT)
			return 200 * (1 - ch.overlapPercent);

		return 0;
	}
	

	public double CalculateFitnessTest(Chromosome ch) {
		// sum total absolute difference of the chromosome equation on each
		// entry in the training data
		double totDiff = 0;
		for (int i = 0; i < CONST.data.size(); i++) {
			Entry<Entry<Double, Double>, Entry<Double, Double>> entry = CONST.data
					.get(i);
			Entry<Double, Double> e = entry.getKey();
			double[] pos = ch.GetValue(e.getKey().intValue(), e.getValue()
					.intValue());
			double diff1 = pos[0]
					- entry.getValue().getKey();
			double diff2 = pos[1]
					- entry.getValue().getValue();
			totDiff += Math.sqrt(Math.pow(diff1, 2) + Math.pow(diff2, 2));
		}

		// calculate the average diff
		double fitness = totDiff / CONST.data.size();

		return fitness;
	}


	public TransformResult calculateTransform(Chromosome ch, boolean force) throws Exception {

		TransformResult res = new TransformResult(_referenced.length, _sensed.length);
		
		ch._minX = -1;
		ch._minY = -1;
		ch._maxX = -1;
		ch._maxY = -1;

		int count = 0;
		// apply transform
		int iterations = 0;
		// double avgRow = 0;
		// double avgCol = 0;
		
		for (int col = 0; col < _twidth2; col++) {
			for (int row = 0; row < _theight2; row++) {
				
				int trow = row;
				int tcol = col;
				if (_sensed.length > _maxImageSize) // large
				{
					tcol = Utils.rand.nextInt(_twidth2);
					trow = Utils.rand.nextInt(_theight2);
				}
				int index = (int) Math.round(Utils.getIndex(_theight2, tcol, trow));

				double[] newPos = null;
				int newIndex = -1;
				if (CONST._2D) {
					
					// if (deep > CONST.MAX_DEEP_CHROMOSOME)
					// newPos = T(tcol, trow);
					// else
					newPos = T(ch, tcol, trow);
					int x = (int) Math.round(newPos[0]);
					int y = (int) Math.round(newPos[1]);
					newIndex = (int) Math.round(Utils.getIndex(_theight1, x, y));
					

				} else {
					newIndex = T1(ch, tcol, trow);
					int newRow = -1;
					int newCol = -1;
					if (newIndex >= 0 && newIndex < _twidth1 * _theight1) {
						newRow = (newIndex % _theight1);
						newCol = (newIndex - newRow) / _theight1;
					}
					newPos = new double[] { newCol, newRow };
				}

				// avgCol += newPos[0];
				// avgRow += newPos[1];


				if (!Double.isInfinite(newPos[0])
						&& !Double.isInfinite(newPos[1])
						&& !Double.isNaN(newPos[0]) && !Double.isNaN(newPos[1])) {
					
					
					if (newPos[0] >= 0 && newPos[0] < _twidth1
							&& newPos[1] >= 0 && newPos[1] < _theight1
							&& newIndex < res._tempSensedFeaturesTransformed.length) {
						// overlap.put(new SimpleEntry<Integer, Integer>(col,
						// row), 1);
						res._tempSensedFeaturesTransformed[newIndex] = _sensedFeatures[index];
						res._tempSensedTransformed[newIndex] = _sensed[index];
						
						res._tempTList[(int) Utils.getIndex(_height2, tcol, trow)] = new double[] {
								newPos[0], newPos[1] };
						
						
						if (!res._tempOverlap[newIndex]) {
							count++;
							res._tempOverlap[newIndex] = true;
							res._tempOverlapList.add(new Point(newPos[0], newPos[1]));

							/*
							 * for (int i = 0; i < newPos.length; i++) { for
							 * (int j = 0; j < newPos.length; j++) {
							 * _tempDiffList[index] = new Point(tcol, trow);
							 * _tempDiffCount[] } }
							 */

						}
					}

					if (_sensedFeatures[index] > 0) {

						// if (!_tempAllList.contains(p))
						res._tempAllList.add(new Point(newPos[0], newPos[1]));
					}

					if (ch._minX == -1 || ch._minX > newPos[0])
						ch._minX = (int) Math.round(newPos[0]);
					if (ch._minY == -1 || ch._minY > newPos[1])
						ch._minY = (int) Math.round(newPos[1]);
					if (ch._maxX == -1 || ch._maxX < newPos[0])
						ch._maxX = (int) Math.round(newPos[0]);
					if (ch._maxY == -1 || ch._maxY < newPos[1])
						ch._maxY = (int) Math.round(newPos[1]);

				} else {
					res._fails++;
				}

				iterations++;
				if (_sensed.length > _maxImageSize && // large
						iterations > _maxSamples) {
					break;
				}
			}
			
			if (_sensed.length > _maxImageSize && // large
					iterations > _maxSamples) {
				break;
			}
		}
		
		ch.overlapPercent = (double) count / iterations;
		// ch.overlapPercent = (double)count / _referencedFeatures.length;
		ch.overlapCount = count;

		// ///////if (force || ch.overlapCount<= CONST.MIN_OVERLAP_PERCENT &&
		// fix)
		// //////////// Fix(ch, new double[] {avgCol/iterations,
		// avgRow/iterations}, new double[] {_width2, _height2});

		res._tempOverlapCount = ch.overlapCount;
		// for large images - sample overlap area for fitness calculation
	/* 8_2016	if (ch.overlapPercent >= CONST.MIN_OVERLAP_PERCENT) {
			_tempOverlapCount = ch.overlapCount;
			int index = -1;
			while (_tempOverlapCount > _maxFitnessSamples) {
				index = Utils.rand.nextInt(_tempOverlapList.size());
				Point point = _tempOverlapList.get(index);
				int ii = (int) Math.round(Utils.getIndex(_theight1, point.x, point.y));
				if (_tempOverlap.length < ii)
				{
					_tempOverlap[ii] = false;
					_tempOverlapList.remove(index);
					_tempOverlapCount--;
				}
			}
		}
*/
		
		return res;
	}
	
	private double[] T(Chromosome ch, int col, int row) {
		return ch.T(col, row, _isCentered);
	}
	
	private int T1(Chromosome ch, int col, int row) {

		// int i = getIndex(_theight2,col,row);
		// int newIndex = (int) Math.round(ch.GetValue(0, i, row));
		int newIndex = (int) Math.round((ch.GetValue(col, row))[0]);
		return newIndex;
	}
	
	// TODO: notice that although fitness is returned, SiftTask already updated chromosome's fitness.....
	private double[] SetSIFTFitness(Chromosome ch, TransformResult res, boolean calcSIFT) throws Exception
	{
		
		if (calcSIFT)
		{
			new SIFTTask(_internalMode, _points1, _points2, ch, false, _twidth1, _theight1).run();
		}
		
		double[] fitness = null;
		if (_internalMode == InternalMode.SIFT_MOO2) {
		
			//double mi = CalculateFitnessMI(ch, res);
			double nc = CalculateFitnessNC(ch, res);

			fitness = new double[ch.get_fitness().Length() + 1];
			for (int i = 0; i < ch.get_fitness().Length(); i++) {
				fitness[i] = ch.getValue(i);
			}
			fitness[ch.get_fitness().Length()] = nc;
			//fitness[ch.get_fitness().Length()+1] = mi;
			
		}
		else
		{
			fitness = new double[ch.get_fitness().Length()];
			for (int i = 0; i < fitness.length; i++) {
				fitness[i] = ch.getValue(i);
			}
		}
		
	
		return fitness;
	}
	
	
}

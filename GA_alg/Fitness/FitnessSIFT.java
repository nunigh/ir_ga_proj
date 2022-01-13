package Fitness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.script.ScriptException;

import org.opencv.core.Point;

import matlabcontrol.MatlabInvocationException;
import utils.InternalMode;
import utils.Utils;
import algorithms.CONST;
import Interfaces.Chromosome;


class Diff
{
	double value;
	double ox;
	double oy;
	double rx;
	double ry;
}

class DiffComparator implements Comparator<Diff>
{

	@Override
	public int compare(Diff arg0, Diff arg1) {
		return Double.compare(arg0.value, arg1.value);
	}
}

/*
private Runnable createTask(final GPMode gpMode, 
		final ArrayList<Point> refPoints,
		final ArrayList<Point> sensedPoints, 
		final Chromosome ch, final boolean force, final int p){

    Runnable aRunnable = new Runnable(){
        public void run(){
        	try {
				FitnessSIFT.SetFitness(gpMode, refPoints, sensedPoints, ch, force, p);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    };

    return aRunnable;

}*/



public final class FitnessSIFT {
	
	private static int _maxSIFTMatchesForFitness = (int)(700*300*0.4);
	private static DiffComparator comp = new DiffComparator();
	
	
	public static void Minimize(Chromosome ch)
	{
		ch.Minimize();
	}
	
	public static boolean SetFitness(InternalMode mode, 
			ArrayList<Point> refPoints,
			ArrayList<Point> sensedPoints, 
			Chromosome ch, boolean force, int popNum) throws Exception
	{
		if (CONST._shouldValidate)
		{
			ch.Validate();
		}
		
		boolean ok = FitnessSIFT.CalculateFitness(mode, refPoints, sensedPoints, ch, force, false, popNum);
		
		ch.AddLog("Fitness", ((Double)ch.get_fitness().GetValue(0)).toString());
		
		/*
		Chromosome cc = ch.clone();
		CalculateFitness(cc, force, true);
		
		if (cc.get_fitness().GetValue(0) < ch.get_fitness().GetValue(0))
			ch = cc.clone();
		*/
		return ok;
	}
	
	private static boolean CalculateFitness(InternalMode mode, 
			ArrayList<Point> refPoints,
			ArrayList<Point> sensedPoints, 
			Chromosome ch, boolean force, boolean forceFix, int popNum) throws Exception
	{
		double[] fitness = null;
		
		fitness = FitnessSIFT.CalculateFitnessSIFT(mode, refPoints, sensedPoints, ch, popNum);
		for (int i = 0; i < fitness.length; i++) {
			fitness[i] = ((int)(fitness[i]*100.0))/100.0;  
		}
		
		base.Fitness f = new base.Fitness(fitness);
		ch.set_fitness(f);
		
		return true;
	}

	 public static double[] CalculateFitnessSIFT(
			InternalMode mode, 
			ArrayList<Point> refPoints,
			ArrayList<Point> sensedPoints, 
			Chromosome ch, 
			int popNum) throws MatlabInvocationException, ScriptException
	{	
	
		 //////////////////////////////////////////////// INIT /////////////////////////////////////////////////////////
		 
		double[] defaultFitness = null;
		if (mode == InternalMode.SIFT)
			defaultFitness = new double[] { 200000 };
		else if (mode == InternalMode.SIFT_MOO || mode == InternalMode.SIFT_MOO2) 
			defaultFitness = new double[] { 200000, 200000 };
		
		if (!ch.IsValid())
			return defaultFitness;
		
		/////////////////////////////////////////////////////// EVALUATE /////////////////////////////////////////////////////
		
		int iterations = Math.min(refPoints.size(), _maxSIFTMatchesForFitness);
		List<Diff> diffX = new ArrayList<Diff>();
		List<Diff> diffY = new ArrayList<Diff>();
		
		int fails = 0;
		for (int i=0; i<iterations;i++)
		{
			int j = i;
			if (refPoints.size() > _maxSIFTMatchesForFitness) // if there are too many matches - test against random list
			{
				j = Utils.rand.nextInt(refPoints.size());
			}
			
			double[] newPos = new double[2];
			double newPosX = -1;
			double newPosY = -1;
			try
			{
			  newPos = ch.GetValue(sensedPoints.get(j).x, sensedPoints.get(j).y);
			  newPosX = newPos[0];
			  newPosY = newPos[1];
			  if (Double.isInfinite(newPosX) || Double.isNaN(newPosX) ||
					  Double.isInfinite(newPosY) || Double.isNaN(newPosY))
				  return defaultFitness;
			}
			catch (Exception ex)
			{
				fails++;
				continue;
			}
			
			double dx = Math.pow(refPoints.get(j).x - newPosX,2);
			double dy = Math.pow(refPoints.get(j).y - newPosY,2);
			Diff fx = new Diff();
			fx.value = dx;
			fx.ox = sensedPoints.get(j).x;
			fx.oy = sensedPoints.get(j).y;
			fx.rx = newPosX;
			fx.ry = newPosY;
			
			Diff fy = new Diff();
			fy.value = dy;
			fy.ox = sensedPoints.get(j).x;
			fy.oy = sensedPoints.get(j).y;
			fy.rx = newPosX;
			fy.ry = newPosY;
			
			diffX.add(fx);
			diffY.add(fy);
		}
		
		if (fails > iterations/4.0)
			return defaultFitness;
		
		///////////////////////////////////////// FITNESS //////////////////////////////////////////////////////////

		//double rmseX = 0;
		//double rmseY = 0;
		double rmse = 0;
		Collections.sort(diffX, comp);
		Collections.sort(diffY, comp);
		double medianX = -1;
		double medianY = -1;
		if (diffX.size() > 50)
		{
		
			List<Double> maxX = new ArrayList<Double>();
			List<Double> maxY = new ArrayList<Double>();
			
			int num = (int) Math.min(_maxSIFTMatchesForFitness, diffX.size());//_width2*_height2*0.05
			List<Double> valsX = new ArrayList<Double>(num);
			List<Double> valsY = new ArrayList<Double>(num);
			int it = 10;
			if (num == diffX.size())
				it = 1;
			for (int i = 0; i < it; i++) {
				valsX.clear();
				valsY.clear();
				
				for (int k = 0; k < num; k++) {
					int ind = k;
					if (it > 1) ind = Utils.rand.nextInt(diffX.size());
					valsX.add(diffX.get(ind).value);
					valsY.add(diffY.get(ind).value);
				}
				
				Collections.sort(valsX);
				Collections.sort(valsY);
				if (valsX.size() % 2 == 0)
				{
				    medianX = ((double)valsX.get(valsX.size()/2) + (double)valsX.get(valsX.size()/2 - 1))/2;
				    medianY = ((double)valsY.get(valsY.size()/2) + (double)valsY.get(valsY.size()/2 - 1))/2;
				}
				else
				{
				    medianX = (double) valsX.get(valsX.size()/2);
				    medianY = (double) valsY.get(valsY.size()/2);
				}
				maxX.add(medianX);
				maxY.add(medianY);
			}
			
			Collections.sort(maxX);
			Collections.sort(maxY);
			if (maxX.size() == 1)
			{
				medianX = maxX.get(0);
				medianY = maxY.get(0);
			}
			else if (maxX.size() % 2 == 0)
			{
			    medianX = ((double)maxX.get(maxX.size()/2) + (double)maxX.get(maxX.size()/2 - 1))/2;
			    medianY = ((double)maxY.get(maxY.size()/2) + (double)maxY.get(maxY.size()/2 - 1))/2;
			}
			else
			{
				
			    medianX = (double) maxX.get(maxX.size()/2);
			    medianY = (double) maxY.get(maxY.size()/2);
			}
			
		}
		else
		{
			
			for (int i = 0; i < diffX.size(); i++) {
				boolean x = false;
				if (!Double.isInfinite(diffX.get(i).value) && !Double.isNaN(diffX.get(i).value))
				{
					x = true;
					//rmseX += diffX.get(i).value;	
				}
				boolean y = false;
				if (!Double.isInfinite(diffY.get(i).value) && !Double.isNaN(diffY.get(i).value))
				{
					y = true;
					//rmseY += diffY.get(i).value;	
				}
				
				if (x && y)
					rmse += diffX.get(i).value + diffY.get(i).value;
			}
			
			// medianX = (double) diffX.get(diffX.size()-1).value;
			// medianY = (double) diffY.get(diffY.size()-1).value;
		}

		////////////////////////////////////////// RETURN VALUE
		
		double[] fitness = null;
		if (mode == InternalMode.SIFT)
		{
			fitness = new double[] { Math.min(rmse/diffX.size(),199999) };
		}
		else if (mode == InternalMode.SIFT_MOO || mode == InternalMode.SIFT_MOO2)
		{
			
			//fitness = new double[] { Math.min(Math.sqrt(rmseX/diffX.size()),199999), Math.min(Math.sqrt(rmseY/diffY.size()),199999) };
			fitness = new double[] { Math.min(Math.sqrt(medianX),199999), Math.min(Math.sqrt(medianY),199999) };
			
		}
		
		return fitness;
		
	}
	 
	
		

}

package Fitness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import matlabcontrol.MatlabInvocationException;

import org.opencv.core.Point;

import utils.InternalMode;
import utils.Utils;
import algorithms.CONST;
import Interfaces.Chromosome;

public class SIFTTask implements Runnable {
	
	private int _maxSIFTMatchesForFitness = (int)2000;//(700*300*0.4);
	private DiffComparator comp = new DiffComparator();
	private ScriptEngineManager mgr = new ScriptEngineManager();
	private ScriptEngine engine = mgr.getEngineByName("JavaScript");

	InternalMode _m;
	ArrayList<Point> _r;
	ArrayList<Point> _s;
	Chromosome _c;
	boolean _f;
	int _width1;
	int _height1;
	
	public SIFTTask(InternalMode gpMode, 
			ArrayList<Point> refPoints,
			ArrayList<Point> sensedPoints, 
			Chromosome ch, boolean force,
			int width1,
			int height1)
	{
		_m = gpMode;
		_r = refPoints;
		_s = sensedPoints;
		_c = ch;
		_f = force;
		_width1 = width1;
		_height1 = height1;
		
	}
	
	@Override
	public void run() {
		try {
			SetFitness(_m, _r, _s, _c, _f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean SetFitness(InternalMode mode, 
			ArrayList<Point> refPoints,
			ArrayList<Point> sensedPoints, 
			Chromosome ch, boolean force) throws Exception
	{
		if (CONST._shouldValidate)
		{
			ch.Validate();
		}
		
		boolean ok = CalculateFitness(mode, refPoints, sensedPoints, ch, force, false);
		
		ch.AddLog("Fitness", ((Double)ch.get_fitness().GetValue(0)).toString());
	
		return ok;
	}
	
	private boolean CalculateFitness(InternalMode mode, 
			ArrayList<Point> refPoints,
			ArrayList<Point> sensedPoints, 
			Chromosome ch, boolean force, boolean forceFix) throws Exception
	{		
		
		double[] SIFTfitness  = CalculateFitnessSIFT(mode, refPoints, sensedPoints, ch);
		for (int i = 0; i < SIFTfitness.length; i++) {
			SIFTfitness[i] = ((int)(SIFTfitness[i]*100.0))/100.0;  
		}
		
		base.Fitness f = new base.Fitness(SIFTfitness);
		ch.set_fitness(f);
		
		return true;
	}
	
	
	 private double[] CalculateFitnessSIFT(
				InternalMode mode, 
				ArrayList<Point> refPoints,
				ArrayList<Point> sensedPoints, 
				Chromosome ch) throws MatlabInvocationException, ScriptException
		{	
		
		 
			 //////////////////////////////////////////////// INIT /////////////////////////////////////////////////////////
			 
			double[] defaultFitness = null;
			if (mode == InternalMode.SIFT)
				defaultFitness = new double[] { 200000 };
			else if (mode == InternalMode.SIFT_MOO)
				defaultFitness = new double[] { 200000, 200000 };
			else if (mode == InternalMode.SIFT_MOO2) 
				defaultFitness = new double[] { 200000 };
	
			if (!ch.IsValid() )//|| 1==1) 
				return defaultFitness;
			
			/////////////////////////////////////////////////////// EVALUATE /////////////////////////////////////////////////////
			
			int iterations = Math.min(refPoints.size(), _maxSIFTMatchesForFitness);
			List<Diff> diffX = new ArrayList<Diff>();
			List<Diff> diffY = new ArrayList<Diff>();
			List<Diff> diff = new ArrayList<Diff>();
			
		
			ArrayList<Point> tmpPoints = new ArrayList<Point>();
			for (int i = 0; i < sensedPoints.size(); i++) {
				Point p = new Point(sensedPoints.get(i).x, sensedPoints.get(i).y);
				tmpPoints.add(p);
			}
			boolean[] used = new boolean[tmpPoints.size()];
			ArrayList<Point> refs = new ArrayList<Point>(refPoints.size());
			ArrayList<Point> sensed = new ArrayList<Point>(refPoints.size());
			
			ch._matches = 0;
			int fails = 0;
		
			for (int i=0; i<iterations;i++)
			{
				int j = i;
				if (refPoints.size() > _maxSIFTMatchesForFitness) // if there are too many matches - test against random list
				{
					j = Utils.rand.nextInt(refPoints.size());
				}
				
				double sourceX = 0;
				double sourceY = 0;
				
				double newPosX = Double.NaN;
				double newPosY = Double.NaN;
				try
				{
					
					int tmpIndex = 0;
					if (CONST.GA_SHUFFLE)
					{
						double distance = 10e10;
						/*int c = 0;	
						double d = 10e10;
						for (int k = 0; k < refPoints.size(); k++) {
							if (k!=j)
							{
								double tmpDistance =  Math.pow(refPoints.get(j).x - refPoints.get(k).x,2) + Math.pow(refPoints.get(j).y - refPoints.get(k).y,2);
								if (tmpDistance<d)
									c = k;
							}
						}
						if (c<i) // should find close correspondance
						{
							Point s = sensed.get(c);
							Point cl = null;
							d = 10e10;
							for (int k = 0; k < tmpPoints.size(); k++) {
								double tmpDistance =  Math.pow(s.x - tmpPoints.get(k).x,2) + Math.pow(s.y - tmpPoints.get(k).y,2);
								if (tmpDistance > 0 && tmpDistance<d)
								{
									
									d = tmpDistance;
									tmpIndex = k;
									cl = tmpPoints.get(k);
								}
							}
							
							double[] tmpPos = ch.GetValue(cl.x, cl.y);
							sourceX = cl.x;
							sourceY = cl.y;
							distance = d;
							newPosX = tmpPos[0];
							newPosY = tmpPos[1];
							
						}
						else*/
						{
						
							boolean only = false;
							Point closest = null;/*
							closest = getClosestMatch(ch, refPoints.get(j), tmpPoints);
							Point closest2 = getClosestMatch(ch, closest, refPoints);
							if (closest2.x == refPoints.get(j).x && 
									closest2.y == refPoints.get(j).y)
							{
								only = true;	
							}
							*/
							
							
							for (int k = 0; k < tmpPoints.size(); k++)
							//int k = j;
							{
								if (! only && !used[k] || only && closest != null && closest.x == tmpPoints.get(k).x && closest.y == tmpPoints.get(k).y)
								{
									double[] tmpPos = ch.GetValue(tmpPoints.get(k).x, tmpPoints.get(k).y);
									if (Double.isInfinite(tmpPos[0]) || Double.isNaN(tmpPos[0]) ||
											Double.isInfinite(tmpPos[1]) || Double.isNaN(tmpPos[1]))
										continue;
									
									double tmpDistance =  Math.pow(refPoints.get(j).x - tmpPos[0],2) + Math.pow(refPoints.get(j).y - tmpPos[1],2);
									
									if (tmpDistance < distance)// && 
											//mode == InternalMode.SIFT || (tmpPos[0] >= 0 && tmpPos[0] < _width1 && tmpPos[1] >= 0 && tmpPos[1] < _height1))
									{
										tmpIndex = k;
										sourceX = tmpPoints.get(k).x;
										sourceY = tmpPoints.get(k).y;
										distance = tmpDistance;
										newPosX = tmpPos[0];
										newPosY = tmpPos[1];
									}
								}
								
							}
						}
						//tmpPoints.remove(tmpIndex);
						used[tmpIndex] = true;
					}
					else
					{
						tmpIndex = j;
						sourceX = sensedPoints.get(j).x;
						sourceY = sensedPoints.get(j).y;
						double[] newPos = ch.GetValue(sourceX, sourceY);
						if (!Double.isInfinite(newPos[0]) || !Double.isNaN(newPos[0]) ||
								!Double.isInfinite(newPos[1]) || !Double.isNaN(newPos[1]))
						{
							newPosX = newPos[0];
							newPosY = newPos[1];
						}
						
					}
					
				//	System.out.println(String.format("%d,%d",i,tmpIndex));
				
					 if (Double.isInfinite(newPosX) || Double.isNaN(newPosX) ||
							  Double.isInfinite(newPosY) || Double.isNaN(newPosY))
						 continue;
						  //return defaultFitness;
					 
					refs.add(new Point(refPoints.get(j).x, refPoints.get(j).y));
					sensed.add(new Point(sourceX, sourceY));
				
				 
				}
				catch (Exception ex)
				{
					fails++;
					continue;
				}
				
				double dx = Math.pow(refPoints.get(j).x - newPosX,2);
				double dy = Math.pow(refPoints.get(j).y - newPosY,2);
				
				
				/*Point closest = getClosestMatch(ch, new Point(newPosX, newPosY), refPoints);
				if (closest.x == refPoints.get(j).x && 
						closest.y == refPoints.get(j).y)
				{
					dx = 0.5 * dx;
					dy = 0.5 * dy;
				}
				*/
				
				Diff fx = new Diff();
				fx.value = dx;
				fx.ox = sourceX;
				fx.oy = sourceY;
				fx.rx = newPosX;
				fx.ry = newPosY;
				
				Diff fy = new Diff();
				fy.value = dy;
				fy.ox = sourceX;
				fy.oy = sourceY;
				fy.rx = newPosX;
				fy.ry = newPosY;
				
				Diff f = new Diff();
				f.value = dx + dy;
				f.ox = sourceX;
				f.oy = sourceY;
				f.rx = newPosX;
				f.ry = newPosY;
				
				diffX.add(fx);
				diffY.add(fy);
				diff.add(f);
				
				if (Math.sqrt(dx+dy)<3)
					ch._matches++;
			}
			

			ch.matchesRefs = refs;
			ch.matchesSensed = sensed;
			
			
			if (fails > iterations/4.0)
				return defaultFitness;
			
			///////////////////////////////////////// FITNESS //////////////////////////////////////////////////////////

			double rmseX = Double.MAX_VALUE;
			double rmseY = Double.MAX_VALUE;
			double rmse = Double.MAX_VALUE;
			Collections.sort(diffX, comp);
			Collections.sort(diffY, comp);
			Collections.sort(diff, comp);
			double medianX = Double.MAX_VALUE;
			double medianY = Double.MAX_VALUE;
			double median = Double.MAX_VALUE;
			/*
			if (diffX.size() > 500)
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
			else*/
			{
			/***********************************************************
				for (int i = 0; i < diffX.size(); i++) {
					boolean x = false;
					if (!Double.isInfinite(diffX.get(i).value) && !Double.isNaN(diffX.get(i).value))
					{
						x = true;
						//System.out.println("thread: " + Thread.currentThread().getId() + " iteration: " + i + " diffX: " + diffX.get(i).value);
						if (rmseX == Double.MAX_VALUE)
							rmseX = diffX.get(i).value;
						else
							rmseX += diffX.get(i).value;	
					}
					else
					{
						rmseX = Double.MAX_VALUE;
					}
					boolean y = false;
					if (!Double.isInfinite(diffY.get(i).value) && !Double.isNaN(diffY.get(i).value))
					{
						y = true;
						if (rmseY == Double.MAX_VALUE)
							rmseY = diffY.get(i).value;
						else
							rmseY += diffY.get(i).value;	
					}
					else
					{
						rmseY = Double.MAX_VALUE;
					}
					if (x && y)
					{
						if (rmse == Double.MAX_VALUE)
							rmse = diffX.get(i).value + diffY.get(i).value;
						else
							rmse += diffX.get(i).value + diffY.get(i).value;
					}
				}
				
				
				if (diffX.size() == 1)
				{
					medianX = diffX.get(0).value;
					medianY = diffY.get(0).value;
				}
				else if (diffX.size() % 2 == 0)
				{
				    medianX = ((double)diffX.get(diffX.size()/2).value + (double)diffX.get(diffX.size()/2 - 1).value)/2;
				    medianY = ((double)diffY.get(diffY.size()/2).value + (double)diffY.get(diffY.size()/2 - 1).value)/2;
				}
				else
				{
					
				    medianX = (double) diffX.get(diffX.size()/2).value;
				    medianY = (double) diffY.get(diffY.size()/2).value;
				}
				
				***********************/
				//if (diffX.size() > 20)
				{
					int num = (int) (diffX.size() * 1)-1;
					medianX = diffX.get(num).value;
					medianY = diffY.get(num).value;
					median = diff.get(num).value;
				}
			}

			////////////////////////////////////////// RETURN VALUE
			
			double[] fitness = null;
			if (mode == InternalMode.SIFT || mode == InternalMode.SIFT_MOO2)
			{
				fitness = new double[] { Math.min(Math.sqrt(median),199999) };
				//fitness = new double[] { Math.min(rmse,199999) };
			}
			else if (mode == InternalMode.SIFT_MOO)
			{				
				//fitness = new double[] { Math.min(Math.sqrt(rmseX/diffX.size()),199999), Math.min(Math.sqrt(rmseY/diffY.size()),199999) };
				//fitness = new double[] { Math.min(rmseX,199999), Math.min(rmseY,199999) };
				
				if (medianX == -1) medianX = 200000;
				if (medianY == -1) medianY = 200000;
				fitness = new double[] { Math.min(Math.sqrt(medianX),199999), Math.min(Math.sqrt(medianY),199999) };
				
			}
			
			
			return fitness;
			
		}
	 
	 	private Point getClosestMatch(Chromosome ch, Point p, ArrayList<Point> list)
	 	{
	 		Point closest = list.get(0);
	 		double dist = 10e10;
	 		for (int k = 0; k < list.size(); k++) {
		 		double[] tmpPos = ch.GetValue(p.x, p.y); 	
				double tmpDistance =  Math.pow(list.get(k).x - tmpPos[0],2) + Math.pow(list.get(k).y - tmpPos[1],2);
				if (tmpDistance < dist)
				{
					dist = tmpDistance;
					closest = list.get(k);
				}
	 		}
	 		
	 		return closest;
	 	}
	 
		public double GetValue(String f, double col, double row) throws ScriptException
		{
			double val = 0;
			try
			{
				engine.put("x", col);
				engine.put("y", row);
			    String foo = f;
				Object o = engine.eval(foo);
			    val = Double.parseDouble(o.toString());
			}
			catch (Exception e) {	
				System.out.print(e.getMessage());
			}
		    return val;
		}
		
}


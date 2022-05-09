package Interfaces;

import java.util.ArrayList;
import java.util.LinkedList;

import org.opencv.core.Point;

import base.Fitness;

import algorithms.CONST;
import algorithms.TransformResult;

/**
 * The GPChromosome class represent chromosome in the GP algorithm
 *
 */
public abstract class Chromosome implements Comparable<Chromosome>{

	public int[][]  _resampling;
	public int _minX;
	public int _maxX;
	public int _minY;
	public int _maxY;
	public double _maxDiff;
	public double overlapPercent;
	//public double goodPercent;
	public int overlapCount;
	

	public int dominatedByNbr;
    public LinkedList<Chromosome> dominateSet;
    public Integer rank;
    public double distance;
    
    public ArrayList<Point> matchesRefs = new ArrayList<Point>();
    public ArrayList<Point> matchesSensed = new ArrayList<Point>();
public int _matches = 0;
	
	public ArrayList<String> _history = new ArrayList<String>();

	// fitness
	protected Fitness _fitness;
	
	private boolean _isNew = true;
	
	TransformResult _res = null;
	
	public Fitness get_fitness() {
	
		
		return _fitness;
	}

	public int get_rank() {
		return rank;
	}

	public void set_fitness(Fitness f) {
		this._fitness = f;
	}
	
	public void set_new(boolean b) {
		this._isNew = b;
	}
	
	public boolean get_new() {
		return _isNew;
	}

	public Chromosome() {
		
		_maxDiff = -1;
	}
	
	public Chromosome(Fitness fitness, double overlapP, 
			int overlapC,int minX, int minY, int maxX, int maxY, double maxDiff,  ArrayList<Point> mref,  ArrayList<Point> msensed ,int matches) {
		
		_fitness = fitness;
		//this.goodPercent = good;
		overlapPercent = overlapP;
		overlapCount = overlapC;
		_minX = minX;
		_minY = minY;
		_maxX = maxX;
		_maxY = maxY;
		_maxDiff = maxDiff;
		matchesRefs = mref;
		matchesSensed = msensed;
		_matches = matches;
	}

	/**
	 * generate random chromosome
	 */
	public abstract void Init();	
	
	/**
	 * generate random chromosome with the specified max deep 
	 * @param maxDeep
	 */
	//public abstract void Init(int maxLength);

	@Override
	public abstract String toString();
	
	public abstract String getDescription();
	
	public abstract String getInverseDescription();
	
	public abstract String getInfo();

	/**
	 * clone the chromosome
	 */
	public abstract Chromosome clone();

	/**
	 * @return random node
	 */
	// public abstract INode getRandomNode(int index);
	 

	public abstract double[] T(int col, int row, boolean isCentered);
	
	public abstract void Minimize();
	public abstract void Validate();
	public abstract boolean IsValid();

	public abstract double[] toArray();

	public abstract double[] GetValue(double x, double y);
	
	 ////////////////////////////////////////////////////////////////////
	 
    public double getValue(int objNum) throws Exception
    {
    	return _fitness.GetValue(objNum);
    }
    
	/**
	 * -1 jestem lepszy
	 * 0  remis
	 * 1  jestem gorszy
	 * @param ind
	 * @return
	 * @throws Exception 
	 */
	public int checkDomination(Chromosome c) throws Exception {
	    
	    int result = 0;
	    result = 0;
	    if (this.dominate(c)) {
	        result = -1;
	    }
	    else if (c.dominate(this)) {
	        result = 1;
	    }

		return result;
	}

	private boolean dominate(Chromosome c) throws Exception {
	    
		try
		{
		return _fitness.dominate(c.get_fitness());
		}
		 catch (Exception e) {
     		System.out.println(e.getMessage());
			}
		return false;
	}
	
	public void AddLog(String operation, String comments)
	{
		///////////////_history.add(String.format("Gen #%d: %s -- %s -- %s, %s", GeneticAlgorithm._gen, operation, comments, toString(), getDescription()));
	}
	
	public String getHistory()
	{
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < _history.size(); j++) {
			sb.append(_history.get(j));
			sb.append("\n");
		}
		return sb.toString();
	}

	public boolean ValidateDist(Point[][] dist)
	{
		int count = 0;
		double[] newPos = null;
		for (int col = 0; col < dist.length; col++) {
			for (int row = 0; row < dist[0].length; row++) {
				newPos = T(col, row, CONST._isCentered);
				Point p = dist[col][row];
				double diffX = p.x - newPos[0];
				double diffY = p.y - newPos[1];
				double diff = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
				if (diff > 20) 
					count++;
				
				if (count > 0.02 * dist.length * dist[0].length)
					return false;
			}
		}
		
		return true;
	}
	
	public abstract Chromosome getTestChromosome();
	public abstract void Enlarge(double num);
	
}

package GP;

import java.util.ArrayList;

import org.opencv.core.Point;

import base.Fitness;

import utils.GPUtils;
import utils.Utils;
import GP.Structure.Interface.INode;
import Interfaces.Chromosome;
import algorithms.CONST;

/**
 * The GPChromosome class represent chromosome in the GP algorithm
 *
 */
public class GPChromosome extends Chromosome {

		
	// root of the chromosome tree
	private INode[] _root = new INode[2];
	
	public GPChromosome()
	{
		super();
	}
	
	public void fixNodeRes(int res)
	{
		for(int index = 0; index<=1;index++)
		{
		set_root(index, _root[index].fixNodeRes(res));
		}
	}
	
	public INode get_root(int index) {
		return _root[index];
	}
	public INode set_root(int index, INode node) {
		return _root[index] = node;
	}

	
	/**
	 * constructor
	 * @param _root - the root of the tree (the chromosome)
	 * @param _fitness
	 */
	public GPChromosome(INode[] _root, Fitness fitness, //double good, 
			double overlapP, 
			int overlapC,int minX, int minY, int maxX, int maxY, double maxDiff,  ArrayList<Point> mrefs,  ArrayList<Point> msensed, int matches) {
		super(fitness, overlapP, overlapC, minX, minY, maxX, maxY, maxDiff, mrefs, msensed, matches);
		this._root = _root;
		
	}

	/**
	 * generate random chromosome
	 */
	@Override
	public void Init() {
		Init(CONST.MAX_DEEP_CHROMOSOME-1);	
	}
	
	
	
	/**
	 * generate random chromosome with the specified max deep 
	 * @param maxDeep
	 */
	public void Init(int maxDeep) {
		_root = new INode[] {InitTree(0, maxDeep),InitTree(1, maxDeep)};
	}
	
	public static INode InitTree(int index, int maxDeep)
	{
		INode node = GPUtils.getRandomOperator(index);
		node.generateSubTree(index, maxDeep);
		return node;
	}


	/**
	 * get x and calculate f(x)
	 * @return f(x)
	 */
	public double GetValue(int index, double x, double y) {
		return _root[index].getValue(x, y);
	}

	/**
	 * @return max height of the tree
	 */
	public int GetDeep(int index) {
		return _root[index].getDeep();
	}
	 
	public int GetDeep() {
		if (CONST._2D)
			return Math.max(_root[0].getDeep(), _root[1].getDeep());
		
		return _root[0].getDeep();
	}

	@Override
	public String toString() {
		if (CONST._2D)
			return _root[0].getDescription() + ";" + _root[1].getDescription();
		
		return _root[0].getDescription();
	}
	
	@Override
	public String getDescription() {
		if (CONST._2D)
			return _root[0].getDescription() + ";" + _root[1].getDescription();
		
		return _root[0].getDescription();
	}
	
	@Override
	public String getInverseDescription() {
		if (CONST._2D)
			return _root[0].getInverseDescription() + ";" + _root[1].getInverseDescription() ;
		
		return _root[0].getInverseDescription();
	}

	@Override
	public String getInfo()
	{
		return String.format("deep x: %s, deep y: %s", GetDeep(0), GetDeep(1));
	}
	
	/**
	 * clone the chromosome
	 */
	@Override
	public Chromosome clone() {
		try
		{
		return new GPChromosome(new INode[]{_root[0].clone(),_root[1].clone()}, _fitness==null ? null : _fitness.clone(), //goodPercent, 
				overlapPercent, overlapCount, 
				_minX,_minY, _maxX, _maxY, _maxDiff, matchesRefs, matchesSensed, _matches);
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return null;
	}

	
	/**
	 * @return random node in the tree (not including the root)
	 */
	 public INode getRandomNode(int index) {
		 return _root[index].getRandomNode();
	 }
	
	/**
	 * clone the chromosome while replacing the specified crossnode with the specified subtree
	 * 
	 * @param crossNode - the cross node in the current tree
	 * @param newSubTree - the new sub tree  
	 * @return cloned chromosome composed from the original and the newSubTree (which replace the crossNode subtree)
	 */
	public GPChromosome clone(int index, INode crossNode, INode newSubTree) {
		INode[] root = null;
		try
		{
			if (index == 0)
				root = new INode[] { _root[0].clone(crossNode, newSubTree), _root[1].clone() };
			else
				root = new INode[] { _root[0].clone(), _root[1].clone(crossNode, newSubTree) };
			
			return new GPChromosome(root, _fitness, //goodPercent,
					overlapPercent, overlapCount,				
					_minX,_minY, _maxX, _maxY, _maxDiff, matchesRefs, matchesSensed, _matches);		
		
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return null;
	}
	
@Override
public double[] T(int col, int row, boolean isCentered)
{
//	double a = 0.523;
	
	double[] pos = new double[2];
	pos[0] = GetValue(0, col, row);
	pos[1] = GetValue(1, col, row);
	
	//int newCol = (int) (Math.cos(a)*col + Math.sin(a)*row) - 50;
	//int newRow = (int) ( Math.cos(a)*row - Math.sin(a)*col) + 75	;
	//int newCol = col - 10;//(int) Math.floor(ch.GetValue(0, col, row));
	//int newRow = row - 30;//(int) Math.floor(ch.GetValue(1, col, row));
	
	// align images with their center
	if (isCentered)
	{
		pos[0] += Utils.centeredColVal;
		pos[1] += Utils.centeredRowVal;
	}
	return pos;
}

@Override
public int compareTo(Chromosome o) {
	return Double.compare(this.GetDeep(),((GPChromosome)o).GetDeep());
}

public void Minimize()
{
	if (GetDeep(0)> 10)
		get_root(0).Minimize();
	if (GetDeep(1)> 10)
		get_root(1).Minimize();
}

public void Validate()
{
	get_root(0).NodeValidation(false, CONST.MAX_DEEP_CHROMOSOME);
	get_root(1).NodeValidation(false, CONST.MAX_DEEP_CHROMOSOME);
	
}

	public boolean IsValid()
	{
		String sx = get_root(0).getDescription();
		String sy = get_root(1).getDescription();
		
		if ((!sx.contains("x") && !sx.contains("y")) ||
			(!sy.contains("x") && !sy.contains("y")))
			return false;
		
		return true;
	}

	@Override
	public double[] toArray() {
		return new double[0];
	}

	public double[] GetValue(double x, double y)
	{
		/*
		String sx = get_root(0).getDescription();
		String sy = get_root(1).getDescription();
		
		try {
			return GPUtils.GetValue(sx, sy, x, y);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		*/
		
		double newCol = GetValue(0, x, y);
		double newRow = GetValue(1, x, y);
		
		return new double[] { newCol, newRow };
	}

	@Override
	public Chromosome getTestChromosome() {
		return new GPChromosome();
	}
	
	@Override
	public void Enlarge(double num)
	{
		
	}
	
}

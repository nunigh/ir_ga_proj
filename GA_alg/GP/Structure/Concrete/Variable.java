package GP.Structure.Concrete;

import GP.Structure.Interface.INode;
import GP.Structure.Interface.IOperatorNullary;
import utils.Utils;

/**
 * this class represent the variable x in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class Variable extends IOperatorNullary {

	//public Variable()
	//{
	//	index = utils.Utils.rand.nextInt(2);
	//}
	
	public Variable(int i)
	{
		this(i, true);
	}
	
	public Variable(int i, boolean use)
	{
		super(i);
		
		if (!use)
		{
			index = Utils.rand.nextInt(2);
		}
		
		
	}
	
	
	@Override
	public double getValue(double x, double y) {
	
		if (index == 0)
			return x;
			
		return y;
	}

	@Override
	public String getDescription() {
		if (index == 0)
			return "x";
		else
			return "y";
	}
	
	@Override
	public String getInverseDescription()
	{
		//String s = getDescription();
		//if (s.compareTo("x")==0)
			//return "i";
		//return "j";
		return "....";
	}
	
	public Variable clone() {
		return new Variable(index, true);
	}
	
	public INode clone(INode crossNode, INode newSubTree) { 
		if (this == crossNode) 
			return newSubTree.clone();
		else
			return new Variable(index, true);
	}
	
	 public INode fixNodeRes(int res)
	 {
		 return this;
		 
	 }
	 	 
	 @Override
	 public boolean Validate() {return true;}

	 
	
}

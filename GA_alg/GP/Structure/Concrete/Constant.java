package GP.Structure.Concrete;

import GP.Structure.Interface.INode;
import GP.Structure.Interface.IOperatorNullary;
import utils.Utils;

/**
 * this class represent the constant value in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class Constant extends IOperatorNullary {

	double value;
	
	/**
	 * set value to be random number between min to max
	 */
	public Constant(int index) {
		super(index);
		
		if (utils.Utils.rand.nextDouble() < 0.5)
		{
			value = Double.valueOf(utils.Utils.rand.nextInt((int)Utils.size)) * Utils.rand.nextDouble();
			//if (value == 0.0) value = Utils.size;
		}
		else
		{
			value = Utils.rand.nextDouble();
			//if (value == 0.0) value = 100.0;
		}
		//value *= 10000;
		//value = ((int)value) / 10000.0;
		
		if (utils.Utils.rand.nextDouble() < 0.5)
			value = -1 * value;
	}
	
	public Constant(int index, double value) {
		super(index);
		
		this.value = value;
	//	this.value *= 10000;
	//	this.value = ((int)this.value) / 10000.0;
	}
	

	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public double getValue(double x, double y) {
		return value;
	}
	
	@Override
	public String getDescription() {
		if (value >= 0)
			return Double.toString(value);
		else
			return "(" + Double.toString(value) + ")";
	}
	
	@Override
	public String getInverseDescription()
	{
		return getDescription();
	}

	public Constant clone() {
		return new Constant(index, value);
	}
	
	public INode clone(INode crossNode, INode newSubTree) {
		if (this == crossNode)
			return newSubTree.clone();
		else
			return new Constant(index, value);
	}
	
	 public INode fixNodeRes(int res)
	 {
		 Multiply m = new Multiply(index);
		 m.setLeft(this.clone());
		 m.setRight(new Constant(res));
		 return m;
	 }
	
	 

	
}

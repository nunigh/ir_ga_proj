package GP.Structure.Concrete;

import GP.Structure.Interface.INode;
import GP.Structure.Interface.IOperatorNullary;

/**
 * this class represent the constant value in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class E extends IOperatorNullary {

	double value;
	
	/**
	 * set value to be Euler number
	 */
	public E(int index) {
		super(index);
		
		value = 2.71828;	
	}
	
	public E(int index, double value) {
		super(index);
		
		this.value = value;
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

	public E clone() {
		return new E(index, value);
	}
	
	public INode clone(INode crossNode, INode newSubTree) {
		if (this == crossNode)
			return newSubTree.clone();
		else
			return new E(index, value);
	}
	
	 public INode fixNodeRes(int res)
	 {
		 Multiply m = new Multiply(index);
		 m.setLeft(this.clone());
		 m.setRight(new E(res));
		 return m;
	 }

	
}

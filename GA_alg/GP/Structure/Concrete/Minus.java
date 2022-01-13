package GP.Structure.Concrete;

import GP.Structure.Interface.IOperatorBinary;

/**
 * this class represent the minus operator in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class Minus extends IOperatorBinary {
	
	public Minus( int index) {
		super(index);
	}
	@Override
	public double getValue(double x, double y) {
		return left.getValue(x, y) - right.getValue(x, y);
	}

	@Override
	public String getDescription() {
		return "(" + left.getDescription() + "-" + right.getDescription() + ")";
	}
	
	@Override
	public String getShortDescription(){
		return "(" + left.getShortDescription() + "-" + right.getShortDescription() + ")";
	}
	
	@Override
	public String getInverseDescription()
	{
		return "-(" + left.getInverseDescription() + "-" + right.getInverseDescription() + ")";
	}

	public Minus clone() {
		Minus operator = new Minus(index);
		operator.setLeft(left.clone());
		operator.setRight(right.clone());
		return operator;
	}
	
	

	
}

package GP.Structure.Concrete;

import GP.Structure.Interface.IOperatorBinary;

/**
 * this class represent the plus operator in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class Plus extends IOperatorBinary {

	public Plus( int index) {
		super(index);
	}
	@Override
	public double getValue(double x, double y) {
		return left.getValue(x,y) + right.getValue(x,y);
	}

	@Override
	public String getDescription() {
		return "(" + left.getDescription() + "+" + right.getDescription() + ")";
	}
	
	@Override
	public String getShortDescription(){
		return "(" + left.getShortDescription() + "+" + right.getShortDescription() + ")";
	}

	
	@Override
	public String getInverseDescription()
	{
		return "-" + "(" + left.getInverseDescription() + "+" + right.getInverseDescription() + ")";
	}

	public Plus clone() {
		Plus operator = new Plus(index);
		operator.setLeft(left.clone());
		operator.setRight(right.clone());
		return operator;
	}
	
}

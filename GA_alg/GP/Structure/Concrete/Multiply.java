package GP.Structure.Concrete;

import GP.Structure.Interface.IOperatorBinary;

/**
 * this class represent the multiplication operator in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class Multiply extends IOperatorBinary {

	public Multiply( int index) {
		super(index);
	}

	@Override
	public double getValue(double x, double y) {
		return (double) (left.getValue(x,y) * right.getValue(x,y));
	}

	@Override
	public String getDescription() {
		return "(" + left.getDescription() + "*" + right.getDescription() + ")";
	}
	
	@Override
	public String getShortDescription(){
		return "(" + left.getShortDescription() + "*" + right.getShortDescription() + ")";
	}
	@Override
	public String getInverseDescription()
	{
		return "(" + left.getDescription() + "/" + right.getInverseDescription() + ")";
	}

	public Multiply clone() {
		Multiply operator = new Multiply(index);
		operator.setLeft(left.clone());
		operator.setRight(right.clone());
		return operator;
	}
	
}

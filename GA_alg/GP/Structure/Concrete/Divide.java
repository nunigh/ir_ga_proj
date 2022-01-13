package GP.Structure.Concrete;

import GP.Structure.Interface.IOperatorBinary;

/**
 * this class represent the divide operator in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class Divide extends IOperatorBinary {

	public Divide( int index) {
		super(index);
	}
	
	@Override
	public double getValue(double x, double y) {
		double divisor = right.getValue(x, y);
		if (divisor == 0)
		{
			return Double.NaN;
		}
		else
		{
			return (double) (left.getValue(x, y) / divisor);
		}
	}

	@Override
	public String getDescription() {
		return "(" + left.getDescription() + "/" + right.getDescription() + ")";
	}
	
	@Override
	public String getShortDescription(){
		return "(" + left.getShortDescription() + "/" + right.getShortDescription() + ")";
	}
	@Override
	public String getInverseDescription()
	{
		return "(" + left.getInverseDescription() + "*" + right.getInverseDescription() + ")";
	}

	public Divide clone() {
		Divide operator = new Divide(index);
		operator.setLeft(left.clone());
		operator.setRight(right.clone());
		return operator;
	}
	
}

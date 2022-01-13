package GP.Structure.Concrete;

import GP.Structure.Interface.IOperatorBinary;

/**
 * this class represent the power operator in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class Power extends IOperatorBinary {

	public Power( int index) {
		super(index);
	}
	
	@Override
	public double getValue(double x, double y) {
		double v = Math.pow(left.getValue(x,y),right.getValue(x,y));
		
		if (Double.isNaN(v) || Double.isInfinite(v))
			return Double.NaN;
		
		return v;
	}

	@Override
	public String getDescription() {
		return "(Math.pow(" + left.getDescription() + "," + right.getDescription() + "))";
	}
	
	@Override
	public String getShortDescription(){
		return "(Math.pow(" + left.getShortDescription() + "," + right.getShortDescription() + "))";
	}
	
	@Override
	public String getInverseDescription()
	{
		return "(" + left.getInverseDescription() + "sqrt" + right.getInverseDescription() + ")";
	}

	public Power clone() {
		Power operator = new Power(index);
		operator.setLeft(left.clone());
		operator.setRight(right.clone());
		return operator;
	}
	
}

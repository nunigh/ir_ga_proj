package GP.Structure.Concrete;

import GP.Structure.Interface.INode;
import GP.Structure.Interface.IOperatorUnary;
import utils.FastSin;

/**
 * this class represent the sine operator in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class Sine extends IOperatorUnary {

	//double value = -1;
	public Sine(int index) {
		//this(index,Utils.getAngle());
		super(index);
	}
	
	//public Sine(int index, double val) {
//		super(index);
	//	value = val;
	//}
	
	@Override
	public double getValue(double x, double y) {
		//return Math.sin(value);
		double angle = child.getValue(1,1);//%(2.0*Math.PI);
		return FastSin.sin(angle);
	}

	@Override
	public String getDescription() {
				
		//return "sin(" + String.valueOf(a) + ")";//child.getDescription() + ")";
		StringBuilder str = new StringBuilder();
		str.append("Math.sin(");
		str.append(child.getDescription());
		str.append(")");
		return str.toString();
	}
	
	@Override
	public String getShortDescription() {
		double angle = child.getValue(1,1);//%(2.0*Math.PI);
		//double a = (180*angle/Math.PI)*100;
		double a = angle * 100;
		a= ((int)a)/100.0;
				
		//return "sin(" + String.valueOf(a) + ")";//child.getDescription() + ")";
		StringBuilder str = new StringBuilder();
		str.append("Math.sin(");
		str.append(a);
		str.append(")");
		return str.toString();
	}
	
	@Override
	public String getInverseDescription()
	{
		return "...";
	}
	
	 public INode fixNodeRes(int res)
	 {
		 return this;
	 }

/*	@Override
public INode clone(INode crossNode, INode newSubTree) {
		if (this == crossNode) 
			return newSubTree.clone();
		else
			return new Sine(index, value);
	}
*/
	@Override
	public INode clone() {
		//return new Sine(index, value);
		Sine operator = new Sine(index);
		operator.setChild(child.clone());
		return operator;
	}
	

	 @Override
	 public boolean Validate() {return true;}
	
	 
	 

}

package GP.Structure.Concrete;

import GP.Structure.Interface.INode;
import GP.Structure.Interface.IOperatorUnary;
import utils.FastCos;

/**
 * this class represent the cosine operator in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class Cosine extends IOperatorUnary {
//	double value = -1;
	public Cosine( int index) {
		//this(index,Utils.getAngle());
		super(index);
	}
	
//	public Cosine(int index, double val) {
	//	super(index);
		//value = val;
	//}
	
	
	@Override
	public double getValue(double x, double y) {		
		//return Math.cos(value);
		double angle = child.getValue(1,1);//%(2*Math.PI);
		return FastCos.cos(angle);
	}

	@Override
	public String getDescription() {
		
		//double angle = child.getValue(1, 1)%(2*Math.PI);
		
		//double a = (180*angle/Math.PI)*100;
		//a= ((int)a)/100.0;
				
		//return "cos(" + String.valueOf(a) + ")";//child.getDescription() + ")";
		
		
		StringBuilder str = new StringBuilder();
		str.append("Math.cos(");
		str.append(child.getDescription());
		str.append(")");
		return str.toString();
	
	}
	
	@Override
	public String getShortDescription() {
		double angle = child.getValue(1,1);//%(2.0*Math.PI);
		//double a = (180*angle/Math.PI)*100;
		double a = angle * 100;
		a = ((int)a)/100.0;
				
		//return "sin(" + String.valueOf(a) + ")";//child.getDescription() + ")";
		StringBuilder str = new StringBuilder();
		str.append("Math.cos(");
		str.append(a);
		str.append(")");
		return str.toString();
	}
	
	@Override
	public String getInverseDescription()
	{
		return "...";
	}
	
/*	@Override
public INode clone(INode crossNode, INode newSubTree) {
		if (this == crossNode) 
			return newSubTree.clone();
		else
			return new Cosine(index, value);
	}
*/
	@Override
	public INode clone() {
		//return new Sine(index, value);
		Cosine operator = new Cosine(index);
		operator.setChild(child.clone());
		return operator;
	}

	@Override
	public INode fixNodeRes(int res) {
		// TODO Auto-generated method stub
		return this;
	}


	 @Override
	 public boolean Validate() {return true;}
	
	
}

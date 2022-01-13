package GP.Structure.Concrete;

import GP.Structure.Interface.IOperatorMultiple;


/**
 * this class represent the minus operator in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class IMQ extends IOperatorMultiple {
	
	public IMQ( int index) {
		super(index,6);	
	}
	@Override
	public double getValue(double x, double y) {
		double v = 0;
		
		v = Math.pow(_nodes[3].getValue(1, 1)*Math.pow(x-_nodes[0].getValue(1, 1),2) + 
						_nodes[4].getValue(1, 1)*Math.pow(y-_nodes[1].getValue(1, 1),2) + 
						_nodes[5].getValue(1, 1)*Math.pow(_nodes[2].getValue(1, 1),2),-0.5);
		
		if (Double.isNaN(v) || Double.isInfinite(v))
			return Double.NaN;
		
		return v;
	}

	@Override
	public String getDescription() {
		
		StringBuilder str = new StringBuilder();
		str.append("Math.pow(");
		str.append(_nodes[3].getDescription());
		str.append("*Math.pow(x-(");
		str.append(_nodes[0].getDescription());
		str.append("),2) + ");
		str.append(_nodes[4].getDescription());
		str.append("*Math.pow(y-(");
		str.append(_nodes[1].getDescription());
		str.append("),2) + ");
		str.append(_nodes[5].getDescription());
		str.append("*Math.pow(");
		str.append(_nodes[2].getDescription());
		str.append(",2),-0.5)");
		return str.toString();
	}
	
	@Override
	public String getShortDescription(){
		double val1 = _nodes[0].getValue(1, 1);
		double val2 = _nodes[1].getValue(1, 1);
		double val3 = _nodes[2].getValue(1, 1);
		double val4 = _nodes[3].getValue(1, 1);
		double val5 = _nodes[4].getValue(1, 1);
		double val6 = _nodes[5].getValue(1, 1);
		
		
		StringBuilder str = new StringBuilder();
		str.append("IMQ(");
		str.append(val1);
		str.append(",");
		str.append(val2);
		str.append(",");
		str.append(val3);
		str.append(",");
		str.append(val4);
		str.append(",");
		str.append(val5);
		str.append(",");
		str.append(val6);
		str.append(")");
		return str.toString();
	}
	
	@Override
	public String getInverseDescription()
	{
		return "...";
	}

	/*
	 public IMQ clone() {
		IMQ operator = new IMQ(index);
		operator.setFirst(first.clone());
		operator.setSecond(second.clone());
		operator.setThird(third.clone());
		return operator;
	}
	*/
	

	 @Override
	 public boolean Validate() {return true;}
	
}

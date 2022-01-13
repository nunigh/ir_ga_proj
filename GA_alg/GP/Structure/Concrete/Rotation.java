package GP.Structure.Concrete;

import GP.Structure.Interface.INode;
import GP.Structure.Interface.IOperatorUnary;
import utils.FastCos;
import utils.FastSin;
import utils.Utils;

/**
 * this class represent the constant value in the chromosome
 * the class implements INode interface and therefore functions 
 * documentation can be found inside INode.java
 */
public class Rotation extends IOperatorUnary {
	
	//double angle = -1;
	
	
	/**
	 * set value to be random number between min to max
	 */

	public Rotation(int i)
	{
		this(i, false);
		
	}
	
	public Rotation(int i, boolean use) {
		super(i);
		//this.share = true;
		
		///////////if (index <0)
		if (!use)
			index = utils.Utils.rand.nextInt(2);
		
		//angle = Utils.getAngle();
		
	}
	
	
	/*public Rotation( int i, double ang) {
		super(i);
	//	this.share = true;
		if (index <0)
			index = utils.Utils.rand.nextInt(2);
		else
			index = i;
		
		angle = ang;		
	}*/

	public double getValue(double x, double y) {
		
		//double length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		//double alpha = Math.asin(length/Utils.height);
		//double beta = (180 - angle)/2.0;
		
		//double value = child.getValue(x, y) ;
		double value = child.getValue(1, 1);//%(2.0*Math.PI);
		double newVal = 0;
		double x_ = (x - Utils.width/2.0);
		double y_ = (y - Utils.height/2.0);
		if (index == 0)
		{
			newVal = (FastCos.cos(value)*x_ - FastSin.sin(value)*y_);
			newVal += Utils.width/2.0;
			
		}
		else
		{
			newVal = FastCos.cos(value)*y_ + FastSin.sin(value)*x_ ;
			newVal += Utils.height/2.0;
		}
		return newVal;
	}
	
	public String getDescription() {
		//double angle = child.getValue(1, 1)%(2.0*Math.PI);
		//double a = (180*angle/Math.PI)*100;
		//a= ((int)a)/100.0;
		
		//return "( rotate" + index + "(" + String.valueOf(a) + "))";//child.getDescription() + "))";
		//return "( rotate" + index + "(" + child.getDescription() + "))";
		StringBuffer desc = new StringBuffer();
		
		String str = child.getDescription();
		
		//desc.append("rotate");
		//desc.append(index);
		//desc.append("(");
		//desc.append(child.getDescription());
		//desc.append(")");
		//return desc.toString();
		
		if (index ==0)
		{
			desc.append("(Math.cos(");
			desc.append(str);
			desc.append(")*(x - ");
			desc.append(Utils.width/2.0);
			desc.append(") - Math.sin("); 
			desc.append(str);
			desc.append(")*(y - ");
			desc.append(Utils.height/2.0);
			desc.append(") + ");
			desc.append(Utils.width/2.0);
			desc.append(")");
			
		}
		else
		{
			desc.append("(Math.cos(");
			desc.append(str);
			desc.append(")*(y - ");
			desc.append(Utils.height/2.0);
			desc.append(") + Math.sin("); 
			desc.append(str);
			desc.append(")*(x - ");
			desc.append(Utils.width/2.0);
			desc.append(") + ");
			desc.append(Utils.height/2.0);
			desc.append(")");
			
		}
		return desc.toString();
		
	}
	
	@Override
	public String getShortDescription() {
		double angle = child.getValue(1,1);
		//double a = (180*angle/Math.PI)*100;
		double a = angle * 100;
		a = ((int)a)/100.0;
		
		StringBuffer desc = new StringBuffer();
		
		desc.append("rotate");
		desc.append(index);
		desc.append("(");
		desc.append(a);
		desc.append(")");
		return desc.toString();
	
	}
		
	
	@Override
	public String getInverseDescription()
	{
		//double a = angle*100;
		//a= ((int)a)/100.0;
	//return "( rotate^-1" + index + "(" + 180*a/Math.PI + "))";//child.getInverseDescription() + "))";
		return "...";
	}

	@Override
	public INode clone(INode crossNode, INode newSubTree) {
		if (this == crossNode) 
			return newSubTree.clone();
		else
		{
			Rotation a =  new Rotation(index, true);
			a.setChild(child.clone(crossNode, newSubTree));
			return a;
		}
	}

	@Override
	public INode clone() {
		Rotation operator = new Rotation(index, true);
		operator.setChild(child.clone());
		return operator;
	}

	@Override
	public INode fixNodeRes(int res) {
		return this;
		 
	}
	
	 @Override
	 public boolean Validate() {return true;}
	
	
	
}

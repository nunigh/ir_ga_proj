package GP.Structure.Interface;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import GP.Structure.Concrete.Constant;

import utils.GPUtils;

/**
 * abstract class which represent unary operator nodes 
 * 
 */
public abstract class IOperatorUnary implements INode {
	
	protected INode child;  //unary operator have one child
	
	public int index = -1;
	
	public IOperatorUnary(int i)
	{
		index = i;
	}
	
	
	// return child
	public INode getChild() {
		return child;
	}
	
	// set chid
	public void setChild(INode child) {
		this.child = child;
	}
	
	/**
	 * generating random sub tree which is no deeper than the given deep
	 * @param deep - max deep of the sub tree
	 */
	public void generateSubTree(int index, int deep) {
		if (deep > 1) {
			child = GPUtils.getRandomOperator(index);
			child.generateSubTree(index, deep-1);	
		}
		else {
			child = GPUtils.getRandomNullaryOperator(index);
		}
	}
	
	/**
	 * @return the tree max height
	 */
	public int getDeep() {
		return 1 + child.getDeep();
	}
	
	/**
	 * @return array of the chromosome childrens (null if there is no children)
	 */
	public INode[] getChildrens() {
		return new INode[]{child};
	}
	
	@Override
	public String toString() {
		return this.getDescription();
	}
	

	/**
	 * @return cloned tree
	 */
	public INode clone() {
		IOperatorUnary operator = null;
		Class<? extends IOperatorUnary> c = this.getClass();
		try {
			Constructor<?> cc = c.getConstructor(Integer.TYPE);
			operator = (IOperatorUnary) cc.newInstance(index);
		
		} catch (Throwable t) {
			t.printStackTrace();
		}
		operator.setChild(child.clone());
		return operator;
	}
	
	/** 
	 * @param crossNode - the cross node in the current tree
	 * @param newSubTree - the new sub tree  
	 * @return cloned tree composed from the original tree and the newSubTree (which replace the crossNode subtree)
	 */
	public INode clone(INode crossNode, INode newSubTree) {
		if (this == crossNode) {
			return newSubTree.clone();
		}
		else {
			IOperatorUnary operator = null;
			Class<? extends IOperatorUnary> c = this.getClass();
			try {
				Constructor<?> cc = c.getConstructor(Integer.TYPE);
				operator = (IOperatorUnary) cc.newInstance(index);
				
			} catch (Throwable t) {
				t.printStackTrace();
			}
			operator.setChild(child.clone(crossNode, newSubTree));
			return operator;
		}
	}
	public INode fixNodeRes(int res)
	{
		setChild(child.fixNodeRes(res));
		return this;
	}

	/**
	 * @return array list holding all the nodes in the tree
	 */	
	public ArrayList<INode> getAllChildrens() {
		ArrayList<INode> nodes = new ArrayList<INode>();
		nodes.add(this);
		nodes.addAll(child.getAllChildrens());
		return nodes;
	}
	

	/**
	 * Get random node
	 * @return the selected random node
	 */
	 @Override
	 public INode getRandomNode() {
		 
	  if (utils.Utils.rand.nextDouble() < 0.5)
	   return child.getRandomNode();
	  else 
	   return this;
	 }
	 
	 @Override
	 public void NodeValidation(boolean v, int deep){

		 v = v || Validate();
		 if (v && !GPUtils.IsValid(child))
			 child = GPUtils.GetValidNode(index, child);
		 //else
			 child.NodeValidation(v, deep-1);

		  if (deep == 0)
		  	 child = new Constant(0, child.getValue(1,1));
		 
	 }
	 
	 @Override
	 public boolean Validate() {return false;}
	 
	 @Override
	 public boolean IsValidSubTree()
	 {
		 if (!GPUtils.IsValid(this))
			 return false;
		 
		 return child.IsValidSubTree();
	 }
	 
	 @Override
	 public void Minimize()
	 {
		 if (child.IsValidSubTree())
		 {
			 child = new Constant(0, child.getValue(1,1));
		 }
		 else
		 {
			 child.Minimize();
		 }
	 }
	 
}

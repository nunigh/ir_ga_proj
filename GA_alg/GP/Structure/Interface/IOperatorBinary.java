package GP.Structure.Interface;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import GP.Structure.Concrete.Constant;

import utils.GPUtils;

/**
 * abstract class which represent binary operator nodes 
 * 
 */
public abstract class IOperatorBinary implements INode {

	protected INode left;
	protected INode right;


	public int index = -1;
	
	
	public IOperatorBinary(int i)
	{
		index = i;
	}
	/**
	 * generating random sub tree which is no deeper than the given deep
	 * @param deep - max deep of the sub tree
	 */
	public void generateSubTree(int index, int deep) {
		if (deep > 1) {
			left = GPUtils.getRandomOperator(index);
			right = GPUtils.getRandomOperator(index);
			left.generateSubTree(index, deep-1);
			right.generateSubTree(index, deep-1);	
		}
		else {
			left = GPUtils.getRandomNullaryOperator(index);
			right = GPUtils.getRandomNullaryOperator(index);
		}
	}

	@Override
	 public void NodeValidation(boolean v, int deep){
		
		v = v || Validate();
		 if (v && !GPUtils.IsValid(right))
			 right = GPUtils.GetValidNode(index, right);
		// else
			 right.NodeValidation(v, deep-1);
		 
		 
		 if (v && !GPUtils.IsValid(left))
			 left = GPUtils.GetValidNode(index, left);
		// else
			 left.NodeValidation(v,deep-1);
			 
			  if (deep == 0)
			  {
			  	 right = new Constant(0, right.getValue(1,1));
			  	left = new Constant(0, left.getValue(1,1));
			  }
		 
		 
		 
	 }
	 @Override
	 public boolean Validate() {return false;}

	// get left child
	public INode getLeft() {
		return left;
	}

	// set left child
	public void setLeft(INode left) {
		this.left = left;
	}

	// get right child
	public INode getRight() {
		return right;
	}

	// set right child
	public void setRight(INode right) {
		this.right = right;
	}

	/**
	 * @return the tree max height
	 */
	public int getDeep() {
		return 1 + Math.max(left.getDeep(), right.getDeep());
	}

	/**
	 * @return array of the chromosome childrens (null if there is no children)
	 */
	public INode[] getChildrens() {
		return new INode[]{left, right};
	}

	@Override
	public String toString() {
		return this.getDescription();
	}

	
	/**
	 * @return cloned tree
	 */
	public INode clone() {
		IOperatorBinary operator = null;
		Class<? extends IOperatorBinary> c = this.getClass();
		try {
			operator = c.newInstance();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		operator.setLeft(left.clone());
		operator.setRight(right.clone());
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
			IOperatorBinary operator = null;
			Class<? extends IOperatorBinary> c = this.getClass();
			try {
				Constructor<?> cc = c.getConstructor(Integer.TYPE);
				operator = (IOperatorBinary) cc.newInstance(index);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			operator.setLeft(left.clone(crossNode, newSubTree));
			operator.setRight(right.clone(crossNode, newSubTree));
			return operator;
		}

	}
	public INode fixNodeRes(int res)
	{
		setRight(right.fixNodeRes(res));
		setLeft(left.fixNodeRes(res));
		return this;
	}

	/**
	 * @return array list holding all the nodes in the tree
	 */
	public ArrayList<INode> getAllChildrens() {
		ArrayList<INode> nodes = new ArrayList<INode>();
		nodes.add(this);
		nodes.addAll(left.getAllChildrens());
		nodes.addAll(right.getAllChildrens());
		return nodes;
	}
	
	/**
	 * Get random node
	 * @return the selected random node
	 */
	@Override
	 public INode getRandomNode() {
	  double val = utils.Utils.rand.nextDouble();
	  if (val < 0.33)
	   return this;
	  else if (val < 0.66)
	   return right.getRandomNode();
	  else 
	   return left.getRandomNode();
	 }
	
	 @Override
	 public boolean IsValidSubTree()
	 {
		 if (!GPUtils.IsValid(this))
			 return false;
		 
		 boolean br = right.IsValidSubTree();
		 boolean bl = left.IsValidSubTree();
		 return br && bl;
	 }
	 
	 @Override
	 public void Minimize()
	 {
		 if (right.IsValidSubTree())
		 {
			 right = new Constant(0, right.getValue(1,1));
		 }
		 else
		 {
			 right.Minimize();
		 }
		 
		 if (left.IsValidSubTree())
		 {
			 left = new Constant(0, left.getValue(1,1));
		 }
		 else
		 {
			 left.Minimize();
		 }
	 }
	 
}

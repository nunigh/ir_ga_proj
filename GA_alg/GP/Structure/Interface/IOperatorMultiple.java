package GP.Structure.Interface;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import GP.Structure.Concrete.Constant;


import utils.GPUtils;

/**
 * abstract class which represent binary operator nodes 
 * 
 */
public abstract class IOperatorMultiple implements INode {

	protected INode[] _nodes;	

	public int index = -1;
	
	
	
	public IOperatorMultiple(int i, int count)
	{
		index = i;
		_nodes = new INode[count];
	}
	/**
	 * generating random sub tree which is no deeper than the given deep
	 * @param deep - max deep of the sub tree
	 */
	public void generateSubTree(int index, int deep) {
		if (deep > 1) {
			for (int i = 0; i < _nodes.length; i++) {
				_nodes[i] = GPUtils.getRandomOperator(index);
				_nodes[i].generateSubTree(index, deep-1);
			}
		}
		else {
			for (int i = 0; i < _nodes.length; i++) {
				_nodes[i] = GPUtils.getRandomNullaryOperator(index);
			}
		}
	}
	
	public int Size()
	{
		return _nodes.length;
	}

	// get left child
	public INode get(int i) {
		return _nodes[i];
	}

	// set left child
	public void set(int i, INode node) {
		_nodes[i] = node;
	}

	/**
	 * @return the tree max height
	 */
	public int getDeep() {
		int max = 0;
		for (int i = 0; i < _nodes.length; i++) {
			max = Math.max(max, 1+_nodes[i].getDeep());
		}		
		return max;
	}

	/**
	 * @return array of the chromosome childrens (null if there is no children)
	 */
	public INode[] getChildrens() {
		return _nodes;
	}

	@Override
	public String toString() {
		return this.getDescription();
	}
	

	/**
	 * @return cloned tree
	 */
	public INode clone() {
		IOperatorMultiple operator = null;
		Class<? extends IOperatorMultiple> c = this.getClass();
		try {
			Constructor<?> cc = c.getConstructor(Integer.TYPE);
			operator = (IOperatorMultiple) cc.newInstance(index);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		for (int i = 0; i < _nodes.length; i++) {
			operator.set(i, _nodes[i].clone());
		}
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
			IOperatorMultiple operator = null;
			Class<? extends IOperatorMultiple> c = this.getClass();
			try {
				Constructor<?> cc = c.getConstructor(Integer.TYPE);
				operator = (IOperatorMultiple) cc.newInstance(index);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			for (int i = 0; i < _nodes.length; i++) {
				operator.set(i, _nodes[i].clone(crossNode, newSubTree));	
			}
			return operator;
		}

	}
	public INode fixNodeRes(int res)
	{
		for (int i = 0; i < _nodes.length; i++) {
			_nodes[i] = _nodes[i].fixNodeRes(res);
		}
		return this;
	}

	/**
	 * @return array list holding all the nodes in the tree
	 */
	public ArrayList<INode> getAllChildrens() {
		ArrayList<INode> nodes = new ArrayList<INode>();
		nodes.add(this);
		for (int i = 0; i < _nodes.length; i++) {
			nodes.addAll(_nodes[i].getAllChildrens());
		}
		return nodes;
	}
	
	/**
	 * Get random node
	 * @return the selected random node
	 */
	@Override
	 public INode getRandomNode() {
	  double val = utils.Utils.rand.nextDouble();
	  double p = 1.0/(_nodes.length+1.0);
	  double cur = p;
	  if (val < cur)	 
	   return this;
	  for (int i = 0; i < _nodes.length; i++) {
	  	 cur += p;
	  	 if (val < cur)
	  		 return _nodes[i]; 
	  		 
	  }
	  return _nodes[_nodes.length-1];
	 }
	
	@Override
	 public void NodeValidation(boolean v, int deep){
		 
		v = v || Validate();
		for (int i = 0; i < _nodes.length; i++) {
			 if (v&& !GPUtils.IsValid(_nodes[i]))
				 _nodes[i] = GPUtils.GetValidNode(index, _nodes[i]);
			 //else
				 _nodes[i].NodeValidation(v, deep-1);
		}

			  if (deep == 0)
			  {
				  for (int i = 0; i < _nodes.length; i++) {
					  _nodes[i] = new Constant(0, _nodes[i].getValue(1,1));
				  }
			  }
		 
	 }
	 
	 
	 @Override
	 public boolean Validate() {return false;}


	 @Override
	 public boolean IsValidSubTree()
	 {
		 if (!GPUtils.IsValid(this))
			 return false;
		 
		 boolean b = true;
		 for (int i = 0; i < _nodes.length; i++) {
			 b = b && _nodes[i].IsValidSubTree();
			 if (!b)
				 return false;
		 }
		 return true;
	 }
	 
	 @Override
	 public void Minimize()
	 {
		 for (int i = 0; i < _nodes.length; i++) {
			if (_nodes[i].IsValidSubTree())
			{
				_nodes[i] = new Constant(0, _nodes[i].getValue(1,1));	
			}
			 else
			 {
				 _nodes[i].Minimize();
			 }
		 }
		
		 
	 }
}

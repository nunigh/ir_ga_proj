package GP.Structure.Interface;

import java.util.ArrayList;

import utils.GPUtils;


/**
 * abstract class which represent nullary operator nodes
 * 
 */
public abstract class IOperatorNullary implements INode {
	
	public int index = -1;
	
	public boolean share = false;
	public IOperatorNullary(int i)
	{
		index = i;
	}
	
	/**
	 * generating random sub tree which is no deeper than the given deep
	 * @param deep - max deep of the sub tree
	 */
	public void generateSubTree(int index, int deep) {
		
	}
	
	/**
	 * @return the tree max height
	 */
	public int getDeep() {
		return 1;
	}
	
	/**
	 * @return array of the chromosome childrens (null if there is no children)
	 */
	public INode[] getChildrens() {
		return null;
	}
	
	@Override
	public String toString() {
		return this.getDescription();
	}
	

	@Override
	public String getShortDescription(){ return getDescription(); }
	/**
	 * @return cloned tree
	 */
	public abstract INode clone();
	
	/**
	 * @return array list holding all the nodes in the tree
	 */
	public ArrayList<INode> getAllChildrens() {
		ArrayList<INode> nodes = new ArrayList<INode>();
		nodes.add(this);
		return nodes;
	}
	
	/**
	 * Get random node
	 * @return the selected random node
	 */
	 @Override
	 public INode getRandomNode() {
		 return this;
	 }
	 
	 @Override
	 public boolean Validate() {return false;}

	 @Override
	 public void NodeValidation(boolean v, int deep){}
	 
	 @Override
	 public boolean IsValidSubTree()
	 {
		 if (!GPUtils.IsValid(this))
			 return false;
		 
		 return true;
	 }
	 
	 @Override
	 public void Minimize()
	 {		
		 
	 }
	
}

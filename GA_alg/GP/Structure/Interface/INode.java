 package GP.Structure.Interface;

import java.util.ArrayList;

/**
 * interface for nodes in the GP tree (chromosome)
 *
 */
public interface INode {
	
	
	/**
	 * @return the value of the tree equation 
	 */
	public double getValue(double x, double y);
	
	/**
	 * @return description of the tree equation
	 */
	public String getDescription();
	public String getShortDescription();
	public String getInverseDescription();
	
	/**
	 * generating random sub tree which is no deeper than the given deep
	 * @param deep - max deep of the sub tree
	 */
	public void generateSubTree(int index, int deep);
	
	/**
	 * @return the tree max height
	 */
	public int getDeep();
	
	/**
	 * @return array of the chromosome childrens (null if there is no children)
	 */
	public INode[] getChildrens();
	
	/**
	 * @return cloned tree
	 */
	public INode clone();
	
	/**
	 * @return array list holding all the nodes in the tree
	 */
	public ArrayList<INode> getAllChildrens();
	
	/** 
	 * @param crossNode - the cross node in the current tree
	 * @param newSubTree - the new sub tree  
	 * @return cloned tree composed from the original tree and the newSubTree (which replace the crossNode subtree)
	 */
	public INode clone(INode crossNode, INode newSubTree);
	
	/**
	 * Get random node
	 * @return the selected random node
	 */
	public INode getRandomNode();
	
	public INode fixNodeRes(int res);
	
	public void NodeValidation(boolean v, int deep);
	public boolean Validate();
	
	public void Minimize();
	public boolean IsValidSubTree();
}

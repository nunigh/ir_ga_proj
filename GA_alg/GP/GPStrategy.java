package GP;

import java.util.ArrayList;
import java.util.Random;

import javax.script.ScriptException;

import org.opencv.core.Point;


import utils.GPUtils;
import utils.InternalMode;
import utils.GPTransformations;
import utils.Utils;
import GP.Structure.Concrete.Constant;
import GP.Structure.Interface.INode;
import GP.Structure.Interface.IOperatorBinary;
import GP.Structure.Interface.IOperatorMultiple;
import GP.Structure.Interface.IOperatorNullary;
import GP.Structure.Interface.IOperatorUnary;
import Interfaces.Chromosome;
import Interfaces.IGeneticStrategy;
import algorithms.CONST;

public class GPStrategy implements IGeneticStrategy {

	InternalMode _mode = null;
	
	public GPStrategy(InternalMode mode)
	{
		_mode = mode;
	}
	
	@Override
	public Chromosome CreateChromosome()
	{
		return new GPChromosome();
	}
	
	@Override
	public Chromosome CreateChromosome(Chromosome ch1, Chromosome ch2)
	{
		GPChromosome cc = new GPChromosome();
		cc.set_root(0, ((GPChromosome)ch1).get_root(0).clone());
		cc.set_root(1, ((GPChromosome)ch2).get_root(1).clone());
		return cc;
	}
	
	@Override
	public  Chromosome[] CrossOver(Chromosome c1, Chromosome c2) {

		GPChromosome parent1 = (GPChromosome)c1;
		GPChromosome parent2 = (GPChromosome)c2;
		
		GPChromosome child1 = new GPChromosome();
		GPChromosome child2 = new GPChromosome();

		boolean mix = (Utils.rand.nextDouble() > 1);
		int maxIndex = 1;
		/*if (_mode == InternalMode.SIFT) {
			maxIndex = 0;
			mix = false;
		}*/
		for (int index = 0; index <= maxIndex; index++) {
			int index2 = index;
			if (mix)
				index2 = 1 - index2;
			// perform cross-over with some probability

			if ((parent1.GetDeep(index) > 1 || parent2.GetDeep(index2) > 1)
					&& new Random().nextDouble() <= CONST.CROSSOVER_PERCENT) {

				if ((parent1.GetDeep(index) > 1 && parent2.GetDeep(index2) > 1)
						|| Utils.rand.nextDouble() > 0.5) {
					INode cross1 = null;
					INode cross2 = null;

					// get random subtree for cross-over for each given
					// chromosome
					// if (c1.get_root(index) instanceof IOperatorNullary)
					// cross1 = Utils.getRandomNullaryOperator();
					// else
					cross1 = parent1.getRandomNode(index);

					// if (c2.get_root(index) instanceof IOperatorNullary)
					// cross2 = Utils.getRandomNullaryOperator();
					// else
					cross2 = parent2.getRandomNode(index2);

					// perform cross-over by cloning given chromosome and
					// replacing the selected subtrees
					child1.set_root(index, ((GPChromosome)parent1.clone(index, cross1, cross2)).get_root(index));
					child2.set_root(index2, ((GPChromosome)parent2.clone(index2, cross2, cross1)).get_root(index2));
				} else {
					child1.set_root(index, parent1.get_root(index).clone());
					child2.set_root(index2, parent2.get_root(index2).clone());
				}
			} else {
				// if (Utils.rand.nextDouble() <= 0.5) // replace
				// {
				// child1.set_root(index, c2.get_root(index).clone());
				// child2.set_root(index, c1.get_root(index).clone());
				// }
				// else // do nothing
				{
					child1.set_root(index, parent1.get_root(index).clone());
					child2.set_root(index2, parent2.get_root(index2).clone());
				}
			}
			
			
		}
		
		// return the new created chromosome, i.e., result of cross-over
		return new Chromosome[] { child1, child2 };

		
	}
	
	@Override
	public void Mutation(Chromosome chrom, double mutationRate) {
	
		GPChromosome ch = (GPChromosome)chrom;
		// generate mutation with some probability
		int maxIndex = 1;
		if (_mode == InternalMode.SIFT) {
			maxIndex = 0;
		}
		for (int index = 0; index <= maxIndex; index++) {
			// String s = ch.toString();
			int deep = ch.GetDeep(index);
			boolean t = false;
			if (Utils.rand.nextDouble() <= CONST.MUTATE_PERCENT) {
				// if (Utils.rand.nextDouble() <= CONST.MUTATE_NODE_PERCENT)
				if (Utils.rand.nextDouble() <= 0.5) {
					// generate mutation on each node with some probability
					if (new Random().nextDouble() <= mutationRate)// CONST.MUTATE_NODE_PERCENT)
					{
						INode op = null;

						if (deep == 1) {
							// generate mutation on each node with some
							// probability
							// IOperatorNullary op = (IOperatorNullary)
							op = ch.get_root(index);// .clone();
						} else // deep > 1
						{

							// INode nn =
							op = ch.getRandomNode(index);
							// nn.generateSubTree(index,
							// CONST.MAX_DEEP_MUTATION);
						}
						INode temp = null;

						// ///// 1) temp = Chromosome.InitTree(index,
						// CONST.MAX_DEEP_MUTATION);

						// ///// 2)
						temp = GPUtils.getRandomMultiOperator(index);
						temp.generateSubTree(index, CONST.MAX_DEEP_MUTATION - 1);

						// if (temp.getDeep() > 1)
						{
							/*
							 * INode rand = temp.getRandomNode(); INode node =
							 * null; if (rand == temp) node = temp; else node =
							 * temp.clone(rand, op);
							 * 
							 * ch.set_root(index, node);
							 */

							// /////////////////////////////////////////////////////
							boolean f = true;
							if (Utils.rand.nextDouble() <= 0.5) {
								if (temp instanceof IOperatorNullary) {
									f = false;
								} else if (temp instanceof IOperatorBinary) {
									if (Utils.rand.nextDouble() < 0.5)
										((IOperatorBinary) temp).setLeft(ch
												.get_root(index));
									else
										((IOperatorBinary) temp).setRight(ch
												.get_root(index));
								} else if (temp instanceof IOperatorMultiple) {
									double v = Utils.rand.nextDouble();
									if (v < 0.33)
										((IOperatorMultiple) temp).set(0,
												ch.get_root(index));
									else if (v < 0.66)
										((IOperatorMultiple) temp).set(1,
												ch.get_root(index));
									else
										((IOperatorMultiple) temp).set(2,
												ch.get_root(index));
								} else if (temp instanceof IOperatorUnary) {
									((IOperatorUnary) temp).setChild(ch
											.get_root(index));
								}

								// ////////////////////////////////////////////////////

								ch.set_root(index, temp);
							} else {
								ch.set_root(index,
										ch.get_root(index).clone(op, temp));
							}

							t = f && true;
						}
						/*
						 * else { ch.set_root(index, temp); t = true; }
						 */
						ch.AddLog("Mutation", String.format("Subtree"));
						t = true;
					} // mutation rate
				} else { // 0.5 prob
					// boolean t = Mutation(ch.get_root(index));

					// for (int d = 0; d<deep;d++)

					{
						ArrayList<INode> all = ch.get_root(index)
								.getAllChildrens();
						// System.out.println("index: " + index + " " +
						// ch.toString());
						for (int i = 0; i < all.size(); i++) {
							if (Mutation(ch, index, all.get(i), mutationRate)) {
								ch.AddLog("Mutation", String.format("Node"));
								t = true;
								// System.out.println("i: " + i + " " +
								// ch.toString());
							}
						}
					}

				}

				if (t) {
					// printRank("mutation (:" + index + ")" + s);
					// printRank("output:   " + ch.toString());
				}

			}

		}
		
	}

	/**
	 * Mutation function loop through all chromosome's nodes and with
	 * CONST.MUTATE_PERCENT probability perform mutation on each node.
	 * 
	 * @param node
	 *            - node for mutation
	 */
	private boolean Mutation(GPChromosome ch, int index, INode node,
			double mutationRate) {

		
		INode newNode = null;
		if (node != null) {
			// generate mutation on each node with some probability
			// if (new Random().nextDouble() <=
			// mutationRate)//CONST.MUTATE_NODE_PERCENT)
			{
				if (new Random().nextDouble() <= CONST.MUTATE_PER_NODE_PERCENT) {
					if (node instanceof IOperatorNullary) 
					{
						if (node instanceof Constant) 
						{
							if (Utils.rand.nextDouble() < 0.5)
							{
								double newVal = getNoiseVal(((Constant) node));
								//((Constant) node).setValue(newVal);
								newNode = new Constant(index,newVal);
							}
							else
							{
								newNode = GPUtils.getRandomNullaryOperator(index);
							}
						} 
						else 
						{
							newNode = GPUtils.getRandomNullaryOperator(index);
						}
					} else if (node instanceof IOperatorBinary) {
						newNode = GPUtils.getRandomBinaryOperator(index);
						if (newNode != null) {
							((IOperatorBinary) newNode)
									.setRight(((IOperatorBinary) node)
											.getRight());
							((IOperatorBinary) newNode)
									.setLeft(((IOperatorBinary) node).getLeft());
						}
					} else if (node instanceof IOperatorMultiple) {
						newNode = GPUtils.getRandomThirdOperator(index);
						if (newNode != null) {
							IOperatorMultiple n = ((IOperatorMultiple) newNode);
							for (int i = 0; i < n.Size(); i++) {
								n.set(i, ((IOperatorMultiple) node).get(i));
							}
						}
					} else if (node instanceof IOperatorUnary) {
						newNode = GPUtils.getRandomUnaryOperator(index);
						if (newNode != null) {
							((IOperatorUnary) newNode)
									.setChild(((IOperatorUnary) node)
											.getChild());
						}
					}

					if (newNode != null
							&& node.getDescription().compareTo(
									newNode.getDescription()) != 0) {
						ch.set_root(index, ch.clone(index, node, newNode).get_root(index));
						return true;
					}
				}
			}

		}

		return false;
	}
	
	private double getNoiseVal(Constant node)
	{
		double noise = 0;
		if (Utils.rand.nextDouble() < 0.5)
		{
			
			noise = (Utils.rand.nextGaussian() * new Constant(0).getValue(1, 1));//(((int)(Utils.rand.nextDouble()*100.0))/1000.0) * (MaxVal() - MinVal());
		}
		else
		{
			noise = Utils.rand.nextGaussian();//*10.0;
		}
		
		 return node.getValue(1, 1) + noise;
	}
	
	@Override
	public double CalculateRMSE(
			ArrayList<Point> A, ArrayList<Point> B,
			Chromosome ch,
			StringBuilder output, Boolean print) {
		
		/*
		GPChromosome cc = (GPChromosome)ch;
		String fx = cc.get_root(0).getDescription();
		String fy = cc.get_root(1).getDescription();
		return CalculateRMSE(A, B, fx, fy, output, print);
		*/
		
		double rmse = Double.MAX_VALUE;
		double diffX = Double.MAX_VALUE;
		double diffY = Double.MAX_VALUE;
		for (int i = 0; i < A.size(); i++) {
			double[] newPos = ch.GetValue(B.get(i).x, B.get(i).y); 	
			if (!Double.isInfinite(newPos[0])
					&& !Double.isInfinite(newPos[1])
					&& !Double.isNaN(newPos[0]) && !Double.isNaN(newPos[1])) {
				diffX = A.get(i).x - newPos[0];
				diffY = A.get(i).y - newPos[1];
				if (rmse == Double.MAX_VALUE)
					rmse = Math.pow(diffX, 2) + Math.pow(diffY, 2);
				else
					rmse += Math.pow(diffX, 2) + Math.pow(diffY, 2);
			}
		}
		
		if (Double.isInfinite(rmse) || Double.isNaN(rmse))
		{
			rmse = Double.MAX_VALUE;
		}
		else if (rmse < Double.MAX_VALUE)
		{
			rmse = rmse / A.size();
			rmse = Math.sqrt(rmse);
		}
		return rmse;
	
	}
	
	public static double CalculateRMSE(
			ArrayList<Point> A, ArrayList<Point> B,
			String fx, String fy,
			StringBuilder output, Boolean print) {
		
		
		double rmse = 0;
		
		for (int i = 0; i < A.size(); i++) {
			double[] newPos = new double[2];
			double diffX = 0;
			double diffY = 0;
			// newPos = T(fx, fy, (int)Math.round(B.get(i).x),
			// (int)Math.round(B.get(i).y));
			// newPos[0] = GetValue(fx, B.get(i).x, B.get(i).y);
			// newPos[1] = GetValue(fy, B.get(i).x, B.get(i).y);
			try {
				newPos = GPTransformations.T(fx, fy, B.get(i).x, B.get(i).y);
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			diffX = A.get(i).x - newPos[0];
			diffY = A.get(i).y - newPos[1];

			if (!Double.isInfinite(newPos[0])
					&& !Double.isInfinite(newPos[1])
					&& !Double.isNaN(newPos[0]) && !Double.isNaN(newPos[1])) {
				rmse += Math.pow(diffX, 2) + Math.pow(diffY, 2);
			}

			StringBuilder s = new StringBuilder();
			if (print || output != null) {
				s.append("(");
				s.append(A.get(i).x);
				s.append(",");
				s.append(A.get(i).y);
				s.append(") (");
				s.append(newPos[0]);
				s.append(",");
				s.append(newPos[1]);
				s.append(")");
				s.append(" --- (");
				s.append(diffX);
				s.append(",");
				s.append(diffY);
				s.append(")");
			}
			if (print) {
				System.out.println(s);
			}
			if (output != null) {
				output.append(s);
				output.append("\n");
			}
			// if (newPos[0] >= 0 && newPos[1] >=0 && newPos[0] < width &&
			// newPos[1] < height)
			{
				// rmse += Math.pow((int)Math.round(A.get(i).x) - newPos[0],2) +
				// Math.pow((int)Math.round(A.get(i).y) - newPos[1],2);

			}
			// else
			// {
			// System.out.println("Point out of bounds: original("+B.get(i).x +
			// "," + B.get(i).y+") transformed("+newPos[0]+","+newPos[1]+")");
			// }
		}
		rmse = rmse / A.size();
		rmse = Math.sqrt(rmse);
		return rmse;
	}


}

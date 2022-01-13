package GA;

import java.util.ArrayList;

import org.opencv.core.Point;

import algorithms.CONST;
import GA.Structure.Interface.IBaseTransformation;
import Interfaces.Chromosome;
import Interfaces.IGeneticStrategy;

import utils.InternalMode;
import utils.Utils;

public class GAStrategy implements IGeneticStrategy {

	InternalMode _mode = null;
	
	public GAStrategy(InternalMode mode)
	{
		_mode = mode;
	}
	
	@Override
	public Chromosome CreateChromosome()
	{
		GAChromosome ch = new GAChromosome();
		/*ch.Set(0, new TranslationX(0));
		ch.Set(1, new TranslationY(0));
		ch.Set(2, new Rotation(0));
		ch.Set(3, new Scaling(1));
		ch.Set(4, new ReflectionOrigin(0));
		*/
		

	
		return ch;
	}
	
	@Override
	public Chromosome CreateChromosome(Chromosome ch1, Chromosome ch2)
	{
		Chromosome cc = ch1.clone();
		// TODO: handle
		return cc;
	}
	
	// perform cross-over with some probability
	private static float alpha = 0.5f;
	@Override
	public  Chromosome[] CrossOver(Chromosome c1, Chromosome c2) {

		GAChromosome parent1 = (GAChromosome)c1;
		GAChromosome parent2 = (GAChromosome)c2;
		
		GAChromosome child1 = new GAChromosome();
		GAChromosome child2 = new GAChromosome();
		
		for (int i = 0; i < parent1.GetLength(); i++) {
			/*if (Utils.rand.nextDouble() < 0.5)
			{
				child1.Set(i, parent1.Get(i));
				child2.Set(i, parent2.Get(i));
			}
			else
			{
				child1.Set(i, parent2.Get(i));
				child2.Set(i, parent1.Get(i));
			}
			*/
			float val1 = parent1.Get(i).GetVal();
			float val2 = parent2.Get(i).GetVal();
			IBaseTransformation t1 = parent1.Get(i).clone();
			IBaseTransformation t2 = parent2.Get(i).clone();
			if (Utils.rand.nextDouble() < 0.3)
			{
		
				//alpha = (float) Utils.rand.nextDouble();
				float diff = Math.abs(alpha * (val1 - val2));
				//diff = 0;
				
				if (val1 < val2)
				{
					t1.SetVal(val1 + diff);
					t2.SetVal(val2 - diff);
				}
				else
				{
					t1.SetVal(val1 - diff);
					t2.SetVal(val2 + diff);
				}
			}
			
			if (Utils.rand.nextDouble() < 0.5)
			{
				child1.Set(i, t1);
				child2.Set(i, t2);
			}
			else
			{
				child1.Set(i, t1);
				child2.Set(i, t2);
			}
			
		}

		// return the new created chromosome, i.e., result of cross-over
		return new Chromosome[] { child1, child2 };

	}
	
	// generate mutation with some probability
	@Override
	public void Mutation(Chromosome chrom, double mutationRate) {
	
		if (Utils.rand.nextDouble() <= CONST.MUTATE_PERCENT) 
		{
			GAChromosome ch = (GAChromosome)chrom;
			for (int i = 0; i < ch.GetLength(); i++) 
			{
				if (Utils.rand.nextDouble() <= mutationRate)// CONST.MUTATE_NODE_PERCENT)
				{
					ch.Get(i).Mutate();
				}
			}
		}
		
	}
	
	@Override
	public double CalculateRMSE(
			
			ArrayList<Point> A, ArrayList<Point> B, 
			Chromosome ch,
			StringBuilder output, Boolean print) {
		double rmse = 0;

		GAChromosome cc = (GAChromosome)ch;
		
		for (int i = 0; i < A.size(); i++) {
			double[] newPos = new double[2];
			double diffX = 0;
			double diffY = 0;
			// newPos = T(fx, fy, (int)Math.round(B.get(i).x),
			// (int)Math.round(B.get(i).y));
			// newPos[0] = GetValue(fx, B.get(i).x, B.get(i).y);
			// newPos[1] = GetValue(fy, B.get(i).x, B.get(i).y);
			newPos = cc.GetValue(B.get(i).x, B.get(i).y);

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
		
		}
		rmse = rmse / A.size();
		rmse = Math.sqrt(rmse);
		return rmse;
	}
	

}

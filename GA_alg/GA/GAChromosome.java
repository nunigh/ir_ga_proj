package GA;


import java.util.ArrayList;

import org.opencv.core.Point;

import utils.InternalMode;
import algorithms.CONST;
import base.Fitness;

import Fitness.SIFTTask;
import GA.Structure.Concrete.ReflectionOrigin;
import GA.Structure.Concrete.ReflectionX;
import GA.Structure.Concrete.ReflectionY;
import GA.Structure.Concrete.Rotation;
import GA.Structure.Concrete.Scaling;
import GA.Structure.Concrete.ScalingX;
import GA.Structure.Concrete.ScalingY;
import GA.Structure.Concrete.ShearingX;
import GA.Structure.Concrete.ShearingY;
import GA.Structure.Concrete.TranslationX;
import GA.Structure.Concrete.TranslationY;
import GA.Structure.Interface.IBaseTransformation;
import Interfaces.Chromosome;

/**
 * The GPChromosome class represent chromosome in the GP algorithm
 *
 */
public class GAChromosome extends Chromosome {

	public ITransformation _trans = null;

	public GAChromosome()
	{
		super();
		
		_trans = new AffineTransformation();
		//_trans = new ProjectiveTransformation();
		//_trans = new ParamTransformation();
		
	}
	
	public GAChromosome(TranslationX tx, TranslationY ty, Rotation rot, Scaling s, ScalingY sy,ShearingX shearx, ShearingY sheary, 
			ReflectionX reflectx, ReflectionY reflecty, ReflectionOrigin reflecto)
	{
		super();
		
		_trans = new AffineTransformation(tx, ty, rot, s,sy, shearx, sheary, reflectx, reflecty, reflecto);
		//_trans = new ParamTransformation();
	}
	
	public GAChromosome(TranslationX tx, TranslationY ty, Scaling a, Scaling b,  Scaling c, Scaling d, Scaling e, Scaling f)
	{
		super();
		
		_trans = new ProjectiveTransformation(tx, ty, a, b, c, d, e, f);
		//_trans = new ParamTransformation();
	}
	
	public GAChromosome(IBaseTransformation[] genes)
	{
		super();
		
		//_trans = new AffineTransformation(tx, ty, rot, s, shearx, sheary, reflectx, reflecty, reflecto);
		_trans = new ParamTransformation(genes);
	}
	
	public GAChromosome(ITransformation trans,						
						Fitness fitness, //double good, 
						double overlapP, 
						int overlapC,int minX, int minY, int maxX, int maxY, double maxDiff,
						 ArrayList<Point> mrefs,  ArrayList<Point> msensed, int matches)
	{
		super(fitness, overlapP, overlapC, minX, minY, maxX, maxY, maxDiff, mrefs, msensed, matches);
		_trans = trans;
		
	}
	
	public int GetLength()
	{
		return _trans.GetLength();
	}
	
	public IBaseTransformation Get(int i)
	{
		return _trans.Get(i);
	}
	
	public void Set(int i, IBaseTransformation t)
	{
		_trans.Set(i, t);
	}
	
	@Override
	public void Init() {
		_trans.Init();
	}

	@Override
	public String toString() {
		return getDescription();
	}

	@Override
	public String getDescription() {
		return _trans.getDescription();
	}

	@Override
	public String getInverseDescription() {
		return "";
	}
	
	@Override
	public String getInfo()
	{
		return "";
	}

	@Override
	public Chromosome clone() {
		
		return new GAChromosome(_trans.clone(), 
				_fitness, //goodPercent,
				overlapPercent, overlapCount,				
				_minX,_minY, _maxX, _maxY, _maxDiff, matchesRefs, matchesSensed, _matches);
	}

	@Override
	public double[] T(int col, int row, boolean isCentered)
	{
		return GetValue(col, row);
	}

	@Override
	public int compareTo(Chromosome o) {
		
		return 0;
	}
		
	@Override
	public void Minimize()
	{
		// do nothing
	}
	
	@Override
	public void Validate()
	{
		// do nothing
	}
	
	@Override
	public boolean IsValid()
	{
		return true;//return this.overlapPercent > CONST.MIN_OVERLAP_PERCENT;
	}


	public void updateChromGenes()
	{
		_trans.updateChromGenes();
	}


	@Override
	public double[] toArray() {
		return _trans.toArray();
	}


	@Override
	public double[] GetValue(double x, double y)
	{
		/*CvMat point = cvCreateMat(3,1, CvType.CV_64F);
		point.put(0, 0, x);
		point.put(1, 0, y);
		point.put(2, 0, 1);
		*/
		return _trans.GetValue(x,y);
	}

	@Override
	public void Enlarge(double num)
	{
		for (int i = 0; i < _trans.GetLength(); i++) {
			if (_trans.Get(i) instanceof TranslationX)					
			{
				float val = (float) (_trans.Get(i).GetVal() * num);
				_trans.Set(i, new TranslationX(val));
			}
			if (_trans.Get(i) instanceof TranslationY)					
			{
				float val = (float) (_trans.Get(i).GetVal() * num);
				_trans.Set(i, new TranslationY(val));
			}
		}
	}
	
	@Override
	public Chromosome getTestChromosome() {
		
		// P1
		/*
		TranslationX tx = new TranslationX(-2.578830);
		TranslationY ty = new TranslationY(-1.467842);
		Rotation rot = new Rotation(115.330437);
		Scaling s = new Scaling(0.926496);
		//IBaseTransformation sx = new ScalingX();
		//IBaseTransformation sy = new ScalingY(sx.get_val());		
		//IBaseTransformation reflect = new ReflectionOrigin();
		ShearingX shearx = new ShearingX(0);
		ShearingY sheary = new ShearingY(0);
		 */
		
		// P2
		/*
		TranslationX tx = new TranslationX(-0.926589);
		TranslationY ty = new TranslationY(-1.205429);
		Rotation rot = new Rotation(-231.168694);
		Scaling s = new Scaling(1.100000);
		//IBaseTransformation sx = new ScalingX();
		//IBaseTransformation sy = new ScalingY(sx.get_val());		
		//IBaseTransformation reflect = new ReflectionOrigin();
		ShearingX shearx = new ShearingX(0);
		ShearingY sheary = new ShearingY(0.500000);
		*/
		
		/*
		TranslationX tx = new TranslationX(-0.248734);
		TranslationY ty = new TranslationY(-1.528371);
		Rotation rot = new Rotation(-48.745777);
		Scaling s = new Scaling(1.100000);
		//IBaseTransformation sx = new ScalingX();
		//IBaseTransformation sy = new ScalingY(sx.get_val());		
		//IBaseTransformation reflect = new ReflectionOrigin();
		ShearingX shearx = new ShearingX(0.078500);
		ShearingY sheary = new ShearingY(0.500000);
		*/
		
		/*
		TranslationX tx = new TranslationX(-0.9260446914290585f);
		TranslationY ty = new TranslationY(-1.8800064983573768f);
		Rotation rot = new Rotation(189.24053462404154f);
		Scaling s = new Scaling(0.8587999999999999f);
		//IBaseTransformation sx = new ScalingX();
		//IBaseTransformation sy = new ScalingY(sx.get_val());		
		//IBaseTransformation reflect = new ReflectionOrigin();
		ShearingX shearx = new ShearingX(0.0f);
		ShearingY sheary = new ShearingY(0.0335f);
		*/
		
		IBaseTransformation[] genes = new IBaseTransformation[6];
		genes[0] = new Param(0,1);
		genes[1] = new Param(1,0);
		genes[2] = new ParamT(2,45f);
		genes[3] = new Param(3,0);
		genes[4] = new Param(4,1);
		genes[5] = new ParamT(5,15f);
		
		return new GAChromosome(genes);
		
		//return new GAChromosome(tx,ty,rot,s,shearx, sheary, new ReflectionX(), new ReflectionY(), new ReflectionOrigin());
	}
	
	/*public static GAChromosome Test(float itx, float ity, float irot, float is, float ishx, float ishy, 
			ArrayList<Point>  points1, ArrayList<Point> points2)
	{
		
		TranslationX tx = new TranslationX(itx);
		TranslationY ty = new TranslationY(ity);
		Rotation rot = new Rotation(irot);
		Scaling s = new Scaling(is);
		ShearingX shx = new ShearingX(ishx);
		ShearingY shy = new ShearingY(ishy);
		
		//tx: -275.34781478207594,ty: -200.0,rot: 180.0,s: 0.9808,shearx: 0.023,sheary: 0.019,
		
		GAChromosome ch = new GAChromosome(tx, ty, rot, s, shx, shy, new ReflectionX(), new ReflectionY(), new ReflectionOrigin());
		new SIFTTask(InternalMode.SIFT, points1, points2, ch, false).run();
		
		return ch;
		
	}*/
	
	
	
	
	
}



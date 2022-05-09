package GA;

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
import algorithms.CONST;

public class AffineTransformation implements ITransformation {
	
	private IBaseTransformation[] _genes = null;


	float _tx;
	float _ty;
	float _rot;
	float _scalexy;
	float _shearx;
	float _sheary;

	public double _angleRad;
	public double _cosAngleRad;
	public double _sinAngleRad;

	final static double angleRadMult =  2 * Math.PI/360;

	@Override
	public void updateChromGenes()
	{
		_tx = _genes[0].GetVal();
		_ty = _genes[1].GetVal();
		_rot =_genes[2].GetVal();
		_scalexy = _genes[3].GetVal();
		_shearx = _genes[4].GetVal();
		_sheary = _genes[5].GetVal();

		_angleRad = _rot*angleRadMult;
		_cosAngleRad = Math.cos(_angleRad);
		_sinAngleRad = Math.sin(_angleRad);
	}
	
	public AffineTransformation(IBaseTransformation[] genes)
	{
		_genes = genes;
		updateChromGenes();
	}
	
	public AffineTransformation()
	{
		_genes =  new IBaseTransformation[6];//!!
		Init();
		updateChromGenes();
	}
	
	public AffineTransformation(TranslationX tx, TranslationY ty, Rotation rot, Scaling s, ScalingY sy,ShearingX shearx, ShearingY sheary,
			ReflectionX reflectx, ReflectionY reflecty, ReflectionOrigin reflecto)
	{
		_genes =  new IBaseTransformation[6];//!!
		Init(tx, ty, rot, s, sy,shearx, sheary, reflectx, reflecty, reflecto);
		updateChromGenes();
	}
	
	
	
	@Override
	public void Init()
	{
		
		TranslationX tx = new TranslationX();
		TranslationY ty = new TranslationY();
		Rotation rot = new Rotation();
		//!!ScalingX s = new ScalingX();
		Scaling s = new Scaling();
		ScalingY sy = new ScalingY();
		//IBaseTransformation sx = new ScalingX();
		//IBaseTransformation sy = new ScalingY(sx.get_val());		
		//IBaseTransformation reflect = new ReflectionOrigin();		
		ShearingX shearx = new ShearingX();
		ShearingY sheary = new ShearingY();
		ReflectionX reflectx = new ReflectionX();
		ReflectionY reflecty = new ReflectionY();
		ReflectionOrigin reflecto = new ReflectionOrigin();
		
		Init(tx, ty, rot, s,sy, shearx, sheary, reflectx, reflecty, reflecto);
		
	}
	
	public void Init(TranslationX tx, TranslationY ty, Rotation rot, Scaling s,ScalingY sy, ShearingX shearx, ShearingY sheary,
			ReflectionX reflectx, ReflectionY reflecty, ReflectionOrigin reflecto)
	{
		// TODO: handle shearing val !!
		
		_genes[0]=tx;
		_genes[1]=ty;
		_genes[2]=rot;
		_genes[3]=s;
		//!!_genes[4]=sy;
		//_genes[4]=reflect;
		_genes[4]=shearx;
		_genes[5]=sheary;
		//_genes[6]=reflectx;
		//_genes[7]=reflecty;
		//_genes[8]=reflecto;
		updateChromGenes();
	}
	
	@Override
	public ITransformation clone()
	{
		IBaseTransformation[] genes = new IBaseTransformation[_genes.length];
		for (int i = 0; i < genes.length; i++) {
			genes[i] = _genes[i].clone();
		}
		return new AffineTransformation(genes);
	}


	/*
	@Override
	public double[] GetValue(double x, double y)
	{
		if (CONST.PROFILING_MODE)
		{
			return GetValue_hadar(x,y);
		}
		return GetValue_orig(x,y);
	}*/

	public double[] GetValue_orig(double x, double y)
	{
		double[] point = new double[] {x,y};
		//for (int i = _genes.length-1; i >= 0; i--) {
		//for (int i = 0; i <= _genes.length-1; i++) {
		//	point = _genes[i].TransPoint(point);
		//}

		/*
		double[] tmpy = new double[] {x,y};
		tmpy = _genes[5].TransPoint(tmpy);
		double[] tmpx = new double[] {x,y};
		tmpx = _genes[4].TransPoint(tmpx);

		point[0] = tmpx[0];
		point[1] = tmpy[1];
		//point = _genes[5].TransPoint(point);
		//point = _genes[4].TransPoint(point);
		point = _genes[3].TransPoint(point);
		point = _genes[2].TransPoint(point);
		point = _genes[1].TransPoint(point);
		point = _genes[0].TransPoint(point);
		*/


		point = _genes[0].TransPoint(point);
		point = _genes[1].TransPoint(point);
		point = _genes[2].TransPoint(point);
		point = _genes[3].TransPoint(point);
		point = _genes[4].TransPoint(point);
		point = _genes[5].TransPoint(point);
		//!!point = _genes[6].TransPoint(point);


		//double[] tmpx = new double[] {point[0],point[1]};
		//	tmpx = _genes[4].TransPoint(tmpx);
		//	double[] tmpy = new double[] {point[0],point[1]};
		//	tmpy = _genes[5].TransPoint(tmpy);

		//	point[0] = tmpx[0];
		//	point[1] = tmpy[1];


		//return new double[] { point.get(0, 0), point.get(1, 0)};
		return point;
	}

	@Override
	public double[] GetValue(double doubleX, double y) {
	//public double[] GetValue_hadar(double doubleX, double y) {
		//updateChromGenes();//changes on each mutation...

		doubleX += _tx;
		y += _ty;
		double prevx = doubleX;
		doubleX = doubleX*_cosAngleRad + y*_sinAngleRad;
		y = prevx* (-1)*_sinAngleRad + y *_cosAngleRad;
		doubleX = doubleX*_scalexy;
		y = y*_scalexy;
		doubleX = _shearx*y + doubleX;
		y = _sheary*doubleX + y;
		return new double[] {doubleX,y};

		// [hadar]: i can do a redsign here
		// for each chromosome, it creates 7 different objects each time
		// (A) those classes can be static, 1 for each kind for the program , and we can pass the value
		// (B) all thing is redundant and it will be faster to just path the matemtic calculation here
	}
	
	@Override
	public String getDescription()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < _genes.length; i++) {
			sb.append(_genes[i].getDescription());
			sb.append(",");
		};
		sb.delete(sb.length()-1, sb.length());
		return sb.toString();
	}

	@Override
	public IBaseTransformation Get(int i) {
		return _genes[i];
	}

	@Override
	public int GetLength() {
		return _genes.length;
	}

	@Override
	public void Set(int i, IBaseTransformation t) {
		_genes[i].SetVal(t.GetVal());
		updateChromGenes();

	}

	@Override
	public double[] toArray() {
		/*double array[] = new double[_genes.length];
		for (int i = 0; i < _genes.length; ++i) {
			array[i] = Get(i).GetVal();
		}*/
		updateChromGenes();
		double [] arr =  {_tx,_ty,_rot,_scalexy,_shearx,_sheary};
		return arr;
	}
}


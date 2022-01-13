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

public class AffineTransformation implements ITransformation {
	
	private IBaseTransformation[] _genes = null;
	
	public AffineTransformation(IBaseTransformation[] genes)
	{
		_genes = genes;
	}
	
	public AffineTransformation()
	{
		_genes =  new IBaseTransformation[6];//!!
		Init();
	}
	
	public AffineTransformation(TranslationX tx, TranslationY ty, Rotation rot, Scaling s, ScalingY sy,ShearingX shearx, ShearingY sheary,
			ReflectionX reflectx, ReflectionY reflecty, ReflectionOrigin reflecto)
	{
		_genes =  new IBaseTransformation[6];//!!
		Init(tx, ty, rot, s, sy,shearx, sheary, reflectx, reflecty, reflecto);
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
	
	@Override
	public double[] GetValue(double x, double y)
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
		
	}
	
}

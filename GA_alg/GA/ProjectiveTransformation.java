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

public class ProjectiveTransformation implements ITransformation {
	
	private IBaseTransformation[] _genes = null;
	
	public ProjectiveTransformation(IBaseTransformation[] genes)
	{
		_genes = genes;
	}
	
	public ProjectiveTransformation()
	{
		_genes =  new IBaseTransformation[8];
		Init();
	}
	
	public ProjectiveTransformation(TranslationX tx, TranslationY ty, Scaling a, Scaling b, Scaling c, Scaling d, Scaling e, Scaling f)
	{
		_genes =  new IBaseTransformation[8];
		Init(tx, ty, a,b,c,d,e,f);
	}


	@Override
	public void updateChromGenes() {

	}

	@Override
	public void Init()
	{
		
		TranslationX tx = new TranslationX();
		TranslationY ty = new TranslationY();
		Scaling a = new Scaling();
		Scaling b = new Scaling();
		Scaling c = new Scaling();
		Scaling d = new Scaling();
		Scaling e = new Scaling();
		Scaling f = new Scaling();
		
		Init(tx,ty,a,b,c,d,e,f);
		
	}
	
	public void Init(TranslationX tx, TranslationY ty, Scaling a, Scaling b,  Scaling c, Scaling d, Scaling e, Scaling f)
	{

		
		_genes[0]=a;
		_genes[1]=b;
		_genes[2]=tx;
		_genes[3]=c;
		_genes[4]=d;
		_genes[5]=ty;
		_genes[6]=e;
		_genes[7]=f;
	}
	
	@Override
	public ITransformation clone()
	{
		IBaseTransformation[] genes = new IBaseTransformation[_genes.length];
		for (int i = 0; i < genes.length; i++) {
			genes[i] = _genes[i].clone();
		}
		return new ProjectiveTransformation(genes);
	}
	
	@Override
	public double[] GetValue(double x, double y)
	{
		double[] point = new double[] {x,y};
		
		double dev = _genes[6].GetVal()*point[0] + _genes[7].GetVal()*point[1] + 1;
		double x2 = (_genes[0].GetVal()*point[0] + _genes[1].GetVal()*point[1] + _genes[2].GetVal()) / dev;
		double y2 = (_genes[3].GetVal()*point[0] + _genes[4].GetVal()*point[1] + _genes[5].GetVal()) / dev;
		
		point[0] = x2;
		point[1] = y2;
		
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

	@Override
	public double[] toArray() {
		return new double[0];
	}

}

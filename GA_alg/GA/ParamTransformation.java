package GA;

import GA.Structure.Interface.IBaseTransformation;

public class ParamTransformation implements ITransformation {
	
	private IBaseTransformation[] _genes = null;
	
	public ParamTransformation(IBaseTransformation[] genes)
	{
		_genes = genes;
	}
	
	public ParamTransformation()
	{
		_genes =  new IBaseTransformation[6];
		Init();
	}
	
	@Override
	public void Init()
	{
		IBaseTransformation p0 = new Param(0);
		IBaseTransformation p1 = new Param(1);
		IBaseTransformation p2 = new ParamT(2);
		IBaseTransformation p3 = new Param(3);
		IBaseTransformation p4 = new Param(4);
		IBaseTransformation p5 = new ParamT(5);
		
		_genes[0]=p0;
		_genes[1]=p1;
		_genes[2]=p2;
		_genes[3]=p3;
		_genes[4]=p4;
		_genes[5]=p5;
	}
	
	@Override
	public ITransformation clone()
	{
		IBaseTransformation[] genes = new IBaseTransformation[_genes.length];
		for (int i = 0; i < genes.length; i++) {
			genes[i] = _genes[i].clone();
		}
		return new ParamTransformation(genes);
	}

	@Override
	public double[] GetValue(double x, double y) {
		double x_ = _genes[0].GetVal() * x 
					+ _genes[1].GetVal() * y 
					+ _genes[2].GetVal();
		
		double y_ = _genes[3].GetVal() * x 
					+ _genes[4].GetVal() * y 
					+ _genes[5].GetVal();
		
		return new double[]{x_,y_};
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

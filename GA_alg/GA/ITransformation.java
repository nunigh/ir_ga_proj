package GA;

import GA.Structure.Interface.IBaseTransformation;


public interface ITransformation {
	
	void Init();

	 ITransformation clone();
	 
	 double[] GetValue(double x, double y);
	 String getDescription();
	 
	 IBaseTransformation Get(int i);
	 int GetLength();
	 void Set(int i, IBaseTransformation t);
}

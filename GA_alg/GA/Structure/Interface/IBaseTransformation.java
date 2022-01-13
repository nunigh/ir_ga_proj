package GA.Structure.Interface;

import com.googlecode.javacv.cpp.opencv_core.CvMat;

@SuppressWarnings("unused")
public interface IBaseTransformation {
	
	String GetTitle();
	String getDescription();
	
	double[] TransPoint(double[] point);
	float GetVal();
	void SetVal(float value);//IBaseTransformation t);
	void Mutate();
	
	IBaseTransformation clone();

	

}

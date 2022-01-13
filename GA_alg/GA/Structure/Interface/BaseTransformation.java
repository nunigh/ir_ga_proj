package GA.Structure.Interface;

import java.awt.List;
import java.math.BigDecimal;
import java.util.SortedSet;

import org.opencv.core.CvType;

import utils.Utils;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import static com.googlecode.javacv.cpp.opencv_core.*;

@SuppressWarnings("unused")
public abstract class BaseTransformation implements IBaseTransformation {

//	protected CvMat _mat;

	public String getDescription()
	{
		return String.format("%s: %s", GetTitle(),  String.valueOf(GetVal()));
	}
	
	public BaseTransformation()
	{
		
	}
	
	/*public CvMat GetMatrix()
	{
		return _mat;
	}
	
	public CvMat TransPoint(CvMat point)
	{
		CvMat out = cvCreateMat(3,1, CvType.CV_64F);
		CvMat mat = GetMatrix();
		cvMatMul(mat, point, out);
		return out;
	}*/
	
	public abstract String GetTitle();
	public abstract double[] TransPoint(double[] point);
	public abstract IBaseTransformation clone();
	public abstract float GetVal();
	public abstract void SetVal(float value);//IBaseTransformation t);
	public abstract void Mutate();
	
	
}

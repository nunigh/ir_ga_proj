package GA.Structure.Concrete;

import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;
import static com.googlecode.javacv.cpp.opencv_core.cvMatMul;
import static com.googlecode.javacv.cpp.opencv_core.cvAdd;
import static com.googlecode.javacv.cpp.opencv_core.cvMul;
import org.opencv.core.CvType;

import utils.FastCos;
import utils.FastSin;
import utils.GAUtils;
import utils.Utils;

import GA.Structure.Interface.BaseContinousTransformation;
import GA.Structure.Interface.IBaseTransformation;

import com.googlecode.javacv.cpp.opencv_core.CvMat;

@SuppressWarnings("unused")
public class Rotation extends BaseContinousTransformation {
	
	//private CvMat _ones =  cvCreateMat(3,1, CvType.CV_64F); 

	@Override
	public String GetTitle() {
		return "rot";
	}
	
	public Rotation()
	{
		super();
	
	}
	
	public Rotation(float val)
	{
		super(val);
		
		//_ones.put(0,0,-1);
		//_ones.put(1,0,-1);
		//_ones.put(2,0,-1);
		
	}
	
	/*public CvMat TransPoint(CvMat point)
	{
		CvMat out = cvCreateMat(3,1, CvType.CV_64F);
		
		CvMat diff = cvCreateMat(3,1, CvType.CV_64F);
		diff.put(0,0,Utils.width/2.0);
		diff.put(0,1,Utils.height/2.0);
		CvMat diffMinus = cvCreateMat(3,1, CvType.CV_64F);
		
		cvMul(diff, _ones, diffMinus, 1);
		
		CvMat center = cvCreateMat(3,1, CvType.CV_64F);
		cvAdd(point,diffMinus,center,null);
		
		cvMatMul(GetMatrix(), center, out);
		
		cvAdd(out,diff,out,null);

		return out;
	}*/
	
	@Override
	public IBaseTransformation clone() {
		return new Rotation(_val);
	}
	
	@Override
	public float MinVal() {
		return GAUtils.MIN_ROTATION;
	}

	@Override
	public float MaxVal() {
		return GAUtils.MAX_ROTATION;
	}
	
	@Override
	public float ResetVal() {
		return GAUtils.RESET_ROTATION;
	}

	@Override
	public double[] TransPoint(double[] point) {
		
		double angleRad = (_val/360.0) * 2 * Math.PI;
		double x_ = (point[0]);// - Utils.width/2.0);
		double y_ = (point[1]);// - Utils.height/2.0);
		double[] vals = new double[] { x_ * Math.cos(angleRad) + y_ * Math.sin(angleRad), 
										x_ * (-1)*Math.sin(angleRad) + y_ * Math.cos(angleRad)};
		
		//vals[0] += Utils.width/2.0;
		//vals[1] += Utils.height/2.0;
		return vals;
		
	}
	
	
}

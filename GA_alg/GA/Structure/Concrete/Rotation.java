package GA.Structure.Concrete;

import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;
import static com.googlecode.javacv.cpp.opencv_core.cvMatMul;
import static com.googlecode.javacv.cpp.opencv_core.cvAdd;
import static com.googlecode.javacv.cpp.opencv_core.cvMul;

import algorithms.CONST;
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

	final static double angleRadMult =  2 * Math.PI/360;
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


	// hadar: to compare the values of this function before and after time optimization, to make sure the value is the same
	// otherwise it might not impact the RMSE but it will impact fminsearch optimization (incossitancy with rotation calculation)
	@Override
	public double[] TransPoint(double[] point) {
		if (CONST.PROFILING_MODE)
		{
			return TransPoint_hadar(point);
		}
		else
			return TransPoint_orig(point);
	}

	public double[] TransPoint_orig(double[] point) {

		double angleRad = (_val/360.0) * 2 * Math.PI;
		double x_ = (point[0]);// - Utils.width/2.0);
		double y_ = (point[1]);// - Utils.height/2.0);
		double[] vals = new double[] { x_ * Math.cos(angleRad) + y_ * Math.sin(angleRad),
				x_ * (-1)*Math.sin(angleRad) + y_ * Math.cos(angleRad)};

		//vals[0] += Utils.width/2.0;
		//vals[1] += Utils.height/2.0;
		return vals;

	}

	public double[] TransPoint_hadar(double[] point) {
		// hadar: this can actually be also omptimzed by moving it to constructor
		// or maybe "final" is enough?
		final double angleRad = _val*angleRadMult;
		final double cosAngleRad = Math.cos(angleRad);
		final double sinAngleRad = Math.sin(angleRad);

		double x_ = (point[0]);// - Utils.width/2.0);
		double y_ = (point[1]);// - Utils.height/2.0);

		double[] vals = new double[] { x_ *cosAngleRad  + y_ * sinAngleRad,
				x_ * (-1)*sinAngleRad + y_ *cosAngleRad};

		//vals[0] += Utils.width/2.0;
		//vals[1] += Utils.height/2.0;
		return vals;

	}


}

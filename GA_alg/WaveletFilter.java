import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;




class Feature
{
	double intensity;
	int x;
	int y;
}

class FeatureComparator implements Comparator<Feature>
{

	@Override
	public int compare(Feature arg0, Feature arg1) {
		return Double.compare(arg1.intensity, arg0.intensity);
	}
}

public class WaveletFilter {
	
	public static short[][] getFeatures(BufferedImage img, double percent)
	{
		
		List<Feature> features = new ArrayList<Feature>();
		short[][] featuresImg = new short[img.getWidth()][];
		for (int i = 0; i < featuresImg.length; i++) {
			featuresImg[i] = new short[img.getHeight()];
			for (int j = 0; j < featuresImg[0].length; j++) {
				
				double val = img.getData().getSampleDouble(i, j, 0);
				//if (val > 150)
				{
					Feature f = new Feature();
					f.x = i;
					f.y = j;
					f.intensity = val;
					features.add(f);
				}
			}
		}
		
		Collections.sort(features, new FeatureComparator());
		int num = (int) (features.size() * percent);
		num = Math.min(num,num);
		
		for (int i = 0; i < num; i++) {
			Feature f = features.get(i);
			if (f.intensity > 170)
			{
			featuresImg[f.x][f.y] = 255;
			}
		}
		
		return featuresImg;
	}

}

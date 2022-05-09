

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;

import utils.*;

import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

import GA.GAChromosome;
import GA.GAStrategy;
import GP.GPStrategy;
import Interfaces.Chromosome;
import Interfaces.IGeneticStrategy;
import algorithms.CONST;
import algorithms.GeneticAlgorithm;
import algorithms.PopulationManager;
import algorithms.Selection.SelectionFactory.SelectionMode;
import base.FeaturePoint;
import base.IFeaturePoint;

import com.googlecode.javacv.FrameGrabber.Array;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.IplConvKernel;


import static com.googlecode.javacv.cpp.opencv_highgui.*;

enum Mode {
	GP,
	GPTest,
	RMSE,
	Convert,
	GA
}

public class Main {

	static Mode mode = Mode.GA;
	static InternalMode internalMode = InternalMode.SIFT;
	
	static FileOption fileOption = FileOption.a;
	
	static boolean _simpleSIFT = true;

	public static boolean useReal = true;
	static int notUsingRealStrategy = 2;
	static ArrayList<ArrayList<Point>> realPoints = null;
	
	static boolean pyramid = false;


	public static String projectPath = CONST.projectPath;
	public static void main( String[] args) throws Exception {

		FminsearchOptimizer.init();
		//FminsearchOptimizer.HD_test_random();



		String path1 = "";
		String path2 = "";
		
		System.out.println("Mode: " + mode + " / " + internalMode);
		System.out.println("File Option: " + fileOption);
		
		
		
		try
		{
			if (args.length > 0)
			{
				String fileOptionStr = args[0];
				fileOption = FileOption.valueOf(fileOptionStr);
			}
			if (args.length > 1)
			{
				useReal = Boolean.valueOf(args[1]);
				System.out.println("use real: " + useReal);
				CONST.useReal = useReal;
			}
			if (args.length > 2)
			{
				projectPath = args[2];
				CONST.projectPath= projectPath;
				System.out.println("project path: " + projectPath);
			}
		/*if (args.length > 0)
		{
			path1 = args[0];
			path2 = args[1];
			
			System.out.println("path1: " + path1);
			System.out.println("path2: " + path2);
		
			if (args.length > 2)
			{
				int r = Integer.parseInt(args[2]);
				useReal = (r != 0);
				
				System.out.println("useReal: " + useReal);
				
				if (args.length > 3)
				{
					String realPath = args[3];
					realPoints = ReadRealPoints(realPath);
				
					if (args.length > 4)
					{
						int m = Integer.parseInt(args[4]);
						mode = Mode.values()[m];
						
						int gm = Integer.parseInt(args[5]);
						internalMode = InternalMode.values()[gm];
						
					}
				}
			}
		}
		else*/
		{
		
			switch (fileOption) {
			case b:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\b.jpg";
				break;
			case trans:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a_synthetic_trans.jpg";
				break;
			case rot:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a_synthetic_rot.jpg";
				break;
			case scale:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a_synthetic_scale.jpg";
				break;
			case shearX:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a_synthetic_shearX.jpg";
				break;
			case reflection:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a_synthetic_reflection.jpg";
				break;
			case combined:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\b\\0002_0006\\a_synthetic_combined4.jpg";
				break;
			case example8:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\Example8\\a.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\Example8\\b.jpg";
				break;
			case e:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\e\\0005_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\e\\0005_0006\\b.jpg";
				break;
			case f:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\f\\0006_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\f\\0006_0006\\b.jpg";
				break;
			case g:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\g\\0007_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\g\\0007_0006\\b.jpg";
				break;
			case h:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\h\\0008_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\h\\0008_0006\\b.jpg";
				break;
			case c:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\c\\0003_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\c\\0003_0006\\b.jpg";
				break;
			case d:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\d\\0004_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\d\\0004_0006\\b.jpg";
			break;
			case file_95:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Chap9\\5.9\\Fig9.5a.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Chap9\\5.9\\Fig9.5b.jpg";
				break;
			case file_92:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Chap9\\Fig9.26l.png";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Chap9\\Fig9.26k.png";
				break;
			case star:
				path1 = "C:\\Users\\Sarit\\Documents\\BIU\\Research\\07_2016\\star\\star1.jpg";
				path2 = "C:\\Users\\Sarit\\Documents\\BIU\\Research\\07_2016\\star\\star2.jpg";
				break;
			case light:
				path1 = "C:\\Users\\Sarit\\Documents\\BIU\\Research\\07_2016\\light\\light1.jpg";
				path2 = "C:\\Users\\Sarit\\Documents\\BIU\\Research\\07_2016\\light\\light2.jpg";
				break;
			case Colorado_7:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\NASA\\Colorado\\Landsat 7\\Synthetic\\a.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\NASA\\Colorado\\Landsat 7\\Synthetic\\r15.jpg";
				break;
			case a:
                        path1 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\a\\0001_0006\\a.jpg";
                        path2 = projectPath + "CHIPS_DC_AREA\\CHIPS_DC_AREA\\a\\0001_0006\\b.jpg";
				break;
			case Misc_a:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\a.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\a2.jpg";
				break;
			case Misc_b:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\b.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\b2.jpg";
				break;
			case Misc_c:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\c.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\c2.jpg";
				break;
			case Misc_d:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\d.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\d2.jpg";
				break;
			case Misc_e:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\e.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\e2.jpg";
				break;
			case Misc_f:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\f.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\f2.jpg";
				break;
			case Misc_g:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\g.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\g2.jpg";
				break;
			case Misc_h:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\h.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\h2.jpg";
				break;
			case Misc_i:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\i.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\i2.jpg";
				break;
			case Misc_j:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\j.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\j2.jpg";
				break;
			case Misc_k:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\k.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\k2.jpg";
				break;
			case Misc_l:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\l.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\l2.jpg";
				break;
			case Misc_m:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\m.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\m2.jpg";
				break;
			case Misc_n:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\n.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\n2.jpg";
				break;
			case Misc_o:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\o.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\o2.jpg";
				break;
			case boat1:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\Mikolajczyk database\\boat\\a.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\Mikolajczyk database\\boat\\b.jpg";
				break;
			case boat2:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\Mikolajczyk database\\boat\\a.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\Mikolajczyk database\\boat\\b2.jpg";
				break;
			case bark:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\Mikolajczyk database\\bark\\a.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\Mikolajczyk database\\bark\\b.jpg";
				break;
			case eastSouth:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\Mikolajczyk database\\East South\\a.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\Mikolajczyk database\\East South\\b.jpg";
				break;
			case graffiti:
				path1 = "C:\\Users\\Sarit\\workspace\\Results\\Mikolajczyk database\\graffiti\\a.jpg";
				path2 = "C:\\Users\\Sarit\\workspace\\Results\\Mikolajczyk database\\graffiti\\b.jpg";
				break;
			case test:
				path1 = "C:\\Users\\Sarit\\Documents\\BIU\\Research\\Papers\\GECCO 2017\\Images\\b2.jpg";
				path2 = "C:\\Users\\Sarit\\Documents\\BIU\\Research\\Papers\\GECCO 2017\\Images\\a2.jpg";
				break;
			default:
				break;
			}
			
		}
		
		InitImages(path1, path2);
		
		String outputPath = null;
		IGeneticStrategy strategy = null;
		if (mode == Mode.GP)
		{
			strategy = new GPStrategy(internalMode);
                outputPath = projectPath + "output_GP";
		}
		else if(mode == Mode.GA)
		{
			GAUtils.init();
			strategy = new GAStrategy(internalMode);
                outputPath = projectPath + "output";
		}
		
		if (mode == Mode.GP || mode == Mode.GA)
		{
			File output = CreateOutputPath(outputPath);
			
			System.out.println("Output path: " + output.getAbsolutePath());
			System.out.println("--------------------------------------------------------------------------------");

			PopulationManager pop = null;
			if (pyramid)
			{
				String temp1 = path1.replace(".jpg", "_p2.jpg");
				String temp2 = path2.replace(".jpg", "_p2.jpg");
				InitImages(temp1,temp2);
				if (internalMode == InternalMode.SIFT || internalMode == InternalMode.SIFT_MOO || internalMode == InternalMode.SIFT_MOO2)
				{
					pop = RunGeneticAlgorithmSIFT(strategy, temp1, temp2, output, null);
					//internalMode = InternalMode.SIFT;
				}
				else
					pop = RunGeneticAlgorithm(strategy, temp1, temp2, output, null);
				
				for (Chromosome ch : pop.get_pop().get_pop()) {
					ch.Enlarge(2);
				}
				InitImages(path1,path2);
				
			}
			
			if (internalMode == InternalMode.SIFT || internalMode == InternalMode.SIFT_MOO || internalMode == InternalMode.SIFT_MOO2)
				RunGeneticAlgorithmSIFT(strategy, path1, path2, output, pop);
			else
				RunGeneticAlgorithm(strategy, path1, path2, output, pop);
			
		}
		else if (mode == Mode.GPTest)
		{
			CONST.data = readFileTest();
			RunGeneticAlgorithmTest();
			
		}
		else if (mode == Mode.RMSE)
		{
			//RunRMSETest(strategy);
			
			Chromosome ch  = null;
			//if (mode == Mode.GA)
				ch = new GAChromosome().getTestChromosome();
			//else
			//	ch = new GPChromosome().getTestChromosome();
			
			ArrayList<ArrayList<Point>> arr = LoadRegistrationDataSynthetic();
			
			Utils.RunTest(ch, arr.get(0), arr.get(1));
			
			
			
		}
		else if (mode == Mode.Convert)
		{
			IplImage src = cvLoadImage("C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\CHIPS_DC_AREA\\b\\0002_0006\\b.jpg",0);
			BufferedImage im = convert(src, "");
			String outPath = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\CHIPS_DC_AREA\\b\\00x02_0006\\out.jpg";
			//cvSaveImage(outPath, im);
			Utils.writeImg(im, outPath);
		
		}
		System.exit(0);
		}
		catch (Exception ex)
		{
			throw  ex;
			//System.out.println(ex.getMessage());
			//System.console().readLine();
		}
	}
	
	private static void InitImages(String path1, String path2)
	{
		System.out.println("Referenced image: " + path1);
		System.out.println("Sensed image: " + path2);
		
		IplImage src1 = cvLoadImage(path1);
		IplImage src2 = cvLoadImage(path2);
		
		Utils.original1 = src1.getBufferedImage();//.getData();
		Utils.original2 = src2.getBufferedImage();//.getData();
		Utils.img1 = src1.getBufferedImage();//.getData();
		Utils.img2 = src2.getBufferedImage();//.getData();
	
		//writeImage(null, null);
		
		Utils.width = Utils.img2.getWidth();
		Utils.height = Utils.img2.getHeight();
		Utils.size = Math.max(Utils.width, Utils.height);
		Utils.size = Math.max(Utils.size, Utils.img1.getWidth());
		Utils.size = Math.max(Utils.size, Utils.img1.getHeight());

		Utils.centeredColVal = (int) Math
				.round((Utils.img1.getWidth() / 2.0 - Utils.img2.getWidth() / 2.0));
		Utils.centeredRowVal = (int) Math
				.round((Utils.img1.getHeight() / 2.0 - Utils.img2.getHeight() / 2.0));
		
	}
	
	private static File CreateOutputPath(String folder)
	{
		Integer max = 0;
		File base = new File(folder);
		if (! base.exists()){
			base.mkdirs();
		}
		for (File f : base.listFiles())
		{
			if (f.isDirectory())
			{
				try
				{
					int i = Integer.parseInt(f.getName());
					max = Math.max(max, i);
				}
				catch (NumberFormatException ex)
				{}
			}
		}
		
		max +=1;
		File outputDir = new File(base.getAbsolutePath() + File.separatorChar + max.toString());
		outputDir.mkdir();
		
		return outputDir;
	}
	
	private static ArrayList<ArrayList<Point>> ReadRealPoints(String path) throws IOException
	{
		
		BufferedReader input = null;
			
		ArrayList<ArrayList<Point>> data = new ArrayList<ArrayList<Point>>();
		try
		{
			FileReader reader = new FileReader(path);
		    input =  new BufferedReader(reader); 
			
			// represent a single line content
			String line = null;
			ArrayList<Point> original = new ArrayList<Point>();
			ArrayList<Point> target = new ArrayList<Point>();
			while ((line = input.readLine()) != null) {
				line = line.trim();
				String[] values = line.split("  ");
				original.add(new Point(Double.parseDouble(values[0]),Double.parseDouble(values[1])));
				target.add(new Point(Double.parseDouble(values[2]),Double.parseDouble(values[3])));
			}
			data.add(original);
			data.add(target);
		}
		finally
		{
		
		}
			
		return data;
	}
	
	private static PopulationManager RunGeneticAlgorithmSIFT(IGeneticStrategy strategy, String path1, String path2, File output, PopulationManager pop) throws Exception
	{
		

		/*if (internalMode == InternalMode.SIFT_MOO)
		{
			InitMatlab();
			//readImages(output, path1, path2);
		}*/
		readImages(output, path1, path2);
		
		SelectionMode selection = SelectionMode.NSGARankedBasedSelection;
		if (internalMode == InternalMode.SIFT)
			selection = SelectionMode.RankBasedSelection;
		else if  (internalMode == InternalMode.SIFT_MOO || internalMode == InternalMode.SIFT_MOO2)
			selection = SelectionMode.NSGARankedBasedSelection;// .NSGARankedBasedSelection;//.RankBasedSelection;
		
		
		//cvSaveImage(output.getAbsolutePath() + File.separatorChar + "img1.jpg", src1);
		//cvSaveImage(output.getAbsolutePath() + File.separatorChar + "img2.jpg", src2);
			
		
		/*
	
		// calculate expected
		String sx = "rotate0(0.42)-7.5";
		String sy = "rotate1(0.42)+51";
		
		sx = Utils.fixTransformation(sx, Utils.img1.getWidth() , Utils.img1.getHeight(), Utils.img2.getWidth(), Utils.img2.getHeight(), 0, false);
		sy = Utils.fixTransformation(sy, Utils.img1.getWidth() , Utils.img1.getHeight(), Utils.img2.getWidth(), Utils.img2.getHeight(), 0, false);
				ArrayList<Point> pe1 = new ArrayList<Point>();
		//GeneticAlgorithm.scriptX = GeneticAlgorithm.compilingEngine.compile(sx);
		//GeneticAlgorithm.scriptY = GeneticAlgorithm.compilingEngine.compile(sy);
		
		for (int j = 0; j < p1.size(); j++) {
			double x = GeneticAlgorithm.GetValue(sx, p2.get(j).y, p2.get(j).x);
			double y  = GeneticAlgorithm.GetValue(sy, p2.get(j).y, p2.get(j).x);
			pe1.add(new Point(x,y));
		}
		double d = GeneticAlgorithm.CalculateRMSE(pe1, p2, sx, sy, null, true);
		System.out.println(d);
		
		*/
		
		
		GeneticAlgorithm alg = null;
		try
		{
			alg = new GeneticAlgorithm(strategy, output, selection, internalMode, CONST._isCentered);
		
			ArrayList<ArrayList<Point>> arr = LoadRegistrationDataSynthetic();
			
			/* 
			 * 
			// affine - 1
			//String fx = "((((0.0072/((((3.8924*(x*0.4893))*(x-((-0.9153)+((-5.13)+0.2561))))-((0.0072/((-30.7206)*x))-(-0.4515)))/((-30.7206)*x)))+(y+0.2561))/((((0.8354-((-0.9153)+(y-((-0.0095)-((-27.2676)*y)))))*(x-((-0.9153)+(y+0.4046))))*(x-((-0.9153)+(y+0.2561))))/((0.0125*(((1.0097*(x-0.4893))*(x-((-0.9153)+(y+0.2561))))*((0.436/((-30.7206)*x))+(y+0.2561))))+(-0.4515))))+((((y+0.2561)/86.1701)/((-30.7206)/x))+((((0.8354-((-0.9153)+(y-((-0.0095)*((-27.2676)*y)))))*(x-((-0.9153)+(y+0.4046))))*(x-((-0.9153)+(y+0.2561))))*((0.0072/((-30.7206)*x))+(y+0.2561)))))";
			//String fy = "((((-0.9153)*(x-((-0.9153)+(y+0.2561))))+((((-0.1935)+(y-0.289))-((-0.0095)*((((1.0097*(x-0.4893))*(x-0.3408))*((0.0067/((-30.7206)*x))+(y+0.2561)))+(y+0.2561))))+((x-0.9389)*((x-((-0.9153)+(y+0.2561)))*(9.0E-4+((((-0.0518)+(y-0.289))-(y*0.8085))+((x-0.3408)*9.0E-4)))))))*0.9389)";
			
			//String fx = "(((((0.9358/(x+0.0129))+((((x*((x*0.2964)*1.8114))-(1.5283/x))+(0.342+(0.9358/(y+0.1583))))/(y+0.0682)))*((0.0746+(0.342+(0.9358/(y+0.1583))))/(y-0.2098)))*((((x*((x*0.2964)*(1.4314/(y-0.2098))))-(1.5283/(y-0.2098)))+(0.342+(0.9358/(y-0.1583))))/(y-(((((0.9358/(x-0.0011))+(0.1583/(y+0.0682)))*(1.2245/(y-0.2098)))*((((x*0.5368)-1.934)+1.4537)/(y-0.2098)))*((((x*0.5369)-(1.5283/y))+1.5283)/(y-0.2098))))))*((((x*((x*0.2964)*(1.4314/(y-0.2098))))-(1.5283/y))+(0.0746+(0.342+(0.9358/(y-0.1583)))))/(y-0.2274)))";
			//String fy = "(((((y-(((y*0.5369)*(-0.0984))/(1.5283/(y-0.2098))))-0.342)+(-0.4388))+(((y*(((0.7884*(1.2244/(y-0.2098)))*(0.0566/(y-0.2098)))*((((x*0.5368)-(1.5283/y))+1.5283)/(y-0.2098))))*((((x-0.5369)-(1.5283/(y-0.2098)))+(0.342+(0.9358/(y-0.1583))))/(y-0.2398)))*(((y*((x*0.2964)*(1.4314/(y-0.2274))))*((((x-0.5369)-(1.5283/(y-0.2098)))+(0.342+(0.9358/(y-0.1583))))/(y-0.2398)))*((0.0746+(0.342+(0.9358/(y-0.1583))))/(y-0.2274)))))-(((y*((x*0.2964)*(1.4314/(y-0.2098))))*((((x-0.5369)-(1.5283/(y-0.2098)))+(0.342+(0.9358/(y-0.1583))))/(y-0.2398)))*((0.0746+(0.342+(0.9358/(y-0.1583))))/(y-0.2274))))";
		
			// affine - 2
			//String fx = "((((x-(0.9716/((-0.228)/(y*(-0.2209)))))*((((x/((-0.1363)/((-0.228)/(y*(-0.0146)))))/(0.9999/((-0.228)/(y/(-0.2209)))))-(-0.4538))*(-0.4835)))+(((((x-(0.9716/((-0.228)/(y*(-0.2209)))))/((((x/(-0.008727982456140352))/19.852993733768553)-(-0.4538))*(-0.4835)))+(-16.072668282563722))-((((x/(-0.1705))/(0.9999-((-0.228)/(y*(-0.2209)))))-0.7690235079917632)/(-0.228)))*(((0.0128/(0.5494/((-0.228)/(y*0.2382))))*((0.3598/(1.1392/((-0.228)/(y*0.2382))))*(1.1392*(0.5159/((0.833*x)/(y*(-0.2209)))))))*(0.9716/((-0.228)/(y*(-0.2209)))))))+(((x-(0.9716/((-0.228)/(y*(-0.2209)))))*(((((((x-0.9413440350877194)*2.5709243812826768)+((x-(-794.4913804404985))*(-9.89084214316336E-4)))+(((x-0.9413440350877194)*(-0.19280408643639627))+(-0.9503896571074971)))/(0.9999/((-0.228)/(y/(-0.2209)))))-(-0.4538))*(-0.4538)))+((-108.5321)*(((0.0128/(0.5494/((-0.228)/(y*0.2382))))*((0.3598/(1.1392/((-0.228)/(y*0.2382))))*(1.1392*(0.5159/((0.833*x)/(y*(-0.2209)))))))*1.1392))))";
			//String fy = "(((((-14.4451)*(0.21174852063841712/((-0.228)/(y*0.2382))))*(1.1392*((0.9177*(0.5803/((-0.228)/(y*(-0.2209)))))/((0.833*x)/(y*(-0.2209))))))+(((-0.7124)/(0.5494/((-0.228)-(y*0.2382))))+((((-0.4980956048868832)+((((x-0.9413440350877194)*2.5709243812826768)+(-0.7857103743534148))+(((x-0.9413440350877194)*(-0.16941403896024831))+0.12991014820170968)))/(((-37.085914240716704)/((-0.6923)*(-0.2209)))/((-0.228)/(y*0.2382))))*((((-14.4451)*(0.21174852063841712/((-0.228)/(y*0.2382))))*(1.1392*(0.51595778675/((0.833*x)/(y*(-0.2209))))))+(((-0.7124)/(0.5494/((-0.228)-(y*0.2382))))+(-1.1331121260213331E-4))))))/(((-0.228)/(y*(-0.2209)))/((-0.228)/(y*0.2382))))";	
			
			String fx = "((((-0.7974)-((((Math.cos(83.12)-Math.cos(21.68))-(Math.sin(-0.68)-((-0.12589212654817908)-(-48.332674960434076))))-((Math.sin(1.24)+rotate0(0.0))-(rotate0(-0.68)-((-0.8604863484260705)-0.27770023462168325))))-(((-0.2703)/(-0.8342))-(Math.cos(-22.32)+(((((-0.7974)*x)-(((-0.7974)-(-242.58186174781844))-((-1288.9813935918362)/250.4753848569094)))/((((-22.548808066758014)-8.61180397292426)-((-265.2035977109454)/32.37216276857555))+((299.42159357475975/37.63429166041175)-(130.15553954327777/183.2378904008882))))-(((-0.7974)*x)-(((-0.7974)-((-254.26623839955053)-35.44457585102805))-((459.93079941578554*(-2.8127174103070804))/rotate0(-267.0)))))))))/(((Math.sin(-0.83)+(((0.15188040976783074-(-289.56087271218723))-((-1.0811095894925795)+(-15.455296951442849)))-(((-0.6789063599999999)-(-0.46586117606607186))-rotate1(-0.92))))*(rotate0(1.96)/((-14.7824)-((149.3796098730931+(-18.65109875709788))-(0.02755891564678294-(-40.83042832489778))))))-rotate0(-267.0)))+(((((-0.7974)*x)-(((-0.7974)-((((Math.cos(83.12)-Math.cos(-16.64))-(Math.sin(-0.68)-(0.8666193866919993-(-48.332674960434076))))-((rotate0(1.24)+rotate0(0.0))-(rotate0(-0.68)-((-0.8604863484260705)-0.27770023462168325))))-(x-rotate1(0.34))))-(((Math.sin(-0.83)+(((0.15188040976783074-(-289.56087271218723))-((-1.0811095894925795)+(-15.455296951442849)))-(((-0.6789063599999999)-(-0.46586117606607186))-rotate1(-0.92))))*(rotate0(1.96)/((-14.7824)-((149.3796098730931+(-18.65109875709788))-(0.02755891564678294-(-40.83042832489778))))))/rotate0(-267.0))))/(((((((((-42.58114704070704)+0.6989127991718947)-((-50.64587345760083)-(-18.43400505360631)))/((207.1463814664554+0.8672962886739981)-((-50.64587345760083)+1.550461391227349)))+((((-2.2138246656144007)-(-257.9141103871793))+((-276.1000501483474)+(-0.9630364144984623)))/(y-0.0094)))+Math.sin(-265.79))-(rotate0(1.96)/(((Math.sin(-90.57)/(288.47101131333415-(-47.48707640974049)))-(((-0.7974)-289.38864330381625)-((-0.9995919286480465)-(-1.0947991890130724))))-((((-0.7974)*0.8513999999999999)-((-0.4688916765967037)-(-0.0030305005306318486)))-((6.456814412692488-260.4091055422501)-(1.3607912937727833-(-0.9988113053621311)))))))-(((1.0968311066954495-(rotate0(1.96)/(((-0.0015147222321960797)-(-290.2812505641813))-((-0.21304518393392807)-(-256.3118937286925)))))-(208.18093797016354-(-49.50768687455307)))/(((Math.sin(51374.31)-((3.4223864451661035-257.6886248447166)-(0.8500584616086222-(-34.44457585102805))))-1.2417418086209377)-((((-0.7974)*(0.2995+0.5519))-(Math.sin(456.01)-((-0.5304266895761741)/175.0294)))-((((-41.882234241535144)-(-48.33904865422763))-(207.62651449806765-(-52.78259104418244)))-((0.4172-(-0.9435912937727833))-Math.sin(-45.6)))))))+((rotate0(2.05)/(((Math.sin(51374.31)-((3.4223864451661035-257.6886248447166)-(0.8500584616086222-(-34.44457585102805))))-(((0.7254621245180196-1.9072439888822756)-((-0.015993252796693617)-0.08467902207498301))+(rotate0(-324.36)/((-14.7824)-89.87052387545066))))-((((-0.7974)*(0.2995+0.5519))-(Math.sin(456.01)-((-0.5304266895761741)/175.0294)))-((((-41.882234241535144)-(-48.33904865422763))-(207.62651449806765-(-52.78259104418244)))-((0.4172-(-0.9435912937727833))-Math.sin(-45.6))))))-(rotate1(-0.79)/((((((-79.52875666999559)+(-2.1481845945742197))-((-2.4350883169619775)-(-40.83042832489778)))-(((-43.60597639564595)/148.9160927515922)-((-40.83555166630622)-7.765758817714859)))-((((-441.1569283183604)-288.91341425057857)-(314.6684498072207-10.132931205883908))/((328.70114634333316-(-0.07419075872555642))-(33.76179689489925-(-255.56931674404396)))))-(((-0.7974)*((328.70114634333316-(-0.07419075872555642))-(33.76179689489925-(-255.56931674404396))))-(((-0.7974)-((-254.26623839955053)-35.44457585102805))-((447.28821987464977*(-2.8127174103070804))/rotate0(-267.0)))))))))-(((-0.7974)*x)-(((-0.7974)-((((Math.cos(83.12)-Math.cos(-28.52))-(Math.sin(-0.68)-((1.1443196213136826-0.27770023462168325)-Math.sin(-1257.61))))-((rotate0(1.24)+rotate0(0.0))-(rotate0(-0.68)-((0.4172/(-0.48484209047953786))-Math.sin(207.62)))))-(x-rotate1(0.34))))-(((Math.sin(1.96)+(((0.15188040976783074-(-289.56087271218723))-((-1.0811095894925795)+((-22.968277174481823)+7.512980223038975)))-(((-0.6789063599999999)-(-0.46586117606607186))-rotate1(-0.92))))*(rotate0(1.96)/((-14.7824)-((149.3796098730931+(-18.65109875709788))-(0.02755891564678294-(-40.83042832489778))))))/rotate0(-267.0))))))";
			String fy = "((((((((((rotate0(36.59)+Math.sin(2.84))-(rotate0(-0.68)-((-1.3189543574737792)/0.030189725668376636)))-((rotate0(1.24)+rotate0(0.0))-(rotate0(-0.68)+((-0.02776473239316673)-(-0.014287381491412352)))))-(((((-2.1455164026500437)-(-0.030095939368169893))-(7.178479170725791*9.092969880584041))+(((-31.14559491219625)/9.092969880584041)-Math.cos(153.66)))/(51.27354791471725-0.005775655858312626)))+Math.sin(-1061.66))-(rotate0(1.96)/((((((-268.50435893211466)+(-3.039882399755944))-(1.9697347117122834-(-40.83042832489778)))-(((-348.1444461035528)/148.9160927515922)-((-40.83555166630622)-7.747876065007791)))-((((-360.5239199443832)-288.91341425057857)-(1.9697347117122834-(-40.838841683933666)))/((335.90191003075284-1.2417418086209377)-((-69.82524127350423)-(-255.56931674404396)))))-((((-0.7974)*(0.2995+0.5519))-((87.48819497980668-(-32.211868403994515))-0.0094))-((((-41.882234241535144)-(-48.33904865422763))-(207.62651449806765-(-52.78259104418244)))-((0.4172-(-0.9435912937727833))-Math.sin(-45.6)))))))+((rotate0(-60.18)/((((((-292.76472306033617)-0.08236853412591578)+((-1.7104513869731337)-1.9697347117122834))-(((-362.6238376670284)/148.9160927515922)-((-40.83555166630622)-(-0.005123341408445004))))-((((-0.7974)-42.80857639564595)/(334.6601682221319-185.7440754705397))-(((-40.25358192026631)+(-0.5819697460399091))-(300.1126839159226/38.64563540543142))))-(((((-391.9904411603034)-49.166487158057016)-((-0.7974)-(-289.71081425057855)))-((19.93480521271274-(-294.733644594508))-((-13.489563968930288)/(-1.331259799839288))))/((-0.7974)*x))))-(rotate0(-16.09)/(((0.15188040976783074-(-335.750029620985))-1.2417418086209377)-(((-70.29157966476716)-(-0.46633839126292553))-((-253.95229112955758)-1.6170256144863824))))))-(((((((((-295.5483990940239)-0.18832642747161726)-(294.3591010240216/(-491.8837361654524)))+((293.3251970253771/(-274.0978654652851))-(293.3251970253771/148.9160927515922)))-((rotate0(-16.09)/(334.6601682221319-185.7440754705397))-(((-40.25358192026631)+(-0.5819697460399091))-(1.2417418086209377/(-242.369522080673)))))-(((((-0.7974)*1.0)-(1.9697347117122834-(-40.838841683933666)))/((335.90191003075284-1.2417418086209377)-((-69.82524127350423)-(-255.56931674404396))))-((((-31.160612039682274)-9.092969880584041)+((-169.7057089180476)/291.605723618509))-(rotate0(2.06)/(293.73314020517034-255.08750479973892)))))-((((((-408.68662777802456)-(-16.696186617721132))-(0.6483438702262605-(-48.51814328783075)))-((-0.7974)-((-254.26623839955053)-35.44457585102805)))-((((-308.0983342316462)/(-15.455296951442849))-((-0.7974)-293.936244594508))-(((-739.6308625887623)-314.56444594350387)/(329.49854634333315-289.3311136389432))))/(((((-15.499254418335262)+8.45037114068342)-((-254.26623839955053)-81.48379122143447))-(-0.7974))-(((289.7127531219551-256.41729461831875)-((-0.4688916765967037)-(-0.002553285333778211)))-((6.456814412692488-260.4091055422501)-(0.9020420904795379-(-0.7149835240068445)))))))/(((Math.sin(51374.31)-((3.4223864451661035-257.6886248447166)-(47.03921537040642-(-34.44457585102805))))-1.2417418086209377)-((((-0.7974)*(15.6347/0.1773627771556398))-(Math.sin(456.01)-((-0.4469)/175.0294)))-((((-41.882234241535144)-(-48.33904865422763))-(207.62651449806765-(-52.78259104418244)))-((0.4172-(-0.48484209047953786))-Math.sin(-303.93))))))-((((((((-9.67036583754063)/257.1090898215029)+((-21.362800841280972)/0.9906))+Math.sin(-265.79))-(rotate0(1.96)/(((-0.0015147222321960797)-(-290.2812505641813))-((-0.21304518393392807)-(-256.3118937286925)))))-(rotate0(1.96)/(((0.15188040976783074-(-289.56087271218723))-1.2417418086209377)-(((-0.6789063599999999)-(-0.46586117606607186))-((-253.95229112955758)-2.3596025991349143)))))+((((((-192.14537823005588)-1.9072439888822756)-((-0.137314086724192)-0.08467902207498301))-(rotate0(1.96)/(288.47101131333415-256.0988485447586)))+((((-0.0014755322488405195)-(-290.2812505641813))-((-0.21304518393392807)-(-256.3118937286925)))+Math.cos(153.66)))/(((rotate0(6616.12)/((-40.25358192026631)+251.31411408464703))-(((-0.7974)-289.3758473865786)-(0.38522682419464266-(-1.0897496862870681))))-0.0094)))-(1.2417418086209377/(((Math.sin(51374.31)-((3.4223864451661035-(-17.053060004531854))-(0.8500584616086222-(-34.44457585102805))))-1.2417418086209377)-((((-0.7974)*(0.2995+0.5519))-(Math.sin(456.01)-((-0.5304266895761741)/175.0294)))-((((-41.882234241535144)-(-48.33904865422763))-(207.62651449806765-(-52.78259104418244)))-((0.4172-(-0.9435912937727833))-Math.sin(-45.6)))))))))-(((((-0.7974)*x)-((rotate0(-16.09)/(((Math.sin(51374.31)-((-254.26623839955053)-81.48379122143447))-1.2417418086209377)-((((-0.7974)*88.15096521791718)-((-0.4688916765967037)-(-0.002553285333778211)))-((6.456814412692488-260.4091055422501)-(0.9020420904795379-(-0.7149835240068445))))))-((((((-21.603128615599292)+(-0.9456794511587228))-(294.3591010240216/34.18088729719051))-(rotate0(1.96)/(288.47101131333415-256.0988485447586)))+((((-193.83062911013897)-9.092969880584041)+(34.18092648717385+(-0.9630364144984623)))/(((-0.03310027855126157)-(-291.6482238970603))-0.0094)))-(((-0.7974)*((335.90191003075284/1.2417418086209377)-((-69.82524127350423)-(-255.56931674404396))))/(((0.15188040976783074-(-14.819187862938715))-1.2417418086209377)-(((-0.6789063599999999)-(-0.46586117606607186))-((-253.95229112955758)-2.3596025991349143)))))))/(((Math.sin(51374.31)-((3.4223864451661035-257.6886248447166)-(47.03921537040642-(-34.44457585102805))))-1.2417418086209377)-((((-0.7974)*(15.6347/0.1773627771556398))-(Math.sin(456.01)-((-0.4469)/175.0294)))-((((-41.882234241535144)-(-48.33904865422763))-(207.62651449806765-(-52.78259104418244)))-((0.4172-(-0.48484209047953786))-Math.sin(-303.93))))))-(((((((((-41.882234241535144)-(-32.211868403994515))/(208.0136777551294-(-49.09541206637348)))+((255.70028572156488+(-277.06308656284585))/(1.0-0.0094)))+Math.sin(-265.79))-(rotate0(1.96)/((((-0.508883184560222)/335.95808772307464)-((-290.18604330381623)-0.09520726036502591))-(((-0.6789063599999999)-(-0.46586117606607186))-((-253.95229112955758)-2.3596025991349143)))))-(rotate0(1.96)/(((Math.sin(51374.31)-((-254.26623839955053)-35.294634312636674))-1.2417418086209377)-((((-0.7974)*0.8513999999999999)-((-0.4688916765967037)-(-0.0030305005306318486)))-((6.456814412692488-260.4091055422501)-(1.3607912937727833-(-0.9988113053621311)))))))+(((((((-191.1491171564546)-0.9962610736012871)-(294.3591010240216/154.33741185705782))-(((-4.445153965853843)/32.37216276857555)-((-0.9995919286480465)-(-1.0842709507230295))))-(rotate0(1.96)/((289.7127531219551-1.2417418086209377)-((-0.21304518393392807)-(-256.3118937286925)))))+(((((-0.508883184560222)/344.88109965749976)-((-290.18604330381623)-0.09520726036502591))-(((-0.6789063599999999)-(-0.46586117606607186))-((-253.95229112955758)-2.3596025991349143)))+Math.cos(153.66)))/(((rotate0(6616.12)/(((-31.160612039682274)-9.092969880584041)+(307.1041155554084/1.221993108799175)))-((((-0.7974)*1.0)-(290.4335965192654-1.0577491326868194))-(Math.sin(-4372.7)-(286.952676937043/(-263.3198068766888)))))-0.0094)))-(rotate0(2.06)/(((Math.sin(51374.31)-((3.4223864451661035-257.6886248447166)-(0.8500584616086222-(-34.44457585102805))))-((((0.7272363449548673*0.997560324852909)-(294.3591010240216/154.33741185705782))-(((-4.445153965853843)/277.9393299387362)-((-0.9995919286480465)-(-1.0842709507230295))))+(rotate0(-324.36)/((-14.7824)-(130.72851111599522-40.85798724054457)))))-((rotate0(-16.09)/((((-265.8060362066275)-(-3.207006945399628))-Math.sin(456.01))-(rotate0(1.96)/(288.47101131333415-256.0988485447586))))-((((-41.882234241535144)-(-48.33904865422763))-(207.62651449806765-(-52.78259104418244)))-((0.4172-Math.sin(-240.66))-Math.sin(-178.04)))))))))-(((((((((((-321.95642339245)-0.997560324852909)-(294.3591010240216/(-491.8837361654524)))+((293.3251970253771/(-274.0978654652851))-(293.3251970253771/148.9160927515922)))-((rotate0(-16.09)/(334.6601682221319-185.7440754705397))-(((-40.25358192026631)+(-0.5819697460399091))-(1.2417418086209377/(-242.369522080673)))))-(((((-46.67574508697748)+(-54.66088953409128))-(1.9697347117122834-(-40.838841683933666)))/((335.90191003075284-1.2417418086209377)-((-69.82524127350423)-(-255.56931674404396))))-((((-31.160612039682274)-9.092969880584041)+((-169.7057089180476)/291.605723618509))-(rotate0(-231.88)/(335.394187648295-185.7440754705397)))))-((((((-271.5442413318706)-42.800163036610066)-((-2.4039121554114993)-(-48.51814328783075)))-((-0.7974)-((-254.26623839955053)-35.44457585102805)))-(((45.570100000000004-(-120.3695697438012))/((-649.3720497514785)-(-361.0607679853481)))-(((-338.2539923795374)-46.56495213568724)-((-11868.62309993443)/32.69840994824341))))/(((((-15.499254418335262)+8.45037114068342)-((-254.26623839955053)-81.48379122143447))-(-0.7974))-(((289.7127531219551-256.41729461831875)-((-0.4688916765967037)-(-0.002553285333778211)))-((6.456814412692488-260.4091055422501)-(0.9020420904795379-(-0.7149835240068445)))))))-(((((((-267.42533968104453)-1.9072439888822756)+((-1.0701476880436604)-1.9697347117122834))-((293.3251970253771/148.9160927515922)-((-40.83555166630622)-(-0.005123341408445004))))-((rotate0(-16.09)/(334.6601682221319-185.7440754705397))-(((-40.25358192026631)+(-0.5819697460399091))-((-0.7974)/(-242.369522080673)))))/(((((-274.0526590419522)-42.800163036610066)-((-0.29282245853969147)-(-48.601310484021084)))-1.2417418086209377)-((((-0.7974)*88.15096521791718)-((-0.4688916765967037)-(-0.002553285333778211)))-((6.456814412692488-260.4091055422501)-(0.9020420904795379-(-0.7149835240068445))))))-((((((-21.603128615599292)+(-0.9456794511587228))-(294.3591010240216/34.18088729719051))-(rotate0(1.96)/(288.47101131333415-256.0988485447586)))+((((-193.83062911013897)-9.092969880584041)+(34.18092648717385+(-0.9630364144984623)))/(((-0.03310027855126157)-(-291.6482238970603))-0.0094)))-(rotate0(-4.27)/(((0.15188040976783074-(-289.56087271218723))-((-1.0811095894925795)+(-2.9392774937226775)))-((293.3251970253771/(-271.2231074652152))-((-253.95229112955758)-2.2167041072120273)))))))-((-0.7974)-((((Math.cos(83.12)-Math.cos(-28.52))-(Math.sin(-0.68)-((1.1443196213136826-0.27770023462168325)-Math.sin(-1257.61))))-((rotate0(1.24)+rotate0(0.0))-(rotate0(-0.68)-((0.4172/(-0.48484209047953786))-Math.sin(207.62)))))-(x-rotate1(0.34)))))-(((((((((-325.39543384662284)-42.800163036610066)-((-0.9679626180977241)-(-41.4589935128894)))-(((-649.3720497514785)-21.270902635771446)/(329.49854634333315-289.3311136389432)))-((((-315.1726291062928)-42.80857639564595)/((-366.4030519126646)-185.7440754705397))-(((-40.25358192026631)+(-0.5819697460399091))-(296.8986347742593/38.64563540543142))))-((-0.7974)-(((1.0968311066954495-(-2.3255553384706538))-(208.18093797016354-(-49.50768687455307)))-(x-rotate1(0.34)))))/((((((-0.03761191735482494)+(-21.56551669824447))+Math.sin(-265.79))-(rotate0(1.96)/(290.2797358419491-256.0988485447586)))-(((1.0968311066954495-8.61180397292426)-(208.18093797016354-(-49.50768687455307)))/((289.7127531219551-1.2417418086209377)-((-0.21304518393392807)-(-256.3118937286925)))))+((rotate0(2.05)/((289.7127531219551-(-4.020387083215257))-((-0.21304518393392807)-(-256.3118937286925))))-(rotate1(-0.79)/(((-307.43817897689206)/308.1272908281919)-((-0.7974)-293.936244594508))))))-(((-0.7974)*x)-(((-0.7974)-(((1.0968311066954495-(-2.3255553384706538))-(208.18093797016354-(-49.50768687455307)))-(x-rotate1(0.34))))-(((Math.sin(1.96)+(293.60658012175475-(-152.76016587743126)))*(rotate0(1.96)/((-14.7824)-89.87052387545066)))/rotate0(-267.0)))))-(((((((0.9817730508116447-0.6023)+((-21.29009418343149)-8.61180397292426))-((-0.7974)*(-33.8175)))-(((-0.7974)*x)-(((-0.7974)-(-289.71081425057855))-((-1293.6553670532335)/250.4753848569094))))+(((((-9.67036583754063)/257.1090898215029)+((-21.362800841280972)/0.9906))+Math.sin(396.15))-(rotate0(1.96)/(((-0.0015147222321960797)-(-290.2812505641813))-((-0.21304518393392807)-(-256.3118937286925))))))/((((((-0.03761191735482494)+(-21.56551669824447))+Math.sin(-265.79))-(rotate0(1.96)/(290.2797358419491-256.0988485447586)))-(((1.0968311066954495-8.61180397292426)-(208.18093797016354-(-49.50768687455307)))/((289.7127531219551-1.2417418086209377)-((-0.21304518393392807)-(-256.3118937286925)))))+((rotate0(2.05)/((289.7127531219551-(-4.020387083215257))-((-0.21304518393392807)-(-256.3118937286925))))-(rotate1(-0.79)/(((-307.43817897689206)/308.1272908281919)-((-0.7974)-293.936244594508))))))/(((((-0.7974)-((3.4223864451661035-257.6886248447166)-(1.0-(-34.44457585102805))))-(((-0.7974)-((-254.26623839955053)-35.44457585102805))-((459.93079941578554*(-2.8127174103070804))/rotate0(-267.0))))-(((((-0.7974)*1.0)-(1.9697347117122834-(-40.838841683933666)))/((335.90191003075284-1.2417418086209377)-((-69.82524127350423)-(-255.56931674404396))))-((((-31.160612039682274)-9.092969880584041)+((-169.7057089180476)/291.605723618509))-(rotate0(2.06)/(293.73314020517034-255.08750479973892)))))/((((((-23.691589283535713)-(-8.19233486520045))+(7.9560842084275825-(-0.49428693225583625)))-((3.4223864451661035-257.6886248447166)-(47.03921537040642-(-34.44457585102805))))-(-0.7974))-((((0.15188040976783074-(-289.56087271218723))-((-1.0811095894925795)+257.49840420781135))-(Math.sin(456.01)-((-0.4469)/175.0294)))-((((-41.882234241535144)-(-48.33904865422763))-(207.62651449806765-(-52.78259104418244)))-((0.4172-(-0.48484209047953786))-Math.sin(-303.93)))))))))/((((((((((-9.67036583754063)/257.1090898215029)+((-21.362800841280972)/0.9906))+Math.sin(-265.79))-(rotate0(1.96)/(((-0.0015147222321960797)-(-290.2812505641813))-(322.0295235871866-289.3311136389432))))-(((1.0968311066954495-(294.3591010240216/34.18088729719051))-(208.18093797016354-(-49.50768687455307)))/(((0.15188040976783074-(-289.56087271218723))-1.2417418086209377)-(((-0.6789063599999999)-(-0.46586117606607186))-((-253.95229112955758)-2.3596025991349143)))))+((rotate0(2.05)/(((0.15188040976783074-(-289.56087271218723))-((-1.0811095894925795)+(-2.9392774937226775)))-(((-0.6789063599999999)-(-0.46586117606607186))-((-253.95229112955758)-2.3596025991349143))))-(rotate1(-0.79)/((((-261.5848110449825)-1.900853395841669)-Math.sin(-250.17))-(rotate0(-16.09)/((-262.13013758463114)-9.092969880584041))))))-((3.4223864451661035-257.6886248447166)-(47.03921537040642-(-34.44457585102805))))-((((((-0.7974)*x)-((rotate0(-16.09)/(334.6601682221319-185.7440754705397))-(((-40.25358192026631)+(-0.5819697460399091))-((-0.7974)/(-242.369522080673)))))/(((Math.sin(51374.31)-((-254.26623839955053)-81.48379122143447))-1.2417418086209377)-((((-0.7974)*88.15096521791718)-((-0.4688916765967037)-(-0.002553285333778211)))-((6.456814412692488-260.4091055422501)-(0.9020420904795379-(-0.7149835240068445))))))-((((((-21.603128615599292)+(-0.9456794511587228))-(294.3591010240216/34.18088729719051))-(rotate0(1.96)/(288.47101131333415-256.0988485447586)))+((((-193.83062911013897)-9.092969880584041)+(34.18092648717385+(-0.9630364144984623)))/(((-0.03310027855126157)-(-291.6482238970603))-0.0094)))-(rotate0(2.06)/(((0.15188040976783074-(-289.56087271218723))-((-1.0811095894925795)+(-2.9392774937226775)))-((293.3251970253771/(-271.2231074652152))-((-253.95229112955758)-2.2167041072120273))))))/(((((((-296.9529621102829)-(-0.02494636910144105))+((-1.0701476880436604)-1.9697347117122834))-(((-362.6238376670284)/148.9160927515922)-((-40.83555166630622)-(-0.005123341408445004))))-((((-0.7974)-42.80857639564595)/(334.6601682221319-185.7440754705397))-(((-40.25358192026631)+(-0.5819697460399091))-(300.1126839159226/38.64563540543142))))-(((((-391.9904411603034)-49.166487158057016)-((-0.7974)-(-253.82612437730066)))-((19.93480521271274-(-294.733644594508))-((-13.489563968930288)/(-1.331259799839288))))/((((-7.048883277651843)-(-335.750029620985))-(-0.7974))-((33.295458503636326-(-0.46633839126292553))-((-253.95229112955758)-1.6170256144863824)))))-((((Math.sin(51374.31)-((-254.26623839955053)-35.294634312636674))-(((-1.1817818643642561)-(-0.10067227487167663))+(256.41691377078064-(-1.081490437030689))))-(Math.sin(456.01)-((-0.4469)/175.0294)))-((((-41.882234241535144)-(-48.33904865422763))-(207.62651449806765-(-52.78259104418244)))-((0.4172-(-0.48484209047953786))-Math.sin(-303.93)))))))-((((Math.sin(51374.31)-((3.4223864451661035-257.6886248447166)-(0.8500584616086222-(-34.44457585102805))))-(((0.7254621245180196-1.9072439888822756)-((-0.015993252796693617)-0.08467902207498301))+((257.332546643676-0.9156328728953376)-(293.3251970253771/(-271.2231074652152)))))-(Math.sin(456.01)-((-0.4469)/175.0294)))-((((-41.882234241535144)-(-48.33904865422763))-(207.62651449806765-(-52.78259104418244)))-((0.4172-(-0.48484209047953786))-(-0.7974)))))))";
			fx = Utils.fixTransformation(fx, Utils.img1.getWidth(), Utils.img1.getHeight(), Utils.img2.getWidth(), Utils.img2.getHeight(), 0, true);
			fy = Utils.fixTransformation(fy, Utils.img1.getWidth(), Utils.img1.getHeight(), Utils.img2.getWidth(), Utils.img2.getHeight(), 1, true);
	
			// non affine - 1
			fx = "((((((((((((-153.0848011966607)/0.045907740557903004)-((-1.2112649290844856E-4)/(-99.82423640565031)))-(((-35.75669546525761)/(-49.87511670860021))+((-1.0822479248934163)*(-39.371347123093884))))/((((-8.765577545748918)/(-1.5447709474761717))+(0.7138232359885281+251.85412991814778))+(((-0.04394472591197611)/(-120219.63599300412))+((-944.6274077581419)+(-0.0915251716175411)))))-(((Math.cos((40.442423825212295/48.15013077167777))*(y - 128.0) + Math.sin((40.442423825212295/48.15013077167777))*(x - 128.0) + 128.0)-((59.52969871177793+3.1409853141194723)/(0.7138232359885281*(-51.58813001490978))))/(((4.84114803959667-(-0.06950653264793862))+(4.357805742220427+44.12276438888152))-((0.10228087666698098+1.7902980961494528)+(150.257527405516/14.255243558682348)))))+(((((0.00942563541286137+0.7484)+(123.71636894036162/51.005575936414374))+((1.0+55.62789806139035)-((-5.6175)+4.891024220084199)))-(((2.4416620571125134+60.288452144630405)-((-3.932478081428668)+2.5561558767319523))-((46.728451894425255*14.830332285853967)/((-0.7953590094238586)*(-43.47068099569756)))))/(((((-2.4086750316113203)+56.84593611403809)-((-3.937031232985157)+9.714154656532749))-((45.454889077017235-13.132474950221008)/(83.37495827812546-(-4.19008104622869))))-(((65.14962877794201+92.23402582534105)/(10.293720575466127-(-3.961522983216221)))-((46.728451894425255-13.132474950221008)/((-0.7953590094238586)*(-51.555634544759755)))))))-(((Math.cos((((55.47567806031538-3.210430393709893)-(8.049120021105843+3.7737038202873507))/(Math.cos(1416.4911441678921)*(Math.cos((-5.48403271839571))*(y - 128.0) + Math.sin((-5.48403271839571))*(x - 128.0) + 128.0))))*(y - 128.0) + Math.sin((((55.47567806031538-3.210430393709893)-(8.049120021105843+3.7737038202873507))/(Math.cos(1416.4911441678921)*(Math.cos((-5.48403271839571))*(y - 128.0) + Math.sin((-5.48403271839571))*(x - 128.0) + 128.0))))*(x - 128.0) + 128.0)-((((3.5986758113430257-(-0.06950653264793862))+((-1.4928574735191804)+57.35437384130615))-(((-5.6175)-1.7820239929978803)+(150.257527405516/14.255243558682348)))/(Math.cos((0.40365849345491994*235303.2030163406))*(Math.cos(((-5.6175)+0.1334672816042897))*(y - 128.0) + Math.sin(((-5.6175)+0.1334672816042897))*(x - 128.0) + 128.0))))/((((((-3608.4511575572064)/(-865.0468055868658))-((-49.74483464027592)/74.27330115839719))-((60.53774544241091-63.18385343674335)/(48.29101301305941-10.221095071167717)))+(((0.00942563541286137+0.7484)+(212.76855861177515/59.10270398701082))+((56.6553107519237-3.230400297152051)-(162.82638104717734/17.504173756660574))))-(((((-5.6175)+1.7378812083794115)+(119.60965786583063/12.773183693803166))+((61.71217958352658-(-5.614597006444219))/(0.6797455011652649*55.32432583505983)))+(((1.0+57.023501580174944)+(0.9466713556126009*97.42982638959796))/((51.236269280572834/4.977429579999331)-((-5.6175)+1.6559770167837788)))))))-(((((((56.30317659522498-(-5.6175))/((-20.045786735554312)+55888.93783313029))+0.7484)+((((-2.7844934653309026E-4)+(-0.033880958519945444))+123.7505283482281)/((51.87381185910927-0.8193093916116244)-((-0.29741554626094346)/(-6.078819398718263)))))+((x+((4.3391885658732585+62.17589903069517)-(277.47756378957195/25.48661092865167)))-((-5.6175)+((65.14962877794201+261.9833703033827)/(60.321554761116495-(-6.562799999999999))))))-(((((3.318047320351348-(-0.18466992573431526))-(51.236269280572834/48.28803422577613))+((0.7578256354128613+2.1762526679113967)+(56.62789806139035-(-0.726475779915801))))-(((-5.6175)+(56.60307095046761/33.591890008445276))+((65.14962877794201+117.56964493586715)/(65.86455450890419-(-5.6175)))))-((((56.6553107519237-0.6247127916083139)-(162.82638104717734/17.504173756660574))-((162.82638104717734/17.504173756660574)+(67.04674233648359/12.128162776854166)))/(Math.cos((0.40365849345491994-235303.2030163406))*(Math.cos((44.62184388014394/41.03108398668787))*(y - 128.0) + Math.sin((44.62184388014394/41.03108398668787))*(x - 128.0) + 128.0)))))/((((((0.89052984770035+0.7484)-(44.12276438888152/10.900956418548683))+((0.7578256354128613+(-1.266263362680918))+(56.62789806139035-(-0.726475779915801))))-(((-5.6175)+(56.45012198419024/33.591890008445276))+((65.14962877794201+97.67675226923534)/(11.144265362431915-(-5.6175)))))-((((56.6553107519237-1.898275609016341)-(162.82638104717734/17.504173756660574))-((162.82638104717734/17.504173756660574)+(67.04674233648359/17.504173756660574)))/(((92.05566123740076+0.008644183887617896)+((-14.385477336859708)/1.6555302832133636))-(((-5.6175)+0.3140627359285435)+(50.59784725712646/45.446234050019186)))))-((((y+64.14962877794201)+(Math.cos(5.955132035818145)*(97.38906390476134/0.9995816221136059)))/(((60.53841534646296-9.302146065890126)/((-1.411273158979994)+6.388702738979325))-((-5.6175)+(62.275436077892444/37.60646159138316))))-((((56.6553107519237-0.6247127916083139)-(162.82638104717734/17.504173756660574))-((162.82638104717734/17.504173756660574)+(67.04674233648359/17.504173756660574)))/(Math.cos((0.40365849345491994-235303.2030163406))*(Math.cos((33.18888417890312/41.03108398668787))*(y - 128.0) + Math.sin((33.18888417890312/41.03108398668787))*(x - 128.0) + 128.0)))))))+((((((((43.742991249692096/(-0.4097662118552047))-((-0.002753481525590827)+3607.811065241697))-((60.397843393688795-0.48450330698636906)-((-84.76355647185761)-7.886144365363549)))/((((-8.765577545748918)-10.92543824823416)+(0.7138232359885281+251.85412991814778))+(((-0.04394472591197611)/(-120219.63599300412))+((-944.6274077581419)+(-0.0915251716175411)))))-(((Math.cos((40.442423825212295/48.15013077167777))*(y - 128.0) + Math.sin((40.442423825212295/48.15013077167777))*(x - 128.0) + 128.0)-((59.52969871177793-3.1409853141194723)/(0.7138232359885281*(-51.58813001490978))))/(((4.84114803959667-(-0.06950653264793862))+(4.357805742220427+44.12276438888152))-(((-5.6175)+1.7902980961494528)+(150.257527405516/14.255243558682348)))))-(((((0.00942563541286137+0.7484)+(123.71636894036162/51.005575936414374))+((1.0+55.62789806139035)-((-5.6175)+4.891024220084199)))-(((2.4416620571125134+60.288452144630405)-((-3.932478081428668)+2.5561558767319523))-((46.728451894425255-14.830332285853967)/((-0.7953590094238586)*(-43.47068099569756)))))/((((2.5429154324087326-(-0.06950653264793862))+(68.248568240807-0.3485166556867103))-(((-5.6175)+1.4502563729521263)+(119.60965786583063/12.773183693803166)))-Math.cos(((88.98389258269492+(-11.166303155934273))-((-25.48738939227662)/13.638724920051153))))))+((Math.cos((((3.7720158242040007+15.039272107105507)-((-3.231321720882747)+0.5573410869348565))-((80.63545049257147-12.33645536078725)/(0.4588699470066366*(-51.540141230237424)))))+((((79.00632153815008+43.5122329868828)+(102.25885383511195-11.76456349363777))+(((-5.6175)+3.0373940105611434)+(59.53212877794201/25.48661092865167)))/(((58.842290196229285-6.968478337120015)-(33.595976944204246/41.00523842173858))-(((-1.429467477746471)-12.956009859113237)/((-0.5484045833862882)+2.3358197347069347)))))+((x+(((3.8059027799348804+0.5332857859383783)+(73.06308856587326-10.887189535178086))-((65.14962877794201+212.3279350116299)/(Math.cos((-29.667176549750238))*(y - 128.0) + Math.sin((-29.667176549750238))*(x - 128.0) + 128.0))))-((-5.6175)+(((1.0+64.14962877794201)+(261.1744352917528+0.8089350116298879))/(((-1.8543442695786763)+62.17589903069517)-((-5.6175)+(-0.9453))))))))-(((-5.6175)+((((Math.cos((40.442423825212295/48.15013077167777))*(y - 128.0) + Math.sin((40.442423825212295/48.15013077167777))*(x - 128.0) + 128.0)-((59.52969871177793-3.1409853141194723)/(0.7138232359885281*(-51.58813001490978))))/(((4.84114803959667-(-0.06950653264793862))+(4.357805742220427+44.12276438888152))-(((-5.6175)+1.7902980961494528)+(150.257527405516/14.255243558682348))))/(Math.cos(Math.sin(((-100.4708)+(-8.5176))))*((0.6635364399040715+(0.9995816221136059*53.68366783252381))+((23.271560696280588+96.33809716955004)/(9.134470582382942-(-3.638713111420223)))))))+(((y+(((3.3414268000528824+61.71217958352658)-((-3.8354760070021197)+11.04040446277934))-((21.780971011871152-(-4.994517956601898))/((-0.6320479272717336)*(-51.338198873543945)))))+((((6.323895844928863-(-0.06950653264793862))+(68.248568240807-7.001206471936829))-(((-5.6175)+1.7902980961494528)+(154.36159874972498/14.255243558682348)))-(((30.8254283004373-(-0.9624922200702476))-((-84.76355647185761)/13.55625944142757))/(Math.cos(60.55604385291357)*(Math.cos(0.9576900601065381)*(y - 128.0) + Math.sin(0.9576900601065381)*(x - 128.0) + 128.0)))))/((((92.41081172580022+((-5.9084331766761045)+2.481514033570806))+((5.627361065916604-87.13293235830216)/((-0.7140110814934454)+8.013255079635288)))/(((-5.6175)+(107.20246697472847/25.48661092865167))+((65.14962877794201+97.67675226923534)/(Math.cos((-29.667176549750238))*(y - 128.0) + Math.sin((-29.667176549750238))*(x - 128.0) + 128.0))))-((-5.6175)+((((-1.1247823583041763)+58.09678117212516)-((-5.6175)+0.3140627359285435))/(Math.cos((-0.8233806922386563))*(54.32474421294623+0.9995816221136059)))))))))-(((-5.6175)+(((((((526.4782294162859+0.12157793354124195)/((-20.045786735554312)+55888.93783313029))+0.7484)+(((122.51855452503287+90.49429034147418)+((-2.5801059894388563)+2.3358197347069347))/((51.87381185910927-0.8193093916116244)-((-14.385477336859708)/1.7874151513206464))))+((x+((4.3391885658732585+62.17589903069517)-(277.47756378957195/25.48661092865167)))-((-5.6175)+((65.14962877794201+261.9833703033827)/(60.321554761116495-(-6.562799999999999))))))-((-5.6175)+((((5.967470677194419+67.37816079062942)-((-2.9329369341091813)+32.19157789993657))-(((-47.86986487726627)-20.54443178494056)/((-0.49357834689992564)*(-48.94596043715694))))/(Math.cos((0.6932268362448037-(-1415.7979173316473)))*(Math.cos(((-24.58581280808712)+0.1334672816042897))*(y - 128.0) + Math.sin(((-24.58581280808712)+0.1334672816042897))*(x - 128.0) + 128.0)))))/(Math.cos(Math.sin(((-100.4708)+(-8.5176))))*((0.6635364399040715+(0.9995816221136059*(Math.cos((-0.3588))*(y - 128.0) + Math.sin((-0.3588))*(x - 128.0) + 128.0)))+((((60.397843393688795-29.572415093251497)-((-18.988136060440155)/12.42085288684827))-((18.71164883030349+(-40.28880948276578))/(11.353818795603626-(-1.2702263741617394))))/(Math.cos((77.81758942676065-(-1.8687516275664444)))*(Math.cos((46.918864413517646/44.5614937797136))*(y - 128.0) + Math.sin((46.918864413517646/44.5614937797136))*(x - 128.0) + 128.0)))))))+(((y+(Math.cos(((((103.50299153996573+(-11.093829588546265))+((-5.0883916214109535)+2.483563170109039))+((2.29355894719372-13.236678816538904)/((-0.861345138626689)+1.9362388830782336)))-(((52.27338634603018-(-4.419426201821286))+((-49.29938940558413)/44.520419963798155))+(((-226.1273574692699)+0.7103003068847596)/(43.33143345211746-3.4412109063652823)))))*(x - 128.0) - Math.sin(((((103.50299153996573+(-11.093829588546265))+((-5.0883916214109535)+2.483563170109039))+((2.29355894719372-13.236678816538904)/((-0.861345138626689)+1.9362388830782336)))-(((52.27338634603018-(-4.419426201821286))+((-49.29938940558413)/44.520419963798155))+(((-226.1273574692699)+0.7103003068847596)/(43.33143345211746-3.4412109063652823)))))*(y - 128.0) + 128.0))+(Math.cos((((-5.6175)+((56.967602778155126-(-5.464595577936946))/(Math.cos((-37.9310396408366))*(y - 128.0) + Math.sin((-37.9310396408366))*(x - 128.0) + 128.0)))+((((-0.6219)+64.14962877794201)+(0.7864936866398915*124.19267176388432))/((53.24627603980904/4.986322940885947)-(-5.6175)))))*((((((-0.255915874187334)+59.990303408044625)-(3.569033789036964+11.483674231475879))-((42.33236880214241-3.2323673508620856)/((-0.7953590094238586)*(-51.540141230237424))))+(0.9995816221136059*(Math.cos((-0.3588))*(y - 128.0) + Math.sin((-0.3588))*(x - 128.0) + 128.0)))/0.9995816221136059)))/(((((((526.5998073498272/55868.892046394736)+0.7484)+((9.434130759793545E-6+123.7505283482281)/(51.05450246749764-0.048926531083265015)))+((x+(66.51508759656843-10.887189535178086))-(((-5.6175)+1.7639663910105485)+(119.60965786583063/13.443602894975427))))-(((y+64.14962877794201)+97.67675226923534)/17.504173756660574))/(((-5.6175)+(((1.0+64.14962877794201)+(0.7864936866398915*53.46875494506163))/(Math.cos(((-30.440012232415103)+0.7728356826648642))*(y - 128.0) + Math.sin(((-30.440012232415103)+0.7728356826648642))*(x - 128.0) + 128.0)))+(((y+64.14962877794201)+(Math.cos((-5.6175))*(124.1407122963661/0.9995816221136059)))/(Math.cos((((-31.21284791507997)+0.7728356826648642)+((-0.7032)/(-0.9098958753757992))))*(y - 128.0) + Math.sin((((-31.21284791507997)+0.7728356826648642)+((-0.7032)/(-0.9098958753757992))))*(x - 128.0) + 128.0))))-((-5.6175)+(((((6.380479186994388E-8+0.5672002707238165)+(123.75045048916992/(-73.13931224791786)))+((1.0+55.62789806139035)-((-5.6175)+4.148616889265181)))-((-5.6175)+((15.5272/0.8524203608666377)/(51.87851371691545-(-6.12080906535643)))))/(Math.cos(((6.102034331535526-(-1.0670340912446439))-((-3.043018381476891)/38.13298947036463)))*((0.6635364399040715+(0.9995816221136059*53.68366783252381))+0.9995816221136059))))))))-(((((((3.5986758113430257-(((3.183371601104759+57.35437384130615)-63.18385343674335)/(48.29101301305941-(11.04040446277934-0.8193093916116244))))+((x+((4.3391885658732585+62.17589903069517)-(277.47756378957195/25.48661092865167)))-((-5.6175)+((65.14962877794201+261.9833703033827)/(60.321554761116495-(-6.562799999999999))))))-(((((0.7484945432278824-(-0.5476460984180872))+((-0.5084377272680568)+57.35437384130615))-(((-5.6175)+1.6804687670148426)+(162.82638104717734/16.761765362431916)))-(((54.75703514290736-9.302146065890126)-(9.302146065890126+3.8303288843308816))/((59.0317265065284-61.97852461427566)-(32.322414126796225/41.03108398668787))))/(Math.cos((-5.6175))*(((69.06823136434396-(-1.411273158979994))+(0.9995816221136059*53.68366783252381))/0.9995816221136059))))-((Math.cos((((3.7720158242040007+15.039272107105507)-((-3.231321720882747)+9.959057841147347))-((80.63545049257147-12.33645536078725)/((-0.7748312494860664)*(-51.540141230237424)))))*(Math.cos((((55.47567806031538-3.210430393709893)-(8.049120021105843+3.7737038202873507))/(Math.cos(1416.4911441678921)*(Math.cos((-5.48403271839571))*(y - 128.0) + Math.sin((-5.48403271839571))*(x - 128.0) + 128.0))))*(y - 128.0) + Math.sin((((55.47567806031538-3.210430393709893)-(8.049120021105843+3.7737038202873507))/(Math.cos(1416.4911441678921)*(Math.cos((-5.48403271839571))*(y - 128.0) + Math.sin((-5.48403271839571))*(x - 128.0) + 128.0))))*(x - 128.0) + 128.0))-((((((-1.1681228800209402)/(-0.06950653264793862))+(4.357805742220427+57.35437384130615))-(((-5.6175)+60.57686363364052)+(157.38365460328305/14.255243558682348)))-(((50.46887973041501-28.68790871854386)-((-84.76355647185761)/16.971318795603626))/(Math.cos(60.55604385291357)*(Math.cos(0.8399234472900081)*(y - 128.0) + Math.sin(0.8399234472900081)*(x - 128.0) + 128.0))))/(Math.cos(((68.99403778177691-6.7277361202646)-(68.29899513178421/39.93491202811319)))*(Math.cos(((52.26524766660549-11.822823841393193)/((-0.9333567771842404)*(-51.58813001490978))))*(y - 128.0) + Math.sin(((52.26524766660549-11.822823841393193)/((-0.9333567771842404)*(-51.58813001490978))))*(x - 128.0) + 128.0)))))-(((y+((((64.1009159019814-7.07888147956312)-(45.63115632965953/33.26939330532595))/((-1.411273158979994)+(162.82638104717734/25.48661092865167)))-((-0.6990630893824501)+((1.0+55.62789806139035)-(-0.726475779915801)))))+25.48661092865167)/((((((3.6998282997829626-(-0.054313678619060536))+(2.635929566738476+57.35437384130615))-(((-5.6175)+1.6559770167837788)+(157.38365460328305/15.911220575466126)))-(((56.03059796031538-13.103709233809107)-((-8.793156010484367)/15.911220575466126))/33.40026076537044))/(((-5.6175)+((65.14962877794201+42.05283819678645)/(Math.cos((-29.667176549750238))*(y - 128.0) + Math.sin((-29.667176549750238))*(x - 128.0) + 128.0)))+(((1.0+64.14962877794201)+(0.7864936866398915*124.19267176388432))/(Math.cos(((-30.440012232415103)+0.7728356826648642))*(y - 128.0) + Math.sin(((-30.440012232415103)+0.7728356826648642))*(x - 128.0) + 128.0))))-(((Math.cos(((52.26524766660549-11.822823841393193)/((-0.9333567771842404)*(-51.58813001490978))))*(y - 128.0) + Math.sin(((52.26524766660549-11.822823841393193)/((-0.9333567771842404)*(-51.58813001490978))))*(x - 128.0) + 128.0)-(((89.05367363472868-(-5.313458582047474))-((-6.180287965656889)+1.2155327686448585))/(Math.cos(94982.13643469321)*(Math.cos((-5.48403271839571))*(y - 128.0) + Math.sin((-5.48403271839571))*(x - 128.0) + 128.0))))/((((4.171394119083716-(-0.669753920512955))-((-2.6461079943324393)/38.069917941891696))+((0.7578256354128613+3.599980106807566)+(53.42491045477165-9.302146065890126)))-(((-5.6175)+(67.32677658997079/37.60646159138316))+((58.023501580174944+92.23402582534105)/(10.293720575466127-(-3.961522983216221)))))))))-(((((((((-3867.1224463178205)/(-711.841995204069))-((-49.80692906748324)/46.677917300079756))-((60.53774544241091-63.18385343674335)/(48.29101301305941-10.221095071167717)))+((Math.cos(24.373148243201737)+(212.76855861177515/59.10270398701082))+((1.0+55.62789806139035)-((-5.6175)+4.891024220084199))))-(((-5.6175)+((55.80082386961166-1.6613031282314858)/(0.6797455011652649*55.32432583505983)))+(((1.0+57.023501580174944)+(60.63955767451016-1.199297048871501))/((51.236269280572834/4.977429579999331)-((-5.6175)+1.6559770167837788)))))+(((((4.110612493936767+59.990303408044625)-((-3.961522983216221)+11.04040446277934))-((40.584382965092786-(-5.046773364566746))/((-0.6480436407065038)*(-51.338198873543945))))/((-1.411273158979994)+((65.14962877794201+97.67675226923534)/25.48661092865167)))-((-0.6990630893824501)+((x+55.62789806139035)-(-0.726475779915801)))))+((((-5.6175)+((((-4.8350009385373545)+58.09678117212516)-(-5.959627038472939))/25.48661092865167))+(((y+64.14962877794201)+(-5.6175))/25.48661092865167))*(Math.cos((-5.6175))*(((-0.21482297266255665)+(0.9995816221136059*(Math.cos((-0.3588))*(y - 128.0) + Math.sin((-0.3588))*(x - 128.0) + 128.0)))/0.9995816221136059))))/((((((3.6998282997829626-((-2.6454380902803862)/48.70666391121612))+(2.635929566738476+(56.62789806139035-(-0.726475779915801))))-(((-5.6175)+(62.275436077892444/37.60646159138316))+((65.14962877794201+92.23402582534105)/(10.293720575466127-(-5.6175)))))-((((56.6553107519237-0.6247127916083139)-(155.75951666442955/11.886673756660574))-((31.49565347228141+(-40.28880948276578))/(10.293720575466127-(-5.6175))))/33.40026076537044))/(((-5.6175)+(((1.0+64.14962877794201)+(0.7864936866398915*53.46875494506163))/(Math.cos(((-30.440012232415103)+0.7728356826648642))*(y - 128.0) + Math.sin(((-30.440012232415103)+0.7728356826648642))*(x - 128.0) + 128.0)))+(((y+64.14962877794201)+(Math.cos((-5.6175))*(124.1407122963661/0.9995816221136059)))/(Math.cos((((-31.21284791507997)+0.7728356826648642)+((-0.7032)/(-0.9098958753757992))))*(y - 128.0) + Math.sin((((-31.21284791507997)+0.7728356826648642)+((-0.7032)/(-0.9098958753757992))))*(x - 128.0) + 128.0))))-((((-5.6175)+(((91.71971938326753+(-40.28880948276578))+((-1.5056740951020087)+0.628746218165859))/((58.84699205403547-6.968478337120015)-(0.9209601763451373+(-0.5526386846803403)))))+((((1.0+90.71971938326753)+((-0.9580520889989423)*42.05283819678645))+(((-5.6175)+1.6559770167837788)+(157.38365460328305/14.255243558682348)))/(((57.645634196973646-6.963925185563525)-(33.595976944204246/396.096041808323))-(((-5.2884096300778225)-(-4.074438179128871))+(29.86504022556091/25.48661092865167)))))+((((y+(87.15096422420511-(-3.5687551590624196)))+(((-3.293871823705877)+2.3358197347069347)*(0.7864936866398915*(-3.1242068222964203))))+(((-5.6175)+(62.275436077892444/37.60646159138316))+((65.14962877794201+92.23402582534105)/(10.293720575466127-(-3.961522983216221)))))/((((0.7996980829355541+56.84593611403809)-((-3.937031232985157)+10.900956418548683))-((46.728451894425255-13.132474950221008)/(87.24642505407381*65.44276885808762)))-((((-4.839277735585562)-0.7782222644144413)-((-5.2884096300778225)+1.2139714509489514))+((65.14962877794201+99.4151426824)/25.48661092865167))))))))/(Math.cos((((92.41081172580022+(((-5.6175)+((67.74169266299941/(-9.135886721189351))/25.48661092865167))+(((0.09087445431038602+64.14962877794201)+(-0.9951205446441925))/25.48661092865167)))+((((((-3749.833453019405)/(-711.841995204069))-((-49.80692906748324)/46.677917300079756))-((90.1286454424109-63.18385343674335)/(48.29101301305941-10.204458653468825)))-(((92.41081172580022+(-0.06292124436252022))+((-14.666146294285374)/1.6536969743966718))-(((-5.6175)+0.9828969453854431)+(49.669776676094166/50.63906939581956))))/(((-0.6629912937314577)+((23.879490586076983-(-4.344404172614684))/(33.3782266894417+(-586.5732907371194))))+(((145.69858570876426+64.14962877794201)+(-5.6175))/25.48661092865167))))-(((((-0.9745)+0.9161)+(((57.02203442241827-1.3715656282302762)/((-1.411273158979994)+6.388702738979325))-((-0.6990630893824501)+((-7.547358218609652)-(-0.726475779915801)))))+((((-5.6175)+(59.221407272060745/25.48661092865167))+((62.821934487942016+(-5.6175))/25.48661092865167))*(Math.cos((-5.6175))*(((-0.21482297266255665)+53.66120777304216)/0.9995816221136059))))/(((((3.754141978402023+59.990303408044625)-((-3.961522983216221)+9.891362756038747))-((42.92688872650628-(-0.5526386846803403))/33.40026076537044))/(((-5.6175)+(107.20246697472847/25.48661092865167))+((64.75735952014698+97.67675226923534)/(Math.cos((-29.667176549750238))*(y - 128.0) + Math.sin((-29.667176549750238))*(x - 128.0) + 128.0))))-((((-5.6175)+(50.5539820235656/51.510192225250655))+((51.43090990050175+7.07888147956312)/(50.59689125662816-(-0.04217813919140179))))+(((89.55691112961881+(-40.28880948276578))+((-3.961522983216221)+11.04040446277934))/((50.68170901141012-(-0.007464313100687468))-((-1.5430618208711326)+6.456910725440577))))))))*(Math.cos(((((((((-3438.8837627797725)/(-711.841995204069))-((-49.80692906748324)/46.677917300079756))-((60.537382746505806-63.18385343674335)/(48.29101301305941-10.196724975214183)))+(((6.499591417690668-(-0.06950653264793862))+(4.325096449162249+57.206584561798))-(((-5.6175)+1.7902980961494528)+(64.85879331046456/14.255243558682348))))-(((-5.6175)+((95.34198862352656-(-5.614929203542319))/(0.6797455011652649*55.32432583505983)))+(((91.20060000000001+57.023501580174944)+(0.9887947124560977*97.42982638959796))/((49.34535123186521/13.572958448599266)-((-5.6175)+1.6559770167837788)))))-((((((-20.79694381470288)-0.6247127916083139)-(29.924496861207334-0.3520817679558383))-(((-45.47507762731098)+(-0.8303326254900928))/(11.353818795603626-(-3.467674430240077))))-(((212.67202490557247+9.288261189533673)+(0.7232899229854635*42.05283819678645))/((56.51283351918987/5.626680280546492)-((-3.4806356585626403)+1.2399291493278408))))/(Math.cos(((88.98389258269492+(-13.367424742886305))-((-25.48738939227662)/13.638724920051153)))*(Math.cos(((52.26524766660549-7.021382267708116)/((-0.9333567771842404)*(-50.61590851857966))))*(y - 128.0) + Math.sin(((52.26524766660549-7.021382267708116)/((-0.9333567771842404)*(-50.61590851857966))))*(x - 128.0) + 128.0))))/(Math.cos(((((0.9935431078679873*83.45902961647738)+((-1.0114182754309584)*42.05283819678645))/((49.56838772228843+54.97047268395381)-((-3.9865249583024758)+50.26576172157272)))-(((10.406587196196075+(-41.90481679912619))*(89.7995990522739-8.05879048835689))-(((-5.6175)+(-12.72691067598249))+((-102176.99968782235)/89.5858493803034)))))*(Math.cos(((((5.967470677194419+67.37816079062942)-((-2.9329369341091813)+32.19157789993657))-(((-47.86986487726627)-20.54443178494056)/((-0.49357834689992564)*(-48.94596043715694))))/(Math.cos((0.6932268362448037-(-1415.7979173316473)))*(Math.cos(((-24.58581280808712)+0.1334672816042897))*(y - 128.0) + Math.sin(((-24.58581280808712)+0.1334672816042897))*(x - 128.0) + 128.0))))*(y - 128.0) + Math.sin(((((5.967470677194419+67.37816079062942)-((-2.9329369341091813)+32.19157789993657))-(((-47.86986487726627)-20.54443178494056)/((-0.49357834689992564)*(-48.94596043715694))))/(Math.cos((0.6932268362448037-(-1415.7979173316473)))*(Math.cos(((-24.58581280808712)+0.1334672816042897))*(y - 128.0) + Math.sin(((-24.58581280808712)+0.1334672816042897))*(x - 128.0) + 128.0))))*(x - 128.0) + 128.0))))*(y - 128.0) + Math.sin(((((((((-3438.8837627797725)/(-711.841995204069))-((-49.80692906748324)/46.677917300079756))-((60.537382746505806-63.18385343674335)/(48.29101301305941-10.196724975214183)))+(((6.499591417690668-(-0.06950653264793862))+(4.325096449162249+57.206584561798))-(((-5.6175)+1.7902980961494528)+(64.85879331046456/14.255243558682348))))-(((-5.6175)+((95.34198862352656-(-5.614929203542319))/(0.6797455011652649*55.32432583505983)))+(((91.20060000000001+57.023501580174944)+(0.9887947124560977*97.42982638959796))/((49.34535123186521/13.572958448599266)-((-5.6175)+1.6559770167837788)))))-((((((-20.79694381470288)-0.6247127916083139)-(29.924496861207334-0.3520817679558383))-(((-45.47507762731098)+(-0.8303326254900928))/(11.353818795603626-(-3.467674430240077))))-(((212.67202490557247+9.288261189533673)+(0.7232899229854635*42.05283819678645))/((56.51283351918987/5.626680280546492)-((-3.4806356585626403)+1.2399291493278408))))/(Math.cos(((88.98389258269492+(-13.367424742886305))-((-25.48738939227662)/13.638724920051153)))*(Math.cos(((52.26524766660549-7.021382267708116)/((-0.9333567771842404)*(-50.61590851857966))))*(y - 128.0) + Math.sin(((52.26524766660549-7.021382267708116)/((-0.9333567771842404)*(-50.61590851857966))))*(x - 128.0) + 128.0))))/(Math.cos(((((0.9935431078679873*83.45902961647738)+((-1.0114182754309584)*42.05283819678645))/((49.56838772228843+54.97047268395381)-((-3.9865249583024758)+50.26576172157272)))-(((10.406587196196075+(-41.90481679912619))*(89.7995990522739-8.05879048835689))-(((-5.6175)+(-12.72691067598249))+((-102176.99968782235)/89.5858493803034)))))*(Math.cos(((((5.967470677194419+67.37816079062942)-((-2.9329369341091813)+32.19157789993657))-(((-47.86986487726627)-20.54443178494056)/((-0.49357834689992564)*(-48.94596043715694))))/(Math.cos((0.6932268362448037-(-1415.7979173316473)))*(Math.cos(((-24.58581280808712)+0.1334672816042897))*(y - 128.0) + Math.sin(((-24.58581280808712)+0.1334672816042897))*(x - 128.0) + 128.0))))*(y - 128.0) + Math.sin(((((5.967470677194419+67.37816079062942)-((-2.9329369341091813)+32.19157789993657))-(((-47.86986487726627)-20.54443178494056)/((-0.49357834689992564)*(-48.94596043715694))))/(Math.cos((0.6932268362448037-(-1415.7979173316473)))*(Math.cos(((-24.58581280808712)+0.1334672816042897))*(y - 128.0) + Math.sin(((-24.58581280808712)+0.1334672816042897))*(x - 128.0) + 128.0))))*(x - 128.0) + 128.0))))*(x - 128.0) + 128.0))))";
			fy = "(((((((((-3.3283814632123376)-((((-6.613492042030608E-5)-2.0964280746613596E-8)-((-235.37149310138523)*(-5.756309372467919E-6)))/(((-0.312330669245172)/(-6.54770784717885))-(8.728803956347137E-4*(-7.4377610371462455E-6)))))+(((-0.0029591263443828745)+(((-0.32743372335005555)-(-5.315898400891663E-4))*0.007502742983143798))+62.685458847732576))+(Math.cos((-5.6175))*((0.08424933402866593+(0.9995816221136059*(Math.cos((-0.3588))*(y - 128.0) + Math.sin((-0.3588))*(x - 128.0) + 128.0)))*0.9995816221136059)))+(Math.sin(((-0.2920911330256692)-((((-0.5358176456647709)-11.620938465529955)+((-0.11204692640617149)*251.85412991814778))-(((-3.9719698014107108)-(-120219.63599300412))+((-944.6274077581419)*0.33583382528457184)))))-((((0.323293270544704-((-0.0677825579329913)-0.7728356826648642))+(Math.cos((-5.6175))-((-0.01761607759553432)/(-0.6530965534724515))))+(Math.cos(((1.028346395276577-0.023282081472521015)+(Math.cos(103.95230744710065)*(y - 128.0) + Math.sin(103.95230744710065)*(x - 128.0) + 128.0)))*(y - 128.0) + Math.sin(((1.028346395276577-0.023282081472521015)+(Math.cos(103.95230744710065)*(y - 128.0) + Math.sin(103.95230744710065)*(x - 128.0) + 128.0)))*(x - 128.0) + 128.0))/(Math.cos(((((-44.07010399159379)+23.489516853415772)+((-0.7032)/(-0.9098958753757992)))+((-0.7032)/Math.cos(15.2802))))*(y - 128.0) + Math.sin(((((-44.07010399159379)+23.489516853415772)+((-0.7032)/(-0.9098958753757992)))+((-0.7032)/Math.cos(15.2802))))*(x - 128.0) + 128.0))))+(((-5.6175)+((((((-1.181258457611474E-5)+2.5470888183512788)+(123.87887856050605/(-16.781030963793853)))+((1.0+70.64311049080194)-((-5.6175)+4.148616889265181)))-((((-2.895595575208024)+25.48661092865167)+(277.47756378957195/25.48661092865167))/(((-5.6175)+0.3551443902449055)+(81.95574492450092/51.29019772054335))))/25.48661092865167))+(((y+64.14962877794201)+Math.sin((((103.49136937368793+(-10.786081445435197))+((-2.5801059894388563)+(-207.15338094346856)))+(((-1.3894085768202387)-13.236678816538904)/((-0.8727113203234724)+(-5.075526580767774))))))/25.48661092865167)))+(((-5.6175)+(((y-(((57.02203442241827-1.3715656282302762)/((-1.411273158979994)+6.388702738979325))-((-0.6990630893824501)+(56.62789806139035-(-0.726475779915801)))))+25.48661092865167)/(((((3.754141978402023+59.990303408044625)-((-3.961522983216221)+9.891362756038747))-((92.52504863794297-(-5.233719614616518))/33.40026076537044))/(((-5.6175)+(107.20246697472847/25.48661092865167))+((65.14962877794201+97.67675226923534)/(Math.cos((-29.667176549750238))*(y - 128.0) + Math.sin((-29.667176549750238))*(x - 128.0) + 128.0))))-((((-5.6175)+((-18.988136060440155)/20.388506878841977))+((51.43090990050175+(-0.8769278769361497))/(51.87851371691545-(-6.12080906535643))))+(((-5.6175)+((-18.988136060440155)/20.388506878841977))+((11.027322105424712-(-11.4620653951431))/(51.87851371691545-(-6.12080906535643))))))))+((((64.14962877794201+(((91.61487798385599+(-0.24428625473192156))+((-14.385477336859708)/3.409181810501063))-(((-5.6175)+0.3551443902449055)+(78.67406465920786/46.45373389398341))))+((((-5.6175)+(59.221407272060745/25.48661092865167))+((65.14962877794201+(-5.6175))/25.48661092865167))*(Math.cos((-5.6175))*(((-0.21482297266255665)+53.66120777304216)/0.9995816221136059))))+(((((89.94378475529572+(-0.06292124436252022))+((-0.2828454869733008)/18.61400008536044))-(((-5.6175)+(-0.8322331890228053))+(52.239513438427856/49.25372067120718)))-(((71.49363572153746-1.8299865637136163)+(66.33653433202896/37.60646159138316))+((23.271560696280588+96.33809716955004)/(9.804889783555204-(-3.638713111420223)))))-((((-5.6175)+((-18.988136060440155)/20.388506878841977))+((51.43090990050175+(-0.8769278769361497))/(51.87851371691545-(-6.12080906535643))))+(((-5.6175)+((-18.988136060440155)/20.388506878841977))+((51.43090990050175+(-7.123174095102009))/(51.87851371691545-(-6.12080906535643)))))))/(((((((-3.3283814632123376)-(-3.5189634550065096E-11))+(0.0037441847914782213+62.685458847732576))+(Math.cos((-5.6175))*(53.74545710707083*0.9995816221136059)))+(Math.sin(((-0.2920911330256692)-120500.46956135037))-((1.9234320381449193+282.43373076908614)/(Math.cos((-19.034915772848287))*(y - 128.0) + Math.sin((-19.034915772848287))*(x - 128.0) + 128.0))))+(((-5.6175)+((68.27699266299942-(-9.135886721189351))/25.48661092865167))+(((1.0+64.14962877794201)+Math.sin((-114.5693048520392)))/25.48661092865167)))+(((((91.71971938326753+22.133882067970646)+(68.62700995549557-1.816684275750675))/((61.45753070142008-(-6.714110066869021))-((-69.08615895074463)+1.2352485859266236)))-(((7.07888147956312+90.50502632030428)+(Math.cos(348.6234761679445)*(y - 128.0) + Math.sin(348.6234761679445)*(x - 128.0) + 128.0))/(Math.cos(((-19.807751455513152)+(-24.421324975987805)))*(y - 128.0) + Math.sin(((-19.807751455513152)+(-24.421324975987805)))*(x - 128.0) + 128.0)))/((Math.cos((27.977394642039616+55.98749980754323))+((90.48864558463426+(-4.311004032628378))/71.2546))+(((1.0+67.23652045847706)+(-5.6175))/25.48661092865167)))))))-(((-5.6175)+(((y+((((64.1009159019814-7.07888147956312)-(45.63115632965953/33.26939330532595))/((-1.411273158979994)+(162.82638104717734/25.48661092865167)))-((-0.6990630893824501)+((1.0+55.62789806139035)-(-0.726475779915801)))))+25.48661092865167)/((((((3.6998282997829626-(-0.054313678619060536))+(2.635929566738476+57.35437384130615))-(((-8.677181300888314)+1.6559770167837788)+(157.38365460328305/15.911220575466126)))-(((56.03059796031538-13.103709233809107)-((-8.793156010484367)/15.911220575466126))/33.40026076537044))/(((-5.6175)+((65.14962877794201+42.05283819678645)/(Math.cos((-29.667176549750238))*(y - 128.0) + Math.sin((-29.667176549750238))*(x - 128.0) + 128.0)))+(((1.0+64.14962877794201)+(0.7864936866398915*124.19267176388432))/(Math.cos(((-30.440012232415103)+0.7728356826648642))*(y - 128.0) + Math.sin(((-30.440012232415103)+0.7728356826648642))*(x - 128.0) + 128.0))))-((((-5.6175)+(((-44.47474698909183)+25.48661092865167)/(11.353818795603626-(-9.03468808323835))))+(((91.71971938326753+(-40.28880948276578))+((-1.5056740951020087)+0.628746218165859))/((58.84699205403547-6.968478337120015)-((-5.56817038067609)+(-0.5526386846803403)))))+(((-5.6175)+(((-44.47474698909183)+25.48661092865167)/(11.353818795603626-(-9.03468808323835))))+(((91.71971938326753+(-40.28880948276578))+((-1.5056740951020087)+(-5.6175)))/((58.84699205403547-6.968478337120015)-((-5.56817038067609)+(-0.5526386846803403)))))))))+((((y+((((102.88812554417783+(-11.273247560321833))+((-2.5801059894388563)+2.3358197347069347))+(((-1.429467477746471)-12.956009859113237)/(1.0733620757941285+2.3358197347069347)))-(((-5.6175)+(18.21542599500301/51.29019772054335))+((81.95574492450092+(-3.281680265293065))/(49.86291570448447-3.409181810501063)))))+((((-5.6175)+((53.26178023358781-(-5.959627038472939))/25.48661092865167))+(((1.0+64.14962877794201)+(-5.6175))/25.48661092865167))*(Math.cos((-5.6175))*(((-0.21482297266255665)+(0.9995816221136059*53.68366783252381))/0.9995816221136059))))+((((((59.35457124850069+43.5122329868828)+((-0.717668852891493)-10.851676356452185))+(((-5.6175)+3.0373940105611434)+(64.15450823329782/25.48661092865167)))+(((180.66392713098307/136.02255113310713)-(340.19846700206074/35.871393231746325))/(((-0.6538707189592714)+1.2094326759536351)+(62.61902045847706/0.5946092568778405))))-(((-5.6175)+(((-44.47474698909183)+25.48661092865167)/(11.353818795603626-(-11.4620653951431))))+(((91.71971938326753+38.04861911385025)+Math.cos(0.5266032750190013))/((50.68170901141012-(-6.714110066869021))-((-0.787275965315532)+1.17179331175755)))))-(((((102.06276395813892+(-11.569345209343677))+((-2.5801059894388563)+2.517184745076336))+((1.3281909920524235-9.483837575089884)/(0.5555619569943637+2.4569379049170355)))-(((-5.6175)+((-18.988136060440155)/22.815884190746726))+((51.43090990050175+0.8645192301393019)/(57.39581907827914-0.38451734644201807))))+((((64.14962877794201+90.71971938326753)+((-0.9580520889989423)*42.05283819678645))+((73.67061938176603-80.32475858409431)-((-4.6175)/66.08229412821638)))/((91.21453670611427+((-2.5801059894388563)+2.517184745076336))+((0.8692767088638097-9.483837575089884)/((-0.5651172243260594)+2.3358197347069347)))))))/((((((((-849.3396782003116)/(-951.0640336909408))+0.7484)-((53.42491045477165-9.302146065890126)/(52.46763430534918-0.04932961932390994)))+(((0.00942563541286137+0.7484)+(123.75053778235886/(-97.72890966406518)))+((1.0+55.62789806139035)-((-5.6175)+4.891024220084199))))-(((-5.6175)+((56.97199881382099-0.5218768296307473)/(Math.cos((-37.9310396408366))*(y - 128.0) + Math.sin((-37.9310396408366))*(x - 128.0) + 128.0)))+(((1.0+64.14962877794201)+(0.9466713556126009*124.19267176388432))/((55.46979606233058/4.977429579999331)-(-5.6175)))))-(((y+((55.650468794187994/4.977429579999331)-((-0.6990630893824501)+57.35437384130615)))+((((-5.6175)+2.3236281762941227)+(59.53212877794201/25.48661092865167))*(Math.cos((-5.6175))*(53.4463848003796/0.9995816221136059))))/((((63.74444538644665-5.929839772822526)-(43.479527411186616/33.40026076537044))/(((-5.6175)+4.206226841020006)+(162.82638104717734/25.48661092865167)))-((0.6635364399040715+(0.9995816221136059*53.68366783252381))+((32.35415875344392-(-1.7092112997297941))/((-0.4116570677284838)*(-45.21724888162896)))))))-((((((7.563303877594967+67.86707752744711)-((-3.8796187916205884)+9.364122581581485))-((32.35415875344392-(-1.7092112997297941))/((-0.4116570677284838)*(-45.21724888162896))))+(((7.238574955428108+66.43204442633792)-(71.4276155488344+8.897143035259912))-(((-6.548815675702812)+0.8716305570212274)+((-6.548815675702812)+0.7639353992412974))))/((((89.94378475529572+(-0.06292124436252022))+((-8.155646583037461)/3.012499861911399))-(((-5.6175)+(-0.8322331890228053))+(52.239513438427856/49.25372067120718)))-(((-5.6175)+((-26.437869249462963)/22.815884190746726))+((114.58053867844376+(-6.584264206901505))/(87.05269396401881-(-5.53343217761728))))))+(((y+64.14962877794201)+((Math.cos((40.442423825212295/47.242701249153846))*(y - 128.0) + Math.sin((40.442423825212295/47.242701249153846))*(x - 128.0) + 128.0)-(((-5.6175)+(-11.273247560321833))+(48.14922963520869/47.27999151400975))))/25.48661092865167))))))-((((-5.6175)+(((y+((((64.1009159019814-7.07888147956312)-(45.63115632965953/33.26939330532595))/((-1.411273158979994)+(162.82638104717734/25.48661092865167)))-((-0.6990630893824501)+((1.0+55.62789806139035)-(-0.726475779915801)))))+25.48661092865167)/((((((3.6998282997829626-(-0.054313678619060536))+(2.635929566738476+57.35437384130615))-(((-5.6175)+1.6559770167837788)+(157.38365460328305/15.911220575466126)))-(((87.17488146087597-(-5.350167177067))-((-6.449733189022805)+1.2160135744062863))/33.40026076537044))/(((-5.6175)+((65.14962877794201+42.05283819678645)/(Math.cos((-29.667176549750238))*(y - 128.0) + Math.sin((-29.667176549750238))*(x - 128.0) + 128.0)))+(((1.0+64.14962877794201)+(0.7864936866398915*124.19267176388432))/(Math.cos(((-30.440012232415103)+0.7728356826648642))*(y - 128.0) + Math.sin(((-30.440012232415103)+0.7728356826648642))*(x - 128.0) + 128.0))))-((((-5.6175)+(((-44.47474698909183)+25.48661092865167)/(11.353818795603626-(-9.03468808323835))))+(((91.71971938326753+(-40.28880948276578))+((-1.5056740951020087)+0.628746218165859))/((58.84699205403547-6.968478337120015)-((-5.56817038067609)+(-0.5526386846803403)))))+(((-5.6175)+(((-44.47474698909183)+25.48661092865167)/(11.353818795603626-(-9.03468808323835))))+(((54.88771923572146/4.977429579999331)-((-5.677185118681585)+(-5.784880276461514)))/((58.84699205403547-6.968478337120015)-((-5.56817038067609)+(-0.5526386846803403)))))))))+((((((y+(Math.cos((79.62368083018428-49.93453417266614))*(x - 128.0) - Math.sin((79.62368083018428-49.93453417266614))*(y - 128.0) + 128.0))+(Math.cos(((-3.758950262536698)+9.892294134785084))*((43.72785613171919+53.66120777304216)/0.9995816221136059)))/((((3.1840415051568147+51.58428863511989)-(162.82638104717734/17.504173756660574))/(((-5.6175)+4.206226841020006)+(162.82638104717734/25.48661092865167)))-((-5.6175)+((56.97199881382099-(-5.303437264071456))/(0.5688550564430671*55.32432583505983)))))+((((y+(87.15096422420511-(-3.5687551590624196)))+(((-3.293871823705877)+2.3358197347069347)*(0.7864936866398915*53.46875494506163)))+(((91.23453778167729+(-0.07703688715738682))-((-6.449733189022805)+2.2913502017847645))-((87.72322880505334-(-5.532451395026555))+(107.99627447154225/86.2865628536922))))/((((0.7996980829355541+56.84593611403809)-((-3.937031232985157)+10.900956418548683))-(((-44.47474698909183)+(-40.28880948276578))/(11.353818795603626-56.15473077665985)))-(((114.58053867844376+4.8079261928148185)/(92.56270734641018-(-5.609807171927692)))+((65.14962877794201+(-35.2845885523811))/25.48661092865167)))))-(((-5.6175)+(((y+(11.180563762831873-56.6553107519237))+25.48661092865167)/(((60.87428691451244-1.301772094434257)/((-1.411273158979994)+6.388702738979325))-(((-6.548815675702812)+0.8716305570212274)+((-6.548815675702812)+0.7639353992412974)))))+(((((89.96992198770013+(-0.06292124436252022))+((-2.470350948571861)+1.6170238401362815))-(((-5.6175)+(-0.8103992156577654))+(52.239513438427856/46.87509757177024)))-((((-5.6175)+(-0.8304247740754213))+(15.79202179723925/68.1159261193919))+((114.58053867844376+8.277523838654025)/(94.67117363490203-(-4.99535129502768)))))/((((0.7996980829355541+56.84593611403809)-((-3.937031232985157)+10.900956418548683))-(((-44.47474698909183)+(-40.28880948276578))/(11.353818795603626-56.15473077665985)))-(((114.58053867844376+4.8079261928148185)/(92.56270734641018-(-5.609807171927692)))+((65.14962877794201+(-35.2845885523811))/25.48661092865167))))))/(((((((4.448965624503254-(-1.2118798038431635))+(16.47476592885735/38.069917941891696))-(((-51.338198873543945)-(-1.5312698060607057))/(53.39122470334656-17.8153111932277)))-(((3.175054286876141+57.35437384130615)-(64.10643640643964-0.9225829696962948))/((48.66013765887918-0.36912464581976734)-(11.04040446277934-0.8193093916116244))))+((((5.432557326446024-(-1.0670340912446439))-((-2.6461079943324393)/65.72725166337183))+((0.7251163423546833+3.599980106807566)+(56.62789806139035-(-0.726475779915801))))-(((-5.6175)+((-1.0670340912446439)/43.292220677894036))+((58.023501580174944+59.44026062563866)/(15.634091487592901-(-3.961522983216221))))))-(((-5.6175)+(((4.357805742220427+57.35437384130615)-((-5.6175)+0.9931452514976162))/(Math.cos((-0.8233806922386563))*(54.32474421294623+1.8299865637136163))))+(((y+(Math.cos(29.689146657518144)*(x - 128.0) - Math.sin(29.689146657518144)*(y - 128.0) + 128.0))+(Math.cos(6.1333438722483855)*(97.38906390476134/0.9995816221136059)))/(((54.7683301402767-9.302146065890126)/((-1.411273158979994)+6.388702738979325))-((-5.6175)+(62.275436077892444/31.471522495577595))))))-((((-5.6175)+(((1.0+(-45.47474698909183))+25.48661092865167)/((54.88771923572146/4.977429579999331)-((-5.677185118681585)+(-6.161063087883943)))))+((((119.60965786583063/12.773183693803166)+(52.239513438427856/46.40180168182543))-(((-5.6175)+(-0.8103992156577654))+(52.239513438427856/46.40180168182543)))/(((7.563338945619306+67.86707752744711)-((-3.8796187916205884)+9.364122581581485))-((32.35415875344392-(-1.7092112997297941))/((-0.4116570677284838)*(-45.21724888162896))))))+((((64.14962877794201+(87.15096422420511-(-3.5687551590624196)))+(((-3.293871823705877)+2.3358197347069347)*(0.7864936866398915*53.46875494506163)))+((((-5.6175)+(-0.8103992156577654))+(52.239513438427856/46.87509757177024))-(((-6.548815675702812)+0.8716305570212274)+((-6.548815675702812)+0.7639353992412974))))/((((89.96992198770013+(-0.06292124436252022))+((-2.470350948571861)+1.6170238401362815))-(((-5.6175)+(-0.8103992156577654))+(52.239513438427856/46.40180168182543)))-((((-5.6175)+(-0.8304247740754213))+(15.79202179723925/59.005418165589575))+((114.58053867844376+26.392087547626133)/(75.43041647306642-5.484503789960897)))))))))+((((64.14962877794201+(((((59.37589255729503+43.5122329868828)+(0.49131593331593687-11.76456349363777))+(((-5.6175)+3.0373940105611434)+(59.53212877794201/25.48661092865167)))+((((-3.765287212453406)+2.3358197347069347)-(344.39199675645096/26.58164052833027))/(((-0.6629912937314577)+1.7363533695255862)+(59.53212877794201/25.48661092865167))))-(((-5.6175)+((15.5272/0.8524203608666377)/(56.65531255344832-5.365114832904968)))+(((122.2445544072667+(-40.28880948276578))+((-5.6175)+2.3358197347069347))/((50.68170901141012-0.8187933069256501)-(1.0733620757941285+2.3358197347069347))))))+((((-5.6175)+((((-4.8350009385373545)+58.09678117212516)-(-5.959627038472939))/25.48661092865167))+(((y+64.14962877794201)+(-5.6175))/25.48661092865167))*(Math.cos((-5.6175))*(((-0.21482297266255665)+(0.9995816221136059*(Math.cos((-0.3588))*(y - 128.0) + Math.sin((-0.3588))*(x - 128.0) + 128.0)))/0.9995816221136059))))+((((-5.6175)+(((y+(11.180563762831873-56.6553107519237))+25.48661092865167)/(((60.87428691451244-1.301772094434257)/((-1.411273158979994)+6.388702738979325))-(((-6.548815675702812)+0.8716305570212274)+((-6.548815675702812)+0.7639353992412974)))))+((((y+(87.15096422420511-(-3.5687551590624196)))+(((-3.293871823705877)+2.3358197347069347)*(0.7864936866398915*53.46875494506163)))+(((91.23453778167729+(-0.07703688715738682))-((-6.449733189022805)+2.2913502017847645))-((87.72322880505334-(-5.532451395026555))+(107.99627447154225/86.2865628536922))))/((((0.7996980829355541+56.84593611403809)-((-3.937031232985157)+10.900956418548683))-(((-44.47474698909183)+(-40.28880948276578))/(11.353818795603626-56.15473077665985)))-(((68.11589105136756+4.8079261928148185)/(92.56270734641018-(-5.609807171927692)))+((65.14962877794201+(-35.2845885523811))/25.48661092865167)))))-((((-5.6175)+(((-44.47474698909183)+25.48661092865167)/(11.353818795603626-(-9.03468808323835))))+(((91.71971938326753+(-40.28880948276578))+((-1.5056740951020087)+0.628746218165859))/((58.84699205403547-6.968478337120015)-((-5.56817038067609)+(-0.5526386846803403)))))+(((-5.6175)+(((-44.47474698909183)+25.48661092865167)/(11.353818795603626-(-9.03468808323835))))+(((91.71971938326753+(-40.28880948276578))+((-1.5056740951020087)+(-5.6175)))/((58.84699205403547-6.968478337120015)-((-5.56817038067609)+(-0.5526386846803403))))))))/(((((((((-3.3283814632123376)-(-1.7539959613060823E-10))+((-0.005411789032749512)+62.685458847732576))+(Math.cos((-5.6175))*(53.74545710707083*0.9995816221136059)))+(Math.sin(((-0.2920911330256692)-(-119938.80242465787)))-((1.9234320381449193+282.43373076908614)/(Math.cos((-19.034915772848287))*(y - 128.0) + Math.sin((-19.034915772848287))*(x - 128.0) + 128.0))))+(((-5.6175)+((68.27699266299942-(-9.135886721189351))/25.48661092865167))+(((1.0+64.14962877794201)+Math.sin((-114.5693048520392)))/25.48661092865167)))+(((-5.6175)+(((1.0-(-45.47474698909183))+25.48661092865167)/((54.88771923572146/4.977429579999331)-((-5.677185118681585)+(-6.161063087883943)))))+((((64.14962877794201+90.71971938326753)+((-0.9580520889989423)*42.05283819678645))+((95.25478073657735-80.32475858409431)-((-5.677185118681585)+(-5.784880276461514))))/(((101.61359924279176+(-11.663107607153442))+((-2.5801059894388563)+2.517184745076336))+((1.3281909920524235-9.483837575089884)/(0.5555619569943637+2.4569379049170355))))))-(((-5.6175)+(((y+(11.180563762831873-56.6553107519237))+25.48661092865167)/(((60.87428691451244-1.301772094434257)/((-1.411273158979994)+6.388702738979325))-(((-6.548815675702812)+0.8716305570212274)+((-6.548815675702812)+0.7639353992412974)))))+((((y+(87.15096422420511-(-3.5687551590624196)))+(((-3.293871823705877)+2.3358197347069347)*(0.7864936866398915*53.46875494506163)))+(((91.23453778167729+(-0.07703688715738682))-((-6.449733189022805)+2.2913502017847645))-((87.72322880505334-(-5.532451395026555))+(107.99627447154225/86.2865628536922))))/((((0.7996980829355541+56.84593611403809)-((-3.937031232985157)+10.900956418548683))-(((-44.47474698909183)+(-40.28880948276578))/(11.353818795603626-56.15473077665985)))-(((114.58053867844376+4.8079261928148185)/(92.56270734641018-(-5.609807171927692)))+((65.14962877794201+(-35.2845885523811))/25.48661092865167))))))-((((-5.6175)+(((y+(11.180563762831873-56.6553107519237))+25.48661092865167)/(((57.814605613624124-2.926886377902662)/((-1.411273158979994)+6.388702738979325))-(((-6.548815675702812)+0.8716305570212274)+((-6.548815675702812)+0.38775258781886907)))))+(((((23.271560696280588+96.33809716955004)/(9.134470582382942-(-3.638713111420223)))+((51.43090990050175+0.8086035379261034)/(48.78970385518251-2.387902173357074)))-(((-5.6175)+((-18.988136060440155)/23.43059530854595))+((51.43090990050175+0.8086035379261034)/(48.78970385518251-2.387902173357074))))/((((59.36082156934691+42.25277767344485)+((-0.9656052274877488)-10.697502379665693))+(((-5.6175)+3.0373940105611434)+(64.15450823329782/25.48661092865167)))+(((180.66392713098307/136.02255113310713)-(89.05857896013856-(-5.302091347320426)))/(((-0.6538707189592714)+1.2094326759536351)+(62.61902045847706/25.48661092865167))))))+((((64.14962877794201+((91.37059172912407+(-4.219627504918962))-((-5.262355609755094)+1.693600450692674)))+((((-5.6175)+2.3236281762941227)+(59.53212877794201/25.48661092865167))*(Math.cos((-5.6175))*(53.4463848003796/0.9995816221136059))))+((((89.88086351093321+(-0.01519530921221782))-((-6.449733189022805)+1.0606206541664602))-((69.66364915782384+1.7639663910105485)+(119.60965786583063/13.443602894975427)))-((((-5.6175)+(-0.9313156757028125))+(50.5539820235656/57.99932278227188))+(((-5.6175)+(-0.9313156757028125))+(44.307735805399744/57.99932278227188)))))/(((((5.660845428346417+0.4327502348180454)-((-49.80692906748324)/35.57591351011886))-((60.52942812818229-63.18385343674335)/(48.29101301305941-10.221095071167717)))+(((6.499591417690668-(-0.040258917380034766))+(4.325096449162249+57.35437384130615))-(((-5.6175)+(-0.02464724780887701))+(117.4637622058136/19.59561447080912))))-(((-5.6175)+((61.71217958352658-(-4.624354748502384))/(0.6797455011652649*56.15473077665985)))+(((1.0+22.271560696280588)+(0.9887947124560977*97.42982638959796))/((45.466184074386575/4.977429579999331)-((-5.6175)+1.9787868885797768)))))))))))";
			
			fx = "(((Math.pow((((Math.pow((-4.989937092570909)*Math.pow(x-(49.655254647991306),2) + 1.8658670297072488*Math.pow(y-((-12.639979974379894)),2) + (-72.10476930284594)*Math.pow((-2.907570687390744),2),0.5)-(0.0/55.27019253271997))-(Math.pow(((-111.55046096453137)+(-1.4639674291819897)),(59.04025418007521+59.123237864930005))))/Math.sin((5.0425+(75.50088465915482/0.8382200505197588)))),Math.cos((((-83.7537)+(0.0+59.41908807101504))*(3.076990481263988+95.77926882762904)))))*(Math.pow(Math.pow((-10.3146)*Math.pow(x-(((Math.pow((0.0/0.7627721593966487),Math.cos((-2405.628707033018))))*(Math.pow(Math.pow((-10.3146)*Math.pow(x-(0.0),2) + 0.7645*Math.pow(y-((-0.6830591398859234)),2) + 59.41908807101504*Math.pow(0.0,2),-0.5),Math.sin(0.13050194768436765))))),2) + 0.7645*Math.pow(y-(Math.sin((Math.pow(93.73124409315147*Math.pow(x-((-0.1682263400351329)),2) + 0.0313*Math.pow(y-((-0.9042)),2) + 44.635*Math.pow(14.7451,2),0.5)*132.9153))),2) + ((Math.pow((0.0/0.7627721593966487),Math.cos((-2405.628707033018))))*(Math.pow(Math.pow((-10.3146)*Math.pow(x-(0.0),2) + 0.7645*Math.pow(y-((-0.6830591398859234)),2) + 59.41908807101504*Math.pow(0.0,2),-0.5),Math.sin(0.13050194768436765))))*Math.pow((((Math.pow(0.0,0.6739412814616318))*(Math.pow(1.0,0.1301318330767638)))+((Math.pow(0.0,0.5403022794650322))+(0.6023157743206629*97.46330283920435))),2),-0.5),Math.sin(Math.sin((Math.pow(92.1981*Math.pow(x-(0.3831),2) + 3.076990481263988*Math.pow(y-(0.1137),2) + 1.0*Math.pow(0.5403022794650322,2),-0.5)-Math.pow(59.04025418007521*Math.pow(x-(5.0425),2) + 59.41908807101504*Math.pow(y-(0.3494),2) + 59.42997119182281*Math.pow(0.0,2),-0.5)))))))+((Math.pow(((Math.pow((-3.5254072273698505)*Math.pow(x-(((-1.9795697258064031)+74.54372837453263)),2) + 2.1880849322871825*Math.pow(y-((1.0+0.0)),2) + (Math.pow(0.0,63.55710156920162))*Math.pow((0.0+58.703684717444624),2),0.5)+((Math.pow(0.0,0.6752564490226459))*92.1981))-Math.pow(((-102.8873)*(-0.8019))*Math.pow(x-((Math.pow((63.79489945531125/73.56763899810346),Math.cos(64.41304397486478)))),2) + ((Math.pow(0.0,0.6739412814616318))*(Math.pow(1.0,0.1307762356897426)))*Math.pow(y-((-0.9440569987619805)),2) + (Math.cos((-138.8833))*(x - 99.5) - Math.sin((-138.8833))*(y - 114.5) + 99.5)*Math.pow((Math.cos((-3.2164))*(x - 99.5) - Math.sin((-3.2164))*(y - 114.5) + 99.5),2),-0.5)),Math.cos((Math.pow((Math.pow(((-1.0)/0.7627721593966487),(0.0+59.123237753275575))),(Math.pow(Math.sin(59.123237753275575),((-0.1762)+0.9401076520639602))))))))+((Math.pow(Math.pow((-10.3146)*Math.pow(x-(Math.cos((59.41908807101504*98.85625930889303))),2) + (Math.pow((-3.60438463728843)*Math.pow(x-(66.80448893771324),2) + 2.134804686051128*Math.pow(y-((-6.653488055250349)),2) + (-44.896176522071826)*Math.pow((-0.2324708965380633),2),0.5)+(Math.pow(0.11584953288918483,96.69755850272227)))*Math.pow(y-((-0.9440569987619805)),2) + ((Math.pow(0.45802767370998787,0.5865068891581663))+(0.2828263491686845+98.85625930889303))*Math.pow(((3.8468/199.8392)-(0.9601856356037293*(-3.6891))),2),-0.5),Math.sin((Math.pow(92.1981*Math.pow(x-(0.3831),2) + 0.0*Math.pow(y-(0.1137),2) + (-0.0374)*Math.pow(5.0425,2),-0.5)-Math.pow(69.45544301420239*Math.pow(x-(5.0425),2) + 0.0*Math.pow(y-(0.3494),2) + 72.95993603092133*Math.pow(1.0,2),-0.5)))))*((Math.pow((Math.pow(1.0,0.1591139402819669))*Math.pow(x-((0.0*1.0)),2) + (Math.cos(1.0)*(y - 114.5) + Math.sin(1.0)*(x - 99.5) + 114.5)*Math.pow(y-((Math.pow(1.0,0.102134608795554))),2) + (0.0+59.123237864930005)*Math.pow((0.02983404536168486/574.4279185256106),2),-0.5)/Math.cos((1.0+0.130832951477089)))+(5.0425+((Math.cos(0.5543799712678396)*(x - 99.5) - Math.sin(0.5543799712678396)*(y - 114.5) + 99.5)/Math.cos(5.7064)))))))";
			fy = "((((((Math.pow((0.543107912500384*0.6600345344036482),(Math.pow((-1.8689936181924167),4.786848782575768))))-Math.pow(Math.sin((-7.3131))*Math.pow(x-(Math.sin((-0.4866))),2) + ((-24.627)+66.9595)*Math.pow(y-(((-45.907199999999996)/242.8972504225495)),2) + Math.pow(0.6567*Math.pow(x-(73.3249),2) + 0.6841*Math.pow(y-((-0.3788)),2) + 1.0*Math.pow((-0.4288),2),-0.5)*Math.pow(Math.pow(172.1852*Math.pow(x-(0.5167),2) + 42.6306*Math.pow(y-((-19.3072)),2) + 116.276*Math.pow((-0.0275),2),0.5),2),-0.5))+(((-0.2844)*(-0.1102))*(Math.cos((0.25073914527857194*(-0.1102)))*(x - 99.5) - Math.sin((0.25073914527857194*(-0.1102)))*(y - 114.5) + 99.5)))*(((-0.2844)*(-0.1102))*(((125.08568121066206/4.33534723089776)+(0.03134088*1.279018118458897))*(((-0.2844)*(-0.1102))*(Math.cos(0.9761)*(x - 99.5) - Math.sin(0.9761)*(y - 114.5) + 99.5)))))*((((((-0.914185710951877)+0.5501370107310615)*(0.03134088*17.553336432514385))*(((-0.2844)*(-0.1102))*(Math.cos(224.95658982670477)*(x - 99.5) - Math.sin(224.95658982670477)*(y - 114.5) + 99.5)))*(-0.1102))*(Math.cos((Math.cos(Math.pow(((-0.20027666370000774)*(-1.2694701404709103))*Math.pow(x-(((-0.1198)-(-0.2076))),2) + Math.sin(1.0)*Math.pow(y-((Math.cos(0.2429)*(y - 114.5) + Math.sin(0.2429)*(x - 99.5) + 114.5)),2) + (6.606751518132844+0.8414709436893656)*Math.pow(((-83.2974)+(-0.4445)),2),0.5))*(y - 114.5) + Math.sin(Math.pow(((-0.20027666370000774)*(-1.2694701404709103))*Math.pow(x-(((-0.1198)-(-0.2076))),2) + Math.sin(1.0)*Math.pow(y-((Math.cos(0.2429)*(y - 114.5) + Math.sin(0.2429)*(x - 99.5) + 114.5)),2) + (6.606751518132844+0.8414709436893656)*Math.pow(((-83.2974)+(-0.4445)),2),0.5))*(x - 99.5) + 114.5))*(x - 99.5) - Math.sin((Math.cos(Math.pow(((-0.20027666370000774)*(-1.2694701404709103))*Math.pow(x-(((-0.1198)-(-0.2076))),2) + Math.sin(1.0)*Math.pow(y-((Math.cos(0.2429)*(y - 114.5) + Math.sin(0.2429)*(x - 99.5) + 114.5)),2) + (6.606751518132844+0.8414709436893656)*Math.pow(((-83.2974)+(-0.4445)),2),0.5))*(y - 114.5) + Math.sin(Math.pow(((-0.20027666370000774)*(-1.2694701404709103))*Math.pow(x-(((-0.1198)-(-0.2076))),2) + Math.sin(1.0)*Math.pow(y-((Math.cos(0.2429)*(y - 114.5) + Math.sin(0.2429)*(x - 99.5) + 114.5)),2) + (6.606751518132844+0.8414709436893656)*Math.pow(((-83.2974)+(-0.4445)),2),0.5))*(x - 99.5) + 114.5))*(y - 114.5) + 99.5)))+(((((Math.cos(Math.sin(118.20357313856303))*(y - 114.5) + Math.sin(Math.sin(118.20357313856303))*(x - 99.5) + 114.5)/(((-0.2844)*(-0.1102))*(Math.cos(0.9761)*(x - 99.5) - Math.sin(0.9761)*(y - 114.5) + 99.5)))+(((-0.2844)*(-0.1102))*(Math.cos((0.25073914527857194*125.56708909278005))*(x - 99.5) - Math.sin((0.25073914527857194*125.56708909278005))*(y - 114.5) + 99.5)))*(((-0.2844)*(-0.1102))*(Math.cos(0.9761)*(x - 99.5) - Math.sin(0.9761)*(y - 114.5) + 99.5)))+((((-1.7277)-((0.29271146372670104*4.013482400326402)*((-0.027631453809698628)*(-39.94667225750197))))-(((0.3584699781581283+(-0.06575851443142727))*(0.03134088*119.7006514997995))*((0.2565055181788383*4.013482400326402)*((-0.027631453809698628)*(-39.94667225750197)))))-((((0.395673651968757+0.03134088)+(0.03134088*(-0.2844)))*(((-0.2844)*(-0.1102))*(Math.cos(1.324108598107337)*(x - 99.5) - Math.sin(1.324108598107337)*(y - 114.5) + 99.5)))*(((0.03134088*9.018642059660138)*(-0.1102))*(Math.cos((126.31107655026666+(-7.828025111602149)))*(x - 99.5) - Math.sin((126.31107655026666+(-7.828025111602149)))*(y - 114.5) + 99.5))))))";
			StringBuilder sb = new StringBuilder();
			double val = new GPStrategy(internalMode).CalculateRMSE(arr.get(0), arr.get(1), fx, fy, sb, true);
			*/
		
		//writeImage(arr.get(0), arr.get(1));
	
		// REAL
		if (realPoints == null)
		{
			if (pyramid && pop == null)
			{
				for (Point point : arr.get(0)) {
					point.x /= 2;
					point.y /= 2;
				}
				for (Point point : arr.get(1)) {
					point.x /= 2;
					point.y /= 2;
				}
			}
			alg.InitRealPoints(arr.get(0),arr.get(1));
		}
		else
		{
			alg.InitRealPoints(realPoints.get(0),realPoints.get(1));
		}
		
		// SIFT
		if (useReal)
		{
			alg.InitFeaturesPoints(arr.get(0),arr.get(1));
			
			IplImage src = cvLoadImage(path1,0);
			IplImage src2 = cvLoadImage(path2,0);
			
			
			short[][] featuresImg = new short[src.width()][];
			for (int i = 0; i < featuresImg.length; i++) {
				featuresImg[i] = new short[src.height()];
			}
			for (int i = 0; i < arr.get(0).size(); i++) {
				int x = (int) arr.get(0).get(i).x;
				int y = (int) arr.get(0).get(i).y;
				if (x > 0 && x < featuresImg.length && y > 0 && y < featuresImg[0].length)
					featuresImg[x][y] = 255;
			}
			Utils.writeImg(featuresImg,src.width(),src.height(), CONST.projectPath+"\\1.jpg");
			
			short[][] featuresImg2 = new short[src2.width()][];
			for (int i = 0; i < featuresImg2.length; i++) {
				featuresImg2[i] = new short[src2.height()];
			}
			for (int i = 0; i < arr.get(1).size(); i++) {
				int x = (int) arr.get(1).get(i).x;
				int y = (int) arr.get(1).get(i).y;
				if (x > 0 && x < featuresImg2.length && y > 0 && y < featuresImg2[0].length)
					featuresImg2[x][y] = 255;
			}
			Utils.writeImg(featuresImg2,src2.width(),src2.height(), CONST.projectPath+"\\2.jpg");
			
		}
		else if (1==1)
		{
			// SIFT
			/*ArrayList<ArrayList<Point>> points = LoadSIFTPoints(path1, path2);
			ArrayList<Point> p1 = new ArrayList<Point>();
			ArrayList<Point> p2 = new ArrayList<Point>();
			p1 = points.get(0);
			p2 = points.get(1);
			*/
			/*
			short[][] featuresImg = new short[Utils.img1.getWidth()][];
			for (int i = 0; i < featuresImg.length; i++) {
				featuresImg[i] = new short[Utils.img1.getHeight()];
			}
			for (int i = 0; i < p1.size(); i++) {
				int x = (int) p1.get(i).x;
				int y = (int) p1.get(i).y;
				if (x > 0 && x < featuresImg.length && y > 0 && y < featuresImg[0].length)
					featuresImg[x][y] = 255;
			}
			Utils.writeImg(featuresImg,Utils.img1.getWidth(),Utils.img1.getHeight(), "c:\\1.jpg");
			
			short[][] featuresImg2 = new short[Utils.img1.getWidth()][];
			for (int i = 0; i < featuresImg2.length; i++) {
				featuresImg2[i] = new short[Utils.img1.getHeight()];
			}
			for (int i = 0; i < p2.size(); i++) {
				int x = (int) p2.get(i).x;
				int y = (int) p2.get(i).y;
				if (x > 0 && x < featuresImg2.length && y > 0 && y < featuresImg2[0].length)
					featuresImg2[x][y] = 255;
			}
			Utils.writeImg(featuresImg2,Utils.img1.getWidth(), Utils.img1.getHeight(), "c:\\2.jpg");
			*/
			
			// wavelets
			ArrayList<Point> p1 = getFeaturesWavelets(path1);
			ArrayList<Point> p2 = getFeaturesWavelets(path2);
			System.out.println("Num of real points: " + p1.size());
			/*
			
			BufferedImage f1 = getFeaturesImageSobel(cvLoadImage(path1, CV_LOAD_IMAGE_GRAYSCALE));
			BufferedImage f2 = getFeaturesImageSobel(cvLoadImage(path2, CV_LOAD_IMAGE_GRAYSCALE));
			
			
			ArrayList<Point> p1 = new ArrayList<Point>();
			ArrayList<Point> p2 = new ArrayList<Point>();
			
			for (int i = 0; i < f1.getWidth(); i++) {
				for (int j = 0; j < f1.getHeight(); j++) {
					if(f1.getData().getSample(i, j,0) > 0)
					{
						p1.add(new Point(i,j));
					}
				}
			}
			for (int i = 0; i < f2.getWidth(); i++) {
				for (int j = 0; j < f2.getHeight(); j++) {
					if(f2.getData().getSample(i, j,0) > 0)
					{
						p2.add(new Point(i,j));
					}
				}
			}
			Utils.writeImg(f1, "c:\\1.jpg");
			Utils.writeImg(f2, "c:\\2.jpg");
			
			*/
			/*
			IplImage src = cvLoadImage(path1,0);
			IplImage src2 = cvLoadImage(path2,0);
			BufferedImage a = getFeaturesImageSobel(src);
			BufferedImage a2 = getFeaturesImageSobel(src2);
			
			Utils.writeImg(a, "c:\\1.jpg");
			Utils.writeImg(a2, "c:\\2.jpg");
			
			ArrayList<ArrayList<Point>> p = GetFeaturePoints(a,a2);
			*/
			
			alg.InitFeaturesPoints(p1, p2);
		}
		else
		{
			/*if (1==1) // A-SIFT
			{

				ArrayList<ArrayList<Point>> p = ReadRealPoints("C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\CHIPS_DC_AREA\\b\\0002_0006\\real_scale.txt");
				alg.InitSIFTPoints(p.get(0),p.get(1));
			}
			else*/
			{
				
				ArrayList<Point> p1 = new ArrayList<Point>();
				ArrayList<Point> p2 = new ArrayList<Point>();
			
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				if (notUsingRealStrategy == 2)
				{
					ArrayList<Point> p1t = new ArrayList<Point>();
					ArrayList<Point> p2t = new ArrayList<Point>();
					/*
					for (int i = 0; i < arr.get(0).size(); i++) {
						System.out.println(arr.get(0).get(i).x + "," + arr.get(0).get(i).y + " -- " + arr.get(1).get(i).x + "," + arr.get(1).get(i).y);
					}
					System.out.println("---------------------");
					*/
					
					ArrayList<Point> points1 = arr.get(0);
					//short[][] firstArr = new short[300][300];
					//for (int i = 0; i < points1.size(); i++) {
					//	firstArr[(int) points1.get(i).x][300-(int) points1.get(i).y-1] = 255;
					//}
					//BufferedImage first = Utils.getImg(firstArr, 300, 300);
					BufferedImage first = Utils.original1;
					
					Mat srcMat1 = getImgMat(first);
					//Utils.writeImg(first, "C:\\Users\\Sarit\\workspace\\Results\\GP\\1.jpg");
					MatOfKeyPoint kp1 = new MatOfKeyPoint();// getKeyPoints(srcMat1, points1);
					List<org.opencv.features2d.KeyPoint> list1 = new ArrayList<org.opencv.features2d.KeyPoint>();
					for (int i = 0; i < points1.size(); i++) {
						list1.add(new org.opencv.features2d.KeyPoint((float)points1.get(i).x+1, (float)points1.get(i).y+1, 3f, -1, 0, 0, -1));
						//kp1.put(i, 0, points1.get(i).x);
						//kp1.put(i, 1, points1.get(i).y);

					}
					kp1.fromList(list1);
					Mat desc1 = getDescriptors(srcMat1, kp1);
				
					ArrayList<Point> points2 = arr.get(1);
					//short[][] secArr = new short[300][300];
					//for (int i = 0; i < points2.size(); i++) {
					//	secArr[(int) points2.get(i).x][300-(int) points2.get(i).y-1] = 255;
					//}
					//BufferedImage sec = Utils.getImg(secArr, 300, 300);
					BufferedImage sec = Utils.original2;
					
					Mat srcMat2 = getImgMat(sec);
					//Utils.writeImg(sec, "C:\\Users\\Sarit\\workspace\\Results\\GP\\2.jpg");
					MatOfKeyPoint kp2 = new MatOfKeyPoint();// getKeyPoints(srcMat1, points1);
					List<org.opencv.features2d.KeyPoint> list2 = new ArrayList<org.opencv.features2d.KeyPoint>();
					for (int i = 0; i < points2.size(); i++) {
						list2.add(new org.opencv.features2d.KeyPoint((float)points2.get(i).x+1, (float)points2.get(i).y+1, 3f, -1, 0, 0, -1));
					}
					kp2.fromList(list2);
					Mat desc2 = getDescriptors(srcMat2, kp2);
				
					ArrayList<IFeaturePoint> features1 = LoadDescriptorsPoints(desc1);
					ArrayList<IFeaturePoint> features2 = LoadDescriptorsPoints(desc2);
					
					List<Double> diff = new ArrayList<Double>();
					List<Double> diffSort = new ArrayList<Double>();
					boolean[] used = new boolean[features2.size()];
					for (int i = 0; i < features1.size(); i++) {
						p1t.add(new Point(kp1.get(i, 0)[0],kp1.get(i, 0)[1]));
					
						double dist = 10e10;
						Point match = new Point();
						int index = 0;
						for (int j = 0; j < features2.size(); j++) {
						//	if (!used[j])
							{
								double tmp = features1.get(i).distance(features2.get(j));
								if (tmp < dist)
								{
									index = j;
									dist = tmp;
									match.x = kp2.get(j, 0)[0];
									match.y = kp2.get(j, 0)[1];
								}
							}
						}
						
						p2t.add(match);
						used[index] = true;
						System.out.println(String.format("%d,%d (%f)", i, index, dist));
						double tmp = Math.sqrt(Math.pow(match.x-p1t.get(i).x, 2) + Math.pow(match.y-p1t.get(i).y, 2));
						diff.add(tmp);
						diffSort.add(tmp);
					}
					
					
					////////// outliers
					/*
					java.util.Collections.sort(diffSort);
					double medianDiff = 0;		
					int index = (int)(diffSort.size()*0.5);
					if (diffSort.size() % 2 == 0)
						medianDiff = ((double)diffSort.get(index) + (double)diffSort.get(index - 1))/2;
					else
						medianDiff = (double) diffSort.get(index);
					*/
					
					for (int i = 0; i < diff.size(); i++) {
						//if (diff.get(i) <= medianDiff)
						{
							p1.add(new Point(p1t.get(i).x, p1t.get(i).y));
							p2.add(new Point(p2t.get(i).x, p2t.get(i).y));
						}
					}

					
					//////////////
					
					for (int i = 0; i < p1t.size(); i++) {
						if (p2t.get(i).x != points2.get(i).x ||
								p2t.get(i).y != points2.get(i).y)
						{
							double dist = Math.sqrt(Math.pow(p2t.get(i).x-points2.get(i).x, 2) + Math.pow(p2t.get(i).y-points2.get(i).y, 2));
							double tmp = Math.sqrt(Math.pow(p2t.get(i).x-p1t.get(i).x, 2) + Math.pow(p2t.get(i).y-p1t.get(i).y, 2));
							System.out.println(i + " (" + tmp + ") ("+ dist + "):" +  p1t.get(i).x + "," + p1t.get(i).y + " -- " + p2t.get(i).x + "," + p2t.get(i).y);
						}
					}
					
					System.out.println("---------------------");
					
					
						
					
				}
				else if (notUsingRealStrategy == 1)
				{
					///////////// 1
					
					//BufferedImage im11 = convertHarrisCorner(cvLoadImage(path1, 0), "out1.jpg");
					//BufferedImage im22 = convertHarrisCorner(cvLoadImage(path2, 0), "out2.jpg");
					
					ArrayList<Point> points1 = arr.get(0);
					short[][] fArr = new short[300][300];
					for (int i = 0; i < points1.size(); i++) {
						fArr[(int) points1.get(i).x][300-(int) points1.get(i).y-1] = 255;
					}
					BufferedImage first = Utils.getImg(fArr, 300, 300);
					
					ArrayList<Point> points2 = arr.get(1);
					short[][] sArr = new short[300][300];
					for (int i = 0; i < points2.size(); i++) {
						sArr[(int) points2.get(i).x][300-(int) points2.get(i).y-1] = 255;
					}
					BufferedImage sec = Utils.getImg(sArr, 300, 300);
					
					Mat[] desc1  = getDescriptors(first, first);//Utils.original1, Utils.img1);
					Mat[] desc2  = getDescriptors(sec, sec);//Utils.original2, Utils.img2);
					
					MatOfKeyPoint kp1 = (MatOfKeyPoint)desc1[0];
					MatOfKeyPoint kp2 = (MatOfKeyPoint)desc2[0];
					
					ArrayList<IFeaturePoint> features1 = LoadDescriptorsPoints(desc1[1]);
					ArrayList<IFeaturePoint> features2 = LoadDescriptorsPoints(desc2[1]);
					
					boolean[] used = new boolean[features2.size()];
					for (int i = 0; i < features1.size(); i++) {
						
						p1.add(new Point(kp1.get(i, 0)[0],kp1.get(i, 0)[1] ));
					
						double dist = 10e10;
						Point match = new Point();
						int index = 0;
						for (int j = 0; j < features2.size(); j++) {
							if (!used[j])
							{
								double tmp = features1.get(i).distance(features2.get(j));
								if (tmp < dist)
								{
									index = j;
									dist = tmp;
									match.x = kp2.get(j, 0)[0];
									match.y = kp2.get(j, 0)[1];
								}
							}
						}						
						p2.add(match);
						used[index] = true;
					}
						
				}
				else if (notUsingRealStrategy == 0)
				{
					///////////// 2
					ArrayList<ArrayList<Point>> points = LoadSIFTPoints(path1, path2);
					p1 = points.get(0);
					p2 = points.get(1);
				}
				
				alg.InitFeaturesPoints(p1, p2);
			}
		}
		
		
	
		
		/*
		String x = "((-5.3)+x)";
		String y = "((Math.cos(((((Math.cos(((Math.cos((53.27)%(2.0*Math.PI))/((55.84/(13.04*(86.36-Math.sin((Math.cos((((-12.18)*70.67))%(2.0*Math.PI)))%(2.0*Math.PI)))))-(((-2.07)/Math.cos(((-6.05))%(2.0*Math.PI)))*((176.75+(-8.8))+6.62)))))%(2.0*Math.PI))+(-3.76))*Math.cos((((-76.72)/(((34.16+36.44)/(-46.05))-(-1.32))))%(2.0*Math.PI)))/44.12))%(2.0*Math.PI))*(y - 128.0) + Math.sin(((((Math.cos(((Math.cos((53.27)%(2.0*Math.PI))/((55.84/(13.04*(86.36-Math.sin((Math.cos((((-12.18)*70.67))%(2.0*Math.PI)))%(2.0*Math.PI)))))-(((-2.07)/Math.cos(((-6.05))%(2.0*Math.PI)))*((176.75+(-8.8))+6.62)))))%(2.0*Math.PI))+(-3.76))*Math.cos((((-76.72)/(((34.16+36.44)/(-46.05))-(-1.32))))%(2.0*Math.PI)))/44.12))%(2.0*Math.PI))*(x - 128.0) + 128.0)-45.35)";
		
		x = "(((((Math.cos((Math.cos(Math.cos(218.45))+((Math.cos(((-0.15423570627198702)/68.02))*(x - 128.0) - Math.sin(((-0.15423570627198702)/68.02))*(y - 128.0) + 128.0)+57.22)))*(x - 128.0) - Math.sin((Math.cos(Math.cos(218.45))+((Math.cos(((-0.15423570627198702)/68.02))*(x - 128.0) - Math.sin(((-0.15423570627198702)/68.02))*(y - 128.0) + 128.0)+57.22)))*(y - 128.0) + 128.0)+((Math.cos((-121.23))+(-1.95))-((-30.72)*(78.84-(Math.sin((-0.8400118252807882))/(-92.02))))))/(Math.cos(Math.sin(((-97.37)+(-72.95))))*(y - 128.0) + Math.sin(Math.sin(((-97.37)+(-72.95))))*(x - 128.0) + 128.0))/(-41.71))+(x+(-41.71)))";
		y = "(((-41.71)+y)+((-3.96)-(Math.sin(((-41.71)-Math.cos(10.32)))-Math.cos(10.32))))";
		x = "((((rotate0(58.92)+((Math.cos(-121.23)+(-1.95))-((-30.72)*(78.84-(Math.sin(-0.84)/(-92.02))))))/rotate1(-0.62))/(-41.71))+(x+(-41.71)))";
		double test = GeneticAlgorithm.CalculateRMSE(real1, real2, x, y, null, false);
		*/

		boolean mustreset = pyramid && pop == null;
		mustreset = true;
		alg.Init(pop, mustreset);
		
		
		//RunRMSETest(strategy);
		
		/*Chromosome ch = new GAChromosome().getTestChromosome();
		alg.calculateTransform(ch, false);
		double fitness = alg.CalculateFitnessMI(ch);
		*/
		
		alg.Run();
		internalMode = alg.GetMode();
		// write result
		writeResult(alg.GetSolution().toString());
		}
		catch(Exception ex)
		{
			alg.Log(ex.getMessage() + " # " + ex.getStackTrace(), 0);
			throw ex;
		}

		//Chromosome winnerChromosome = alg.GetSolution();
		Chromosome winnerChromosome = ((GeneticAlgorithm)alg).GetBest(alg.GetPop());

		for (FminsearchOptimizer.Method method : FminsearchOptimizer.Method.values()) {

			long beforeFminsearch = System.currentTimeMillis();
			Chromosome winnerChromosome2 = FminsearchOptimizer.run(winnerChromosome, method, path1, path2,	alg, fileOption, output.getName(), projectPath);
			long afterFminsearch = System.currentTimeMillis();
			ResultsDbManager.writeResultSummaryToCSV(alg, winnerChromosome, winnerChromosome2, fileOption, method ,useReal, afterFminsearch - beforeFminsearch);
		}

		System.out.println("this is quite the end");
		return alg.GetPop();
	}
	
	private static ArrayList<IFeaturePoint> LoadDescriptorsPoints(Mat desc)
	{
		
		ArrayList<IFeaturePoint> descriptors = new ArrayList<IFeaturePoint>();
		for (int i = 0; i < desc.rows(); i++) {
			
			double[] features = new double[desc.cols()];
			for (int j = 0; j < desc.cols(); j++) {
				features[j] = desc.get(i, j)[0];
			}
			descriptors.add(new FeaturePoint(features));
		}
				
		return descriptors;
	}
	
	private static ArrayList<ArrayList<Point>> LoadSIFTPoints(String path1, String path2) throws Exception
	{
		InitMatlab();
		
		String s;
		if (_simpleSIFT)
			s = "[n,match2, matches, loc1, loc2] = match('" + path1 + "','" + path2 + "');";
		else
			s = "[TransX,TransY,Theta,Scale,Result, RefPairs, SensedPairs] = MS_SIFT_MY_2('" + path1 + "','" + path2 + "');";
		
		CONST.Myproxy.eval(s);
						
		ArrayList<Point> p1 = new ArrayList<Point>();
		ArrayList<Point> p2 = new ArrayList<Point>();
		
		if (_simpleSIFT)
		{
			double[] matches = ((double[]) CONST.Myproxy.getVariable("matches"));
			int rows = matches.length/4;
			for (int i = 0; i < rows; i++) {
				p1.add(new Point(matches[i+rows],matches[i]));
				p2.add(new Point(matches[i+3*rows],matches[i+2*rows]));
				System.out.println(matches[i+rows]+","+matches[i] + "--"+matches[i+3*rows]+","+matches[i+2*rows]);
			}
		}
		else
		{
			double[] ref = ((double[]) CONST.Myproxy.getVariable("RefPairs"));
			double[] sensed = ((double[]) CONST.Myproxy.getVariable("SensedPairs"));
			
			int rows = ref.length/2;
			if (ref.length < 10)
			{
				System.out.println("NOT ENOUGH POINTS !!");
				throw new Exception("Bla bla bla ...");
			}
			
			for (int i = 0; i < rows; i++) {
				p1.add(new Point(ref[i] //+ Utils.img1.getWidth()
						,ref[i+rows]));//+ Utils.img1.getHeight()));
				p2.add(new Point(sensed[i]//+ Utils.img1.getWidth()
						,sensed[i+rows]));//+ Utils.img1.getHeight()));
				//System.out.println(ref[i][0]+","+ref[i][1] + "--"+sensed[i][0]+","+sensed[i][1]);
			}
		}
		
		ArrayList<ArrayList<Point>> points = new ArrayList<ArrayList<Point>>();
		points.add(p1);
		points.add(p2);
		return points;
		
	}
	
	private static ArrayList<ArrayList<Point>> LoadRegistrationDataSynthetic()
	{		
		ArrayList<ArrayList<Point>> res = null;
		
		
		
		switch (fileOption) {
		case b:
			res = Points.get_b();
			break;
		case trans:
			res = Points.get_trans();
			break;
		case rot:
			res = Points.get_rot();
			break;
		case scale:
			res = Points.get_Scale();
			break;
		case shearX:
			res = Points.get_ShearX();
			break;
		case reflection:
			res = Points.get_Reflection();
			break;
		case combined:
			res = Points.get_Combined();
			break;
		case example8:
			res = Points.get_Example8();
			break;
		case c:
			res = Points.get_c();
			break;
		case d:
			res = Points.get_d();
			break;
		case e:
			res = Points.get_e();
			break;
		case f:
			res = Points.get_f();
			break;
		case g:
			res = Points.get_g();
			break;
		case h:
			res = Points.get_h();
			break;
		case file_95:
			res = Points.get_59();
			break;
		case Colorado_7:
			res = Points.get_Colorado7();
			break;
		case a:
			res = Points.get_a();
			break;
		case Misc_a:
			res = Points.get_Misc_a();
			break;
		case Misc_b:
			res = Points.get_Misc_b();
			break;
		case Misc_c:
			res = Points.get_Misc_c();
			break;
		case Misc_d:
			res = Points.get_Misc_d();
			break;
		case Misc_e:
			res = Points.get_Misc_e();
			break;
		case Misc_f:
			res = Points.get_Misc_f();
			break;
		case Misc_g:
			res = Points.get_Misc_g();
			break;
		case Misc_h:
			res = Points.get_Misc_h();
			break;
		case Misc_i:
			res = Points.get_Misc_i();
			break;
		case Misc_j:
			res = Points.get_Misc_j();
			break;
		case Misc_k:
			res = Points.get_Misc_k();
			break;
		case Misc_l:
			res = Points.get_Misc_l();
			break;
		case Misc_m:
			res = Points.get_Misc_m();
			break;
		case Misc_n:
			res = Points.get_Misc_n();
			break;
		case Misc_o:
			res = Points.get_Misc_o();
			break;
		case boat1:
			res = Points.get_boat1();
			break;
		case boat2:
			res = Points.get_boat2();
			break;
		case bark:
			res = Points.get_boat1();
			break;
		case eastSouth:
			res = Points.get_eastSouth();
			break;
		case graffiti:
			res = Points.get_graffiti();
			break;
		case test:
			res = Points.get_dazu();
			break;
		default:
			ArrayList<Point> real1 = new ArrayList<Point>();
			ArrayList<Point> real2 = new ArrayList<Point>();	
			ArrayList<ArrayList<Point>> arr = new ArrayList<ArrayList<Point>>();
			arr.add(real1);
			arr.add(real2);
			res = arr; 
			break;
		}
		
		//res = BenchmarkPoints.getPointSetReal1();
		//res = BenchmarkPoints.get_PointSet2();
		//res = NonAffineBenchmarkPoints.get_PointSet1();
		
		
		return res;
	}
	
	private static PopulationManager RunGeneticAlgorithm(IGeneticStrategy strategy, String path1, String path2, File output, PopulationManager pop) throws Exception
	{
			
		//InitMatlab();
		if (internalMode == InternalMode.HD || internalMode == InternalMode.HD_ALL ||
				internalMode == InternalMode.MultiObjective || internalMode == InternalMode.MultiObjectiveHD)
		{
			InitMatlab();
		}
		
		readImages(output, path1, path2);
		
		
		SelectionMode selection = null;
		if (internalMode == InternalMode.HD || internalMode == InternalMode.HD_ALL || internalMode == InternalMode.MI)
			selection = SelectionMode.TournamentSelection;
		else if (internalMode == InternalMode.MultiObjective || internalMode == InternalMode.MultiObjectiveMI || internalMode == InternalMode.MultiObjectiveHD)
			selection = SelectionMode.NSGARankedBasedSelection;
		
		
		GeneticAlgorithm alg = new GeneticAlgorithm(strategy, output, selection, internalMode, CONST._isCentered);
		
		ArrayList<ArrayList<Point>> arr = null;//LoadRegistrationData(); arr.clear();
		arr = LoadRegistrationDataSynthetic();
		alg.InitRealPoints(arr.get(0),arr.get(1));
		
		ArrayList<Point> f1 = getFeaturesWavelets(path1);
		ArrayList<Point> f2 = getFeaturesWavelets(path2);
		//alg.InitFeaturesPoints(arr.get(0),arr.get(1));
		alg.InitFeaturesPoints(f1,f2);
		
		boolean mustreset = pyramid && pop == null;
		alg.Init(pop, mustreset);
		alg.Run();
		internalMode = alg.GetMode();
		// write result
		writeResult(alg.GetSolution().toString());
		
		if (CONST.Myproxy != null)
			CONST.Myproxy.disconnect();
		
		return alg.GetPop();
	}
	
	private static ArrayList<ArrayList<Point>> GetFeaturePoints(BufferedImage im1, BufferedImage im2)
	{
		ArrayList<Point> ref = new ArrayList<Point>();
		for (int i = 0; i < im1.getWidth(); i++) {
			for (int j = 0; j <im1.getHeight(); j++) {
				if (im1.getData().getSample(i, j, 0) > 200)
				{
					ref.add(new Point(i,j));
				}
				
			}
		}
		
		ArrayList<Point> sensed = new ArrayList<Point>();
		for (int i = 0; i < im2.getWidth(); i++) {
			for (int j = 0; j <im2.getHeight(); j++) {
				if (im2.getData().getSample(i, j, 0) > 200)
				{
					sensed.add(new Point(i,j));
				}
			}
		}
		
		ArrayList<ArrayList<Point>> f = new ArrayList<ArrayList<Point>>();
		f.add(ref);
		f.add(sensed);
		return f;
	}
	
	private static void InitMatlab() throws Exception
	{
		MatlabProxyFactory factory = new MatlabProxyFactory();
		CONST.Myproxy = factory.getProxy();
		if (internalMode == InternalMode.HD || internalMode == InternalMode.HD_ALL || internalMode == InternalMode.MultiObjectiveHD)
		{
			CONST.Myproxy.eval("addpath('C:\\Users\\Sarit\\workspace\\Image_Registration_GP')");
		}
		if (internalMode == InternalMode.SIFT || internalMode == InternalMode.SIFT_MOO || internalMode == InternalMode.SIFT_MOO2)
		{
		if (_simpleSIFT)
			CONST.Myproxy.eval("addpath('C:\\SIFT\\SIFT_SIMPLE')");
		else
			CONST.Myproxy.eval("addpath('C:\\SIFT\\MS_SIFT_Distrib\\Distrib')");
		}
		CONST.processor = new MatlabTypeConverter(CONST.Myproxy);
	}
	
	private static void RunRMSETest(IGeneticStrategy strategy) throws Exception
	{
		
		List<RMSEData> list = readFile();
		StringBuilder results = new StringBuilder();
		for (RMSEData data : list) {
			String path1 = data.path1;
			String path2 = data.path2;
			
			// read input images
			//##BufferedImage[] imgs =
			File a = new File("");
			readImages(a, path1, path2);	
			//##if (imgs.length != 4)
				//##throw new Exception("Invalid number of images !!!");
		
		    data.fx = Utils.fixTransformation(data.fx, (int)Utils.img1.getWidth(), (int)Utils.img1.getHeight(), (int)Utils.img2.getWidth(), (int)Utils.img2.getHeight(), 0, CONST._isCentered);
			data.fy = Utils.fixTransformation(data.fy, (int)Utils.img1.getWidth(), (int)Utils.img1.getHeight(), (int)Utils.img2.getWidth(), (int)Utils.img2.getHeight(), 0, CONST._isCentered);
			results.append(data.title);
			results.append("\n");
			results.append(data.path1);
			results.append("\n");
			results.append(data.path2);
			results.append("\n");
			results.append(data.fx);
			results.append("\n");
			results.append(data.fy);
			results.append("\n");
			
			System.out.println(data.title);
			//##double rmse = GeneticAlgorithm.CalculateRMSE(imgs[0], imgs[2], data.points1, data.points2,  data.fx, data.fy, results);
			double rmse = GPStrategy.CalculateRMSE(//Utils.original1.getWidth(), 
													//Utils.original1.getHeight(),
													data.points1, data.points2, data.fy , data.fx, results, true);
			String s = "RMSE (" + data.points1.size() + " points): " + rmse;
			System.out.println(s);
		
			results.append(s);
			results.append("\n------------------------\n");
		} 
		writeResult(results.toString());
	}
	
	
	
	public static void readImages(File outputPath, String path1, String path2) throws IOException, MatlabInvocationException { 
	//##BufferedImage[] readImages(String path1, String path2) throws IOException {
		
		IplImage src = cvLoadImage(path1,0);
		cvSaveImage(outputPath.getAbsolutePath() + File.separatorChar + "img1.jpg", src);
		
		IplImage src2 = cvLoadImage(path2,0);
		cvSaveImage(outputPath.getAbsolutePath() + File.separatorChar + "img2.jpg", src2);
		
		BufferedImage im11  = null;
		BufferedImage im22 = null;
		if ((mode == Mode.GP || mode == Mode.GA) && 
				(internalMode == InternalMode.HD || 
				internalMode == InternalMode.HD_ALL || 
				internalMode == InternalMode.MultiObjective || 
				internalMode == InternalMode.MultiObjectiveMI || 
				internalMode == InternalMode.MultiObjectiveHD))
		{
			
			im11 = getFeaturesImageWavelets(path1);
			im22 = getFeaturesImageWavelets(path2);
			
			if (internalMode == InternalMode.MultiObjectiveMI)
			{
				/*
			
			Mat desc1 = getDescriptors(src, im11);
			Mat desc2 = getDescriptors(src2, im22);
						
				
				Mat matches1 = getNearestNeighbour(desc1, desc2);
				for (int i = 0; i < matches1.height(); i++) {
					int index = (int) matches1.get(i, 0)[0];
					System.out.println("(" + kp1.get(i, 0)[1] + "," + kp1.get(i, 0)[0] + ") --> (" + kp2.get(index, 0)[1] + "," + kp2.get(index, 0)[0] + ")");
				}
				System.out.println("--------------------------");
				Mat matches2 = getNearestNeighbour(desc2, desc1);
				for (int i = 0; i < matches2.height(); i++) {
					int index = (int) matches2.get(i, 0)[0];
					System.out.println("(" + kp1.get(i, 0)[1] + "," + kp1.get(i, 0)[0] + ") --> (" + kp2.get(index, 0)[1] + "," + kp2.get(index, 0)[0] + ")");
				}
				*/
			}
			
		}
		else
		{
			im11 = src.getBufferedImage();
			im22 = src2.getBufferedImage();
		}
		
		String outPath1 = outputPath.getAbsolutePath() + File.separatorChar + "out1.jpg";
		String outPath2 = outputPath.getAbsolutePath() + File.separatorChar + "out2.jpg";
	
		//cvSaveImage(outPath1, im11);
		//cvSaveImage(outPath2, im22);
		
		Utils.writeImg(im11, outPath1) ;
		Utils.writeImg(im22, outPath2);
		
		
		Utils.original1 = src.getBufferedImage();//.getData();
		Utils.original2 = src2.getBufferedImage();//.getData();
		
		Utils.img1 = im11;//.getData();
		Utils.img2 = im22;//.getData();
	}
	
	private static BufferedImage getFeaturesImageHarris(IplImage src) throws IOException, MatlabInvocationException
	{
	
		BufferedImage img = convert(src, "");
		return img;
	}
	
	private static BufferedImage getFeaturesImageSobel(IplImage src) throws IOException
	{
	
		BufferedImage img = convertHarrisCorner(src, "");
		//String tmp = "c:\\tmp.jpg";
		//Utils.writeImg(img, tmp);
		//BufferedImage r = cvLoadImage("c:\\tmp1.jpg", CV_LOAD_IMAGE_GRAYSCALE).getBufferedImage();
		
		for (int i = 0; i < img.getWidth(); i++) {
			System.out.println();
			for (int j = 0; j < img.getHeight(); j++)
			{
				if (img.getRaster().getSample(i, j, 0) < 230)
					img.getRaster().setSample(i, j, 0, 0);
				//System.out.print(img.getRaster().getSample(i, j, 0) + ",");
			}
			System.out.println();
		}
		System.out.println("");
		return img;//getTopFeaturesImage(img, 0.01);
	}
	
	private static BufferedImage getFeaturesImageNone(IplImage src) throws IOException
	{
	
		BufferedImage img = src.getBufferedImage();
		return img;
	}
	
	private static BufferedImage getFeaturesImageWavelets(String path) throws IOException
	{
	
		IplImage img = cvLoadImage(path.replaceFirst(".jpg", "_.jpg"),0);
		BufferedImage imgB = img.getBufferedImage();
		return getTopFeaturesImage(imgB, 0.01);
		
	}
	
	private static BufferedImage getTopFeaturesImage(BufferedImage img, double percent) throws IOException
	{
	
		short[][] features = WaveletFilter.getFeatures(img, percent);
		for (int i = 0; i < features.length; i++) {
			for (int j = 0; j < features[0].length; j++) {
				//if (features[i][j] > 0)
				{
					img.getRaster().setSample(i,j, 0, 255);			
				}
				
			}
		}
		
		
		
		return Utils.getImg(features, img.getWidth(), img.getHeight());		
		
	}
	
	private static ArrayList<Point> getFeaturesWavelets(String path) throws IOException
	{
	
		IplImage img = cvLoadImage(path.replaceFirst(".jpg", "_.jpg"),0);
		BufferedImage imgB = img.getBufferedImage();
		
		short[][] features = WaveletFilter.getFeatures(imgB, 0.01);
		ArrayList<Point> f = new ArrayList<Point>();
		
		
		for (int i = 0; i < features.length; i++) {
			for (int j = 0; j < features[0].length; j++) {
				if (features[i][j] > 0)
				{
					f.add(new Point(i,j));
				}
			}
		}
				
		Utils.writeImg(features, imgB.getWidth(), imgB.getHeight(), path.replaceFirst(".jpg", "_111.jpg"));
		return f;
		
	}
	
	private static Mat[] getDescriptors(BufferedImage src, BufferedImage features) throws IOException
	{
		Mat srcMat = getImgMat(src);		
		
		MatOfKeyPoint kp = getKeyPoints(srcMat, features);
		
		Mat desc = getDescriptors(srcMat, kp);
		return new Mat[] {kp,desc};
	}
	
	private static Mat getImgMat(BufferedImage img)
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Mat imgMat = Mat.zeros(img.getHeight(),  img.getWidth(),opencv_core.CV_8UC1);
		Raster data = img.getRaster();
		for (int row = 0; row <img.getHeight() ; row++) {
			for (int col = 0; col < img.getWidth(); col++) {
				imgMat.put(row, col, data.getSample(col, row, 0));	
			}
		}
		return imgMat;
	}
	
	private static Mat getNearestNeighbour(Mat src, Mat dest)
	{
		Mat res = Mat.zeros(src.height(), 1, opencv_core.CV_8UC1);
		
		for (int l = 0; l < src.height(); l++) {
			double bestDist = -1;
			double bestIndex = -1;
			for (int i = 0; i < dest.height(); i++) {
				double dist = 0;
				for (int j = 0; j < dest.width(); j++) {
					dist += Math.pow(dest.get(i,j)[0]-src.get(l,j)[0], 2);
				}
				if (bestIndex == -1 || bestDist > dist)
				{
					bestDist = dist;
					bestIndex = i;
				}
			}
			res.put(l, 0, bestIndex);
		}
		
		for (int i = 0; i < res.height(); i++) {
			System.out.print(res.get(i, 0)[0] + ",");
		}
		System.out.println();
		return res;
	}
	
	private static boolean print = false;
	private static MatOfKeyPoint getKeyPoints(Mat imgMat, BufferedImage featurePoints) throws IOException
	{

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		FeatureDetector d = FeatureDetector.create(FeatureDetector.HARRIS);
		int rows = imgMat.height();
		int cols = imgMat.width();
		
		//////////////// mask
	
		Mat mask = Mat.zeros(rows, cols,opencv_core.CV_8UC1);
		Raster maskData = featurePoints.getRaster();
		for (int row = 0; row <rows ; row++) {
			for (int col = 0; col < cols; col++) {
				double val = maskData.getSample(col, row, 0);
				if (val > 0)
				{
					mask.put(row, col, val);
					/*
					for (int i = Math.max(row-10, 0); i < Math.min(row+10, rows); i++) {
						for (int j = Math.max(col-10, 0); j < Math.min(col+10, cols); j++) {
							mask.put(i, j, val);
						}
					}
					*/
				}
			}
		}
		
		//mask = Scalar(255,255,255);
		//Mat roi(mask, cv::Rect(10,10,100,100));
		
		
		//////////////// detect
		
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		/*
		x - x-coordinate of the keypoint
		y - y-coordinate of the keypoint
		_size - keypoint diameter
		_angle - keypoint orientation
		_response - keypoint detector response on the keypoint (that is, strength of the keypoint)
		_octave - pyramid octave in which the keypoint has been detected
		_class_id - object id
		*/
		d.detect(imgMat, keypoints, mask);
		
		if (print)
		{
			for (int row = 0; row <keypoints.rows() ; row++) {
				for (int col = 0; col < keypoints.cols(); col++) {
					for (int i = 0; i < keypoints.get(row, col).length; i++) {
						System.out.print(keypoints.get(row, col)[i]+";");	
					}			
					System.out.print(",");
				}
				System.out.println(",");
			}
			
			System.out.println("--------------------------");
			
			short[][] b = new short[featurePoints.getWidth()][featurePoints.getHeight()];
			for (int i = 0; i < keypoints.rows(); i++) {
				double[] vals = keypoints.get(i, 0);
				b[(int)vals[0]][(int)vals[1]] = 255;
			}
			int in = 0;
			Utils.writeImg(b, b.length, b[0].length, "C:\\Users\\Sarit\\workspace\\Results\\GA\\temp" + in + ".jpg");
		}
		return keypoints;
		
	}
	
	private static Mat getDescriptors(Mat imgMat, MatOfKeyPoint keypoints)
	{
		Mat descriptors = new Mat();
		DescriptorExtractor e = DescriptorExtractor.create(DescriptorExtractor.SIFT);	
		e.compute(imgMat, keypoints, descriptors);
		for (int row = 0; row <descriptors.rows() ; row++) {
			for (int col = 0; col < descriptors.cols(); col++) {
				for (int i = 0; i < descriptors.get(row, col).length; i++) {
					if (print)
					System.out.print(descriptors.get(row, col)[i]+";");	
				}
				if (print)
				System.out.print(",");
			}
			if (print)
			System.out.println(",");
		}
		
		
		return descriptors;
	}

	static int blockSize = 3;//2
	static int apertureSize = 9;//21;//9
	static double k = 0.07;//0.1;//0.07
	static int threshold = 200; // todo: dynamic	
	private static BufferedImage convertHarrisCorner(IplImage src, String name) throws IOException
	{
		CvMat resMat = CvMat.create(src.height(), src.width(), com.googlecode.javacv.cpp.opencv_core.CV_32FC1, 1);
		//IplImage res = com.googlecode.javacv.cpp.opencv_core.cvCreateImage(com.googlecode.javacv.cpp.opencv_core.cvGetSize(src), src.depth() , 1);
		//cvConvertImage(res, res, com.googlecode.javacv.cpp.opencv_core.CV_32FC1);
		//cvConvertImage(res, res, 5);
		com.googlecode.javacv.cpp.opencv_imgproc.cvCornerHarris(src, resMat, blockSize, apertureSize, k);
		//com.googlecode.javacv.cpp.opencv_imgproc.cvThreshold(resMat, resMat, threshold, 255, com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY);
		com.googlecode.javacv.cpp.opencv_imgproc.cvDilate(resMat, resMat, null, 2);
		
		
		BufferedImage res = new BufferedImage(src.width(), src.height(), BufferedImage.TYPE_BYTE_GRAY);
		
		for (int i = 0; i < src.width(); i++) {
			for (int j = 0; j < src.height(); j++) {
				//if (resMat.get(j, i) > 200)
					res.getRaster().setSample(i, j, 0, resMat.get(j, i));
			}
		}
        //res.getRaster().setDataElements(0, 0, src.width(), src.height(), resMat);
        
		return res;
	}
	
	private static BufferedImage convert(IplImage src, String name) throws IOException, MatlabInvocationException
	{
		int w = src.getBufferedImage().getWidth();
		int h = src.getBufferedImage().getHeight();
		double[][] a1 = new double[h][];
		for (int j = 0; j < h; j++) {
			a1[j] = new double[w];
		for (int i = 0; i < w; i++) {
				a1[j][i] = src.getBufferedImage().getData().getSample(i,j, 0);
			}
		}
		MatlabNumericArray arrB = new MatlabNumericArray(a1,null);
	
		CONST.processor.setNumericArray("Src",arrB);
		CONST.Myproxy.eval("I = edge(Src, 'sobel');");
		boolean[] result2 = ((boolean[]) CONST.Myproxy.getVariable("I"));
		
		//IplImage img = cvCreateImage(cvGetSize(src), src.depth(), 1);
		short[][] img = new short[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (result2[i*h + j])
					img[i][j] = 255;//img.getBufferedImage().setRGB(i, j, 255);
				else
					img[i][j] =	0;//img.getBufferedImage().setRGB(i, j, 0);
			}
		}
		
		return Utils.getImg(img, w, h);		
		
	}
	
	/**
	 * Write the output into a file 'equation.txt'
	 * 
	 * @param output - the output string
	 * @throws IOException
	 */
	public static void writeResult(String output) throws IOException {
		BufferedWriter out = null;
		try {
			FileWriter writer = new FileWriter("equation.txt");
			out = new BufferedWriter(writer);
			out.write(output);
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			if (out != null) out.close();
		}
	}
	
	/**
	 * Reads the input file line by line, parse it and returns a list holding
	 * all the entries from the file
	 * 
	 * @return the list holding all the entries from the file
	 * @throws IOException
	 */
	public static List<RMSEData> readFile() throws IOException {

		BufferedReader input = null; // used for reading input file content
		String line = null; // represent a single line content
		List<RMSEData> list = new ArrayList<RMSEData>();

		try {

			// reading input file
		//FileReader reader = new FileReader("C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\CHIPS_DC_AREA\\registration.txt");
			FileReader reader = new FileReader("C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\CHIPS_DC_AREA\\registration.txt");
			//FileReader reader = new FileReader("C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Konza\\registration.txt");

			input =  new BufferedReader(reader);

			// read header line
			while ((line = input.readLine()) != null) {
				
				RMSEData data = new RMSEData();
				data.title = line;
				line = input.readLine();
				data.folderPath = line;
				data.path1 = data.folderPath + "\\" + input.readLine();
				while ((line = input.readLine()) != null && !line.equals("END")) {

					if (line.startsWith("--"))
						continue;
					
					// read arguments
					String[] arguments = line.split("\t");
					if (arguments.length < 2)
						throw new IllegalArgumentException("Argument missing ...");
	
					// add to data list
					Double x = Double.parseDouble(arguments[0]);
					Double y = Double.parseDouble(arguments[1]);
					data.points1.add(new Point(y,x)); 
					
				}
				
				data.path2 = data.folderPath + "\\" + input.readLine();
				while ((line = input.readLine()) != null && !line.equals("END")) {

					if (line.startsWith("--"))
						continue;
					
					// read arguments
					String[] arguments = line.split("\\t");
					if (arguments.length < 2)
						throw new IllegalArgumentException("Argument missing ...");
	
					// add to data list
					Double x = Double.parseDouble(arguments[0]);
					Double y = Double.parseDouble(arguments[1]);
					data.points2.add(new Point(y,x)); 
					
				}
				line = input.readLine();
				String[] arguments = line.split(";");
				if (arguments.length < 2)
					throw new IllegalArgumentException("Argument missing ...");
				//data.fx = fixTransformation(arguments[0]);
				//data.fy = fixTransformation(arguments[1]);
				data.fx = arguments[0];
				data.fy = arguments[1];
				list.add(data);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) input.close();
		}

		return list;
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	public static List<Entry<Entry<Double, Double>,Entry<Double, Double>>> readFileTest() throws IOException {

		BufferedReader input = null; // used for reading input file content
		String line = null; // represent a single line content
		CONST.data = new ArrayList<Entry<Entry<Double, Double>,Entry<Double, Double>>>();

		try {

			// reading input file
			FileReader reader = new FileReader("c:\\a\\test.txt");
			input =  new BufferedReader(reader);

			// read header line
			line = input.readLine();

			int lineNum = 1;
			while ((line = input.readLine()) != null) {

				lineNum ++;

				// read arguments
				String[] arguments = line.split(",");
				if (arguments.length < 4)
					throw new IllegalArgumentException("Argument missing, line #" + lineNum);

				// add to data list
				Double x = Double.parseDouble(arguments[0]);
				Double y = Double.parseDouble(arguments[1]);
				Double res = Double.parseDouble(arguments[2]);
				Double res2 = Double.parseDouble(arguments[3]);
				Entry<Double, Double> p = new SimpleEntry<Double, Double>(x,y);
				Entry<Double, Double> o = new SimpleEntry<Double, Double>(res,res2);
				Entry<Entry<Double, Double>, Entry<Double, Double>> element = new SimpleEntry<Entry<Double, Double>, Entry<Double, Double>>(p,o); 
				CONST.data.add(element);
			}

		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) input.close();
		}

		return CONST.data;
	}

	private static void RunGeneticAlgorithmTest() throws Exception
	{
		File outputPath = CreateOutputPath("C:\\Users\\Sarit\\workspace\\Results\\Stage B\\00020006_trans");	
		String path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\CHIPS_DC_AREA\\b\\a.jpg";
		String path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\CHIPS_DC_AREA\\b\\r.jpg";
		readImages(outputPath, path1, path2);
		///////////
		SelectionMode selection = SelectionMode.TournamentSelection;
		IGeneticStrategy strategy = new GPStrategy(internalMode);;
		GeneticAlgorithm alg = new GeneticAlgorithm(strategy, outputPath, selection, internalMode, CONST._isCentered);
		alg.Init(null, false);

		alg.Run();
		
		writeResult(alg.GetSolution().toString());
		
	}
	

	

	

}



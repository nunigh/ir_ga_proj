package utils;


import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.script.ScriptException;


import org.opencv.core.Point;


import Interfaces.Chromosome;
import algorithms.CONST;


public class Utils {

	public static Random rand = new Random();
	
	public static double[] a = null;
	public static double width = 0;
	public static double height = 0;
	public static double size = 0;
	
	public static BufferedImage original1;
	public static BufferedImage original2;
	public static BufferedImage img1;
	public static BufferedImage img2;
	
	public static int centeredRowVal = 0;
	public static int centeredColVal = 0;
	
	
	
	public  static double getAngle()
	{
		/*if (a== null)
		{
			a = new double[35];
			a[0] = 0.174533;
			double step = a[0];
			for (int i=1;i<a.length;i++)
			{
				a[i] += a[i-1] + step;
				a[i]*=100;
				a[i]= ((int)a[i])/100.0;
			}
		}

		//angle = utils.Utils.rand.nextDouble() * Math.PI*2;
		return a[Utils.rand.nextInt(a.length)];
		*/
		
		return ((rand.nextDouble()*360.0)/180.0) * Math.PI;
	}
	
	


//public static String SetNoVariable(String t)
//{
	//return t.replace("x", "1").replace("y", "1");
//}
	public static String fixTransformation(String t, int width1, int height1, int width2, int height2, int index, boolean isCentered)
	{
		
		String out = t;
		int lastIndex = 0;
		
		out = out.replace("Sin", "sin");
		out = out.replace("Cos", "cos");
		while(out.indexOf("sin", lastIndex)>=0)
		{
			int startindex = out.indexOf("sin", lastIndex)+"sin".length()+1;
			if (startindex < 9 || !out.substring(startindex - 9, startindex - 4).equals("Math."))
			{
				int endIndex = out.indexOf(")", startindex);
				double angle = Double.parseDouble(out.substring(startindex, endIndex));
				double angleRad = 0;
				//if (!isCentered)
					angleRad = angle;// (angle / 180.0) * Math.PI;
				//else
					//angleRad = angle;
				angleRad = angleRad % (2.0*Math.PI);
				out = out.replace("sin("+angle+")", "Math.sin("+angleRad+")");
				lastIndex = startindex + "sin".length();
			}
			else
			{
				lastIndex = startindex;
			}
		}
		lastIndex = 0;
		while(out.indexOf("cos", lastIndex)>=0)
		{
			int startindex = out.indexOf("cos", lastIndex)+"cos".length()+1;
			if (startindex < 9 || !out.substring(startindex - 9, startindex - 4).equals("Math."))
			{
				int endIndex = out.indexOf(")", startindex);
				double angle = Double.parseDouble(out.substring(startindex, endIndex));
				double angleRad = 0;
				//if (!isCentered)
					angleRad = angle;//(angle / 180.0) * Math.PI;
				//else
					//angleRad = angle;
				angleRad = angleRad % (2.0*Math.PI);
				out = out.replace("cos("+angle+")", "Math.cos("+angleRad+")");
				lastIndex = startindex + "cos".length();
			}
			else
			{
				lastIndex = startindex;
			}
		}
		while(out.indexOf("rotate0")>=0)
		{
			int startindex = out.indexOf("rotate0") + "rotate0".length()+1;
			int endIndex = out.indexOf(")", startindex);
			String all = out.substring(startindex, endIndex);
			////////////double angle = Double.parseDouble(out.substring(startindex, endIndex)) ;
		////////////	double angleRad = 0;
		//	if (!isCentered)
		///////////////		angleRad = (angle / 180.0) * Math.PI;
			//else
				//angleRad = angle;
		//////////	angleRad = angleRad % (2.0*Math.PI);
			out = out.replace("rotate0("+all+")", "((x-" + width2/2.0 + ")*Math.cos(" + all + ")-(y-" + height2/2.0 + ")*Math.sin("+all+") + " + width2/2.0 +")");
			
		}
		while(out.indexOf("rotate1")>=0)
		{
			int startindex = out.indexOf("rotate1") + "rotate1".length()+1;
			int endIndex = out.indexOf(")", startindex);
			double angle = Double.parseDouble(out.substring(startindex, endIndex));
			double angleRad = 0;
			//if (!isCentered)
				angleRad = (angle / 180.0) * Math.PI;
			//else
				//angleRad = angle;
			angleRad = angleRad % (2.0*Math.PI);
			out = out.replace("rotate1("+angle+")", "((x-" + width2/2.0 + ")*Math.sin(" + angle + ")+(y-" + height2/2.0 + ")*Math.cos("+angle+") + " +height2/2.0 +")");
			
			
		}
		
		
		// set the images at the center// starting 7.3.14 6:30
					if (isCentered)
					{
						if (index == 0)
							out = out + " + (" + (width1/2.0 - width2/2.0) + ")";
						else
							out = out + " + (" + (height1/2.0 - height2/2.0) + ")";
					}
					
					
		
		return out;
	}
	
	public static BufferedImage getImg(short[] img, int width, int height) throws IOException
	{

		//BufferedImage bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		BufferedImage bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_GRAY);
		
		WritableRaster raster = bufferedImage.getRaster();
		 for(int x=0; x< width; x++){
	            for(int y=0; y< height; y++){
	            	/// int value = img[getIndex(height,x,y)] << 16 | img[getIndex(height,x,y)] << 8 | img[getIndex(height,x,y)];
	                ///bufferedImage.setRGB(x,y,value);
	                raster.setSample(x, y, 0, img[x*height + y]);
	            }
	        }
		 return bufferedImage;	 		
				
	}
	
	public static void writeImg(short[] img, int width, int height, String path) throws IOException
	{

		//BufferedImage bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		BufferedImage bufferedImage = getImg(img, width, height);
		writeImg(bufferedImage, path); 		 				
	}
	
	public static void writeImg(BufferedImage img, String path) throws IOException
	{
		File outputfile = new File(path);
		ImageIO.write(img, "jpg", outputfile);			 				
	}
	
	

	public static BufferedImage getImg(short[][] img, int width, int height) throws IOException
	{

		//BufferedImage bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		BufferedImage bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_GRAY);
		
		WritableRaster raster = bufferedImage.getRaster();
		 for(int x=0; x< width; x++){
	            for(int y=0; y< height; y++){
	            	/// int value = img[getIndex(height,x,y)] << 16 | img[getIndex(height,x,y)] << 8 | img[getIndex(height,x,y)];
	                ///bufferedImage.setRGB(x,y,value);
	                raster.setSample(x, y, 0, img[x][y]);
	            }
	        }
		 return bufferedImage;		 
		 
			
				
	}
	
	public static void writeImg(short[][] img, int width, int height, String path) throws IOException
	{

		//BufferedImage bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		BufferedImage bufferedImage = getImg(img, width, height);
		writeImg(bufferedImage, path); 
				
	}

	private static short[] SensedTransformed;
	private static short[] diff;
	private static short[] sensed;
	private static short[] referenced;
	public static String writeChromosomeImg(Chromosome ch, String folder, int island, int i, boolean original) throws IOException, ScriptException
	{
		return writeChromosomeImg (ch,folder, island, i, original, "", false);
	}
	public static String writeChromosomeImg(Chromosome ch, String folder, int island, int i, boolean original, String phase) throws IOException, ScriptException
	{
		return writeChromosomeImg (ch,folder, island, i, original, phase, false);
	}
	public static String writeChromosomeImg(Chromosome ch, String folder, int island, int i, boolean original, String phase,boolean force) throws IOException, ScriptException
	{
		if (!CONST.WRITE_IMG && !force)
			return "";

		if (SensedTransformed == null)
		{
			SensedTransformed = new short[original1.getWidth()*original1.getHeight()];
			referenced = new short[SensedTransformed.length];
			diff = new short[SensedTransformed.length];
			sensed = new short[original2.getWidth()*original2.getHeight()];
		}
		//else
		{
			Arrays.fill(SensedTransformed, (short)0);
			Arrays.fill(referenced, (short)0);
			Arrays.fill(diff, (short)0);
			Arrays.fill(sensed, (short)0);
		}

		/*
		String path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\CHIPS_DC_AREA\\e\\0005_0006\\etm_0005_b5.jpg";//data.path1);
		String path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\CHIPS_DC_AREA\\e\\0005_0006\\tm0_0006_b5_wind4.jpg";//data.path2);
		path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\NASA\\Chesapeake Bay\\Landsat 7\\aa.jpg";
		path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\NASA\\Chesapeake Bay\\Landsat 7\\aa_rot.jpg";
		path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Chap9\\Fig9.26i.png";
		path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Chap9\\Fig9.26j.png";
		path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\e.jpg";
		path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\Miscellaneous\\e2.jpg";
		path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Chap9\\Fig9.26a.png";
		path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Chap9\\Fig9.26b.png";

		path1 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\CHIPS_DC_AREA\\b\\a.jpg";//data.path1);
		path2 = "C:\\Users\\Sarit\\workspace\\Results\\RealDataSets\\Satellites\\CHIPS_DC_AREA\\b\\a_rot.jpg";//data.path2);

		path1 = "C:\\Users\\Sarit\\workspace\\Results\\Example7\\6\\a.jpg";
		path2 = "C:\\Users\\Sarit\\workspace\\Results\\Example7\\6\\b.jpg";

		IplImage src1 = cvLoadImage(path1);
		IplImage src2 = cvLoadImage(path2);

		Utils.original1 = src1.getBufferedImage().getData();
		Utils.original2 = src2.getBufferedImage().getData();
		*/
		int width1 = Utils.original1.getWidth();
		int height1 = Utils.original1.getHeight();
		int width2 = Utils.original2.getWidth();
		int height2 = Utils.original2.getHeight();

		//////////

		////sx = Utils.fixTransformation(sx, width1, height1, width2, height2,  0, false);
		/////sy = Utils.fixTransformation(sy, width1, height1, width2, height2, 0, false);

		SensedTransformed = new short[width1*height1];
		diff = new short[width1*height1];
		sensed = new short[width2*height2];
		referenced = new short[width1*height1];
		referenced = new short[width1*height1];

		Raster original2 = Utils.original2.getData();
		Raster original1 = Utils.original1.getData();
		for (int col=0; col < width2; col++)
		{
			for (int row=0; row < height2; row++)
			{
				sensed[(int)Math.round(getIndex(height2, col,row))] = (short) (original2.getSample(col,row, 0));
			}
		}

		for (int col=0; col <width1; col++)
		{
			for (int row=0; row < height1; row++)
			{
				referenced[(int) Math.round(getIndex(height1, col,row))] = (short) (original1.getSample(col,row, 0));
			}
		}

		@SuppressWarnings("unused")
		BufferedImage bufferedImage = new BufferedImage(width1,height1, BufferedImage.TYPE_BYTE_GRAY);
		//WritableRaster raster = bufferedImage.getRaster();

		@SuppressWarnings("unused")
		BufferedImage bufferedImagediff = new BufferedImage(width1,height1, BufferedImage.TYPE_BYTE_GRAY);
		//WritableRaster rasterdiff = bufferedImagediff.getRaster();

		/*int minX = -1;
		int maxX = -1;
		int minY = -1;
		int maxY = -1;
		*/
		//GeneticAlgorithm.scriptX = GeneticAlgorithm.compilingEngine.compile(sx);
		//GeneticAlgorithm.scriptY = GeneticAlgorithm.compilingEngine.compile(sy);

		for (int col=0; col < width2; col++)
		{
			//System.out.println(String.format("col %d of %d -----------------------------",col+1,width2));
			for (int row=0; row < height2; row++)
			{


				double[] newPos = ch.T(col, row, CONST._isCentered);

				//double x = GPTransformations.GetValue(sx, col, row);
				//double y = GPTransformations.GetValue(sy, col, row);
				//int[] newPos = new int[] {(int) Math.round(x),(int)Math.round(y)};
				int newIndex = (int) Math.round(getIndex(height1, (int) Math.round(newPos[0]),(int) Math.round(newPos[1])));

				if (newPos[0] >= 0 && newPos[0] < width1 &&
						newPos[1] >= 0 && newPos[1] < height1 &&
						newIndex >= 0 && newIndex < SensedTransformed.length)
				{
					//System.out.println(String.format("%d,%d,%f,%f",col, row, x,y));
					/*if (minX > x || minX < 0) minX = (int) Math.round(x);
					if (maxX < x || maxX < 0) maxX = (int) Math.round(x);
					if (minY > y || minY < 0) minY = (int) Math.round(y);
					if (maxY < y || maxY < 0) maxY = (int) Math.round(y);
					*/
					SensedTransformed[newIndex] = sensed[(int) Math.round(getIndex(height2,col,row))];
					diff[newIndex] = (short) Math.abs(SensedTransformed[newIndex] - referenced[newIndex]);

					/*short val = (short) (Utils.original2.getSample(col,row, 0));
		            raster.setSample(newPos[0], newPos[1],0, val);//SensedTransformed[index]);
		            rasterdiff.setSample(
		            				newPos[0],
		            				newPos[1],
		            				0,
		            				(short) (Utils.original1.getSample(newPos[0],newPos[1], 0) - val));
		            				*/
				}
			}

		}
		//System.out.println(minX);
		//System.out.println(maxX);
		//System.out.println(minY);
		//System.out.println(maxY);

		String s = null;
		if (original)
		{
			s = String.format("%s%ssensed.jpg",folder,File.separatorChar);
			writeImg(sensed, width2, height2, s);

			s = String.format("%s%sreferenced.jpg",folder,File.separatorChar);
			writeImg(referenced, width1, height1, s);
		}

		// s = String.format("%s%s_best.jpg", "C:\\Users\\Sarit\\workspace\\Results\\SIFT",File.separatorChar);

		//File outputfile = new File(s);
		//ImageIO.write(bufferedImage, "jpg", outputfile);


		// s = String.format("%s%s_diff.jpg","C:\\Users\\Sarit\\workspace\\Results\\SIFT",File.separatorChar);

		// File outputfilediff = new File(s);
		// ImageIO.write(bufferedImagediff, "jpg", outputfilediff);

		String resultPath = String.format("%s%s%d_best%d_%s.jpg", folder,File.separatorChar, island, i,phase);
		writeImg(SensedTransformed, width1, height1, resultPath);

		s = String.format("%s%s%d_diff_%s.jpg",folder,File.separatorChar, island,phase);
		writeImg(diff, width1, height1, s);
		return resultPath;
	}

	public static double getIndex(int height, double col, double row)
	{
		return col*height + row;
	}
	
	public static void RunTest(Chromosome ch, ArrayList<Point> r, ArrayList<Point> s) throws Exception
	{
		for (int i = 0; i < s.size(); i++) {
			double[] newPos = ch.GetValue(s.get(i).x, s.get(i).y);
			double diffX = Math.abs(newPos[0] - r.get(i).x);
			double diffY = Math.abs(newPos[1] - r.get(i).y); 
			System.out.print("sensed: ("+s.get(i).x+ "," + s.get(i).y+")");
			System.out.print(",");
			System.out.print("referenced: ("+r.get(i).x+ "," + r.get(i).y+")");
			System.out.print(" - ");
			System.out.print("transformed: ("+newPos[0]+ "," + newPos[1]+")");
			System.out.print(" diff: ");
			System.out.print("("+diffX+ "," + diffY+")");
			System.out.println();
		}
	}
	
	
	
}
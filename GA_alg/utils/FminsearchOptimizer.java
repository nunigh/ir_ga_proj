package utils;

import GA.GAChromosome;
import GA.Structure.Concrete.*;
import Interfaces.Chromosome;
import algorithms.CONST;
import algorithms.GeneticAlgorithm;
import com.mathworks.toolbox.javabuilder.*;
import fminsearch4irDbg.fminsearchOptimizer;
import org.opencv.core.Point;
import org.opencv.objdetect.Objdetect;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by root on 12/12/2017.
 */
public class FminsearchOptimizer {

    static fminsearchOptimizer fopt = null;
    static String basePath;
    static String projectPath;

    public static void init() throws MWException {
        if (fopt == null)
        {
            long current1 = System.currentTimeMillis();
            System.out.println("before init");
            fopt   = new fminsearchOptimizer();
            basePath = "C:\\Users\\root\\Documents\\ir_res_vis\\resultsImages";
            basePath = CONST.projectPath + "ir_res_vis\\resultsImages";
            long current2 = System.currentTimeMillis();
            System.out.println("after init - diff " + String.valueOf((current2-current1)/1000));
        }
    }

    public enum Method {
        NCC,
        COND_NCC,
        HD,
        NCC_HD_NCC_HD,
        HD_NCC_HD_NCC,
        NCC_HD,
        HD_NCC;
    }
    public static Chromosome run(Chromosome chromosome, Method method, String referencedPath, String sensedPath, GeneticAlgorithm alg, FileOption fileOption, String taskID, String inProjectPath)
    {
        projectPath = inProjectPath;
        return matlabCallWrapper(chromosome, referencedPath,sensedPath, alg,method,fileOption,taskID);
    }

    static private MWNumericArray pointListToMatArray (ArrayList<Point> pointList)
    {
        double [] [] refPointsArray  = new double[pointList.size()][2];
        for (int i = 0; i < pointList.size(); ++i)
        {
            Point point = pointList.get(i);
            refPointsArray [i] = new double[]{point.x, point.y};
        }
        MWNumericArray mwPoints = new MWNumericArray(refPointsArray, MWClassID.DOUBLE);
        return mwPoints;
    }

    public static double getMedian(ArrayList<Double> set) {
        Collections.sort(set);
        int middle = set.size() / 2;
        middle = middle % 2 == 0? middle - 1 : middle;
        return set.get(middle);
    }


    static public void HD_test_random() throws MWException {

        int sumNoise = 0;
        int sumRegular = 0;
        boolean res = true;
        for (int i =1 ; i<100; ++i ) {
            res = HD_test_random(50, 500, 500, true);
            sumRegular += res?0:1;
            if (!res)
                break;

        }
        //System.out.println("number of failures with noise: "+ sumNoise);
        System.out.println("number of failures with regular: "+ sumRegular);
    }

    static public boolean HD_test_random (int vectorSize, int xlimit, int ylimit,  boolean addNoise) throws MWException {

        Random rand = new Random();
        float tx = rand.nextFloat()*10;
        float ty = rand.nextFloat()*10;
        double expectedHDres = Math.sqrt(tx*tx + ty*ty);
        System.out.println("tx "+ tx + " ty " + ty + " hd "+expectedHDres);


        // create chromosome
        Chromosome ch = new GAChromosome(new TranslationX(tx),
                new TranslationY(ty),
                new Rotation(0),
                new Scaling(1),
                new ScalingY(1),
                new ShearingX(0),
                new ShearingY(0),null,null,null);


        // create random set of features points
        ArrayList<Point> randomPointsArray= new ArrayList<>();
        for (int i = 0; i < vectorSize; ++i) {
            Point p = new Point(rand.nextInt(xlimit), rand.nextInt(ylimit) );
            randomPointsArray.add(p);
        }



        /* calculate the affine transformation on the features point
        for (Point p : randomPoints)
        {
            double [] transformedPoint = resChrom.GetValue (p.x,p.y);
            transformedPoints.add(new Point (transformedPoint[0], transformedPoint[1]));
        }*/


        ArrayList<Point> secondArray= new ArrayList<>();

        if (addNoise) {
            // add gaussian noise
            for (Point p : randomPointsArray) {
                double noise = rand.nextGaussian();
                Point newp = new Point(p.x + noise, p.y + noise);
                secondArray.add(newp);
            }
        } else {
            secondArray = new ArrayList<>(randomPointsArray);
        }

        // mix the order in both arrays
        Collections.shuffle(randomPointsArray);
        Collections.shuffle(secondArray);

        int [] limits = new int[]{xlimit+1, ylimit+1};
        double hdresult = calcHD( ch, randomPointsArray, secondArray,limits);
        System.out.println("result: " + hdresult);
        System.out.println("expected res: " + expectedHDres);

        double roundedRes = Math.round(hdresult*100)/100;
        double roundedExpectedRes = Math.round(expectedHDres*100)/100;

        System.out.println("-- " + roundedExpectedRes  + " " + roundedRes);
        double diff = expectedHDres - hdresult;
        return ((diff < 1 ) && (diff > -1));
    }

    public static double calcHD(Chromosome ch, ArrayList<Point> points1, ArrayList<Point> points2, int[] limits) throws MWException {
        init();
        MWNumericArray x0 =ChromosomeToMatArray(ch);
        Object[] matRes;

        MWNumericArray mwLimits = new MWNumericArray (limits, MWClassID.INT32);

        MWNumericArray mwRefPoints = pointListToMatArray(points1);
        MWNumericArray mwSensedPoints = pointListToMatArray(points2);

        matRes = fopt.myHD(1,x0,mwRefPoints,mwSensedPoints,mwLimits);
        double hdresult = ((MWNumericArray) matRes[0]).getDouble();
        return hdresult;
    }


    static private MWNumericArray ChromosomeToMatArray (Chromosome chromosome)
    {
        double [] chromosomeArrayWithoutScaleY = chromosome.toArray();
        double [] chromosomeArray = new double[7];
        chromosomeArray[0] = chromosomeArrayWithoutScaleY[0];
        chromosomeArray[1] = chromosomeArrayWithoutScaleY[1];
        chromosomeArray[2] = chromosomeArrayWithoutScaleY[2];
        chromosomeArray[3] = chromosomeArrayWithoutScaleY[3];
        chromosomeArray[4] = chromosomeArrayWithoutScaleY[3];
        chromosomeArray[5] = chromosomeArrayWithoutScaleY[4];
        chromosomeArray[6] = chromosomeArrayWithoutScaleY[5];

        MWNumericArray x0 = new MWNumericArray(chromosomeArray, MWClassID.DOUBLE);
        return x0;
    }

    static public Object[] newHD (MWNumericArray x0, GeneticAlgorithm alg, String taskID, boolean writeResult) throws MWException
    {
        init();
        //MWNumericArray x0 =ChromosomeToMatArray(chromosome);
        Object[] matRes;

        MWNumericArray mwLimits = new MWNumericArray (alg.getLimits(), MWClassID.INT32);

        MWNumericArray mwRefPoints = pointListToMatArray(alg.getReferencedFeaturesPoints());
        MWNumericArray mwSensedPoints = pointListToMatArray(alg.getSensedFeaturesPoints());

        matRes = fopt.fminsearch_HausdorffDist(3,x0,mwRefPoints,mwSensedPoints,mwLimits, basePath,taskID);
        return matRes;
        //return handleResults(matRes,chromosome, alg, "newHD", fileOption, writeResult);
    }

    static public Chromosome matlabCallWrapper(Chromosome chromosome, String referencedPath, String sensedPath, GeneticAlgorithm alg, Method method, /*to do with callable*/FileOption fileOption, String taskID)
    {
        try {
            init();

            MWNumericArray x0 = ChromosomeToMatArray(chromosome);
            Object[] matRes;
            switch (method) {
                case HD:
                    matRes = newHD(x0, alg, taskID, false);
                    break;
                case NCC:
                    matRes = doNCC(x0, sensedPath, referencedPath, taskID);
                    break;
                case COND_NCC: {
                    matRes = conditionalNCC(x0, sensedPath, referencedPath, taskID, -0.35);
                    break;
                }
                case NCC_HD: {
                    MWNumericArray tempCh = x0;
                    matRes = conditionalNCC(x0, sensedPath, referencedPath, taskID, -0.35);
                    if (matRes != null) {
                        //tempCh = matVect2Chromosom(((MWNumericArray)matRes[2]))
                        tempCh = (MWNumericArray) matRes[2];
                    }
                    matRes = newHD(tempCh, alg, taskID, true);
                    break;
                }
                case HD_NCC: {
                    matRes = newHD(x0, alg, taskID, false);
                    x0 = ((MWNumericArray) matRes[2]);
                    Object[] newMatRes = conditionalNCC(x0, sensedPath, referencedPath, taskID, -0.35);
                    if (newMatRes != null) {
                        matRes = newMatRes;
                    }
                    break;
                }
                case NCC_HD_NCC_HD: {
                    MWNumericArray tempCh = x0;
                    matRes = conditionalNCC(x0, sensedPath, referencedPath, taskID, -0.35);
                    if (matRes != null) {
                        tempCh = (MWNumericArray) matRes[2];
                    }
                    matRes = newHD(tempCh, alg, taskID, true);
                    tempCh = (MWNumericArray) matRes[2];
                    Object[] newmatRes = conditionalNCC(tempCh, sensedPath, referencedPath, taskID, -0.35);
                    if (newmatRes != null) {
                        tempCh = (MWNumericArray) newmatRes[2];
                        matRes = newHD(tempCh, alg, taskID, true);
                    }
                    break;
                }
                case HD_NCC_HD_NCC: {
                    matRes = newHD(x0, alg, taskID, false);
                    x0 = ((MWNumericArray) matRes[2]);
                    Object[] newMatRes = conditionalNCC(x0, sensedPath, referencedPath, taskID, -0.35);
                    if (newMatRes != null) {
                        x0 = ((MWNumericArray) newMatRes[2]);
                        matRes = newHD(x0, alg, taskID, true);
                        x0 = ((MWNumericArray) matRes[2]);
                        newMatRes = conditionalNCC(x0, sensedPath, referencedPath, taskID, -0.35);
                        if (newMatRes != null) {
                            matRes = newMatRes;
                            matRes = newMatRes;
                        }
                    }
                    break;
                }
                default:
                    System.out.println("error: invalid fitness option!");
                    return null;

            }

            return handleResults (matRes, chromosome,alg,method.name(),fileOption,true,referencedPath,sensedPath,taskID);

        } catch (MWException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static Chromosome handleResults(Object[] matRes, Chromosome origChromosome, GeneticAlgorithm alg, String fitnessFunctionName, /*to do with callable*/FileOption fileOption, boolean writeResult, String referencePath, String sensedPath, String taskID) throws IOException, ScriptException {

        double origFitness = ((MWNumericArray) matRes[0]).getDouble();
        double improvedFitness = ((MWNumericArray) matRes[1]).getDouble();

        MWNumericArray resAsArray  = ((MWNumericArray)matRes[2]);
        Chromosome resChrom = matVect2Chromosom(resAsArray);
        if (writeResult) {
            double origRMSE = alg.calcRMSE(origChromosome);
            double improvedRMSE = alg.calcRMSE(resChrom);
            double RMSE_diff = origRMSE - improvedRMSE;
            String resSign = RMSE_diff > 0 ? "+" : RMSE_diff < 0 ? "-" : "=";
            System.out.println("matlab     res: " + resChrom.getDescription());
            //RunID \ Original RMSE \t Original HD \t original MI \t\t HD After \t RMSE After\t RMSE DIFF \t\t MI After \t RMSE After \t RMSE DIFF

            System.out.println("summary: ");
            System.out.println("taskID\t\t\torigRMSE\t\torigFitness\t\timprovedRMSE\timprovedFitness\tRMSE_diff");
            double[] doublesToPrint = {origRMSE, origFitness, improvedRMSE, improvedFitness, RMSE_diff};
            String doublesString = "";
            for (double element : doublesToPrint) {
                doublesString += String.format("%.2f", element) + "\t\t\t";
            }
            System.out.println(taskID + "\t\t\t" + doublesString + "\t\t" + resSign);
            ResultsDbManager.ResultStruct resultStruct = new ResultsDbManager.ResultStruct(taskID, basePath, fileOption.name(), fitnessFunctionName, origRMSE, origFitness, improvedRMSE, improvedFitness, origChromosome, resChrom, referencePath,sensedPath, alg._gen);
            ResultsDbManager.addToDB(resultStruct);
        }
        return resChrom;

    }

    public static Object []  doHD(MWNumericArray x0, String sensedPath, String referencedPath ,String taskID) throws MWException {
        return fopt.fminsearch_HausdorffDist(3,x0,sensedPath,referencedPath,basePath,taskID);
    }

    public static Object []  doMI(MWNumericArray x0, String sensedPath, String referencedPath,String taskID ) throws MWException {
        return fopt.fminsearch_MI(3,x0,sensedPath,referencedPath,basePath,taskID);
    }

    public static Object []  doNCC(MWNumericArray x0, String sensedPath, String referencedPath,String taskID ) throws MWException {
        return fopt.fminsearch_NCC(3,x0,sensedPath,referencedPath,basePath,taskID);
    }

    public static Chromosome matVect2Chromosom (MWNumericArray matlabVect )
    {
        double[] resDoubleArray = matlabVect.getDoubleData();
        GAChromosome resChrom = new GAChromosome(new TranslationX((float) resDoubleArray[0]),
                new TranslationY((float) resDoubleArray[1]),
                new Rotation((float) resDoubleArray[2]),
                new Scaling((float) resDoubleArray[3]),
                new ScalingY((float) resDoubleArray[4]),
                new ShearingX((float) resDoubleArray[5]),
                new ShearingY((float) resDoubleArray[6]), null, null, null);
        return resChrom;
    }
    public static Object[] conditionalNCC (MWNumericArray x0, String sensedPath, String referencedPath,String taskID, double threshold) throws MWException {
        Object[] matRes = fopt.calcNCCbyImgPath(1, x0, sensedPath, referencedPath);
        Object resultedNCCObject =  matRes[0];
        double NCC0 = ((MWNumericArray) resultedNCCObject).getDouble();
        if (NCC0 < threshold) {
            return doNCC(x0, sensedPath, referencedPath, taskID);
        }
        else
        {

            //we put those variables for handleResults
            matRes = new Object[]{resultedNCCObject, resultedNCCObject, x0};
        }
        return matRes;
    }

}



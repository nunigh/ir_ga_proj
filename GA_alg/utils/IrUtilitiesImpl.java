package utils;

import Fitness.FitnessSIFT;
import Interfaces.Chromosome;
import algorithms.GeneticAlgorithm;

import java.io.File;


/**
 * Created by root on 23/01/2018.
 */
public class IrUtilitiesImpl {




    public static double  calcHD(GeneticAlgorithm alg , Chromosome chromosome) throws Exception {

        //GAUtils.init();
        //InternalMode internalMode = InternalMode.SIFT;
        //GAStrategy strategy = new GAStrategy(internalMode);
        //GeneticAlgorithm alg = Main_ex.createSIFTGA (strategy,referencedImgPath,sensedImgPath,null,null);
        double[] fitnessArray = FitnessSIFT.CalculateFitnessSIFT(alg.GetMode(), alg.getReferencedFeaturesPoints(), alg.getSensedFeaturesPoints(), chromosome, 0);
        return fitnessArray[0];

    }


    public  void printTransformation(String originaPicPath, Chromosome chromosome, String destinationFolder)
    {
        System.out.println("initializing images");
        IRUtilitiesManager.InitImage(originaPicPath);
        String filename = new File(originaPicPath).getName();
        try {
            System.out.println("starting...");
            Utils.writeChromosomeImg(chromosome,destinationFolder,0,0,false,filename);
        } catch (Exception e) {
            System.out.println("printTransformation: Error");
            e.printStackTrace();
        }
        System.out.println("done");
    }

}

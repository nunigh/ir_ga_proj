package utils;

import Interfaces.Chromosome;
import algorithms.GeneticAlgorithm;

/**
 * Created by root on 23/01/2018.
 */
public interface IrUtilities {

    public double calcMI(GeneticAlgorithm alg, String sensedImgPath, String referencedImgPath, Chromosome chromosome);

    public double calcHD (String sensedImgPath, String referencedImgPath, Chromosome chromosome) throws Exception;

    public void printTransformation (String originaPicPath, Chromosome chromosome, String destinationFolder);

}

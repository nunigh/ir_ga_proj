package utils;

import GA.GAChromosome;
import GA.Structure.Concrete.*;
import Interfaces.Chromosome;
import algorithms.CONST;
import com.googlecode.javacv.cpp.opencv_core;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import fminsearch4irDbg.fminsearchOptimizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

/**
 * Created by root on 23/01/2018.
 */
public class IRUtilitiesManager {

    final static public String destinationFolder = CONST.projectPath+ "hadar";

    static public Chromosome createAffineTransformationChrom (float tranX,
                                                              float tranY,
                                                              float rot,
                                                              float scale,
                                                              float scaleY,
                                                              float shearX,
                                                              float shareY)
    {
        return new GAChromosome(new TranslationX(tranX),
                                new TranslationY(tranY),
                                new Rotation(rot),
                                new Scaling(scale),
                                new ScalingY(scaleY),
                                new ShearingX(shearX),
                                new ShearingY(shareY)
                                , null, null, null);
    }

    static public void runTest()
    {}

    static public void MIcalcTest(){


    }
    /*
    14/3 meeting notes:
    page 27-29 in Tesis
    first you get set of features of the original features.
    then you calc the transformation only on the features.
    i should pass the matlab the 2 fetures set of Sarit.
    - need to consider overlap (in case there are points outside the frame)
    - bigemya - what if the nearst neighoubr is already match to a previous point
    to send Sarit an email today.
    */

    static public void transformTest() throws Exception {
        String path = "C:\\Users\\root\\Pictures\\cloud.jpg";
        System.out.println("Creating Chromosome");
        Chromosome chromosome = createAffineTransformationChrom(45.55509f,17.08337f,4.0037556f,0.9972017f,0.9972017f,-0.065546274f,0.06552182f);

        /* transformation that worked ok (comparison shows it equal):
        (0,0,0,1,1,0,0)
        (50,200,0,1,1,0,0);
        (0,0,10,1,1,5,0);
        didn't work ok  - there is a slight difference:
        (0,0,0,1,1,0,1)
         */

                ;
        IrUtilitiesImpl utils = new IrUtilitiesImpl();
        utils.printTransformation(path, chromosome,destinationFolder+"\\" + "test");
        IRUtilitiesManager.matlabPrintTransformation(chromosome, path,"test");

        String diffFileName = "testdiff.jpg";
        imageComparison(destinationFolder  + "\\" + "test/" + "\\"+ "test_matlab_transformation_result.jpg",destinationFolder  + "\\" + "test" + "\\"+ "0_best0_cloud.jpg.jpg" ,destinationFolder + "\\" +"test" + "\\" + diffFileName);
    }

    static public void InitImage(String path1)
    {

        opencv_core.IplImage src1 = cvLoadImage(path1);

        Utils.original1 = src1.getBufferedImage();
        Utils.original2 = Utils.original1;
        Utils.img1 = src1.getBufferedImage();
        Utils.img2 = Utils.img1;

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

    static public Chromosome matlabPrintTransformation(Chromosome chromosome, String referencedPath , String destFolder)
    {
        try {
            fminsearchOptimizer fopt = new fminsearchOptimizer();
            double [] chromosomeArray = chromosome.toArray();

            MWNumericArray x0 = new MWNumericArray(chromosomeArray, MWClassID.DOUBLE);
            fopt.printTransformation(x0,referencedPath,destinationFolder, destFolder);


        } catch (MWException e) {
            e.printStackTrace();
        }

        return null;

    }

    static public boolean imageComparison (String path1, String path2, String diffPath) throws Exception {
        boolean equal = areImagesEqual(path1, path2);
        if (diffPath != null )
        {
            createImageDiff(path1, path2, diffPath);
        }
        System.out.println("result: images are " + (equal? "Equal": "Not Equal"));
        System.out.println("diff path " + destinationFolder);
        return equal;
    }

    static public boolean areImagesEqual (String path1, String path2) throws IOException {
        BufferedImage bufferedImage1 = readImageFromFile (path1);
        BufferedImage bufferedImage2 = readImageFromFile (path2);
        return  compareImages(bufferedImage1, bufferedImage2);
    }

    static public boolean areImagesEqual_bySha1 (String path1, String path2) throws Exception {
        byte[] sha1Path1 = createSha1(new File (path1));
        byte[] sha1Path2 = createSha1(new File (path2));
        return Arrays.equals(sha1Path1, sha1Path2);
    }

    static public byte[] createSha1(File file) throws Exception  {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream fis = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }
        return digest.digest();
    }

    /**
     * Compares two images pixel by pixel.
     *
     * @param imgA the first image.
     * @param imgB the second image.
     * @return whether the images are both the same or not.
     */
    //https://stackoverflow.com/questions/11006394/is-there-a-simple-way-to-compare-bufferedimage-instances
    public static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {
            int width = imgA.getWidth();
            int height = imgA.getHeight();

            // Loop over every pixel.
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Compare the pixels for equality.
                    if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }

        return true;
    }

    static public void createImageDiff (String inPath1, String inPath2, String outDiffPath ) throws IOException {
        BufferedImage bufferedImage1 = readImageFromFile(inPath1);
        BufferedImage bufferedImage2 = readImageFromFile(inPath2);
        BufferedImage diffImage = getDifferenceImage(bufferedImage1,bufferedImage2);
        saveImageToJpgFile(diffImage, outDiffPath);
    }

    public static BufferedImage readImageFromFile (String path) throws IOException {
        File outputfile = new File(path);
        return ImageIO.read(outputfile);
    }

    public static void saveImageToJpgFile (BufferedImage img, String path) throws IOException {
        File outputfile = new File(path);
        ImageIO.write(img, "jpg", outputfile);
    }

    public static BufferedImage getDifferenceImage(BufferedImage img1, BufferedImage img2) {
        int width1 = img1.getWidth();
        int width2 = img2.getWidth();
        int height1 = img1.getHeight();
        int height2 = img2.getHeight();
        if ((width1 != width2) || (height1 != height2)) {
            System.err.println("Error: Images dimensions mismatch");
            System.exit(1);
        }

        // NEW - Create output Buffered image of type RGB
        BufferedImage outImg = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);

        // Modified - Changed to int as pixels are ints
        int diff;
        int result; // Stores output pixel
        for (int i = 0; i < height1; i++) {
            for (int j = 0; j < width1; j++) {
                int rgb1 = img1.getRGB(j, i);
                int rgb2 = img2.getRGB(j, i);
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = (rgb1) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = (rgb2) & 0xff;
                diff = Math.abs(r1 - r2); // Change
                diff += Math.abs(g1 - g2);
                diff += Math.abs(b1 - b2);
                diff /= 3; // Change - Ensure result is between 0 - 255
                // Make the difference image gray scale
                // The RGB components are all the same
                result = (diff << 16) | (diff << 8) | diff;
                outImg.setRGB(j, i, result); // Set result
            }
        }

        // Now return
        return outImg;
    }
}

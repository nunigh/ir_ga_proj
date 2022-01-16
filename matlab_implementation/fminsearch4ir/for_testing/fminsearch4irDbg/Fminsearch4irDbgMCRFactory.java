/*
 * MATLAB Compiler: 6.4 (R2017a)
 * Date: Wed May 08 12:51:52 2019
 * Arguments: 
 * "-B""macro_default""-W""java:fminsearch4irDbg,fminsearchOptimizer""-T""link:lib""-d""C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\fminsearch4ir\\for_testing""class{fminsearchOptimizer:C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\calcNCCbyImgPath.m,C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\fminsearch_HausdorffDist.m,C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\fminsearch_MI.m,C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\fminsearch_NCC.m,C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\fminsearch_surfThingFunc.m,C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\my_NCC.m,C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\myHD.m,C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\NCC_abs.m,C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\printTransformation.m,C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\surfThingTest.m}"
 */

package fminsearch4irDbg;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class Fminsearch4irDbgMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "fminsearch4i_650B471CC48333C47B8B50440FD53C12";
    
    /** Component name */
    private static final String sComponentName = "fminsearch4irDbg";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(Fminsearch4irDbgMCRFactory.class)
        );
    
    
    private Fminsearch4irDbgMCRFactory()
    {
        // Never called.
    }
    
    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException
    {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(
            componentOptions, 
            Fminsearch4irDbgMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{9,2,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}

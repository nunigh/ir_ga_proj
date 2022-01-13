/*
 * MATLAB Compiler: 6.4 (R2017a)
 * Date: Tue Dec 12 22:48:32 2017
 * Arguments: 
 * "-B""macro_default""-W""java:fminsearch4ir,fminsearchOptimizer""-T""link:lib""-d""C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\fminsearch4ir\\for_testing""class{fminsearchOptimizer:C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\fminsearch_HausdorffDist.m}"
 */

package fminsearch4ir;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class Fminsearch4irMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "fminsearch4i_71FFE6CE58D1023A38EEBB62BA7E7BE9";
    
    /** Component name */
    private static final String sComponentName = "fminsearch4ir";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(Fminsearch4irMCRFactory.class)
        );
    
    
    private Fminsearch4irMCRFactory()
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
            Fminsearch4irMCRFactory.class, 
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

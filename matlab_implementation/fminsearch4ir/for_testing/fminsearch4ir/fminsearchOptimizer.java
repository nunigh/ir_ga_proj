/*
 * MATLAB Compiler: 6.4 (R2017a)
 * Date: Tue Dec 12 22:48:32 2017
 * Arguments: 
 * "-B""macro_default""-W""java:fminsearch4ir,fminsearchOptimizer""-T""link:lib""-d""C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\fminsearch4ir\\for_testing""class{fminsearchOptimizer:C:\\Users\\root\\Documents\\MATLAB\\IR_opt\\fminsearch_HausdorffDist.m}"
 */

package fminsearch4ir;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;
import java.util.*;

/**
 * The <code>fminsearchOptimizer</code> class provides a Java interface to MATLAB functions. 
 * The interface is compiled from the following files:
 * <pre>
 *  C:\Users\root\Documents\MATLAB\IR_opt\fminsearch_HausdorffDist.m
 * </pre>
 * The {@link #dispose} method <b>must</b> be called on a 
 * <code>fminsearchOptimizer</code> instance when it is no longer needed to ensure that 
 * native resources allocated by this class are properly freed.
 * @version 0.0
 */
public class fminsearchOptimizer extends MWComponentInstance<fminsearchOptimizer>
{
    /**
     * Tracks all instances of this class to ensure their dispose method is
     * called on shutdown.
     */
    private static final Set<Disposable> sInstances = new HashSet<Disposable>();

    /**
     * Maintains information used in calling the <code>fminsearch_HausdorffDist</code> 
     *MATLAB function.
     */
    private static final MWFunctionSignature sFminsearch_HausdorffDistSignature =
        new MWFunctionSignature(/* max outputs = */ 1,
                                /* has varargout = */ false,
                                /* function name = */ "fminsearch_HausdorffDist",
                                /* max inputs = */ 3,
                                /* has varargin = */ false);

    /**
     * Shared initialization implementation - private
     * @throws MWException An error has occurred during the function call.
     */
    private fminsearchOptimizer (final MWMCR mcr) throws MWException
    {
        super(mcr);
        // add this to sInstances
        synchronized(fminsearchOptimizer.class) {
            sInstances.add(this);
        }
    }

    /**
     * Constructs a new instance of the <code>fminsearchOptimizer</code> class.
     * @throws MWException An error has occurred during the function call.
     */
    public fminsearchOptimizer() throws MWException
    {
        this(Fminsearch4irMCRFactory.newInstance());
    }
    
    private static MWComponentOptions getPathToComponentOptions(String path)
    {
        MWComponentOptions options = new MWComponentOptions(new MWCtfExtractLocation(path),
                                                            new MWCtfDirectorySource(path));
        return options;
    }
    
    /**
     * @deprecated Please use the constructor {@link #fminsearchOptimizer(MWComponentOptions componentOptions)}.
     * The <code>com.mathworks.toolbox.javabuilder.MWComponentOptions</code> class provides an API to set the
     * path to the component.
     * @param pathToComponent Path to component directory.
     * @throws MWException An error has occurred during the function call.
     */
    public fminsearchOptimizer(String pathToComponent) throws MWException
    {
        this(Fminsearch4irMCRFactory.newInstance(getPathToComponentOptions(pathToComponent)));
    }
    
    /**
     * Constructs a new instance of the <code>fminsearchOptimizer</code> class. Use this 
     * constructor to specify the options required to instantiate this component.  The 
     * options will be specific to the instance of this component being created.
     * @param componentOptions Options specific to the component.
     * @throws MWException An error has occurred during the function call.
     */
    public fminsearchOptimizer(MWComponentOptions componentOptions) throws MWException
    {
        this(Fminsearch4irMCRFactory.newInstance(componentOptions));
    }
    
    /** Frees native resources associated with this object */
    public void dispose()
    {
        try {
            super.dispose();
        } finally {
            synchronized(fminsearchOptimizer.class) {
                sInstances.remove(this);
            }
        }
    }
  
    /**
     * Invokes the first MATLAB function specified to MCC, with any arguments given on
     * the command line, and prints the result.
     *
     * @param args arguments to the function
     */
    public static void main (String[] args)
    {
        try {
            MWMCR mcr = Fminsearch4irMCRFactory.newInstance();
            mcr.runMain( sFminsearch_HausdorffDistSignature, args);
            mcr.dispose();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    /**
     * Calls dispose method for each outstanding instance of this class.
     */
    public static void disposeAllInstances()
    {
        synchronized(fminsearchOptimizer.class) {
            for (Disposable i : sInstances) i.dispose();
            sInstances.clear();
        }
    }

    /**
     * Provides the interface for calling the <code>fminsearch_HausdorffDist</code> MATLAB function 
     * where the first argument, an instance of List, receives the output of the MATLAB function and
     * the second argument, also an instance of List, provides the input to the MATLAB function.
     * <p>
     * Description as provided by the author of the MATLAB function:
     * </p>
     * <pre>
     * %  [-10.792972, -43.632618, 1.3954129, 1.0046675, 0.9795167, 0.04440333, 
     * -0.014209942]
     * </pre>
     * @param lhs List in which to return outputs. Number of outputs (nargout) is
     * determined by allocated size of this List. Outputs are returned as
     * sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>.
     * Each output array should be freed by calling its <code>dispose()</code>
     * method.
     *
     * @param rhs List containing inputs. Number of inputs (nargin) is determined
     * by the allocated size of this List. Input arguments may be passed as
     * sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or
     * as arrays of any supported Java type. Arguments passed as Java types are
     * converted to MATLAB arrays according to default conversion rules.
     * @throws MWException An error has occurred during the function call.
     */
    public void fminsearch_HausdorffDist(List lhs, List rhs) throws MWException
    {
        fMCR.invoke(lhs, rhs, sFminsearch_HausdorffDistSignature);
    }

    /**
     * Provides the interface for calling the <code>fminsearch_HausdorffDist</code> MATLAB function 
     * where the first argument, an Object array, receives the output of the MATLAB function and
     * the second argument, also an Object array, provides the input to the MATLAB function.
     * <p>
     * Description as provided by the author of the MATLAB function:
     * </p>
     * <pre>
     * %  [-10.792972, -43.632618, 1.3954129, 1.0046675, 0.9795167, 0.04440333, 
     * -0.014209942]
     * </pre>
     * @param lhs array in which to return outputs. Number of outputs (nargout)
     * is determined by allocated size of this array. Outputs are returned as
     * sub-classes of <code>com.mathworks.toolbox.javabuilder.MWArray</code>.
     * Each output array should be freed by calling its <code>dispose()</code>
     * method.
     *
     * @param rhs array containing inputs. Number of inputs (nargin) is
     * determined by the allocated size of this array. Input arguments may be
     * passed as sub-classes of
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or as arrays of
     * any supported Java type. Arguments passed as Java types are converted to
     * MATLAB arrays according to default conversion rules.
     * @throws MWException An error has occurred during the function call.
     */
    public void fminsearch_HausdorffDist(Object[] lhs, Object[] rhs) throws MWException
    {
        fMCR.invoke(Arrays.asList(lhs), Arrays.asList(rhs), sFminsearch_HausdorffDistSignature);
    }

    /**
     * Provides the standard interface for calling the <code>fminsearch_HausdorffDist</code> MATLAB function with 
     * 3 comma-separated input arguments.
     * Input arguments may be passed as sub-classes of
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>, or as arrays of
     * any supported Java type. Arguments passed as Java types are converted to
     * MATLAB arrays according to default conversion rules.
     *
     * <p>
     * Description as provided by the author of the MATLAB function:
     * </p>
     * <pre>
     * %  [-10.792972, -43.632618, 1.3954129, 1.0046675, 0.9795167, 0.04440333, 
     * -0.014209942]
     * </pre>
     * @param nargout Number of outputs to return.
     * @param rhs The inputs to the MATLAB function.
     * @return Array of length nargout containing the function outputs. Outputs
     * are returned as sub-classes of
     * <code>com.mathworks.toolbox.javabuilder.MWArray</code>. Each output array
     * should be freed by calling its <code>dispose()</code> method.
     * @throws MWException An error has occurred during the function call.
     */
    public Object[] fminsearch_HausdorffDist(int nargout, Object... rhs) throws MWException
    {
        Object[] lhs = new Object[nargout];
        fMCR.invoke(Arrays.asList(lhs), 
                    MWMCR.getRhsCompat(rhs, sFminsearch_HausdorffDistSignature), 
                    sFminsearch_HausdorffDistSignature);
        return lhs;
    }
}

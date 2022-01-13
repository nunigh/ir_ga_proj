package algorithms;

import java.util.List;
import java.util.Map.Entry;

import matlabcontrol.MatlabProxy;
import matlabcontrol.extensions.MatlabTypeConverter;



public class CONST {

	public static boolean _isCentered = false;
	
	public static boolean _shouldValidate = true;
	public static boolean _2D = true;
	
	// maximum size of GP population
	public static int MAX_POPULATION_SIZE = 100;

	// population percent to copy to new generation
	public static double TOP_POPULATION_PRECENT_FOR_COPY = 2.0/100;

	// percent for mutation on a given chromosome
	public static double MUTATE_PERCENT = 1.0;

	// percent for mutation per node
	public static double MUTATE_NODE_PERCENT = 0.3;
	public static double MUTATE_PER_NODE_PERCENT = 0.2;
	public static double DEFAULT_MUTATE_NODE_PERCENT = MUTATE_NODE_PERCENT;

	// crossover percent
	public static double CROSSOVER_PERCENT = 1.0;

	// chromosome tree height limitation
	public static int MAX_DEEP_CHROMOSOME = 7;

	// mutation subtree height limitation
	public static int MAX_DEEP_MUTATION = 3;
	
	// minimum required overlap
	public static double MIN_OVERLAP_PERCENT = 0.35;
	
	public static double MAX_CONVERGENCE_FITNESS = 1.5;
	public static double MAX_GENERATIONS_UNCHANGED = 50;
	public static double FITNESS_CONVERGENCE_NUM = 15;
	public static double FITNESS_CONVERGENCE_DELTA = 1.5;
	
	// islands params
	public static int ISLANDS_NUMBER = 1;
	public static int ISLANDS_GENERATIONS_FOR_MIGRATION = 20;
	public static double ISLANDS_PERCENT_OF_CHROMOSOMES = 0.1;
	
	public static boolean GA_SHUFFLE = true;
	public static boolean WRITE_IMG = false;

	public static MatlabProxy   Myproxy = null;
	public static MatlabTypeConverter processor = null;
	
	public static List<Entry<Entry<Double, Double>,Entry<Double, Double>>> data = null;

	public static String projectPath_hadar = "C:\\Users\\root\\Desktop\\IR_GA\\";
	public static String projectPath_biu = "C:\\Users\\usr\\Desktop\\IR_GA\\";
	public static String projectPath =  projectPath_hadar;
}

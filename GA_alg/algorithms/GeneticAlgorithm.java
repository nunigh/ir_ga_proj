package algorithms;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import utils.InternalMode;
import utils.GPTransformations;
import utils.Utils;

import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.script.ScriptException;
import javax.swing.text.PlainDocument;

import matlabcontrol.MatlabInvocationException;
import matlabcontrol.extensions.MatlabNumericArray;

import org.opencv.core.Point;

import com.sun.org.apache.bcel.internal.generic.POP;
import com.sun.org.apache.xml.internal.security.encryption.Reference;
import com.sun.org.apache.xpath.internal.operations.Mult;

import Fitness.CalculateFitnessTask;
import Fitness.FitnessSIFT;
import Fitness.SIFTTask;
import GA.GAChromosome;
import GA.Structure.Concrete.ReflectionOrigin;
import GA.Structure.Concrete.ReflectionX;
import GA.Structure.Concrete.ReflectionY;
import GA.Structure.Concrete.Rotation;
import GA.Structure.Concrete.Scaling;
import GA.Structure.Concrete.ScalingX;
import GA.Structure.Concrete.ScalingY;
import GA.Structure.Concrete.ShearingX;
import GA.Structure.Concrete.ShearingY;
import GA.Structure.Concrete.TranslationX;
import GA.Structure.Concrete.TranslationY;
import GP.GPChromosome;
import GP.GPStrategy;
import GP.Structure.Concrete.Constant;
import GP.Structure.Concrete.Multiply;
import GP.Structure.Concrete.Plus;
import GP.Structure.Concrete.Variable;
import Interfaces.Chromosome;
import Interfaces.IGeneticStrategy;
import Log.LoggerManager;
import algorithms.Selection.SelectionFactory.SelectionMode;
import base.ChromosomeByFitnessComparator;
import base.ChromosomeByRankComparator;
import base.Fitness;
import base.Population;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import GA.GAStrategy;

class SIFTDiffComparator implements Comparator<Point> {

	@Override
	public int compare(Point arg0, Point arg1) {
		return Double.compare(arg0.y, arg1.y);
	}
}

public class GeneticAlgorithm {

	private LoggerManager _logger = null;
	private File _file = null;
	FileWriter[] _writer;
	BufferedWriter[] _out;
	private InternalMode _internalMode;
	private boolean _isCentered;
	// chromosomes population
	private PopulationManager[] _pop;
	private PopulationManager[] _pop2;

	// selection mode that is used to select chromosome from the population
	private SelectionMode _selectionMode;

	private ArrayList<ArrayList<Entry<Integer, Chromosome>>> _history;

	// //////////// BufferedImage _img1; BufferedImage _img2;
	// Raster _img1Data; Raster _img2Data;

	short[] _referenced;
	short[] _referencedFeatures;
	short[] _sensed;
	short[] _sensedFeatures;

	

	// ///int[][] _tempBest;
	// ///int[][] _tempBestFeatures;

	int _width1;
	int _height1;
	int _width2;
	int _height2;

	int _twidth1;
	int _theight1;
	int _twidth2;
	int _theight2;

	String lastBestDesc = "";

	int _maxFitnessSamples = (int) (256 * 256 * 0.4);
	int _maxSamples = (int) (256 * 256 * 0.4);
	int _maxImageSize = 256 * 256;

	// // SIFT
	static ArrayList<Point> _refPoints = null;
	static ArrayList<Point> _sensedPoints = null;

	ArrayList<Point> _refRealPoints = null;
	ArrayList<Point> _sensedRealPoints = null;

	int _numProcessors = 1;
	ExecutorService _parallelService = null;

	IGeneticStrategy _geneticStrategy = null;


	/**
	 * initialization
	 * 
	 * @param data
	 *            - list of training data entries
	 * @param mode
	 *            - selection mode
	 * @throws Exception
	 *             write
	 */
	public GeneticAlgorithm(IGeneticStrategy strategy, File outputPath, SelectionMode mode, InternalMode internalMode,
			boolean isCentered) throws Exception
	// ##(Raster original1, Raster original2, Raster img1, Raster img2,
	// SelectionMode mode, InternalMode gpmode) throws Exception
	{
		_numProcessors = Runtime.getRuntime().availableProcessors();
		_parallelService = Executors.newFixedThreadPool(_numProcessors);
		_geneticStrategy = strategy;
		_file = outputPath;
		_logger = new LoggerManager(outputPath, CONST.ISLANDS_NUMBER);

		StringBuilder str = new StringBuilder();
		str.append("pop size: " + CONST.MAX_POPULATION_SIZE);
		str.append('\n');
		str.append("# elitism: " + CONST.TOP_POPULATION_PRECENT_FOR_COPY);
		str.append('\n');
		str.append("mutation: " + CONST.MUTATE_PERCENT);
		str.append('\n');
		str.append("mutation node: " + CONST.MUTATE_NODE_PERCENT);
		str.append('\n');
		str.append("crossover: " + CONST.CROSSOVER_PERCENT);
		str.append('\n');
		str.append("max deep: " + CONST.MAX_DEEP_CHROMOSOME);
		str.append('\n');
		str.append("min overlap: " + CONST.MIN_OVERLAP_PERCENT);
		printRank(str.toString(), -1);

		_internalMode = internalMode;
		_selectionMode = mode;
		_isCentered = isCentered;
		/*
		 * _width1 = img1.getWidth(); _height1 = img1.getHeight(); _width2 =
		 * img2.getWidth(); _height2 = img2.getHeight();
		 * 
		 * int res = 1; _twidth1 = img1.getWidth();///res+1; _theight1 =
		 * img1.getHeight();///res+1; _twidth2 = img2.getWidth();///res+1;
		 * _theight2 = img2.getHeight();///res+1;
		 */
		if (Utils.img1 != null) {
			_width1 = Utils.img1.getWidth();
			_height1 = Utils.img1.getHeight();
			_width2 = Utils.img2.getWidth();
			_height2 = Utils.img2.getHeight();

			_twidth1 = Utils.img1.getWidth();// /res+1;
			_theight1 = Utils.img1.getHeight();// /res+1;
			_twidth2 = Utils.img2.getWidth();// /res+1;
			_theight2 = Utils.img2.getHeight();// /res+1;
		}
		int res = 1;
		_referenced = new short[_twidth1 * _theight1];
		_referencedFeatures = new short[_referenced.length];

		_sensed = new short[_twidth2 * _theight2];
		_sensedFeatures = new short[_sensed.length];

		Raster original1 = Utils.original1.getData();
		Raster img1 = Utils.img1.getData();
		Raster original2 = Utils.original2.getData();
		Raster img2 = Utils.img2.getData();
		for (int col = 0; col < _width1; col++) {
			for (int row = 0; row < _height1; row++) {

				_referenced[(int) Math.round(Utils.getIndex(_theight1, col / res, row
						/ res))] = (short) (original1.getSample(col, row,
						0) / (res * res));

				int val = 0;
				val = img1.getSample(col, row, 0);
				if (val < 0.001)
					val = 0;
				_referencedFeatures[(int) Math.round(Utils.getIndex(_theight1, col
						/ res, row / res))] = (short) (val / (res * res));
			}
		}

		for (int col = 0; col < _width2; col++) {
			for (int row = 0; row < _height2; row++) {
				_sensed[(int) Math.round(Utils.getIndex(_theight2, col / res, row
						/ res))] = (short) (original2.getSample(col, row,
						0) / (res * res));

				int val = 0;
				val = img2.getSample(col, row, 0);
				if (val < 0.001)
					val = 0;
				_sensedFeatures[(int) Math.round(Utils.getIndex(_theight2, col / res,
						row / res))] = (short) (val / (res * res));
			}
		}

	
	}

	public void InitFeaturesPoints(ArrayList<Point> ref, ArrayList<Point> sensed) {
		_refPoints = new ArrayList<Point>();
		_sensedPoints = new ArrayList<Point>();

		

		for (int i = 0; i < ref.size(); i++) {
			Point refP = new Point(ref.get(i).x, ref.get(i).y);
		
			int noise = 0;//4;
			double prob = 0;
			if (Utils.rand.nextDouble() > prob)
				refP.x += Utils.rand.nextGaussian() * noise;
			if (Utils.rand.nextDouble() > prob)
				refP.y += Utils.rand.nextGaussian() * noise;
			
			_refPoints.add(refP);

		}
		
		for (int i = 0; i < sensed.size(); i++) {
		
			Point sensedP = new Point(sensed.get(i).x, sensed.get(i).y);

			int noise = 0;//4;
			double prob = 0;
			if (Utils.rand.nextDouble() > prob)
				sensedP.x += Utils.rand.nextGaussian() * noise;
			if (Utils.rand.nextDouble() > prob)
				sensedP.y += Utils.rand.nextGaussian() * noise;

			_sensedPoints.add(sensedP);

		}
		
	//	Collections.shuffle(_sensedPoints);
		
		/*
		Point p1 = new Point(Utils.rand.nextInt(Utils.original1.getWidth()),
							Utils.rand.nextInt(Utils.original1.getHeight()));
		Point p2 = new Point(Utils.rand.nextInt(Utils.original1.getWidth()),
						Utils.rand.nextInt(Utils.original1.getHeight()));
		_refPoints.add(p1);
		_refPoints.add(p2);
		
		Point p21 = new Point(Utils.rand.nextInt(Utils.original2.getWidth()),
							Utils.rand.nextInt(Utils.original2.getHeight()));
		Point p22 = new Point(Utils.rand.nextInt(Utils.original2.getWidth()),
				Utils.rand.nextInt(Utils.original2.getHeight()));

		_sensedPoints.add(p21);
		_sensedPoints.add(p22);
		*/
		
		/*
		 * Collections.sort(diff, new SIFTDiffComparator()); for (int i = 0; i <
		 * 0.3*ref.size(); i++) { //if (diff.get(i).y < 30) { int index = (int)
		 * diff.get(i).x; _refPoints.add(ref.get(index));
		 * _sensedPoints.add(sensed.get(index)); } }
		 */
		
		/* HD
		_referencedFeatures = new short[_referenced.length];
		for (int i = 0; i < _refPoints.size(); i++) {
			_referencedFeatures[(int) Math.round(Utils.getIndex(_theight1, _refPoints.get(i).x, _refPoints.get(i).y))] = 255;
		}
		_sensedFeatures = new short[_sensed.length];
		for (int i = 0; i < _sensedPoints.size(); i++) {
			_sensedFeatures[(int) Math.round(Utils.getIndex(_theight2, _sensedPoints.get(i).x, _sensedPoints.get(i).y))] = 255;
		}
		*/

	}

	public void InitRealPoints(ArrayList<Point> ref, ArrayList<Point> sensed) {
		_refRealPoints = new ArrayList<Point>();
		_sensedRealPoints = new ArrayList<Point>();
		for (int i = 0; i < ref.size(); i++) {
			_refRealPoints.add(ref.get(i));
			_sensedRealPoints.add(sensed.get(i));
		}
	}

	ArrayList<Point> points1 = null;
	ArrayList<Point> points2 = null;
	@SuppressWarnings("unused")
	public void Init(PopulationManager pop, boolean mustreset) throws Exception {

		//if (pop != null)
		{
			_mustreset = mustreset;
		}
		// //_tempBest = new int[_referenced.length][_referenced[0].length];
		// //_tempBestFeatures = new
		// int[_referenced.length][_referenced[0].length];
		

		Comparator<Chromosome> c = InitComparator();

		_pop = new PopulationManager[CONST.ISLANDS_NUMBER];
		if (_internalMode == InternalMode.SIFT && 1 == 0)
			_pop2 = new PopulationManager[CONST.ISLANDS_NUMBER];

		_history = null;// new
						// ArrayList<ArrayList<Entry<Integer,Chromosome>>>();
		if (_history != null) {
			_writer = new FileWriter[CONST.ISLANDS_NUMBER];
			_out = new BufferedWriter[CONST.ISLANDS_NUMBER];
		}
		for (int p = 0; p < _pop.length; p++) {
			_pop[p] = new PopulationManager(2 * CONST.MAX_POPULATION_SIZE, c);
			if (_pop2 != null)
				_pop2[p] = new PopulationManager(2 * CONST.MAX_POPULATION_SIZE,
						c);

			if (_history != null) {
				_history.add(new ArrayList<Entry<Integer, Chromosome>>());
				_writer[p] = new FileWriter(String.format("%s%sbest%d.txt",
						_file.getAbsolutePath(), File.separatorChar, p));
				_out[p] = new BufferedWriter(_writer[p]);
			}
			
			

			if (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) {
				points1 = GetSIFTPoints(_refPoints);
				points2 = GetSIFTPoints(_sensedPoints);
			}
			
			if (pop != null)
			{
				
				for (int i = 0; i < pop.Size();  i++) {
					Chromosome ch = pop.GetChromosome(i);
					//if (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) {					
					//	SetSIFTFitness(ch, true);
				//	} else {
						SetFitness(ch, p);
					//}
					_pop[p].AddChromosome(ch);
				}
			}
			else
			{
				for (int i = 0; i < 2 * CONST.MAX_POPULATION_SIZE; i++) {
					Chromosome ch = _geneticStrategy.CreateChromosome();
					ch.Init();

					//if (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) {					
					//	SetSIFTFitness(ch, true);
					//} else {
						SetFitness(ch, p);
					//}
	
					_pop[p].AddChromosome(ch);
				}
			}

			_pop[p].SetChromosomesList(_pop[p]
					.GetTopsChromosomes(CONST.MAX_POPULATION_SIZE));
			
			Selection.SelectionFactory.CreateSelection(_selectionMode,
					_pop[p].get_pop());

			if (_pop2 != null) {
				for (int i = 0; i < 2 * CONST.MAX_POPULATION_SIZE; i++) {
					Chromosome ch = _geneticStrategy.CreateChromosome();
					ch.Init();
					ch.AddLog("New", String.format("Init (island: %d)", p));

				//	if (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) {
						
					//	SetSIFTFitness(ch, true);
						
				//	} else {
						SetFitness(ch, 1);
					//}
					_pop2[p].AddChromosome(ch);
				}
			}
		}

		for (int p = 0; p < _pop.length; p++) {
			if (_pop2 != null)
				System.out
						.println("X ---------------------------------------------------------");
			printAll(GetBest(_pop[p]), 0, 0, 0, p, _pop[p]);

			if (_pop2 != null) {
				System.out
						.println("Y ---------------------------------------------------------");
				printAll(GetBest(_pop2[p]), 0, 0, 0, p, _pop2[p]);
			}
		}

	}
	
	private Comparator<Chromosome> InitComparator()
	{
		Comparator<Chromosome> c = null;
		if (_internalMode == InternalMode.MI || _internalMode == InternalMode.HD
				|| _internalMode == InternalMode.HD_ALL || _internalMode == InternalMode.SIFT)
			c = new ChromosomeByFitnessComparator();
		else if (_internalMode == InternalMode.MultiObjective || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2
				|| _internalMode == InternalMode.MultiObjectiveMI
				|| _internalMode == InternalMode.MultiObjectiveHD)
			c = new ChromosomeByRankComparator();
		
		return c;
	}

	
	

	// #########################################################################################3

	/**
	 * run the GP algorithm
	 * 
	 * @throws Exception
	 */

	
	public void Run() throws Exception {
	
		Run(false);
	}

	public static int _gen = 0;
	private boolean debug = false;
	public long start = 0;
	public long current = 0;

	Point[][] _SIFTDist = null;
	boolean _fromSIFT = false;

	boolean _reset = false; 
	boolean _hasreset = false;
	boolean _mustreset = false;
	int final_counter = 0;
	public void Run(boolean t) throws Exception {

		start = System.currentTimeMillis();
		boolean cont = true;
		boolean cont2 = false;
		while (cont && _gen <1500)// hadar added 1500 limit// ||(ch==null || ch.get_fitness()>4.8||
					// ch.overlapPercent<0.5 || !t || good < 10 ||
					// fitnessUnchangedCount < 15)
		// && (t || current - start < 10*60000) && (ch == null || current -
		// start < 1*60000 || fitnessUnchangedCount < 15 || ch.overlapPercent <
		// 0.75)){// || ch.goodPercent < 0.5))
		{
			Chromosome cc = GetBest(_pop[0]);
			boolean a = false;
			boolean b = false;
			if (a || !_hasreset && (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO) && 
					(cc.get_fitness().GetValue(0) < 6 || //(cc.get_fitness().Length() ==1 || cc.get_fitness().GetValue(1) < 6)) &&
					_pop[0].isFitnessConverge() ||				_pop[0].getFitnessUnchangedCount() >  CONST.MAX_GENERATIONS_UNCHANGED*4))//
			{

				_hasreset = true;
				
				if (a || _internalMode != InternalMode.SIFT_MOO2 &&
						_mustreset || 
						!(GetBest(_pop[0]).get_fitness().GetValue(0) < (double)Utils.size * 0.05)) 
						
				{
					_reset = true;
					_pop[0].setInitFitness();
				}

			}
			
			//Log(String.format("START %d",current = System.currentTimeMillis()), 0);
			if (_reset) // TODO: currently hard coded 24.4.16
			{
			
				//CONST.MAX_POPULATION_SIZE = 20;
				//CONST.TOP_POPULATION_PRECENT_FOR_COPY = 2.0/20.0;
					
				_internalMode = InternalMode.SIFT_MOO2;
				_selectionMode = SelectionMode.NSGARankedBasedSelection;//TournamentSelection;//NSGARankedBasedSelection; // RoletteWheelSelection//.TournamentSelection;
				if (_geneticStrategy instanceof GAStrategy)
					_geneticStrategy = new GAStrategy(_internalMode);
				else
					_geneticStrategy = new GPStrategy(_internalMode);
				
				Comparator<Chromosome> c = InitComparator();
				for (int p = 0; p < _pop.length; p++) {
					_pop[p].setComparator(c);
					for (int i = 0; i < _pop[p].Size(); i++) {
						SetFitness(_pop[p].GetChromosome(i),p);
					}
					_pop[p].setFitnessUnchanged(_pop[p].GetBestChromosome().get_fitness());
					_pop[p].setFitnessUnchangedCount(0);
					if (_pop2 != null)
						_pop2[p].setComparator(c);
					
					Selection.SelectionFactory.CreateSelection(_selectionMode,
							_pop[p].get_pop());
					
					printAll(GetBest(_pop[p]), 0, 0, 0, p, _pop[p]);
				}
				
			
				

				
				_reset = false;
			}
			// calculate next generation
			current = System.currentTimeMillis();
			for (int island = 0; island < _pop.length; island++) {
				Run(island);
			}
			cont = false;
			for (int i = 0; i < _pop.length; i++) {
				cont = cont || _pop[i].isCont();
			}
			
			if (!cont) {
				if (!cont && cont2) // for debugging ...
				{
					cont2 = false;
					if (_internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO2) {
						cont = true;
						_internalMode = InternalMode.MI;
						_selectionMode = SelectionMode.TournamentSelection;
						Comparator<Chromosome> c = new ChromosomeByFitnessComparator();
						for (int i = 0; i < _pop.length; i++) {
							Population p = _pop[i].get_pop();
							int count = _pop[i].getFitnessUnchangedCount();
							int index = _pop[i].getBestIndex();
							_pop[i] = new PopulationManager(_pop[i].Size(), c);
							_pop[i].SetChromosomesList(p.get_pop());
							_pop[i].setFitnessUnchangedCount(count);
							_pop[i].setBestIndex(index + 1);
							_pop[i].setCont(true);
						}

						for (int i = 0; i < _pop.length; i++) {
							Chromosome best = GetBest(_pop[i]);
							if (i == 0) {

								_SIFTDist = new Point[_width2][_height2];
								double[] newPos = null;
								for (int col = 0; col < _width2; col++) {
									for (int row = 0; row < _height2; row++) {
										newPos = T(best, col, row);
										_SIFTDist[col][row] = new Point(
												newPos[0], newPos[1]);
									}

								}
							}
							_fromSIFT = true;

							for (int j = 0; j < _pop[i].Size(); j++) {
								SetFitness(_pop[i].GetChromosome(j), 0);
							}
							Fitness f = best.get_fitness();
							_pop[i].setNewFitness(f);
							_pop[i].setInitFitness();
							_pop[i].setFitnessUnchanged(f);

						}
						CONST.MAX_CONVERGENCE_FITNESS = 0.3;

					}
				}
			} else {
				if (_pop.length > 1 && _gen > 0
						&& _gen % CONST.ISLANDS_GENERATIONS_FOR_MIGRATION == 0) // migration
				{
					Migrate();
				}
			}
						
			Chromosome ch = GetBest(_pop[0]);
			
			// str = new StringBuilder();
			//str.append("### _hasreset ");
			//str.append(_hasreset);
			//str.append(" Converge ");
			//str.append(_pop[0].isFitnessConverge());
			//str.append(" Unchanged ");
			//str.append(_pop[0].getFitnessUnchangedCount());
			//str.append(" # ");
			//str.append(final_counter);
			//logger.Log(str .toString() + '\n',0);
			
			if (_hasreset)
			{
				double rmse = _geneticStrategy.CalculateRMSE(_refRealPoints, _sensedRealPoints, ch, null, false);
				if (rmse < 2.5)
					final_counter = final_counter+1;
				else
					final_counter=0;
				
			}
			
			if ((_hasreset || !_mustreset) && 
					 (ch.get_fitness().GetValue(0) < 150 || b) &&// (ch.get_fitness().Length() ==1 || ch.get_fitness().GetValue(1) < 5)) && //(double)Utils.size * 0.05) &&
					 //(ch.get_fitness().Length() == 1 || ch.get_fitness().GetValue(1) <  -0.75) &&
					( ((_mustreset && _pop[0].isFitnessConverge() || _pop[0].getFitnessUnchangedCount() >  3 * CONST.MAX_GENERATIONS_UNCHANGED )&&
						 
					 (_pop[0].getFitnessUnchangedCount() >  CONST.MAX_GENERATIONS_UNCHANGED || ch.get_fitness().GetValue(0) < 3 && ch.get_fitness().GetValue(0) < 3))
					 ||
					((_hasreset ||!_mustreset) && _pop[0].isFitnessConverge() && (_pop[0].getFitnessUnchangedCount() >  CONST.MAX_GENERATIONS_UNCHANGED || final_counter > 20)) 
					|| 
					b))
			{
				b = false;
				cont = false;
			}

			//Log(String.format("END %d",current = System.currentTimeMillis()), 0);
			_gen++;
		}

		current = System.currentTimeMillis();
		for (int island = 0; island < _pop.length; island++) {
			printRank("-----------------------------------------", island);
			Chromosome best = GetBest(_pop[island]);
			printAll(best, current - start, _gen,
					_pop[island].getFitnessUnchangedCount(), island,
					_pop[island]);
		
		}

	}

	private void Migrate() throws Exception {
		for (int i = 0; i < CONST.ISLANDS_NUMBER-1; i++) {
			NextGeneration((i + 1) % CONST.ISLANDS_NUMBER, i, _pop[(i + 1)% CONST.ISLANDS_NUMBER], _pop[i], 0);
		}
		if (CONST.ISLANDS_NUMBER > 1)
		{
			NextGeneration(CONST.ISLANDS_NUMBER-1, 0, _pop[CONST.ISLANDS_NUMBER-1], _pop[0], 0);
		}
	}

	private boolean Run(int island, PopulationManager pop) throws Exception {
		boolean isnew = false;

		// set cont & mutation rate
		Chromosome bestForIsland = GetBest(pop);
		pop.setCont(true);

		// set unchanged count and fitness history
		boolean unchanged = false;
		if (bestForIsland != null) {
			if (_internalMode == InternalMode.MultiObjective
					|| _internalMode == InternalMode.MultiObjectiveMI) {
				if (!MOO_flag_changed)
					unchanged = true;
			} else {
				if (pop.getFitnessUnchanged() != null
						&& bestForIsland.get_fitness().compareTo(
								pop.getFitnessUnchanged()) == 0)
					unchanged = true;
			}
		}

		if (unchanged) {
			pop.setFitnessUnchangedCount(pop.getFitnessUnchangedCount() + 1);
			if (pop.getFitnessUnchangedCount() >= 10) {
				if (pop.getFitnessUnchangedCount() % 10 == 0
						&& pop.getMutationRate() < CONST.DEFAULT_MUTATE_NODE_PERCENT + 0.2) {
					pop.setMutationRate(pop.getMutationRate() + 0.02);
					
				}

			} else {
				pop.setMutationRate(CONST.DEFAULT_MUTATE_NODE_PERCENT);
			}

		} else {
			pop.setFitnessUnchangedCount(0);
			pop.setMutationRate(CONST.DEFAULT_MUTATE_NODE_PERCENT);
			pop.setNewFitness(bestForIsland.get_fitness());
		}

		if ((pop.getFitnessUnchangedCount() > CONST.MAX_GENERATIONS_UNCHANGED || pop
				.isFitnessConverge())
				&& GetSolution().get_fitness().GetValue(0) < CONST.MAX_CONVERGENCE_FITNESS
				&& (_internalMode != InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2 || GetSolution().get_fitness()
						.GetValue(1) < CONST.MAX_CONVERGENCE_FITNESS)) {
			pop.setCont(false);
		}

		// log & save last fitness
		if (bestForIsland != null && pop.getFitnessUnchangedCount() == 0)// lastBestDesc.compareTo(newDesc)
																			// !=
																			// 0)
		{
			isnew = (pop.getBestDescription() == null || pop
					.getBestDescription().compareTo(bestForIsland.toString()) != 0);
			if (isnew)
				pop.setBestIndex(pop.getBestIndex() + 1);

			// printRank(getBestPrintChrom(bestForIsland, island), island);
			// printRank(String.format("Gen #%d     Time: %d", gen,(current
			// -start)/ 1000), island);

			if (_history != null && isnew)

			{
				_history.get(island).add(
						new SimpleEntry<Integer, Chromosome>(_gen,
								bestForIsland));
				bestForIsland.AddLog("Best", ((Double) bestForIsland
						.get_fitness().GetValue(0)).toString());

				_out[island]
						.write("---------------------------------------------------------------\n");
				_out[island].write(String.format("gen: %d, %s\n", _gen,
						bestForIsland.toString()));
				// _out[island].write(bestForIsland.getHistory());
				_out[island].flush();

				pop.setBestDescription(bestForIsland.toString());

			}
			// lastBestDesc = ch.toString();
			pop.setFitnessUnchanged(bestForIsland.get_fitness().clone());
		}

		return isnew;
	}

	boolean write = true;
	int siftID = 1;

	private void Run(int island) throws Exception {

		boolean isnew = Run(island, _pop[island]);
		if (_pop2 != null) {
			isnew = isnew || Run(island, _pop2[island]);
			// writeChromosomeImg(_pop[island].GetBestChromosome(), siftID,
			// true, island);
			siftID++;
		} else {
			if ((isnew && write))// || _gen%100==0)
			{
				Chromosome cc = GetBest(_pop[island]);
				// Utils.writeChromosomeImg(cc.get_root(0).getDescription(),
				// cc.get_root(1).getDescription(), _file.getAbsolutePath(),
				// _pop[island].getBestIndex(), island, false);
				Utils.writeChromosomeImg(cc, _file.getAbsolutePath(), island,
						_pop[island].getBestIndex(), false);
			}
		}

		if (_gen % 1 == 0 && _gen != 0) {
			if (_pop2 != null)
				System.out
						.println("X ---------------------------------------------------------");
			
			Chromosome best = GetBest(_pop[island]);
			printAll(best, current - start, _gen,
					_pop[island].getFitnessUnchangedCount(), island,
					_pop[island]);

			if (_pop2 != null) {
				System.out
						.println("Y ---------------------------------------------------------");
				printAll(_pop2[island].GetBestChromosome(), current - start,
						_gen, _pop2[island].getFitnessUnchangedCount(), island,
						_pop2[island]);
			}

		}
		printRank(String.format("Gen #%d    Time: %d", _gen,
				(current - start) / 1000), island);

	
		//Log(String.format("START GEN %d",current = System.currentTimeMillis()), 0);
		NextGeneration(island);
		//Log(String.format("END GEN %d",current = System.currentTimeMillis()), 0);


	}

	private void printAll(Chromosome best, long time, int gen,
			int fitnessUnchangedCount, int island, PopulationManager pop)
			throws Exception {

		
		StringBuilder str = new StringBuilder();


		pop.Sort();
	
		for (int i = 0; i < pop.Size(); i++) {
			Chromosome cc = pop.GetChromosome(i);
			// if (cc.getValue(0) > 1e3)
			// {
			// return;
			// }
			if (CONST.PRINT_ALL_CHROMOSOMES) {
				str.append(getPrintChrom(cc));
				str.append('\n');
			}
		}
		
		
		str.append(getBestPrintChrom(best, island, pop));
		printRank(str.toString(), island);
		
		if (test)
			Utils.RunTest(best, best.matchesRefs, best.matchesSensed)	;

		// //////////////////////printRank(String.format("**BEST FITNESS: %s",
		// best.get_fitness().toString()), island);

	}
	private boolean test = false;
	
	@SuppressWarnings("unused")
	private void printChrom(Chromosome cc, int island) throws Exception {
		printRank(getPrintChrom(cc), island);
	}

	boolean printRMSE = false;
	private String getBestPrintChrom(Chromosome cc, int island,
			PopulationManager pop) throws Exception {
		double real = -1;
		if (_refRealPoints != null) {
			boolean b  = SetFitness(cc, 0); //
			real = _geneticStrategy.CalculateRMSE(_refRealPoints, _sensedRealPoints, cc, null, printRMSE);
		
			
		}
	
		StringBuilder str = new StringBuilder();
		str.append("best ");
		str.append(pop.getBestIndex());
		str.append(" : unchanged: ");
		str.append(pop.getFitnessUnchangedCount());
		str.append(", ");
		if (real >= 0) {
			str.append("RMSE: ");
			str.append(real);
			str.append(", ");
		}
		str.append(getPrintChrom(cc));
		// str.append(", ");
		// str.append(cc.getDescription());
		return str.toString();
	}

	boolean printDesc = false;

	private String getPrintChrom(Chromosome cc) throws Exception {
		StringBuilder str = new StringBuilder();
		str.append("fitness: ");
		str.append(cc.get_fitness());
		//str.append(",");
		//str.append(cc._maxDiff);

		str.append(", overlap: ");
		str.append(cc.overlapPercent);
		str.append(", rank: ");
		str.append(cc.rank);
		str.append(", ");
		str.append(cc.toString());
		str.append(", ");
		str.append(cc.getInfo());
		if (printDesc) {
			str.append(", ");
			str.append(cc.getDescription());
		}

		return str.toString();
	}

	@SuppressWarnings("unused")
	private void writeChromosomeImg(Chromosome ch, TransformResult res, int i, boolean diff,
			int island) throws IOException, ScriptException {

		

		// int deep = ch.GetDeep();
		/*
		 * if (deep > CONST.MAX_DEEP_CHROMOSOME) { String[] f =
		 * ch.toString().split(";"); f[0] = Utils.fixTransformation(f[0],
		 * _width1, _height1, _width2, _height2, 0, _isCentered); f[1] =
		 * Utils.fixTransformation(f[1], _width1, _height1, _width2, _height2,
		 * 1, _isCentered); scriptX = compilingEngine.compile(f[0]); scriptY =
		 * compilingEngine.compile(f[1]); }
		 */

		BufferedImage bufferedImage = new BufferedImage(_twidth1, _theight1,
				BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = bufferedImage.getRaster();

		
		/*
		 * if (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO) { String sx
		 * = ch.get_root(0).getShortDescription(); sx =
		 * Utils.fixTransformation(sx, _width1, _height1, _width2, _height2, 0,
		 * _isCentered);
		 * 
		 * String sy = ch.get_root(1).getShortDescription(); sy =
		 * Utils.fixTransformation(sy, _width1, _height1, _width2, _height2, 0,
		 * _isCentered);
		 * 
		 * GeneticAlgorithm.scriptX = GeneticAlgorithm.compilingEngine.compile(sx);
		 * GeneticAlgorithm.scriptY = GeneticAlgorithm.compilingEngine.compile(sy); }
		 */
		for (int col = 0; col < _twidth2; col++) {
			for (int row = 0; row < _theight2; row++) {

				// //////////////////////////////////////////////////////////////////////////////////////////////
				// 2D
				double[] newPos = null;
				double newIndex = -1;
				if (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) {

					double[] pos = ch.GetValue(col, row);
					double x = pos[0];
					double y = pos[1];
					//double x = ch.GetValue((0, col, row);// GetValue(0, col,
														// row);
					//double y = ch.GetValue(1, col, row);// GetValue(1, col,
														// row);
					newPos = new double[] { x, y };
					newIndex = Utils.getIndex(_theight1, x, y);
				} else if (CONST._2D) {
					// if (deep > CONST.MAX_DEEP_CHROMOSOME)
					// newPos = T(col, row);
					// else
					double[] t = T(ch, col, row);
					newPos = new double[] { t[0], t[1] };
					newIndex = (int) Math.round(Utils.getIndex(_theight1, newPos[0],
							newPos[1]));
				} else {

					newIndex = T1(ch, col, row);
					int newRow = -1;
					int newCol = -1;
					if (newIndex >= 0 && newIndex < _twidth1 * _theight1) {
						newRow = (int) Math.round((newIndex % _theight1));
						newCol = (int) Math
								.round(((newIndex - newRow) / _theight1));
					}
					newPos = new double[] { newCol, newRow };
				}

				if (newPos[0] >= 0 && newPos[0] < _twidth1 && newPos[1] >= 0
						&& newPos[1] < _theight1 && newIndex >= 0
						&& newIndex < res._tempOverlap.length) {
					newIndex = Math.round(newIndex);
					res._tempOverlap[(int) newIndex] = true;
					res._tempSensedTransformed[(int) newIndex] = _sensed[(int) Math
							.round(Utils.getIndex(_theight2, col, row))];
					res._tempSensedFeaturesTransformed[(int) newIndex] = (short) Math
							.abs(res._tempSensedTransformed[(int) newIndex]
									- _referenced[(int) newIndex]); // re-using
																	// array ...

					raster.setSample((int) Math.round(newPos[0]),
							(int) Math.round(newPos[1]), 0,
							res._tempSensedTransformed[(int) newIndex]);
					// ////////////_tempBestFeatures[newPos[0]][newPos[1]] =
					// _sensedFeatures[col][row];
				}

			}
		}
		String fileName = String.format("%s%s%d_best%d.jpg",
				_file.getAbsolutePath(), File.separatorChar, island, i);
		// File outputfile = new File(fileName);
		// ImageIO.write(bufferedImage, "jpg", outputfile);
		writeImg(res._tempSensedTransformed, _twidth1, _theight1, fileName);

		// writeImg(_tempSensedTransformed, _twidth1, _theight1, "best\\best!" +
		// i + ".jpg");
		// //////////writeImg(_tempBestFeatures, "best\\bestFeatured.png");

		bufferedImage = null;

		if (diff) {
			/*
			 * for(int y=0; y< _tempSensedTransformed.length; y++) { if
			 * (_tempOverlap[y]) _tempSensedTransformed[y] = (short)
			 * Math.abs(_tempSensedTransformed[y]- _referenced[y]); else
			 * _tempSensedTransformed[y] = 0;
			 * 
			 * }
			 */
			writeImg(res._tempSensedFeaturesTransformed, _twidth1, _theight1,
					String.format("%s%s%d_diff%d.jpg", _file.getAbsolutePath(),
							File.separatorChar, island));
		}

	}

	private void writeImg(short[] img, int width, int height, String path)
			throws IOException {

		// BufferedImage bufferedImage = new BufferedImage(width,height,
		// BufferedImage.TYPE_INT_RGB);
		BufferedImage bufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);

		WritableRaster raster = bufferedImage.getRaster();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// / int value = img[getIndex(height,x,y)] << 16 |
				// img[getIndex(height,x,y)] << 8 | img[getIndex(height,x,y)];
				// /bufferedImage.setRGB(x,y,value);
				raster.setSample(x, y, 0,
						img[(int) Math.round(Utils.getIndex(height, x, y))]);
			}
		}
		File outputfile = new File(path);
		ImageIO.write(bufferedImage, "jpg", outputfile);

	}

	/*
	 * public static void writeResult(String output) throws IOException {
	 * BufferedWriter out = null; try { FileWriter writer = new
	 * FileWriter(_file.getAbsolutePath() + File.separatorChar + "best.txt");
	 * out = new BufferedWriter(writer); out.write(output); } catch (Exception
	 * e) { e.getStackTrace(); } finally { if (out != null) out.close(); } }
	 */

	/**
	 * calculate the next generation
	 * 
	 * @throws Exception
	 */
	private void NextGeneration(int island) throws Exception {

		NextGeneration(island, island, _pop[island], _pop[island], 0);
		if (_pop2 != null)
			NextGeneration(island, island, _pop2[island], _pop2[island], 1);
	}

	boolean MOO_flag_changed = false;

	private void NextGeneration(int islandFrom, int islandTo,
			PopulationManager popFrom, PopulationManager popTo, int popNum)
			throws Exception {
		// initialize selection
		
		
		Selection selectionStrategy1 = Selection.SelectionFactory
				.CreateSelection(_selectionMode, popFrom.get_pop());
		Selection selectionStrategy2;
		if (islandFrom == islandTo)
			selectionStrategy2 = selectionStrategy1;
		else
			selectionStrategy2 = Selection.SelectionFactory.CreateSelection(
					_selectionMode, popTo.get_pop());
		
		Chromosome lastBest = popTo.GetBestChromosome();
		
		// initialize new population
		Comparator<Chromosome> c = null;
		if (_internalMode == InternalMode.MI || _internalMode == InternalMode.HD
				|| _internalMode == InternalMode.HD_ALL || _internalMode == InternalMode.SIFT)
			c = new ChromosomeByFitnessComparator();
		else if (_internalMode == InternalMode.MultiObjective || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2
				|| _internalMode == InternalMode.MultiObjectiveMI
				|| _internalMode == InternalMode.MultiObjectiveHD)
			c = new ChromosomeByRankComparator();
		
		Population newPopulation = new Population(CONST.MAX_POPULATION_SIZE, c);

		// calculate number of chromosomes to copy from currnet population to
		// the new population
		int newpopSize = 0;
		if (islandFrom == islandTo) {
			int numParents = (int) Math
					.ceil((CONST.MAX_POPULATION_SIZE * CONST.TOP_POPULATION_PRECENT_FOR_COPY));
			if (numParents % 2 != 0)
				numParents++;
			newpopSize = CONST.MAX_POPULATION_SIZE;// - numParents;
		} else {
			newpopSize = (int) Math
					.ceil((CONST.MAX_POPULATION_SIZE * CONST.ISLANDS_PERCENT_OF_CHROMOSOMES));
		}

		List<Callable<Object>> r = new ArrayList<Callable<Object>>(newpopSize);
		
		if (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) {
			points1 = GetSIFTPoints(_refPoints);
			points2 = GetSIFTPoints(_sensedPoints);
		}
		
		//Log(String.format("GEN INIT %d",current = System.currentTimeMillis()), 0);
		
		//until population size is reached
		while ((_internalMode != InternalMode.SIFT && _internalMode != InternalMode.SIFT_MOO && _internalMode != InternalMode.SIFT_MOO2 && newPopulation.Size() < newpopSize)
				|| ((_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) && 
						//newPopulation.Size() < newpopSize) )
						r.size() < newpopSize)) 
						{

			// perform cross over - for 2 selected chromosomes from current
			// population
			Chromosome c1 = selectionStrategy1.Select().clone();
			Chromosome c2 = selectionStrategy2.Select().clone();
			//Log(String.format("GEN SELECT %d",current = System.currentTimeMillis()), 0);
			
			// printRank("crossover: " +c1.toString() + " -- " + c2.toString());
			Chromosome[] childs = CrossOver(c1, c2);
			//Log(String.format("GEN CROSSOVER %d",current = System.currentTimeMillis()), 0);
			childs[0].AddLog("CrossOver", String.format(
					"Parent 1: %s (island: %d), Parent 2: %s (island: %d)",
					c1.toString(), islandFrom, c2.toString(), islandTo));
			childs[1].AddLog("CrossOver", String.format(
					"Parent 1: %s (island: %d), Parent 2: %s (island: %d)",
					c1.toString(), islandFrom, c2.toString(), islandTo));
			if (childs == null ||
					childs[0] == null || childs[1] == null)
				continue;
			// Share(childs[0]);
			// Share(childs[1]);

			// perform mutation on the new child chromosomes
			// if (new Random().nextDouble() <= CONST.MUTATE_PERCENT)
			Mutation(childs[0], popTo.getMutationRate());
			Mutation(childs[1], popTo.getMutationRate());
			//Log(String.format("GEN MUTATION %d",current = System.currentTimeMillis()), 0);
			// update fitness for the new child chromosomes
			boolean ok1 = true;
			boolean ok2 = true;
			if (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) {

				
				//r.add(Executors.callable(new SIFTTask(_internalMode, points1, points2, childs[0], false)));
				r.add(Executors.callable(new CalculateFitnessTask(childs[0], false, popNum, _internalMode, _referenced, _sensed, _referencedFeatures, _sensedFeatures,
						_twidth1, _theight1, _twidth2, _theight2, _refPoints, _sensedPoints, _maxImageSize, _maxSamples, _isCentered, _height2, points1, points2)));
				//new CalculateFitnessTask(childs[0], false, popNum, _internalMode, _referenced, _sensed, _referencedFeatures, _sensedFeatures,
					//	_twidth1, _theight1, _twidth2, _theight2, _refPoints, _sensedPoints, _maxImageSize, _maxSamples, _isCentered, _height2, points1, points2).run();
				//SetSIFTFitness(childs[0]);
				
				//r.add(Executors.callable(new SIFTTask(_internalMode, points1, points2, childs[1], false)));
				r.add(Executors.callable(new CalculateFitnessTask(childs[1], false, popNum, _internalMode, _referenced, _sensed, _referencedFeatures, _sensedFeatures,
						_twidth1, _theight1, _twidth2, _theight2, _refPoints, _sensedPoints, _maxImageSize, _maxSamples, _isCentered, _height2, points1, points2)));
				//new CalculateFitnessTask(childs[1], false, popNum, _internalMode, _referenced, _sensed, _referencedFeatures, _sensedFeatures,
					//	_twidth1, _theight1, _twidth2, _theight2, _refPoints, _sensedPoints, _maxImageSize, _maxSamples, _isCentered, _height2, points1, points2).run();
				//SetSIFTFitness(childs[1]);
				
			} else {
				ok1 = SetFitness(childs[0], popNum);
				ok2 = SetFitness(childs[1], popNum);
			}

			// add the new children to new population
			if (ok1)// && childs[0].get_fitness().GetValue(0) < 200)
				newPopulation.AddChromosome(childs[0]);
			if (ok2)// && childs[1].get_fitness().GetValue(0) < 200)
				newPopulation.AddChromosome(childs[1]);
		}
		List<Future<Object>> answers = null;
		//Log(String.format("1 %d",System.currentTimeMillis()), 0);
		//Log(String.format("GEN FITNESS 0 %d",current = System.currentTimeMillis()), 0);
		if (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) {
			answers = _parallelService.invokeAll(r);
			
			//Log(String.format("GEN FITNESS 1 %d",current = System.currentTimeMillis()), 0);
			/*for (int i = 0; i < newPopulation.Size(); i++) {
				SetSIFTFitness(newPopulation.GetChromosome(i), false);
			}*/
			//Log(String.format("GEN FITNESS 2 %d",current = System.currentTimeMillis()), 0);
		}
		//Log(String.format("GEN FITNESS 3 %d",current = System.currentTimeMillis()), 0);
		
	
		if (islandFrom == islandTo) {

			Selection.SelectionFactory.CreateSelection(_selectionMode, newPopulation);
			// add best chromes
			ArrayList<Chromosome> bestParents = GetBestParents(popTo, newPopulation);
			
			if (_internalMode == InternalMode.SIFT || _internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) {
				
				/*if (popTo.getFitnessUnchangedCount() >= 100 && popTo.getFitnessUnchangedCount() % 100 == 0) // re-evaluate fitness
				{
					
					points1 = GetSIFTPoints(_refPoints);
					points2 = GetSIFTPoints(_sensedPoints);
					
					for (int i = 0; i < bestParents.size(); i++) {
						
						Chromosome cc = bestParents.get(i);
						SetSIFTFitness(cc, true);
					}
				}*/
			}
				
			popTo.SetChromosomesList(bestParents);
		
			// add chromosomes from new population
			if (CONST.MAX_POPULATION_SIZE > bestParents.size())
				popTo.Union(newPopulation.GetTopsChromosomes(CONST.MAX_POPULATION_SIZE - Math.max(10, bestParents.size())));// get_pop());
			else
				popTo.Union(newPopulation.GetTopsChromosomes((int)(CONST.MAX_POPULATION_SIZE/2.0)));// get_pop());
			//Log(String.format("GEN BEST %d",current = System.currentTimeMillis()), 0);
			
		} else // different populations 
		{
				Population topNew = new Population(newpopSize, c);
				Fitness avg = popTo.GetAvg();
				for (int i = 0; i < newpopSize; i++) {
					Chromosome ch = newPopulation.GetChromosome(i);
					if (ch.get_fitness().compareTo(avg) <= 0) {
						topNew.AddChromosome(ch);
						ch.AddLog("Migration", String.format(
								"From island: %d, To island: %d", islandFrom,
								islandTo));
					}
				}

			ArrayList<Chromosome> bestParents = popTo.GetTopsChromosomes(CONST.MAX_POPULATION_SIZE - topNew.Size() + 1);
			
			if (topNew.Size() > 0)
			{
				Selection.SelectionFactory.CreateSelection(_selectionMode, topNew);
				popTo.SetChromosomesList(topNew.get_pop());
			}
			popTo.Union(bestParents);
		}
		
		

		// TODO: !!!! this is done only to update rank and distance for new
				// chromes for later printing etc .............
		Selection.SelectionFactory.CreateSelection(_selectionMode,
						popTo.get_pop());
		
		if (popTo.Size() > CONST.MAX_POPULATION_SIZE)
		{
			Chromosome best = GetBest(popTo);
			popTo.SetChromosomesList(popTo.GetTopsChromosomes(CONST.MAX_POPULATION_SIZE-1));
			popTo.AddChromosome(best);
			
			Selection.SelectionFactory.CreateSelection(_selectionMode,
					popTo.get_pop());
		}
		

		
		if (_internalMode == InternalMode.MultiObjective
				|| _internalMode == InternalMode.MultiObjectiveMI) {
			MOO_flag_changed = false;

			popTo.Sort();
			for (int i = 0; i < popTo.Size(); i++) {
				Chromosome cc = popTo.GetChromosome(i);
				if (cc.rank > 0)
					break;
				
				if (cc.get_new()
						&& cc.get_fitness().GetValue(0) != lastBest.get_fitness().GetValue(0)) {
					MOO_flag_changed = true;
					break;
				}
			}
			for (int i = 0; i < popTo.Size(); i++) {
				popTo.GetChromosome(i).set_new(false);
			}
		}

	}

	private ArrayList<Chromosome> GetBestParents(PopulationManager pop, Population newPop) throws Exception
	{
		ArrayList<Chromosome> bestParents = null;
		
		// copy the best chromosome from the current population to the new
		// population
		if (_internalMode == InternalMode.MultiObjective
				|| _internalMode == InternalMode.MultiObjectiveMI
				|| _internalMode == InternalMode.MultiObjectiveHD) {
			bestParents = new ArrayList<Chromosome>(10);
			pop.Sort();
			int i = 0;
			while (i < CONST.MAX_POPULATION_SIZE &&
					pop.GetChromosome(i).rank == 0
					//&& bestParents.size() < CONST.TOP_POPULATION_PRECENT_FOR_COPY * CONST.MAX_POPULATION_SIZE) 
					){
				Chromosome cc = pop.GetChromosome(i).clone();
				cc.set_new(false);
				bestParents.add(cc);
				i++;
			}
		} else if (_internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) {
			bestParents = new ArrayList<Chromosome>(CONST.MAX_POPULATION_SIZE);

			//pop.Sort();
			/**22.9.2015***********************/
			int i = 0;
			while (i < CONST.MAX_POPULATION_SIZE &&
					pop.GetChromosome(i).rank == 0
				//	&& bestParents.size() < CONST.TOP_POPULATION_PRECENT_FOR_COPY
						//	* CONST.MAX_POPULATION_SIZE
					){
				boolean duplicate = false; 
				for (int j = 0; j < i; j++) {
					if (pop.GetChromosome(i).toString().compareTo( pop.GetChromosome(j).toString()) == 0)
					{
						duplicate = true;
						break;
					}
					
				}
				if (!duplicate)
				{
					Chromosome cc = pop.GetChromosome(i).clone();
					cc.set_new(false);
					bestParents.add(cc);
				}
				i++;
			}
			/*********************************/
			
			Chromosome cc = null;
			if (CONST.MAX_POPULATION_SIZE > bestParents.size())
			{
				pop.Sort(0);
				Chromosome bestX1 = pop.GetChromosome(0);
				newPop.Sort(0);
				Chromosome bestX2 = newPop.GetChromosome(0);

				Chromosome bestX = null;
				if (bestX1.get_fitness().GetValue(0) < bestX2.get_fitness()
						.GetValue(0))
					bestX = bestX1;
				else
					bestX = bestX2;

				pop.Sort(1);
				Chromosome bestY1 = pop.GetChromosome(0);
				newPop.Sort(1);
				Chromosome bestY2 = newPop.GetChromosome(0);;

				Chromosome bestY = null;
				if (bestY1.get_fitness().GetValue(1) < bestY2.get_fitness()
						.GetValue(1))
					bestY = bestY1;
				else
					bestY = bestY2;

				cc = _geneticStrategy.CreateChromosome(bestX, bestY);
			
				
				//SetSIFTFitness(cc, true);
				SetFitness(cc,0);

				if (bestX != bestY)
					cc.set_new(true);

				bestParents.add(cc);
			}

			if (CONST.MAX_POPULATION_SIZE > bestParents.size())
			{
				// if (popTo.getFitnessUnchangedCount() % 20 == 0)
				{
					Chromosome bestMin = cc.clone();
					FitnessSIFT.Minimize(bestMin);
					
					//SetSIFTFitness(bestMin, true);
					SetFitness(bestMin,0);
					
					bestParents.add(bestMin);
				}
			}
			
			
		} else {
			int num = (int) Math.ceil(CONST.TOP_POPULATION_PRECENT_FOR_COPY
					* CONST.MAX_POPULATION_SIZE);
			bestParents = pop.GetTopsChromosomes(num);
		}
		
		return bestParents;
	}
	
	@SuppressWarnings("unused")
	private void print(String s, int island) {
		if (debug)
			printRank(s, island);
	}

	private void printRank(String s, int island) {
		_logger.Log(s + '\n', island);
		if (_gen % 100 == 0) {
			_logger.Force(island);
		}

	}

	public void Log(String s, int island) {
		_logger.Log(s + '\n', island);
		_logger.Force(island);

	}

	/**
	 * Perform cross over - create 2 new chromosomes from the given chromosomes
	 * by randomly replacing subtree of each of the given chromosomes
	 * 
	 * @param c1
	 *            parent chromosome 1
	 * @param c2
	 *            parent chromosome 2
	 * @return array of 2 chromosome that are result of the cross-over,
	 *         otherwise - null
	 */
	/*
	 * private void Share(Chromosome ch) { for (int index=0; index<=1; index ++)
	 * { if (new Random().nextDouble() <= CONST.MUTATE_NODE_PERCENT) {
	 * ArrayList<INode> n = ch.get_root(1-index).getAllChildrens();
	 * ArrayList<INode> s = new ArrayList<INode>(); for (int i=0;i<n.size();i++)
	 * { if (n.get(i) instanceof IOperatorNullary &&
	 * ((IOperatorNullary)n.get(i)).share) { s.add(n.get(i)); } } if (s.size() >
	 * 0) {
	 * 
	 * int ii = Utils.rand.nextInt(s.size()); INode root =
	 * Utils.getRandomBinaryOperator(index);
	 * ((IOperatorBinary)root).setLeft(ch.get_root(index).clone());
	 * ((IOperatorBinary)root).setRight(s.get(ii).clone()); ch.set_root(index,
	 * root); } } }
	 * 
	 * }
	 */

	private Chromosome[] CrossOver(Chromosome c1, Chromosome c2) {

		return _geneticStrategy.CrossOver(c1, c2);
	}

	/**
	 * Mutation function initiates mutation on the given chromosome.
	 * 
	 * @param ch
	 *            - chromosome for mutations
	 */
	private void Mutation(Chromosome ch, double mutationRate) {
		_geneticStrategy.Mutation(ch, mutationRate);
	}

	

	/**
	 * Returns the solution of the algorithms - the best found chromosome
	 * 
	 * @return
	 */
	public Chromosome GetSolution() {
		Chromosome best = _pop[0].GetBestChromosome();
		for (int i = 1; i < _pop.length; i++) {
			if (best.get_fitness().compareTo(
					_pop[i].GetBestChromosome().get_fitness()) > 0)
				best = _pop[i].GetBestChromosome();
		}
		return best;
	}
	
	public PopulationManager GetPop() {
		return _pop[0];
	}
	
	public InternalMode GetMode() {
		return  _internalMode;
	}

	private double[] T(Chromosome ch, int col, int row) {
		return ch.T(col, row, _isCentered);
	}

	private int T1(Chromosome ch, int col, int row) {
		// int i = getIndex(_theight2,col,row);
		// int newIndex = (int) Math.round(ch.GetValue(0, i, row));
		int newIndex = (int) Math.round((ch.GetValue(col, row))[0]);
		return newIndex;
	}

	@SuppressWarnings("unused")
	private double T1D(Chromosome ch, double col, double row, boolean useScript)
			throws ScriptException {
		// int i = getIndex(_theight2,col,row);
		// int newIndex = (int) Math.round(ch.GetValue(0, i, row));

		double newIndex = 0;
		if (useScript) {
			// String s = ch.get_root(0).getDescription();
			// newIndex = GetValue(s, col, row);
			newIndex = GPTransformations.GetValue(0, col, row);
		} else {
			double[] pos = ch.GetValue(col, row);
			newIndex = pos[0];
		}
		return newIndex;
	}

	/**
	 * Calculate the fitness of the given chromosome - measure of the error
	 * tested on training data
	 * 
	 * @param ch
	 *            - the chromosome
	 * @return - the value representing the fitness
	 * @throws ScriptException
	 */
	/*
	 * public double CalculateFitnessDiff(Chromosome ch) {
	 * 
	 * //Map<AbstractMap.SimpleEntry<Integer, Integer>, Integer> overlap = new
	 * HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer>(); int count
	 * = 0; // apply transform for (int col=0; col <
	 * _transformedOriginal.length; col++) { for (int row=0; row <
	 * _transformedOriginal[0].length; row++) { int[] newPos = T(ch, col, row);
	 * if (newPos[0] >= 0 && newPos[0] < _transformedOriginal.length &&
	 * newPos[1] >= 0 && newPos[1] < _transformedOriginal[0].length) { //
	 * overlap.put(new SimpleEntry<Integer, Integer>(col, row), 1);
	 * ch._transformed[newPos[0]][newPos[1]] = _transformedOriginal[col][row];
	 * 
	 * if (ch.overlap[newPos[0]][newPos[1]] == 0) { count++;
	 * ch.overlap[newPos[0]][newPos[1]] = 1; } } } }
	 * 
	 * // sum total absolute difference of the warped images on overlaping area
	 * double totDiff = 0; int countGood = 0; double percentGood = 0;
	 * ch.overlapPercent = (double)count / (_transformedOriginal.length*
	 * _transformedOriginal[0].length); if ( ch.overlapPercent > 0.3) { for (int
	 * col=0; col < _transformedOriginal.length; col++) { for (int row=0; row <
	 * _transformedOriginal[0].length; row++) { if (ch.overlap[col][row] == 1) {
	 * double diff = Math.abs(_original[col][row] - ch._transformed[col][row]);
	 * if (diff <= 7) countGood++; else totDiff += diff; } }
	 * 
	 * 
	 * } // avg totDiff = totDiff / count; percentGood =
	 * (double)countGood/count; } else { totDiff = 255; }
	 * 
	 * 
	 * if (ch.overlapPercent < 0.5) percentGood = percentGood *
	 * ch.overlapPercent; else if (ch.overlapPercent < 0.1) percentGood = 0;
	 * 
	 * //percentGood = percentGood*ch.overlapPercent;
	 * 
	 * ch.goodPercent = percentGood; totDiff += 255*(1 - ch.goodPercent);
	 * //totDiff += 255 * (1 - ch.overlapPercent);
	 * 
	 * // calculate the average diff double fitness =
	 * totDiff;//Math.sqrt(totDiff);
	 * 
	 * // penalty on deep of the chromosome int deep = ch.GetDeep(); if (deep >
	 * CONST.MAX_DEEP_CHROMOSOME) { fitness += fitness*0.1*deep; }
	 * 
	 * return fitness;
	 * 
	 * // TODO: const by image size ?? }
	 */


	
	
	public Chromosome GetBest(PopulationManager pop) throws Exception
	{
		Chromosome best = pop.GetBestChromosome();
		if (1==0)
			return best;
		else if (_internalMode == InternalMode.SIFT_MOO || _internalMode == InternalMode.SIFT_MOO2) 
		{
			
			pop.Sort();
			int i = 0;
			try
			{
				while (i < pop.Size() && pop.GetChromosome(i).rank == 0 ) {
					// compare distance measure
					
					boolean found = false;
					if (_internalMode == InternalMode.SIFT_MOO2)
					{
						found = true;

						Fitness f = pop.GetChromosome(i).get_fitness();
						double fi = f.GetValue( pop.GetChromosome(i).get_fitness().Length()-1);//f.GetValue(0);
						double fbest = best.get_fitness().GetValue(best.get_fitness().Length()-1);//best.get_fitness().GetValue(0);
						
						//if (fi<fbest && (f.GetValue(0) < 5 && f.GetValue(1) < 5 || best.get_fitness().GetValue(0) > 5 || best.get_fitness().GetValue(1) > 5))
						if (fi<fbest && (f.GetValue(0) < 5 || best.get_fitness().GetValue(0) > 5))
						{
							best = pop.GetChromosome(i);							
						}
						else if (fi==fbest)
						{
							found = false;
						}
					}
						
					if (!found)
					{
						found = true;
						
						if (pop.GetChromosome(i).get_fitness().GetValue(0) < 200000.0 && 
								pop.GetChromosome(i).get_fitness().GetValue(1) < 200000.0)
						{
							double absi = Math.abs(pop.GetChromosome(i).get_fitness().GetValue(0));// + Math.abs(pop.GetChromosome(i).get_fitness().GetValue(1)); 
							double absbest = Math.abs(best.get_fitness().GetValue(0));// + Math.abs(best.get_fitness().GetValue(1));
			
							if (absi < absbest) 
							{
								best = pop.GetChromosome(i);
							}
							else if (absi == absbest)
							{
								found = false;
							}
						}
					}
					i++;
					
				}
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
		else if (_internalMode == InternalMode.MultiObjective)
		{
			pop.Sort();
			int i = 0;
			if (pop.GetChromosome(0).rank == null) // this is a bypass - in case of reset (change of method) where rank still have not been calculated
			{
				Selection.SelectionFactory.CreateSelection(_selectionMode,
						pop.get_pop());
		
			}
			while (i < pop.Size() && pop.GetChromosome(i).rank == 0 ) {
				{
					double absi = pop.GetChromosome(i).get_fitness().GetValue(0); 
					double absbest = best.get_fitness().GetValue(0);
					if (absi < absbest || 
							absi == absbest && pop.GetChromosome(i).get_fitness().GetValue(1) < best.get_fitness().GetValue(1)) 
					{
						best = pop.GetChromosome(i);
					}
				}
				i++;
			}
		}
		
		return best;
	}

	

	/*
	 * private void Fix(Chromosome ch, double[] diff, double[] size) throws
	 * Exception { double overlap = ch.overlapPercent;
	 * 
	 * for (int index = 0; index <= 1; index++) { INode c = ch.get_root(index);
	 * Plus p = new Plus(index); p.setLeft(new Constant(index,
	 * diff[index]+size[(int)(index/2.0)])); p.setRight(c); ch.set_root(index,
	 * p); }
	 * 
	 * calculateTransform(ch, false, false);
	 * 
	 * ch.AddLog("Fix Overlap", ((Double)ch.overlapPercent).toString()); }
	 */
	

	
	
	
	@SuppressWarnings("unused")
	private int NeighbourhoodDistance(Chromosome ch, int col, int row, TransformResult res) {
		int distance = 0;

		if (col >= _twidth2 - 1)
			return 0;

		// double[] newPos = T(ch, col, row);

		double[] newPos = res._tempTList[(int) Utils.getIndex(_height2, col, row)];

		if (newPos != null) {
			int x = (int) Math.round(newPos[0]);
			int y = (int) Math.round(newPos[1]);
			// for (int i = Math.max(0,col-1); i < Math.min(_twidth1,col+2);
			// i++)
			{
				// for (int j = Math.max(0,row-1); j <
				// Math.min(_theight1,row+2); j++)
				{
					// double[] newPos2 = T(ch, i, j);
					// double[] newPos2 = T(ch, col+1, row);
					double[] newPos2 = res._tempTList[(int) Utils.getIndex(_height2, col+1, row)];
					if (newPos2 != null) {
						int x2 = (int) Math.round(newPos2[0]);
						int y2 = (int) Math.round(newPos2[1]);
						int diff = Math.abs(x - x2) + Math.abs(y - y2);
						if (diff >= 2)
							distance = Math.max(distance, diff);
					}
				}
			}
		}
		
		
		if (row >= _theight2 - 1)
		{
			newPos = res._tempTList[(int) Utils.getIndex(_height2, col, row)];

		if (newPos != null) {
			int x = (int) Math.round(newPos[0]);
			int y = (int) Math.round(newPos[1]);
			
					double[] newPos2 = res._tempTList[(int)Utils.getIndex(_height2, col+1, row)];
					if (newPos2 != null) {
						int x2 = (int) Math.round(newPos2[0]);
						int y2 = (int) Math.round(newPos2[1]);
						int diff = Math.abs(x - x2) + Math.abs(y - y2);
						if (diff >= 2)
							distance = Math.max(distance, diff);
					}
				}
		}
		
		return distance;

	}

	
	/**
	 * Set the fitness of the given chromosome
	 * 
	 * @param ch
	 * @throws Exception
	 */
	private boolean SetFitness(Chromosome ch, int popNum) throws Exception {
		if (CONST._shouldValidate) {
			ch.Validate();

			// ch.get_root(0).Minimize();
			// ch.get_root(1).Minimize();
		}
		
		new CalculateFitnessTask(ch, false, popNum, _internalMode, _referenced, _sensed, _referencedFeatures, _sensedFeatures,
				_twidth1, _theight1, _twidth2, _theight2, _refPoints, _sensedPoints, _maxImageSize, _maxSamples, _isCentered, _height2, points1, points2).run();
		
		boolean ok = true;

		ch.AddLog("Fitness", ((Double) ch.get_fitness().GetValue(0)).toString());

		return ok;
	}

	@Override
	protected void finalize() throws Throwable {

		for (int i = 0; i < _pop.length; i++) {

			_out[i].close();
		}

		super.finalize();

	}
	
	private ArrayList<Point> GetSIFTPoints(ArrayList<Point> points)
	{
		ArrayList<Point> tmpPoints = new ArrayList<Point>();
		int increment = 1;
		if (_mustreset && !_hasreset && points.size() > 50)
		{
			increment = 3;
		}
		for (int i = 0; i < points.size(); i=i+increment) {
			Point p = new Point(points.get(i).x, points.get(i).y);
			tmpPoints.add(p);
		}
		if (CONST.GA_SHUFFLE)
		{
			Collections.shuffle(tmpPoints);
		}
		return tmpPoints;
	}
	
	private void InitTest() throws Exception
	{
		ArrayList<Point> points1 = GetSIFTPoints(_refPoints);
		ArrayList<Point> points2 = GetSIFTPoints(_sensedPoints);
		
		
		_pop[0].RemoveAll();
		for (int itx = -200; itx <= 200; itx+=200) 
		{
			for (int ity = -200; ity <= 200; ity+=200) 
			{
				for (int irot = 0; irot <= 360; irot+=60) 
				{
					//for (double is = 0.9; is <= 1.1; is+=0.2) 
					{
						//for (double ishearX = 0; ishearX <=0.4 ; ishearX+=0.2) 
					{
							//for (double ishearY = 0; ishearY <=0.4 ; ishearY+=0.2) 
					{
								

								TranslationX tx = new TranslationX(itx);
								TranslationY ty = new TranslationY(ity);
								Rotation rot = new Rotation(irot);
								Scaling s = new Scaling(1);
								ScalingY sy = new ScalingY(1);
								ShearingX shearx = new ShearingX(0);
								ShearingY sheary = new ShearingY(0);
								
								GAChromosome ch = new GAChromosome(tx, ty, rot, s, sy,shearx, sheary, new ReflectionX(), new ReflectionY(), new ReflectionOrigin());
								
								//SetSIFTFitness(ch, true);
								SetFitness(ch,0);
								
								_pop[0].AddChromosome(ch);
								
								
							/*
								double itx = -275;
								double ity = -200;
								double irot = 180;
								double is = 0.98;
								double ishx = 0.023;
								double ishy = 0.019;
								GAChromosome ch = GAChromosome.Test(itx, ity, irot, is, ishx, ishy, points1, points2, _internalMode);
								double rmse = _geneticStrategy.CalculateRMSE(_refRealPoints, _sensedRealPoints, ch, null, false);
								System.out.println(String.format("%f %s", rmse,getPrintChrom(ch)));

								itx = -200;
								ity = -200;
								irot = 180;
								is = 0.98;
								ishx = 0.023;
								ishy = 0.019;
								ch = GAChromosome.Test(itx, ity, irot, is, ishx, ishy, points1, points2, _internalMode);
								rmse = _geneticStrategy.CalculateRMSE(_refRealPoints, _sensedRealPoints, ch, null, false);
								System.out.println(String.format("%f %s", rmse,getPrintChrom(ch)));

								itx = -100;
								ity = -200;
								irot = 180;
								is = 0.98;
								ishx = 0.023;
								ishy = 0.019;
								ch = GAChromosome.Test(itx, ity, irot, is, ishx, ishy, points1, points2, _internalMode);
								rmse = _geneticStrategy.CalculateRMSE(_refRealPoints, _sensedRealPoints, ch, null, false);
								System.out.println(String.format("%f %s", rmse,getPrintChrom(ch)));
								
								itx = -275;
								ity = -200;
								irot = 100;
								is = 0.98;
								ishx = 0.023;
								ishy = 0.019;
								ch = GAChromosome.Test(itx, ity, irot, is, ishx, ishy, points1, points2, _internalMode);
								rmse = _geneticStrategy.CalculateRMSE(_refRealPoints, _sensedRealPoints, ch, null, false);
								System.out.println(String.format("%f %s", rmse,getPrintChrom(ch)));*/
							}
						}
					}
				}
			}
		}
	}

	public double calcRMSE(Chromosome chromosome) {
		return _geneticStrategy.CalculateRMSE(_refRealPoints, _sensedRealPoints, chromosome, null, false);
	}

	public ArrayList<Point> getReferencedFeaturesPoints() {
		return _refPoints;
	}

	public ArrayList<Point> getSensedFeaturesPoints() {
		return _sensedPoints;
	}

	public int[] getLimits() {
		return new int[]{_width1, _height1};
	}
}
package algorithms;

import Interfaces.Chromosome;
import base.Population;

/**
 * 
 * Selection class provides general methods for selection functionality, i.e., 
 * selecting 
 *
 */
public abstract class Selection {
	
	// population of chromosomes
	protected Population _chromes;
	
	public Selection(Population chromes)
	{
		_chromes = chromes;		
	}
	
	// returns a chromosome from the initial population based on the selection mode
	public abstract Chromosome Select() throws Exception;
	
	/**
	 * 
	 * Selection Factory - Creating Selection with the specified SelectionMode
	 *
	 */
	public static class SelectionFactory
	{
		
		// possible selection modes 
		public static enum SelectionMode
		{
			RoletteWheelSelection,
			RankBasedSelection,
			NSGARankedBasedSelection,
			TournamentSelection,
			NSGARankBasedSelectionTournment
		}
		
		// init selection object based on the given mode and chromosomes population
		public static Selection CreateSelection(SelectionMode mode, Population chromes) throws Exception
		{
			Selection s = null;
			
			switch (mode) {
			case RoletteWheelSelection:
				s = new RolleteWheelSelection(chromes);
				break;
			case RankBasedSelection:
				s = new RankBasedSelection(chromes);
				break;
			case NSGARankedBasedSelection:
				s = new NSGARankBasedSelection(chromes);
				break;
			case TournamentSelection:
				s = new TournamentSelection(chromes);
				break;
			case NSGARankBasedSelectionTournment:
				s = new NSGARankBasedSelectionTournment(chromes);
				break;
			default:
				s = new RolleteWheelSelection(chromes);
				break;
			}
			return s; 
		}
	}	
	

}

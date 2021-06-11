package edu.uno.ai.planning.ex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import edu.uno.ai.SearchBudget;
import edu.uno.ai.logic.Conjunction;
import edu.uno.ai.logic.Literal;
import edu.uno.ai.logic.Proposition;
import edu.uno.ai.planning.Plan;
import edu.uno.ai.planning.Step;
import edu.uno.ai.planning.ss.StateSpaceNode;
import edu.uno.ai.planning.ss.StateSpaceProblem;
import edu.uno.ai.planning.ss.StateSpaceRoot;
import edu.uno.ai.planning.ss.StateSpaceSearch;

/**
 * A planner that uses simple breadth first search through the space of states.
 * 
 * @author Stephen G. Ware
 */
public class HeuristicSearch extends StateSpaceSearch {

	/** The queue which will hold the frontier (states not yet visited) */
	//private final Queue<StateSpaceNode> queue = new LinkedList<>();
	
	//TO DO: PriorityQueue of a weighted StateSpaceNode
	private PriorityQueue<WeightedState> pQueue = new PriorityQueue<>(problem.steps.size(), new StateComparator());
	
	/**
	 * Constructs a new state space search object.
	 * 
	 * @param problem the problem to solve
	 * @param budget the search budget, which constrains how many states may be
	 * visited and how much time the search can take
	 */
	public HeuristicSearch(StateSpaceProblem problem, SearchBudget budget) {
		super(problem, budget);
	}

	@Override
	public Plan solve() {
		// Start with only the root node (initial state) in the queue.
		WeightedState wRoot = new WeightedState(root, problem, 0.0);
		pQueue.add(wRoot);
		// Search until the queue is empty (no more states to consider).
		while(!pQueue.isEmpty()) {
			// Pop a state off the frontier.
			WeightedState current = pQueue.poll();
			// Check if it is a solution.
			if(problem.isSolution(current.currentState.plan))
				return current.currentState.plan;
			// Consider every possible step...
			for(Step step : problem.steps) {
				// If it's precondition is met in the current state...
				if(step.precondition.isTrue(current.currentState.state)) {
					// Add the state results from that step to the frontier.
					WeightedState temp = new WeightedState(current.currentState.expand(step), problem);
					pQueue.offer(temp);
				}
			}
		}
		// If the queue is empty and we never found a solution, the problem
		// cannot be solved. Return null.
		return null;
	}
	
}

class StateComparator implements Comparator<WeightedState> {
	public int compare(WeightedState ws1, WeightedState ws2) {
		if(ws1.weight > ws2.weight)
			return 1;
		else if(ws1.weight < ws2.weight)
			return -1;
		return 0;
	}
}

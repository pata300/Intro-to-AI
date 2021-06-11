package edu.uno.ai.planning.ex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uno.ai.logic.Conjunction;
import edu.uno.ai.logic.Literal;
import edu.uno.ai.logic.Proposition;
import edu.uno.ai.planning.Step;
import edu.uno.ai.planning.ss.StateSpaceNode;
import edu.uno.ai.planning.ss.StateSpaceProblem;
import edu.uno.ai.util.ImmutableArray;

class WeightedState {

	protected double weight;	
	protected StateSpaceNode currentState;
	protected StateSpaceProblem problem;
	

		
		public WeightedState(StateSpaceNode currentState, StateSpaceProblem problem){
			this.currentState = currentState;
			this.problem = problem;
			this.weight = evaluate();
		}
		
		public WeightedState(StateSpaceNode currentState, StateSpaceProblem problem, double weight) {
			this.currentState = currentState;
			this.problem = problem;
			this.weight = weight;
		}
		
		
		private double evaluate() {
			boolean costChange = true;
			double evaluation = 0;
			double preconditionValue;
			List<Literal> goalLiterals = getLiterals(problem.goal);
			//take the current state
			//set all literals to infinity
			HashMap<Literal, Double> cost = setLiterals(problem.literals);
			//System.out.println( problem.literals.toString());
			
			//every literal that is true in the current state has a cost of 0
			for(Literal literal : problem.literals) {
				// If it's precondition is met in the current state...
				if(literal.isTrue(currentState.state))
					cost.replace(literal, Double.parseDouble("0"));
			}
			
			//the cost of a conjunction is the sum of the costs of its conjuncts
			 
			
			//do this until the costs of the literals stop changing:
			while(costChange) {
				costChange = false;
				//for every step S:
				for(Step s : problem.steps) {
					//for every literal E in the effect of S:
					Proposition p = s.effect;
					List<Literal> effectList = getLiterals(p);
					List<Literal> preconditionList = getLiterals(s.precondition);
					
					preconditionValue = 0;
					
					//sum of preconditionList
					for(Literal literal : preconditionList) {
						//System.out.println(literal);
						preconditionValue += cost.get(literal).doubleValue();
						
					}

					for(Literal literal : effectList) {
						//let the cost of E be the minimum of :
						//1. the current cost of E
						//2. the cost of S's precondition + 1
						if(Math.min(cost.get(literal).doubleValue(), preconditionValue + 1) != cost.get(literal).doubleValue()) {
							cost.replace(literal, Math.min(cost.get(literal).doubleValue(), preconditionValue + 1));
							costChange = true;
						}
					}		
				}
				
			}
			
			for(Literal goal : goalLiterals) {
				evaluation += cost.get(goal).doubleValue();
			}
				
			//return the cost of the problem's goal
			return evaluation;
		}
		
		private static HashMap<Literal, Double> setLiterals(ImmutableArray<Literal> literals) {
			HashMap<Literal, Double> list = new HashMap<>();
			for(Literal l : literals) {
				list.put(l,  Double.POSITIVE_INFINITY);
			}
			return list;
		}
		
		private static List<Literal> getLiterals(Proposition proposition) {
			ArrayList<Literal> list = new ArrayList<>();
			if(proposition instanceof Literal){
				list.add((Literal) proposition);
			} else {
				for(Proposition conjunct : ((Conjunction) proposition).arguments){
					list.add((Literal) conjunct);
				}
			}
			return list;
		}
}

package edu.uno.ai.sat.ex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


import edu.uno.ai.logic.Proposition;
import edu.uno.ai.sat.Assignment;
import edu.uno.ai.sat.Clause;
import edu.uno.ai.sat.Literal;
import edu.uno.ai.sat.Solver;
import edu.uno.ai.sat.Value;
import edu.uno.ai.sat.Variable;
import edu.uno.ai.util.ImmutableArray;

/**
 * 
 * @author Patrick Griffin
 */
public class SolverBot extends Solver {

	private final Random random = new Random(0);
	
	
	/**
	 * Constructs a new random SAT solver. You should change the string below
	 * from "random" to your ID. You should also change the name of this class.
	 * In Eclipse, you can do that easily by right-clicking on this file
	 * (RandomAgent.java) in the Package Explorer and choosing Refactor > Rename.
	 */
	public SolverBot() {
		super("pgriffi1");
	}

	@Override
	public boolean solve(Assignment assignment) {
//		
		ArrayList<Clause> clauses = new ArrayList<Clause>(assignment.problem.clauses.size());
		
			for(Clause c: assignment.problem.clauses)
				clauses.add(c);
		
		
		ArrayList<Variable> variables = new ArrayList<Variable>(assignment.problem.variables.size());
		
			for(Variable v: assignment.problem.variables)
				variables.add(v);
		
			//call DPLL algorithm
		Boolean result = DPLL(clauses, variables, assignment);
		return result;
		
	}
	
	
	/**
	 * DPLL algorithm implementation. Adapted from code in the class book, Artificial Intelligence A Modern Approach (3rd Editoin)
	 * Original authors: Ciaran O'Reilly, Ravi Mohan, Mike Stampone
	 * 
	 * @param clauses		list of clauses in the assignment
	 * @param variables		list of variables in the assignment
	 * @return				a boolean depending on the satisfiability of the assignment
	 */
	private boolean DPLL(ArrayList<Clause> clauses, ArrayList<Variable> variables, Assignment assignment) {
//		System.out.println("In DPLL");
		
		//every cluase is true
		if(assignment.countTrueClauses() == assignment.problem.clauses.size())
			return true;
		
		//some clause is false
		if(assignment.countFalseClauses() > 0) //may have to develop some boolean for Tasks.currIsCancelled() implementation
			return false;
		
		ArrayList<ValueState> originalValues = new ArrayList<ValueState>(variables.size());
		for(Variable v: variables) {
			ValueState temp = new ValueState(v, assignment.getValue(v));
			originalValues.add(temp);
		}
		
		
		//find the pure symbol, should have boolean value assigned
		Variable pureLiteral = findPureSymbol(variables, clauses, assignment);
		
		 //if p has non-null value then
		 if(pureLiteral != null) {
			 //getFirst is p, getSecond is the value of p (true/false)
			 //pureSymbol should have a value assigned
			 if(DPLL(clauses, minus(variables, pureLiteral.literals.get(0)), assignment))
				 return true;
			 else {
				 for(ValueState vs : originalValues) {
					 if(vs.getVariable() == pureLiteral) {
						 assignment.setValue(pureLiteral, vs.getValue());
					 }
				 }
				 return false;
			 }
//			 return tryValue(assignment, pureLiteral, assignment.getValue(pureLiteral), clauses, minus(variables, pureLiteral.literals.get(0)));
		 }
		 
		 //find unit clause
		 Literal unitClause = findUnitClause(clauses, assignment);
		 if(unitClause != null) {
			 //getFirst is p, getSecond is the value of p (true/false)
			 //pureSymbol should have a value assigned
			 if(DPLL(clauses, minus(variables, unitClause), assignment))
				 return true;
			 else {
				 for(ValueState vs : originalValues) {
					 if(vs.getVariable() == unitClause.variable) {
						 assignment.setValue(unitClause.variable, vs.getValue());
					 }
				 }
				 return false;
			 }
//			 return tryValue(assignment, pureLiteral, assignment.getValue(pureLiteral), clauses, minus(variables, unitClause));
		 }
		 
		 //first variable; rest of variables;
		 //rest is variables minus the first variable
		 ArrayList<Variable> rest = new ArrayList<Variable>(variables.size());
		 //if first variable is true, return 
		 Variable p = variables.get(0);
		 for(Variable v: variables) {
			 if(!v.equals(variables.get(0)))
				 rest.add(v);
		 }

		 assignment.setValue(p, Value.TRUE);
		 if(DPLL(clauses, rest, assignment))
			 return true;
		 for(ValueState vs : originalValues) {
			 if(vs.getVariable() == p) {
				 assignment.setValue(p, vs.getValue());
			 }
		 }

		 assignment.setValue(p, Value.FALSE);
		 if(DPLL(clauses, rest, assignment))
			 return true;
		 for(ValueState vs : originalValues) {
			 if(vs.getVariable() == p) {
				 assignment.setValue(p, vs.getValue());
			 }
		 }
		 
		 return false;
		
	}
	
	/**
	 * Checks the problem for any unit clauses.
	 * 
	 * @param assignment the assignment is being worked on
	 * @return boolean depending on the search results
	 */
	private boolean checkForUnitClause(Clause c) {
//		System.out.println("In checkForUnitClause function");

		return c.literals.size() == 1;
		
	}
	

	
	/**
	 * Looks through all clauses for a literal that never changes valence
	 * @param variables		list of variables in the problem
	 * @param clauses		list of clauses in the problem
	 * @param assignment	a model of the assignments for all the variables
	 * @return				a pure Variable
	 */
	private Variable findPureSymbol(ArrayList<Variable> variables, ArrayList<Clause> clauses, Assignment assignment) {
//		System.out.println("In findPureSymbol");
		Set<Variable> variablesToKeep = new HashSet<Variable>(variables);
		Set<Variable> possiblePureTrueVariables = new HashSet<Variable>();
		Set<Variable> possiblePureFalseVariables = new HashSet<Variable>();
		
		for(Clause c: clauses) {
			//if true, ignore
			if(assignment.getValue(c) == Value.TRUE){
				continue;
			}
			//sort through possible Pure True/Pure False variables
			for(Literal l: c.literals) {
				if(l.valence == true) {
					if(variablesToKeep.contains(l.variable)) {
						possiblePureTrueVariables.add(l.variable);
					
					}
				}
				//this should work **check for errors here**
				if(l.valence == false) {
					if(variablesToKeep.contains(l.variable)) {
						possiblePureFalseVariables.add(l.variable);
					}
				}
			}
		}
		
		//look for overlap between the true and false possible pure variables
		for(Variable pv: variablesToKeep) {
			//Remove non-pure variables
			if(possiblePureTrueVariables.contains(pv) && possiblePureFalseVariables.contains(pv)) {
				possiblePureTrueVariables.remove(pv);
				possiblePureFalseVariables.remove(pv);
			}
		}
		Variable pureVariable = null;
		if(possiblePureTrueVariables.size() > 0) {
			pureVariable = possiblePureTrueVariables.iterator().next();
			assignment.setValue(pureVariable, Value.TRUE);
			return pureVariable;
			
		} else if(possiblePureFalseVariables.size() > 0) {
			pureVariable = possiblePureFalseVariables.iterator().next();
			assignment.setValue(pureVariable, Value.FALSE);
			return pureVariable;
		}
		
		return pureVariable;
	}
	
	/**
	 * searches for a clause with a single literal or a clause that contains
	 * all but one literal that isn't assigned false
	 * @param clauses		list of clauses in the problem
	 * @param assignment	a model of the assignments for all the variables
	 * @return				a literal that is in the unit clause
	 */
	private Literal findUnitClause(ArrayList<Clause> clauses, Assignment assignment) {
//		System.out.println("In findUnitClause function");
		for(Clause c: clauses) {
			//if cluase's value is currently unknown
			if(assignment.getValue(c) == Value.UNKNOWN) {
				Literal unassigned = null;
				if(checkForUnitClause(c)) {
					unassigned = c.literals.iterator().next();
				} else {
					//check for single unassigned literal
					for(Literal l: c.literals) {
						Value value = assignment.getValue(l);
						if(value == Value.UNKNOWN) {
							//first unassigned literal
							if(unassigned == null) {
								unassigned = l;
							} else {
								//only enter this statement if there is more than one unassigned literal
								unassigned = null;
								break;
							}
																		
						}
					}
				}
				
				//if value assigned it means we have a single unassigned literal
				if(unassigned != null && unassigned.valence == false) {
					assignment.setValue(unassigned.variable, Value.FALSE);
					return unassigned;
				} else if(unassigned != null) {
					assignment.setValue(unassigned.variable, Value.TRUE);
					return unassigned;
				}
			}
		}
		return null;
	}

	
	/**
	 * Removes every clause in the set contain the pure literal.
	 * And removes every occurrence in the set of the negation of the pure literal.
	 * 
	 * @param assignment the assignment is being worked on
	 * @return returns the assignment that is being worked on
	 */
//	private Assignment Simplify(Assignment assignment, Literal p) {
//		
//		return assignment;
//	}
	
	private ArrayList<Variable> minus(ArrayList<Variable> variables, Literal p){
		ArrayList<Variable> result = new ArrayList<Variable>(variables.size());
		for(Variable v: variables) {
//			System.out.println("literal is " + p + " variable p is " + p.variable.toString() + " variable v is " + v.toString());
			if(v.toString() != p.variable.toString()) {
				result.add(v);
			}
		}
		return result;
	}
	

	
}//end of SolverBot



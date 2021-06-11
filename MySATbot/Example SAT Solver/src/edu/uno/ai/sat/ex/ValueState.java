package edu.uno.ai.sat.ex;

import edu.uno.ai.sat.Value;
import edu.uno.ai.sat.Variable;

public class ValueState {
	Variable v;
	Value value;
		
	public ValueState(Variable v, Value value) {
		this.v = v;
		this.value = value;
	}
	
	protected Value getValue() {
		return this.value;
	}
	
	protected Variable getVariable() {
		return this.v;
	}
		

}

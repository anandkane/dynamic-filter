package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

class UnaryConditionImpl extends AbstractCondition implements UnaryCondition {

	UnaryConditionImpl(String field, Operator operator) {
		super(field, operator);
	}

	public void accept(ConditionVisitor visitor) {
		visitor.visitUnary(this);
	}
}

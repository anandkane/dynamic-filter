package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

class NestingStartImpl extends AbstractCondition implements NestingStart {
	NestingStartImpl(String field, Operator operator) {
		super(field, operator);
	}

	public void accept(ConditionVisitor visitor) {
		visitor.visitNestedOpen(this);
	}
}

package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

class NestingEndImpl extends AbstractCondition implements NestingEnd {
	NestingEndImpl(String field, Operator operator) {
		super(field, operator);
	}

	public void accept(ConditionVisitor visitor) {
		visitor.visitNestedClose(this);
	}
}

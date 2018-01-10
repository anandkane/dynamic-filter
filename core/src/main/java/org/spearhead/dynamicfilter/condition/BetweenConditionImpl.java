package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

class BetweenConditionImpl extends AbstractCondition implements BetweenCondition {
	private Object value1;
	private Object value2;

	BetweenConditionImpl(String field, Operator operator, Object value1, Object value2) {
		super(field, operator);
		this.value1 = value1;
		this.value2 = value2;
	}

	public Object getValue1() {
		return value1;
	}

	public Object getValue2() {
		return value2;
	}

	public void accept(ConditionVisitor visitor) {
		visitor.visitBetween(this);
	}
}

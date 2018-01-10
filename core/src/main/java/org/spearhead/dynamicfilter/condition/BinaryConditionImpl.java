package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

class BinaryConditionImpl extends AbstractCondition implements BinaryCondition {

	private Object value;

	public BinaryConditionImpl(String field, Operator operator, Object value) {
		super(field, operator);
		this.value = value;
	}

	public void accept(ConditionVisitor visitor) {
		visitor.visitBinary(this);
	}

	public Object getValue() {
		return value;
	}
}

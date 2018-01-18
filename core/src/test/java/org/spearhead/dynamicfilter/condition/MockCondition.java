package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

public class MockCondition extends AbstractCondition implements BinaryCondition {
	MockCondition(String field, Operator operator) {
		super(field, operator);
	}

	public void accept(ConditionVisitor visitor) {
		visitor.visitBinary(this);
	}

	@Override
	public Object getValue() {
		return null;
	}
}

package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

public class TestCondition extends AbstractCondition {
	TestCondition(String field, Operator operator) {
		super(field, operator);
	}

	public void accept(ConditionVisitor visitor) {

	}
}

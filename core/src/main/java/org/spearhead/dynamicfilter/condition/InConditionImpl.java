package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

import java.util.Collection;

class InConditionImpl extends AbstractCondition implements InCondition {
	private Collection<Object> values;
	InConditionImpl(String field, Operator operator, Collection<Object> values) {
		super(field, operator);
		this.values = values;
	}

	public Collection<Object> getValues() {
		return values;
	}

	public void accept(ConditionVisitor visitor) {
		visitor.visitIn(this);
	}
}

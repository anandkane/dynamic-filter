package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

class SubQueryConditionImpl extends AbstractCondition implements SubQueryCondition {
	private String subQuery;

	SubQueryConditionImpl(String field, Operator operator, String subQuery) {
		super(field, operator);
		this.subQuery = subQuery;
	}

	public String getSubQuery() {
		return subQuery;
	}

	public void accept(ConditionVisitor visitor) {
		visitor.visitSubQuery(this);
	}
}

package org.spearhead.dynamicfilter.condition;

public interface ConditionContainer extends Condition {
	Condition start(Condition condition);

	Condition getStart();
}

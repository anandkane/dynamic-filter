package org.spearhead.dynamicfilter.condition;

public interface ConditionContainer {
	Condition start(Condition condition);

	Condition getStart();
}

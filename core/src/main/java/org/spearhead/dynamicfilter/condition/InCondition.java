package org.spearhead.dynamicfilter.condition;

import java.util.Collection;

public interface InCondition extends UnaryCondition {
	Collection<Object> getValues();
}

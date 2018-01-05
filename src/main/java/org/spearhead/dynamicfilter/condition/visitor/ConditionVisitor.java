package org.spearhead.dynamicfilter.condition.visitor;

import org.spearhead.dynamicfilter.condition.BinaryCondition;
import org.spearhead.dynamicfilter.condition.InCondition;
import org.spearhead.dynamicfilter.condition.NestedCondition;
import org.spearhead.dynamicfilter.condition.UnaryCondition;

public interface ConditionVisitor {
	void visitUnary(UnaryCondition condition);

	void visitBinary(BinaryCondition condition);

	void visitIn(InCondition condition);

	void visitNestedOpen(NestedCondition condition);

	void visitNestedClose(NestedCondition condition);

}

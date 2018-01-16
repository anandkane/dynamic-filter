package org.spearhead.dynamicfilter.condition.visitor;

import org.spearhead.dynamicfilter.condition.*;

public interface ConditionVisitor {
	void visitUnary(UnaryCondition condition);

	void visitBinary(BinaryCondition condition);

	void visitIn(InCondition condition);

	void visitBetween(BetweenCondition condition);

	void visitSubQuery(SubQueryCondition condition);

	void visitNestedOpen(NestedCondition condition);

	void visitNestedClose(NestedCondition condition);

}

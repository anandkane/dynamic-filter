package org.spearhead.dynamicfilter.condition.visitor;

import org.spearhead.dynamicfilter.condition.BinaryCondition;
import org.spearhead.dynamicfilter.condition.NestedCondition;

public class IterationTestVisitor extends ConditionVisitorAdaptor {
	public int binaryVisitCount;
	public int openVisitCount;
	public int closeVisitCount;

	@Override
	public String toString() {
		return new StringBuilder().append("IterationTestVisitor{").append("binaryVisitCount=")
				.append(binaryVisitCount).append(", openVisitCount=")
				.append(openVisitCount).append(", closeVisitCount=")
				.append(closeVisitCount).append('}').toString();
	}

	@Override
	public void visitBinary(BinaryCondition condition) {
		binaryVisitCount++;
	}

	@Override
	public void visitNestedOpen(NestedCondition condition) {
		openVisitCount++;
	}

	@Override
	public void visitNestedClose(NestedCondition condition) {
		closeVisitCount++;
	}
}

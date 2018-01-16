package org.spearhead.dynamicfilter.sql;

import org.apache.commons.lang3.StringUtils;
import org.spearhead.dynamicfilter.condition.*;
import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

import java.util.Iterator;

public class SqlPredicateBuilderVisitor implements ConditionVisitor {
	private StringBuilder builder = new StringBuilder();

	public String getPredicate() {
		int index;
		while ((index = builder.indexOf("( ")) != -1) {
			builder.deleteCharAt(index + 1);
		}

		return builder.toString();
	}

	public void visitUnary(UnaryCondition condition) {
		appendField(condition).append(condition.getOperator().getText());
		appendJoin(condition);
	}

	public void visitBinary(BinaryCondition condition) {
		appendField(condition).append(condition.getOperator().getText()).append(" ").append(condition.getValue());
		appendJoin(condition);
	}

	public void visitIn(InCondition condition) {
		appendField(condition).append(" IN (");
		StringBuilder builder1 = new StringBuilder();
		Iterator<Object> iterator = condition.getValues().iterator();
		while (iterator.hasNext()) {
			builder1.append(iterator.next().toString()).append(", ");
		}
		if (builder1.length() > 1) {
			builder1.setLength(builder1.length() - 2);
		}

		builder.append(builder1.toString()).append(")");
		appendJoin(condition);
	}

	public void visitBetween(BetweenCondition condition) {
		appendField(condition).append(condition.getOperator().getText()).append(" ")
				.append(condition.getValue1().toString()).append(" AND ").append(condition.getValue2().toString());
		appendJoin(condition);
	}

	public void visitSubQuery(SubQueryCondition condition) {
		appendField(condition).append(condition.getOperator().getText())
				.append(" (").append(condition.getSubQuery()).append(")");
		appendJoin(condition);
	}

	public void visitNestedOpen(NestedCondition condition) {
		builder.append(" (");
	}

	public void visitNestedClose(NestedCondition condition) {
		builder.append(")");
		appendJoin(condition);
	}

	private StringBuilder appendField(Condition condition) {
		return builder.append(" ").append(condition.getFiled()).append(" ");
	}

	private void appendJoin(Condition condition) {
		String text = condition.forwardJoin().getText();
		if (StringUtils.isNotEmpty(text)) {
			builder.append(" ").append(text);
		}
	}
}

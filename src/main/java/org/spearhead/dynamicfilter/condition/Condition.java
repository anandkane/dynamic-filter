package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

import java.util.ListIterator;

public interface Condition extends Iterable<Condition> {
	String getFiled();

	Operator getOperator();

	JoinOperator forwardJoin();

	JoinOperator backwardJoin();

	Condition previous();

	Condition next();

	Condition and(Condition condition);

	Condition or(Condition condition);

	Condition stop();

	void accept(ConditionVisitor visitor);

	ListIterator<Condition> listIterator();

	public enum Operator {
		EQUAL("="), NULL("IS NULL");

		private String operator;

		Operator(String operator) {
			this.operator = operator;
		}

		public String getOperator() {
			return operator;
		}
	}

	public enum JoinOperator {
		START("", false, true), AND("AND", true, true), OR("OR", true, true), END("", true, false);

		private String text;
		private boolean hasPrevious;
		private boolean hasNext;

		JoinOperator(String text, boolean hasPrevious, boolean hasNext) {
			this.text = text;
			this.hasPrevious = hasPrevious;
			this.hasNext = hasNext;
		}

		public String getText() {
			return text;
		}

		public boolean hasNext() {
			return hasNext;
		}

		public boolean hasPrevious() {
			return hasPrevious;
		}
	}
}

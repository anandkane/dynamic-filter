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

	enum Operator {
		EQ("="), NOT_EQ("<>"), GT(">"), GT_EQ(">="), LT("<"), LT_EQ("<="), NULL("IS NULL"), BETWEEN("BETWEEN"),
		IN("IN"), LIKE("LIKE"), EXISTS("EXISTS"),
		NOT_NULL("IS NOT NULL"), NOT_BETWEEN("NOT BETWEEN"), NOT_IN("NOT IN"), NOT_LIKE("NOT LIKE"),
		NOT_EXISTS("NOT EXISTS");

		private String text;

		Operator(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	enum JoinOperator {
		START(""), AND("AND"), OR("OR"), END("");

		private String text;

		JoinOperator(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}
}

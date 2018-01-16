package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

import java.util.Iterator;
import java.util.ListIterator;

public interface Condition extends Iterable<Condition> {
	String getFiled();

	Operator getOperator();

	JoinOperator forwardJoin();

	JoinOperator backwardJoin();

	Condition previous();

	Condition next();

	ConditionContainer getContainer();

	void setContainer(ConditionContainer container);

	Condition and(Condition condition);

	Condition or(Condition condition);

	Condition nestOr(Condition condition);

	Condition nestAnd(Condition condition);

	Condition first();

	void accept(ConditionVisitor visitor);

	void acceptRecursive(ConditionVisitor visitor);

	Iterator<Condition> shallowIterator();

	ListIterator<Condition> listIterator();

	enum Operator {
		EQ("="), NOT_EQ("<>"), GT(">"), GT_EQ(">="), LT("<"), LT_EQ("<="), NULL("IS NULL"), BETWEEN("BETWEEN"),
		IN("IN"), LIKE("LIKE"), EXISTS("EXISTS"),
		NOT_NULL("IS NOT NULL"), NOT_BETWEEN("NOT BETWEEN"), NOT_IN("NOT IN"), NOT_LIKE("NOT LIKE"),
		NOT_EXISTS("NOT EXISTS"), NONE("");

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

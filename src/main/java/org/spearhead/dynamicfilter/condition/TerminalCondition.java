package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

import java.util.Iterator;
import java.util.ListIterator;

public class TerminalCondition implements Condition {
	private Condition next;
	private Condition previous;

	public TerminalCondition(Condition next, Condition previous) {
		this.next = next;
		this.previous = previous;
	}

	public String getFiled() {
		return null;
	}

	public Operator getOperator() {
		return null;
	}

	public JoinOperator forwardJoin() {
		return JoinOperator.END;
	}

	public JoinOperator backwardJoin() {
		return JoinOperator.START;
	}

	public Condition previous() {
		return previous;
	}

	public Condition next() {
		return next;
	}

	public Condition and(Condition condition) {
		return null;
	}

	public Condition or(Condition condition) {
		return null;
	}

	public Condition stop() {
		return null;
	}

	public void accept(ConditionVisitor visitor) {

	}

	public ListIterator<Condition> listIterator() {
		return null;
	}

	public Iterator<Condition> iterator() {
		return null;
	}

	private String throwUnsupportedException() {
		throw new UnsupportedOperationException("This operationg is not supported on DoNothing Condition");
	}
}

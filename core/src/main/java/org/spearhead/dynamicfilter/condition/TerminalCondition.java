package org.spearhead.dynamicfilter.condition;

public class TerminalCondition extends ConditionAdapter {
	private Condition next;
	private Condition previous;

	public TerminalCondition(Condition next, Condition previous) {
		this.next = next;
		this.previous = previous;
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

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}

}

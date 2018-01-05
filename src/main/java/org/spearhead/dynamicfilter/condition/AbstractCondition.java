package org.spearhead.dynamicfilter.condition;

import java.util.Iterator;
import java.util.ListIterator;

abstract class AbstractCondition implements Condition {
	protected String field;
	protected Operator operator;
	protected Condition next;
	private Condition previous;
	private JoinOperator forwardJoin = JoinOperator.END;
	private JoinOperator backwardJoin = JoinOperator.START;

	AbstractCondition(String field, Operator operator) {
		this.field = field;
		this.operator = operator;
	}

	public String getFiled() {
		return field;
	}

	public JoinOperator forwardJoin() {
		return forwardJoin;
	}

	public JoinOperator backwardJoin() {
		return backwardJoin;
	}

	public Condition next() {
		return next;
	}

	public Condition previous() {
		return previous;
	}

	public Condition and(Condition condition) {
		return join(condition, JoinOperator.AND);
	}

	public Condition or(Condition condition) {
		return join(condition, JoinOperator.OR);
	}

	public Condition end() {
		Condition condition = this;
		ListIterator<Condition> iterator = condition.listIterator();
		while (iterator.hasPrevious()) {
			condition = this.previous();
		}
		return condition;
	}

	public Iterator<Condition> iterator() {
		return new ConditionIterator(this);
	}

	public ListIterator<Condition> listIterator() {
		return new ConditionListIterator(this);
	}

	private Condition join(Condition condition, JoinOperator join) {
		forwardJoin = join;
		this.next = condition;
		AbstractCondition condition1 = (AbstractCondition) condition;
		condition1.backwardJoin = join;
		condition1.previous = this;

		return condition;
	}

	public Operator getOperator() {
		return null;
	}

	private class ConditionIterator implements Iterator<Condition> {

		private Condition condition;

		private ConditionIterator(Condition condition) {
			this.condition = condition;
		}

		public boolean hasNext() {
			return condition.forwardJoin().hasNext();
		}

		public Condition next() {
			Condition ret = condition;
			condition = condition.next();
			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException("Cannot remove condition from condition chain");
		}
	}

	private class ConditionListIterator implements ListIterator<Condition> {

		private Condition condition;

		private ConditionListIterator(Condition condition) {
			this.condition = condition;
		}

		public boolean hasNext() {
			return condition.forwardJoin().hasNext();
		}

		public Condition next() {
			Condition ret = condition;
			condition = condition.next();
			return ret;
		}

		public boolean hasPrevious() {
			return condition.backwardJoin().hasPrevious();
		}

		public Condition previous() {
			Condition ret = condition;
			condition = condition.previous();
			return ret;
		}

		public int nextIndex() {
			throw new UnsupportedOperationException("Operation not supported in condition chain");
		}

		public int previousIndex() {
			throw new UnsupportedOperationException("Operation not supported in condition chain");
		}

		public void remove() {
			throw new UnsupportedOperationException("Operation not supported in condition chain");
		}

		public void set(Condition condition) {
			throw new UnsupportedOperationException("Operation not supported in condition chain");
		}

		public void add(Condition condition) {
			throw new UnsupportedOperationException("Operation not supported in condition chain");
		}
	}
}

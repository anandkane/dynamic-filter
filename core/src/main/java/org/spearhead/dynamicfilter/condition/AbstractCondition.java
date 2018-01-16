package org.spearhead.dynamicfilter.condition;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;

abstract class AbstractCondition implements Condition {
	private ConditionContainer container;
	protected String field;
	protected Operator operator;
	protected Condition next = new TerminalCondition(null, this);
	private Condition previous = new TerminalCondition(this, null);
	private JoinOperator forwardJoin = JoinOperator.END;
	private JoinOperator backwardJoin = JoinOperator.START;

	AbstractCondition(String field, Operator operator) {
		this.field = field;
		this.operator = operator;
		this.container = new InvisibleContainer(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AbstractCondition that = (AbstractCondition) o;
		return new EqualsBuilder().append(field, that.field).append(operator, that.operator).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(field).append(operator).toHashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder().append(this.getClass().getSimpleName()).append("{").append("field='").append(field)
				.append('\'').append(", operator=").append(operator).append(", forwardJoin=").append(forwardJoin)
				.append(", backwardJoin=").append(backwardJoin).append('}').toString();
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

	public ConditionContainer getContainer() {
		return container;
	}

	public void setContainer(ConditionContainer container) {
		this.container = container;
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

	public Condition nestOr(Condition condition) {
		return nest(condition, JoinOperator.OR);
	}

	public Condition nestAnd(Condition condition) {
		return nest(condition, JoinOperator.AND);
	}

	public Condition first() {
		return this.container.getStart();
	}

	public void acceptRecursive(ConditionVisitor visitor) {
		Iterator<Condition> iterator = this.iterator();
		while (iterator.hasNext()) {
			iterator.next().accept(visitor);
		}
	}

	public Iterator<Condition> shallowIterator() {
		return new ShallowIterator();
	}

	public Iterator<Condition> iterator() {
		return new ConditionIterator();
	}

	public ListIterator<Condition> listIterator() {
		return new ConditionListIterator();
	}

	public Operator getOperator() {
		return operator;
	}

	private Condition join(Condition condition, JoinOperator join) {
		forwardJoin = join;
		this.next = condition;

		AbstractCondition casted = (AbstractCondition) condition;
		casted.backwardJoin = join;
		casted.previous = this;

		// Container of linked condition should be same as that of the linking condition
		casted.container = this.container;

		return condition;
	}

	private Condition nest(Condition condition, JoinOperator joinOperator) {
		NestedCondition container = new NestedCondition(condition);
		// Link the nested condition with 'this' condition
		join(container, joinOperator);
		return container;
	}

	private class ShallowIterator implements Iterator<Condition> {
		private Condition next = AbstractCondition.this;

		public boolean hasNext() {
			return next.next() != null;
		}

		public Condition next() {
			Condition retained = next;
			next = next.next();
			return retained;
		}

		public void remove() {

		}
	}

	private class ConditionIterator implements Iterator<Condition> {

		private HashSet<Integer> iteratedLevels = new HashSet<Integer>();
		private Condition condition = AbstractCondition.this;

		public boolean hasNext() {
			return condition.next() != null;
		}

		public Condition next() {
			Condition ret = condition;
			if (condition instanceof NestedCondition) {
				if (iteratedLevels.remove(condition.hashCode())) {
					// This nested condition has been iterated over already
					condition = condition.next();
					if (!hasNext() && ret.getContainer() instanceof NestedCondition) {
						// If this is the last in the chain of 'nested' conditions, next after this is the containing
						// nested condition. And the next thereafter is the next of nested condition.
						condition = (Condition) ret.getContainer();
					}
				} else {
					// If this is nested condition and has not been iterated over yet, the one to be returned after this
					// is the first of the nested chain.
					iteratedLevels.add(condition.hashCode());
					this.condition = ((NestedCondition) condition).getStart();
				}
			} else {
				condition = condition.next();
				if (!hasNext() && ret.getContainer() instanceof NestedCondition) {
					// If this is the last in the chain of 'nested' conditions, next after this is the containing
					// nested condition. And the next thereafter is the next of nested condition.
					condition = (Condition) ret.getContainer();
				}
			}
			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException("Cannot remove condition from condition chain");
		}
	}

	private class ConditionListIterator implements ListIterator<Condition> {

		private Condition condition = AbstractCondition.this;

		public boolean hasNext() {
			return condition.next() != null;
		}

		public Condition next() {
			Condition ret = condition;
			if (condition instanceof NestedCondition) {
				// If this is nested condition, the one to be returned next is the first condition within itself.
				condition = ((NestedCondition) condition).getStart();
			} else {
				condition = condition.next();
				if (!hasNext() && ((AbstractCondition) condition).container instanceof NestedCondition) {
					// If this is the last in the chain if 'nested' conditions, next is the containing nested condition.
					condition = ((NestedCondition) ((AbstractCondition) condition).container);
				}
			}
			return ret;
		}

		public boolean hasPrevious() {
			return condition.previous() != null;
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

	private class InvisibleContainer implements ConditionContainer {
		private Condition start;

		private InvisibleContainer(Condition start) {
			this.start = start;
		}

		public Condition start(Condition condition) {
			this.start = condition;
			((AbstractCondition) this.start).container = this;
			return this.start;
		}

		public Condition getStart() {
			return start;
		}
	}
}

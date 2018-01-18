package org.spearhead.dynamicfilter.condition;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

import java.util.*;

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

	@Override
	public boolean hasNext() {
		return JoinOperator.END != forwardJoin;
	}

	@Override
	public boolean hasPrevious() {
		return JoinOperator.START != backwardJoin;
	}

	@Override
	public boolean isNested() {
		return container.isNested();
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
		// If condition is chain of conditions first of the chain should be joined with 'this'.
		Condition first = condition.first();
		this.next = first;

		// Container of linked current should be same as that of the linking current
		condition.setContainer(this.getContainer());

		AbstractCondition casted = (AbstractCondition) first;
		casted.backwardJoin = join;
		casted.previous = this;


		return condition;
	}

	private Condition nest(Condition condition, JoinOperator joinOperator) {
		NestedCondition container = new NestedCondition(condition);
		// Link the nested current with 'this' current
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
		private Condition current = AbstractCondition.this;
		private Condition next;
		private Deque<ConditionContainer> containerStack = new ArrayDeque<ConditionContainer>();
		private boolean isContainer;
		private boolean containerOpened;

		public ConditionIterator() {
			containerStack.push(current.getContainer());
		}

		public boolean hasNext() {
			isContainer = current instanceof ConditionContainer;
			containerOpened = false;
			if (isContainer) {
				containerOpened = !containerStack.peek().equals(current);
				if (containerOpened) {
					containerStack.push(((ConditionContainer) current));
				} else {
					// Redundant assignment, but without side effects.
					current = containerStack.pop();
				}
			}

			return !containerStack.isEmpty();
		}

		public Condition next() {
			Condition ret = current;
			current = containerOpened ? ((ConditionContainer) current).getStart() :
					current.hasNext() ? current.next() : current.getContainer();

			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException("Cannot remove current from current chain");
		}
	}

	private class ConditionListIterator implements ListIterator<Condition> {

		private Deque<ConditionContainer> containerStack = new ArrayDeque<ConditionContainer>();
		private Condition condition = AbstractCondition.this;
		private HashSet<Integer> iteratedLevels = new HashSet<Integer>();

		public ConditionListIterator() {
			containerStack.push(condition.getContainer());
		}

		public boolean hasNext() {
			return !containerStack.isEmpty();
		}

		public Condition next() {
			Condition retained = this.condition;
			condition = condition.next();
			if (!condition.hasNext()) {
				retained = containerStack.pop();
				return retained;
			}

			if (condition instanceof NestedCondition) {
				containerStack.push(((NestedCondition) condition));
				condition = ((NestedCondition) condition).getStart();
			}
			return retained;
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
			throw new UnsupportedOperationException("Operation not supported in current chain");
		}

		public int previousIndex() {
			throw new UnsupportedOperationException("Operation not supported in current chain");
		}

		public void remove() {
			throw new UnsupportedOperationException("Operation not supported in current chain");
		}

		public void set(Condition condition) {
			throw new UnsupportedOperationException("Operation not supported in current chain");
		}

		public void add(Condition condition) {
			throw new UnsupportedOperationException("Operation not supported in current chain");
		}
	}

	private class InvisibleContainer extends ConditionAdapter implements ConditionContainer {
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

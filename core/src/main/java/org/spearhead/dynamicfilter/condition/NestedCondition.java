package org.spearhead.dynamicfilter.condition;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

import java.util.Iterator;

public class NestedCondition extends AbstractCondition implements ConditionContainer {
	private Condition start;
	private boolean visited;

	// This is required to override hash code and equals implementation of AbstractCondition
	private Object identity = new Object();

	@Override
	public String toString() {
		return new StringBuilder().append("NestedCondition{").append("start=").append(start).append(", visited=")
				.append(visited).append(", identity=").append(identity).append('}').toString();
	}

	@Override
	public boolean isNested() {
		return true;
	}

	public NestedCondition(Condition condition) {
		super("", Operator.NONE);
		start = condition = condition.first();

		Iterator<Condition> iterator = condition.shallowIterator();
		while (iterator.hasNext()) {
			iterator.next().setContainer(this);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		NestedCondition that = (NestedCondition) o;

		return new EqualsBuilder()
				.append(identity, that.identity)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(identity)
				.toHashCode();
	}

	public Condition start(Condition condition) {
		this.start = condition;
		for (Condition each : this.start) {
			each.setContainer(this);
		}
		return this.start;
	}

	public Condition getStart() {
		return start;
	}

	public void accept(ConditionVisitor visitor) {
		if (!visited) {
			visitor.visitNestedOpen(this);
		} else {
			visitor.visitNestedClose(this);
		}
		visited = !visited;
	}
}

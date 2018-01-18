package org.spearhead.dynamicfilter.condition;

import org.spearhead.dynamicfilter.condition.visitor.ConditionVisitor;

import java.util.Iterator;
import java.util.ListIterator;

public class ConditionAdapter implements Condition {
	public String getFiled() {
		return null;
	}

	public Operator getOperator() {
		return null;
	}

	public JoinOperator forwardJoin() {
		return null;
	}

	public JoinOperator backwardJoin() {
		return null;
	}

	public Condition previous() {
		return null;
	}

	public Condition next() {
		return null;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}

	@Override
	public boolean isNested() {
		return false;
	}

	public ConditionContainer getContainer() {
		return null;
	}

	public void setContainer(ConditionContainer container) {

	}

	public Condition and(Condition condition) {
		return null;
	}

	public Condition or(Condition condition) {
		return null;
	}

	public Condition nestOr(Condition condition) {
		return null;
	}

	public Condition nestAnd(Condition condition) {
		return null;
	}

	public Condition first() {
		return null;
	}

	public void accept(ConditionVisitor visitor) {

	}

	public void acceptRecursive(ConditionVisitor visitor) {

	}

	public Iterator<Condition> shallowIterator() {
		return null;
	}

	public ListIterator<Condition> listIterator() {
		return null;
	}

	public Iterator<Condition> iterator() {
		return null;
	}
}

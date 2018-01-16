package org.spearhead.dynamicfilter.condition;

import org.junit.Test;

import java.util.Iterator;
import java.util.ListIterator;

import static org.junit.Assert.*;
import static org.spearhead.dynamicfilter.condition.Condition.JoinOperator.*;
import static org.spearhead.dynamicfilter.condition.Condition.Operator.EQ;

public class ConditionTest {

	@Test
	public void testSingleCondition() {
		Condition condition = new TestCondition("field1", EQ);
		assertEquals("field1", condition.getFiled());
		assertEquals(EQ, condition.getOperator());

		Condition next = condition.next();
		assertTrue(next instanceof TerminalCondition);
		assertNull(next.next());
		assertNotNull(next.previous());
		assertEquals(condition, next.previous());

		Condition previous = condition.previous();
		assertTrue(previous instanceof TerminalCondition);
		assertNull(previous.previous());
		assertNotNull(previous.next());
		assertEquals(condition, previous.next());

		assertEquals(START, condition.backwardJoin());
		assertEquals(END, condition.forwardJoin());

		Iterator<Condition> iterator = condition.iterator();
		assertNotNull(iterator);
		assertEquals(true, iterator.hasNext());
		assertEquals(condition, iterator.next());
		assertEquals(false, iterator.hasNext());

		ListIterator<Condition> listIterator = condition.listIterator();
		assertNotNull(listIterator);
		assertEquals(true, listIterator.hasNext());
		assertEquals(condition, listIterator.next());
		assertEquals(false, listIterator.hasNext());

		listIterator = condition.listIterator();
		assertNotNull(listIterator);
		assertEquals(true, listIterator.hasPrevious());
		assertEquals(condition, listIterator.previous());
		assertEquals(false, listIterator.hasPrevious());
	}

	@Test
	public void testConditionORing() {
		TestCondition condition1 = new TestCondition("field1", EQ);
		TestCondition condition2 = new TestCondition("field2", EQ);

		Condition or = condition1.or(condition2);
		assertEquals(condition2, or);

		assertEquals(condition2, condition1.next());
		assertEquals(condition1, condition2.previous());

		Condition previous = condition1.previous();
		assertTrue(previous instanceof TerminalCondition);
		assertNull(previous.previous());
		assertNotNull(previous.next());
		assertEquals(condition1, previous.next());

		Condition next = condition2.next();
		assertTrue(next instanceof TerminalCondition);
		assertNull(next.next());
		assertNotNull(next.previous());
		assertEquals(condition2, next.previous());

		assertEquals(START, condition1.backwardJoin());
		assertEquals(OR, condition1.forwardJoin());
		assertEquals(END, condition2.forwardJoin());
		assertEquals(OR, condition2.backwardJoin());
	}

	@Test
	public void testConditionANDing() {
		TestCondition condition1 = new TestCondition("field1", EQ);
		TestCondition condition2 = new TestCondition("field2", EQ);

		Condition and = condition1.and(condition2);
		assertEquals(condition2, and);

		assertEquals(condition2, condition1.next());
		assertEquals(condition1, condition2.previous());

		Condition previous = condition1.previous();
		assertTrue(previous instanceof TerminalCondition);
		assertNull(previous.previous());
		assertNotNull(previous.next());
		assertEquals(condition1, previous.next());

		Condition next = condition2.next();
		assertTrue(next instanceof TerminalCondition);
		assertNull(next.next());
		assertNotNull(next.previous());
		assertEquals(condition2, next.previous());

		assertEquals(START, condition1.backwardJoin());
		assertEquals(AND, condition1.forwardJoin());
		assertEquals(END, condition2.forwardJoin());
		assertEquals(AND, condition2.backwardJoin());
	}

	@Test
	public void testConditionChainEnding() {
		TestCondition condition1 = new TestCondition("field1", EQ);
		TestCondition condition2 = new TestCondition("field2", EQ);

		Condition condition = condition1.or(condition2).first();
		assertNotNull(condition);
		assertEquals(condition1, condition);

		condition = condition1.and(condition2).first();
		assertNotNull(condition);
		assertEquals(condition1, condition);
	}
}

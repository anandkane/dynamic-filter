package org.spearhead.dynamicfilter.condition;

import org.junit.Test;
import org.spearhead.dynamicfilter.condition.visitor.IterationTestVisitor;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class ConditionIterationTest {

	@Test
	public void testSingleCondition() {
		MockCondition mock = eq();
		validateIterationCount(mock, 1);
	}

	@Test
	public void testConditionChainOf2() {
		Condition condition = eq("f1").and(eq("f2")).first();
		validateIterationCount(condition, 2);
	}

	@Test
	public void testConditionChainOf3() {
		Condition condition = eq("f1").and(eq("f2").and(eq("f3"))).first();
		validateIterationCount(condition, 3);
	}

	@Test
	public void testNestedSimple() {
		Condition condition = eq("f1").nestAnd(eq("f2").and(eq("f3"))).and(eq("f4")).first();
		validateIterationCount(condition, 4, 1);
	}

	@Test
	public void testShallowIteration() {
		MockCondition c1 = eq();
		MockCondition c2 = eq();
		MockCondition c3 = eq();
		MockCondition c4 = eq();

		Condition condition = c1.nestAnd(c2.or(c3)).or(c4).first();
		Iterator<Condition> iterator = condition.shallowIterator();
		int count = 0;
		while (iterator.hasNext()) {
			iterator.next();
			count++;
		}

		assertEquals(3, count);
	}

	private void validateIterationCount(Condition mock, int binaryCount) {
		validateIterationCount(mock, binaryCount, 0);
	}

	private void validateIterationCount(Condition condition, int binaryCount, int nestCount) {
		IterationTestVisitor visitor = new IterationTestVisitor();
		for (Iterator<Condition> iterator = condition.iterator(); iterator.hasNext(); ) {
			Condition next = iterator.next();
			next.accept(visitor);
//			System.out.println(next);
		}

		assertEquals(binaryCount, visitor.binaryVisitCount);
		assertEquals(nestCount, visitor.openVisitCount);
		assertEquals(nestCount, visitor.closeVisitCount);
	}


	private MockCondition eq() {
		String field = "f";
		return eq(field);
	}

	private MockCondition eq(String field) {
		return new MockCondition(field, Condition.Operator.EQ);
	}
}

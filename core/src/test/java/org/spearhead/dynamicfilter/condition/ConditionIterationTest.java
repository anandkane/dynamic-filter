package org.spearhead.dynamicfilter.condition;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.spearhead.dynamicfilter.condition.ConditionUtil.nest;

public class ConditionIterationTest {

	@Test
	public void testShallowIteration() {
		TestCondition c1 = new TestCondition("f", Condition.Operator.EQ);
		TestCondition c2 = new TestCondition("f", Condition.Operator.EQ);
		TestCondition c3 = new TestCondition("f", Condition.Operator.EQ);
		TestCondition c4 = new TestCondition("f", Condition.Operator.EQ);

		Condition condition = c1.nestAnd(c2.or(c3)).or(c4).first();
		Iterator<Condition> iterator = condition.shallowIterator();
		int count = 0;
		while (iterator.hasNext()) {
			iterator.next();
			count++;
		}

		assertEquals(3, count);
	}
}

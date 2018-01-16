package org.spearhead.dynamicfilter.sql;

import org.junit.Test;
import org.spearhead.dynamicfilter.condition.BinaryCondition;
import org.spearhead.dynamicfilter.condition.Condition;

import static org.junit.Assert.assertEquals;
import static org.spearhead.dynamicfilter.condition.ConditionUtil.eq;
import static org.spearhead.dynamicfilter.condition.ConditionUtil.nest;

public class SqlPredicateBuilderVisitorTest {

	@Test
	public void testEquals() {
		BinaryCondition eq = eq("field1", "value");
		String sql = " field1 = value";
		validateSql(eq, sql);
	}

	@Test
	public void testAnd() {
		Condition eq = eq("field1", "value1").and(eq("field2", "value2")).first();
		String sql = " field1 = value1 AND field2 = value2";
		validateSql(eq, sql);
	}

	@Test
	public void testOr() {
		Condition eq = eq("field1", "value1").or(eq("field2", "value2")).first();
		String sql = " field1 = value1 OR field2 = value2";
		validateSql(eq, sql);
	}

	@Test
	public void testNestingInBetween() {
		Condition condition = eq("field1", "value1")
				.nestOr(eq("field2", "value2").and(eq("field3", "value3")))
				.and(eq("field4", "value4")).first();

		String sql = " field1 = value1 OR (field2 = value2 AND field3 = value3) AND field4 = value4";
		validateSql(condition, sql);
	}

	@Test
	public void testNestingAtStart() {
		Condition condition = nest(eq("field1", "value1").and(eq("field2", "value2")))
				.and(eq("field3", "value3")).first();
		String sql = " (field1 = value1 AND field2 = value2) AND field3 = value3";
		validateSql(condition, sql);
	}

	@Test
	public void testNestingAtEnd() {
		Condition condition = eq("field1", "value1")
				.nestAnd(eq("field2", "value2").and(eq("field3", "value3"))).first();
		String sql = " field1 = value1 AND (field2 = value2 AND field3 = value3)";
		validateSql(condition, sql);
	}

	@Test
	public void testMultipleNests() {
		Condition condition = eq1(1).nestOr(eq1(2).and(eq1(3)))
				.nestOr(eq1(4).and(eq1(5))).first();
		String sql = " f1 = v1 OR (f2 = v2 AND f3 = v3) OR (f4 = v4 AND f5 = v5)";

		validateSql(condition, sql);
	}

	@Test
	public void testDoubleNesting1() {
		String sql = " f1 = v1 OR ((f2 = v2 AND f3 = v3) OR (f4 = v4 AND f5 = v5))";
		Condition condition = eq1(1).nestOr(nest(eq1(2).and(eq1(3))).nestOr(eq1(4).and(eq1(5)))).first();
		validateSql(condition, sql);
	}

	public void testDoubleNesting2() {
		String sql = " (f = v OR (f = v AND f = v)) OR (f = v AND f = v)";

	}

	public void testDoubleNesting3() {
		String sql = " f = v OR (f = v AND f = v OR (f = v AND f = v))";

	}

	private void validateSql(Condition condition, String sql) {
		SqlPredicateBuilderVisitor visitor = new SqlPredicateBuilderVisitor();
		condition.acceptRecursive(visitor);

		System.out.println(visitor.getPredicate());
		assertEquals(sql, visitor.getPredicate());
	}

	private BinaryCondition eq1(int i) {
		return eq("f" + i, "v" + i);
	}
}

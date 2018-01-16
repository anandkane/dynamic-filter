package org.spearhead.dynamicfilter.condition;

public class ConditionUtil {
	public static BinaryCondition eq(String field, Object value) {
		return new BinaryConditionImpl(field, Condition.Operator.EQ, value);
	}

	public static BinaryCondition notEq(String field, Object value) {
		return new BinaryConditionImpl(field, Condition.Operator.NOT_EQ, value);
	}

	public static BinaryCondition gt(String field, Object value) {
		return new BinaryConditionImpl(field, Condition.Operator.GT, value);
	}

	public static BinaryCondition gtEq(String field, Object value) {
		return new BinaryConditionImpl(field, Condition.Operator.GT_EQ, value);
	}

	public static BinaryCondition lt(String field, Object value) {
		return new BinaryConditionImpl(field, Condition.Operator.LT, value);
	}

	public static BinaryCondition ltEq(String field, Object value) {
		return new BinaryConditionImpl(field, Condition.Operator.LT_EQ, value);
	}

	public static BetweenCondition between(String field, Object value1, Object value2) {
		return new BetweenConditionImpl(field, Condition.Operator.BETWEEN, value1, value2);
	}

	public static BetweenCondition notBetween(String field, Object value1, Object value2) {
		return new BetweenConditionImpl(field, Condition.Operator.NOT_BETWEEN, value1, value2);
	}

	public static UnaryCondition null_(String field) {
		return new UnaryConditionImpl(field, Condition.Operator.NULL);
	}

	public static UnaryCondition notNull(String field) {
		return new UnaryConditionImpl(field, Condition.Operator.NOT_NULL);
	}

	public static NestedCondition nest(Condition condition) {
		return new NestedCondition(condition);
	}
}

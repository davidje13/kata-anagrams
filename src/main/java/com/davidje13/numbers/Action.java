package com.davidje13.numbers;

import java.util.ArrayList;
import java.util.List;

public class Action {
	private final int a;
	private final int b;
	private final Operator operator;
	private final Action previous;

	public Action(int a, int b, Operator operator, Action previous) {
		this.a = a;
		this.b = b;
		this.operator = operator;
		this.previous = previous;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public int getResult() {
		return operator.apply(a, b);
	}

	public String getOperator() {
		return operator.getName();
	}

	@Override
	public String toString() {
		String v;
		try {
			v = Integer.toString(getResult());
		} catch (IllegalArgumentException e) {
			v = e.getMessage();
		}
		return a + " " + operator.getName() + " " + b + " = " + v;
	}

	public static List<Action> collect(Action action) {
		List<Action> result = new ArrayList<>();
		for (Action current = action; current != null; current = current.previous) {
			result.add(0, current);
		}
		return result;
	}
}

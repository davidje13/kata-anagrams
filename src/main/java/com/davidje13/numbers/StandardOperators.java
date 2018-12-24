package com.davidje13.numbers;

import java.util.Collection;

import static java.util.Arrays.asList;

public class StandardOperators {
	public static final Operator ADD = new Operator() {
		@Override
		public String getName() {
			return "+";
		}

		@Override
		public boolean supports(int a, int b) {
			// prevent duplicate solutions, since a + b == b + a
			return a >= b;
		}

		@Override
		public int apply(int a, int b) {
			return a + b;
		}
	};

	public static final Operator SUBTRACT_POSITIVE = new Operator() {
		@Override
		public String getName() {
			return "-";
		}

		@Override
		public boolean supports(int a, int b) {
			return a > b;
		}

		@Override
		public int apply(int a, int b) {
			return a - b;
		}
	};

	public static final Operator MULTIPLY = new Operator() {
		@Override
		public String getName() {
			return "*";
		}

		@Override
		public boolean supports(int a, int b) {
			// prevent duplicate solutions, since a + b == b + a
			return a >= b;
		}

		@Override
		public int apply(int a, int b) {
			return a * b;
		}
	};

	public static final Operator DIVIDE_WHOLE = new Operator() {
		@Override
		public String getName() {
			return "/";
		}

		@Override
		public boolean supports(int a, int b) {
			return b != 0 && (a % b) == 0;
		}

		@Override
		public int apply(int a, int b) {
			return a / b;
		}
	};

	public static final Collection<Operator> COUNTDOWN_RULES = asList(
			ADD,
			SUBTRACT_POSITIVE,
			MULTIPLY,
			DIVIDE_WHOLE
	);
}

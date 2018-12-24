package com.davidje13.numbers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class FormulaFinder {
	private final Collection<Operator> operators;

	public FormulaFinder(Collection<Operator> operators) {
		this.operators = operators;
	}

	public Collection<Formula> findShortestFormulas(Collection<Integer> inputs, int target) {
		Map<ValueCounter, List<Action>> current = new HashMap<>();
		current.put(new ValueCounter(inputs), singletonList(null));

		for (int n = inputs.size(); n > 0; -- n) {
			Collection<Formula> solutions = current.entrySet().stream()
					.filter((i1) -> i1.getKey().containsKey(target))
					.map(Map.Entry::getValue)
					.flatMap(Collection::stream)
					.map(Action::collect)
					.map(Formula::new)
					.collect(toList());

			if (!solutions.isEmpty()) {
				return solutions;
			}

			current = current.entrySet().stream()
					.map((i) -> new State(i.getKey(), i.getValue()))
					.flatMap(State::allPairings)
					.flatMap(this::applyOperators)
					.collect(groupingBy(
							(i) -> i.remaining,
							flatMapping((i) -> i.history.stream(), toList())
					));
		}

		return emptySet();
	}

	private Stream<State> applyOperators(Pairing pairing) {
		return operators.stream()
				.filter((op) -> op.supports(pairing.a, pairing.b))
				.map(pairing::applyOperator);
	}

	private static class State {
		private final ValueCounter remaining;
		private final Collection<Action> history;

		private State(ValueCounter remaining, Collection<Action> history) {
			this.remaining = remaining;
			this.history = history;
		}

		private Stream<Pairing> allPairings() {
			return remaining.keySet().stream()
					.flatMap((i) -> remaining.keySet().stream()
							.filter((j) -> (!i.equals(j) || remaining.get(i) >= 2))
							.map((j) -> withPairing(i, j))
					);
		}

		private Pairing withPairing(int a, int b) {
			return new Pairing(
					remaining.copy().decrement(a).decrement(b),
					history,
					a,
					b
			);
		}
	}

	private static class Pairing {
		private final ValueCounter remaining;
		private final Collection<Action> history;
		private final int a;
		private final int b;

		private Pairing(ValueCounter remaining, Collection<Action> history, int a, int b) {
			this.remaining = remaining;
			this.history = history;
			this.a = a;
			this.b = b;
		}

		private State applyOperator(Operator op) {
			return new State(
					remaining.copy().increment(op.apply(a, b)),
					history.stream()
							.map((h) -> new Action(a, b, op, h))
							.collect(toList())
			);
		}
	}
}

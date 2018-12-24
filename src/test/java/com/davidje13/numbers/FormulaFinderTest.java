package com.davidje13.numbers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class FormulaFinderTest {
	private final FormulaFinder finder = new FormulaFinder(StandardOperators.COUNTDOWN_RULES);

	@Test
	public void findShortestFormulas_returnsEquationsWhichProduceTheTarget() {
		List<Integer> inputs = asList(1, 2, 3);
		int target = 6;

		Collection<Formula> formulas = finder.findShortestFormulas(inputs, target);

		assertThat(formulas, hasSize(greaterThan(0)));
		for (Formula formula : formulas) {
			assertThat(result(formula), equalTo(target));
		}
	}

	@Test
	public void findShortestFormulas_returnsEquationsWhichUseTheInputs() {
		List<Integer> inputs = asList(1, 2, 3);
		int target = 6;

		Collection<Formula> formulas = finder.findShortestFormulas(inputs, target);

		assertThat(formulas, hasSize(greaterThan(0)));
		for (Formula formula : formulas) {
			assertValid(inputs, formula);
		}
	}

	@Test
	public void findShortestFormulas_canUseAllInputs() {
		List<Integer> inputs = asList(1, 2, 3);
		int target = 7;

		Collection<Formula> formulas = finder.findShortestFormulas(inputs, target);

		assertThat(formulas, hasSize(greaterThan(0)));
		for (Formula formula : formulas) {
			assertValid(inputs, formula);
			assertThat(result(formula), equalTo(target));
			assertThat(formula.getActions().size(), equalTo(2));
		}
	}

	@Test
	public void findShortestFormulas_needNotUseAllInputValues() {
		List<Integer> inputs = asList(1, 2, 3);
		int target = 5;

		Collection<Formula> formulas = finder.findShortestFormulas(inputs, target);

		assertThat(formulas, hasSize(greaterThan(0)));
		for (Formula formula : formulas) {
			assertValid(inputs, formula);
			assertThat(result(formula), equalTo(target));
			assertThat(formula.getActions().size(), equalTo(1));
		}
	}

	@Test
	public void findShortestFormulas_returnsTheShortestSolutions() {
		List<Integer> inputs = asList(1, 2, 3);
		int target = 6;

		Collection<Formula> formulas = finder.findShortestFormulas(inputs, target);

		assertThat(formulas, hasSize(greaterThan(0)));
		for (Formula formula : formulas) {
			assertValid(inputs, formula);
			assertThat(result(formula), equalTo(target));
			assertThat("did not produce shortest solution", formula.getActions().size(), equalTo(1));
		}
	}

	@Test
	public void findShortestFormulas_returnsNoFormulasIfTargetIsImpossible() {
		List<Integer> inputs = asList(1, 2, 3);
		int target = 1000;

		Collection<Formula> formulas = finder.findShortestFormulas(inputs, target);

		assertThat(formulas, hasSize(0));
	}

	@Test
	public void findShortestFormulas_canSolveRealProblems() {
		List<Integer> inputs = asList(2, 6, 8, 9, 75, 100);
		int target = 776;

		Collection<Formula> formulas = finder.findShortestFormulas(inputs, target);
		assertThat(formulas, hasSize(greaterThan(0)));

		for (Formula formula : formulas) {
			assertValid(inputs, formula);
			assertThat(result(formula), equalTo(target));
		}
	}

	@Test
	public void findShortestFormulas_runsInReasonableTime() {
		List<Integer> inputs = asList(2, 6, 8, 9, 75, 100);
		int target = 1000000000; // impossible target forces full evaluation

		int repetitions = 4;

		long tm0 = System.currentTimeMillis();
		for (int i = 0; i < repetitions; ++ i) {
			finder.findShortestFormulas(inputs, target);
		}
		long tm1 = System.currentTimeMillis();

		double averageTime = (tm1 - tm0) * 0.001 / repetitions;
		assertThat(averageTime, lessThan(1.0));
	}

	private void assertValid(Collection<Integer> inputs, Formula formula) {
		System.out.println("Testing formula with inputs: " + inputs);

		List<Integer> current = new ArrayList<>(inputs);
		for (Action action : formula.getActions()) {
			System.out.println(action);

			int a = action.getA();
			int b = action.getB();
			int v = action.getResult();

			assertThat("required for " + action, current, hasItem(a));
			current.remove(Integer.valueOf(a));

			assertThat("required for " + action, current, hasItem(b));
			current.remove(Integer.valueOf(b));

			current.add(v);
		}
	}

	private int result(Formula formula) {
		List<Action> actions = formula.getActions();
		Action lastAction = actions.get(actions.size() - 1);
		return lastAction.getResult();
	}
}

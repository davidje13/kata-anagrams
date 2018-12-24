package com.davidje13.numbers;

import java.util.List;

public class Formula {
	private final List<Action> actions;

	public Formula(List<Action> actions) {
		this.actions = actions;
	}

	public List<Action> getActions() {
		return actions;
	}
}

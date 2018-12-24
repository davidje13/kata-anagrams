package com.davidje13.numbers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class ValueCounter extends HashMap<Integer, Long> {
	public ValueCounter(Collection<Integer> values) {
		super(values.stream().collect(groupingBy((v) -> v, counting())));
	}

	private ValueCounter(Map<Integer, Long> copy) {
		super(copy);
	}

	public ValueCounter copy() {
		return new ValueCounter(this);
	}

	public ValueCounter increment(Integer v) {
		put(v, getOrDefault(v, 0L) + 1);
		return this;
	}

	public ValueCounter decrement(Integer v) {
		long count = get(v);
		if (count == 1) {
			remove(v);
		} else {
			put(v, count - 1);
		}
		return this;
	}
}

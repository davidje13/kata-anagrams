package com.davidje13;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class OptionParser {
	Options parseOptions(String[] args) {
		Options options = new Options();

		for (String arg : args) {
			if (SORT_ORDERS.containsKey(arg)) {
				options.sortOrder = SORT_ORDERS.get(arg);
			}
		}

		return options;
	}

	static class Options {
		private Comparator<Set<String>> sortOrder = ANY_ORDER;

		Comparator<Set<String>> getSortOrder() {
			return sortOrder;
		}
	}

	private static final Comparator<Set<String>> ANY_ORDER =
			Comparator.comparingInt((o) -> 0);

	private static final Comparator<Set<String>> MOST_WORDS_LAST =
			Comparator.comparingInt(Set::size);

	private static final Comparator<Set<String>> LONGEST_WORDS_LAST =
			Comparator.comparingInt((set) -> set.iterator().next().length());

	private static final Map<String, Comparator<Set<String>>> SORT_ORDERS;

	static {
		SORT_ORDERS = new HashMap<>();
		SORT_ORDERS.put("--words-asc", MOST_WORDS_LAST);
		SORT_ORDERS.put("--words-desc", MOST_WORDS_LAST.reversed());
		SORT_ORDERS.put("--length-asc", LONGEST_WORDS_LAST);
		SORT_ORDERS.put("--length-desc", LONGEST_WORDS_LAST.reversed());
	}
}

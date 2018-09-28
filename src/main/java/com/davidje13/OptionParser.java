package com.davidje13;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OptionParser {
	Options parseOptions(String[] args) {
		Options options = new Options();

		for (String arg : args) {
			if (SORT_ORDERS.containsKey(arg)) {
				options.sortOrder = SORT_ORDERS.get(arg);
			} else if (options.wordListFile == null) {
				options.wordListFile = arg;
			} else {
				options.words.add(arg);
			}
		}

		return options;
	}

	static class Options {
		private String wordListFile = null;
		private final List<String> words = new ArrayList<>();
		private Comparator<Collection<String>> sortOrder = ANY_ORDER;

		String getWordListFile() {
			return wordListFile;
		}

		List<String> getWords() {
			return words;
		}

		Comparator<Collection<String>> getSortOrder() {
			return sortOrder;
		}
	}

	private static final Comparator<Collection<String>> ANY_ORDER =
			Comparator.comparingInt((o) -> 0);

	private static final Comparator<Collection<String>> MOST_WORDS_LAST =
			Comparator.comparingInt(Collection::size);

	private static final Comparator<Collection<String>> LONGEST_WORDS_LAST =
			Comparator.comparingInt((set) -> set.iterator().next().length());

	private static final Map<String, Comparator<Collection<String>>> SORT_ORDERS;

	static {
		SORT_ORDERS = new HashMap<>();
		SORT_ORDERS.put("--words-asc", MOST_WORDS_LAST);
		SORT_ORDERS.put("--words-desc", MOST_WORDS_LAST.reversed());
		SORT_ORDERS.put("--length-asc", LONGEST_WORDS_LAST);
		SORT_ORDERS.put("--length-desc", LONGEST_WORDS_LAST.reversed());
	}
}

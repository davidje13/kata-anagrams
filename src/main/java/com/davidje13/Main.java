package com.davidje13;

import com.davidje13.OptionParser.Options;
import com.davidje13.anagram.AnagramFinder;
import com.davidje13.numbers.Action;
import com.davidje13.numbers.Formula;
import com.davidje13.numbers.FormulaFinder;
import com.davidje13.numbers.StandardOperators;
import com.davidje13.permutation.PermutationExpander;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
	public static void main(String[] args) {
		boolean numeric = args.length >= 2;
		for (String arg : args) {
			try {
				Integer.parseInt(arg);
			} catch (NumberFormatException ignore) {
				numeric = false;
			}
		}
		if (numeric) {
			solveNumeric(args);
		} else {
			solveString(args);
		}
	}

	private static void solveNumeric(String[] args) {
		Collection<Integer> inputs = new ArrayList<>();
		for (int i = 0; i < args.length - 1; ++ i) {
			inputs.add(Integer.parseInt(args[i]));
		}
		int target = Integer.parseInt(args[args.length - 1]);

		FormulaFinder finder = new FormulaFinder(StandardOperators.COUNTDOWN_RULES);
		Collection<Formula> formulas = finder.findShortestFormulas(inputs, target);
		if (formulas.isEmpty()) {
			System.err.println("No formula found");
		} else {
			Formula formula = formulas.iterator().next();
			for (Action action : formula.getActions()) {
				System.out.println(action.toString());
			}
			System.out.println("= " + target);
		}
	}

	private static void solveString(String[] args) {
		Options options = new OptionParser().parseOptions(args);

		String wordListFile = options.getWordListFile();
		if (wordListFile == null) {
			showUsage();
			return;
		}

		AnagramFinder finder = new AnagramFinder();
		PermutationExpander expander = new PermutationExpander();

		try {
			finder.loadWords(
					Files.lines(new File(wordListFile).toPath(), UTF_8),
					1048576
			);
		} catch (IOException e) {
			System.err.println("Failed to load word list from " + wordListFile);
			return;
		}

		if (options.getWords().isEmpty()) {
			finder.findAnagrams()
					.sorted(options.getSortOrder())
					.map((group) -> String.join(" ", group))
					.forEach(System.out::println);
		} else if (options.isLengthBased()) {
			for (String word : options.getWords()) {
				finder.findWordsWithin(word)
						.sorted(options.getSortOrder())
						.map((group) -> String.join(" ", group))
						.forEach(System.out::println);
			}
		} else {
			for (String word : options.getWords()) {
				finder.findMultiWordAnagrams(word, 3)
						.flatMap(expander::expand)
						.sorted(options.getSortOrder())
						.map((group) -> String.join(" ", group))
						.forEach(System.out::println);
			}
		}
	}

	private static void showUsage() {
		System.err.println("Finds anagrams in a given dictionary.");
		System.err.println();
		System.err.println("Usage:");
		System.err.println("  ./program <path_to_word_list>");
		System.err.println("  ./program <path_to_word_list> <word>");
		System.err.println();
		System.err.println("Combines numbers to reach a target.");
		System.err.println();
		System.err.println("Usage:");
		System.err.println("  ./program <number1> <number2> [...] <target>");
	}
}

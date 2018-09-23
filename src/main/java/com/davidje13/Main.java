package com.davidje13;

import com.davidje13.OptionParser.Options;
import com.davidje13.anagram.AnagramFinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {
	public static void main(String[] args) {
		Options options = new OptionParser().parseOptions(args);
		Charset utf8 = StandardCharsets.UTF_8;

		try (
				Reader isr = new InputStreamReader(System.in, utf8);
				BufferedReader br = new BufferedReader(isr)
		) {
			AnagramFinder finder = new AnagramFinder();

			finder.findAnagrams(br.lines(), 1048576)
					.sorted(options.getSortOrder())
					.map((group) -> String.join(" ", group))
					.forEach(System.out::println);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}

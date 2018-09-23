package com.davidje13;

import com.davidje13.anagram.AnagramFinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) {
		Charset utf8 = StandardCharsets.UTF_8;

		try (
				Reader isr = new InputStreamReader(System.in, utf8);
				BufferedReader br = new BufferedReader(isr)
		) {
			parseWords(br.lines());
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void parseWords(Stream<String> words) {
		AnagramFinder finder = new AnagramFinder();
		finder.findAnagrams(words)
				.forEach((group) -> System.out.println(String.join(" ", group)));
	}
}

package com.davidje13.permutation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class PermutationExpander {
	public <T> Stream<Collection<T>> expand(Collection<Set<T>> parts) {
		return expand(parts.stream().map(ArrayList::new).collect(toList()));
	}

	public <T> Stream<Collection<T>> expand(List<List<T>> parts) {
		int size = parts.size();
		List<Integer> ordinals = new ArrayList<>();
		List<Integer> counts = new ArrayList<>();
		int total = 1;
		for (List<T> part : parts) {
			int count = part.size();
			ordinals.add(0);
			counts.add(count);
			total *= count;
		}

		return Stream
				.iterate(ordinals, (vs) -> {
					List<Integer> result = new ArrayList<>(size);
					boolean carry = true;
					for (int i = 0; i < size; ++ i) {
						int v = vs.get(i);
						if (carry) {
							++ v;
							if (v == counts.get(i)) {
								v = 0;
							} else {
								carry = false;
							}
						}
						result.add(v);
					}
					return result;
				})
				.limit(total)
				.map((v) -> {
					Collection<T> result = new ArrayList<>(size);
					for (int i = 0; i < size; ++ i) {
						result.add(parts.get(i).get(v.get(i)));
					}
					return result;
				});
	}
}

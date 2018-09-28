# Anagram Finder

Implementation of katas to:

* find anagrams in a list of words
* find n-word anagrams of an input word (up to 3 words)

---

The kata specifications are here:

* http://codekata.com/kata/kata06-anagrams/
* http://codingdojo.org/kata/Anagram/

## Testing

```sh
./gradlew test
```

## Running

```sh
# Report all anagrams in wordlist
./gradlew run --args='/usr/share/dict/words --length-asc'

# Report all multi-word anagrams of the input word
./gradlew run --args='/usr/share/dict/words documenting --words-desc'

# Report all words contained within the input word ("countdown"-style)
./gradlew run --args='/usr/share/dict/words documenting --length-asc'
```

Identified anagrams will be printed to stdout.

### Flags

The order of the output can be controlled with the following flags:

* `--words-desc`: sets with the most words first
* `--words-asc`: sets with the most words last
* `--length-desc`: sets with the longest words first
* `--length-asc`: sets with the longest words first

## Profiling

The executable can be profiled using:

```sh
./gradlew installDist && time ./build/install/anagrams/bin/anagrams /usr/share/dict/words documenting
```

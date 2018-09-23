# Anagram Finder

Implementation of a kata to find anagrams in a list of words.

---

The kata specification is here: http://codekata.com/kata/kata06-anagrams/

## Testing

```sh
./gradlew test
```

## Running

```sh
./gradlew installDist && ./build/install/anagrams/bin/anagrams < /usr/share/dict/words
```

Identified anagrams will be printed to stdout.

### Flags

The order of the output can be controlled with the following flags:

* `--words-desc`: sets with the most words first
* `--words-asc`: sets with the most words last
* `--length-desc`: sets with the longest words first
* `--length-asc`: sets with the longest words first

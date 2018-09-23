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

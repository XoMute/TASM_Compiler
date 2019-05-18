package com.xomute.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

  public static List<String> split(String line) {
    List<String> splittedLine = new ArrayList<>();

    // check for String constant
    if (line.contains("\"")) {
      splittedLine.add(line.split(" ")[0]);
      splittedLine.add(line.split(" ")[1]);
      splittedLine.add(line.substring(line.indexOf("\"")));
    } else {
      return splitByOneSymbLexems(
          Arrays.stream(line.split(" "))
              .filter(_line -> !_line.isEmpty())
              .collect(Collectors.toList()));
    }
    return splittedLine;
  }

  private static List<String> splitByOneSymbLexems(List<String> list) {
    List<String> splittedLine = new ArrayList<>();
    for (String word : list) {
      if (AssemblerHelper.containsOneSymbLexems(word)) {
        splittedLine.addAll(splitByLexem(word));
      } else {
        splittedLine.add(word);
      }
    }
    return splittedLine;
  }

  private static List<String> splitByLexem(String word) {
    List<String> splittedWord = new ArrayList<>();
    StringBuilder correctWord = new StringBuilder();
    boolean isWord = true;
    for (char c : word.toCharArray()) {
      isWord = true;
      for (String lexem : AssemblerHelper.ONE_SYMBOL_LEXEMS) {
        if (lexem.charAt(0) == c) {
          isWord = false;
          break;
        }
      }
      if (isWord) {
        correctWord.append(c);
      } else {
        if (!correctWord.toString().isEmpty()) {
          splittedWord.add(correctWord.toString());
        }
        correctWord = new StringBuilder();
        splittedWord.add("" + c);
      }
    }
    if (!correctWord.toString().isEmpty()) {
      splittedWord.add(correctWord.toString());
    }
    return splittedWord;
  }
}

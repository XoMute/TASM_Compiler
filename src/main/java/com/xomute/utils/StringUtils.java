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
      if (line.indexOf("\"") == 0) {
        splittedLine.add(line.substring(line.indexOf("\"")));
      } else {
        splittedLine.add(line.split("\\s+")[0]);
        splittedLine.add(line.split("\\s+")[1]);
        splittedLine.add(line.substring(line.indexOf("\"")));
      }
    } else {
      splittedLine.addAll(
          splitByOneSymbLexems(
              Arrays.stream(line.split(" "))
                  .filter(_line -> !_line.isEmpty())
                  .collect(Collectors.toList())));
    }
    return splittedLine.stream().map(String::trim).collect(Collectors.toList());
  }

  public static List<String> splitByOperands(String line) {
    line = line.trim();
    if (line.contains("\"")) {
      return StringUtils.split(line);
    }
    return Arrays.asList(line.split(",?\\s+"));
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
    boolean isWord;
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

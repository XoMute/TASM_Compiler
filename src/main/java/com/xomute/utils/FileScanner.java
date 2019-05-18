package com.xomute.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileScanner {

  public List<String> readFile(String path) {
    List<String> fileLines = new ArrayList<>();
    File file = new File(path);

    try (Scanner sc = new Scanner(file)) {
      while (sc.hasNextLine()) {
        fileLines.add(sc.nextLine().trim());
      }
    } catch (IOException e) {
      System.out.println("Some error in reading file: " + e.getMessage());
      System.exit(-1);
    }
    return fileLines;
  }
}

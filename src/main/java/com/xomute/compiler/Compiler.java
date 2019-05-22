package com.xomute.compiler;

import com.xomute.compiler.interfaces.Mnemocode;
import com.xomute.lexer.SourceLine;

import java.util.List;

public class Compiler {

  public void compileToFile(List<SourceLine> srcLines) {
    compile(srcLines);
  }

  public void compileToConsole(List<SourceLine> srcLines) {
    compile(srcLines);
    int i = 1;
    for (SourceLine srcLine : srcLines) {
      System.out.println(String.format("%3d\t", i++) + convertToLSTString(srcLine));
    }
  }

  private void compile(List<SourceLine> srcLines) {
    firstGoThrough(srcLines);
    secondGoThrough(srcLines);
  }

  private String convertToLSTString(SourceLine srcLine) {
    String line =
        String.format("%4s  %s  %s", srcLine.getOffset(), srcLine.getByteCode(), srcLine.getLine());
    return line;
  }

  ///////////////// FIRST GO THROUGH ////////////////////////

  /** this method adds offset in each SourceLine */
  private void firstGoThrough(List<SourceLine> srcLines) {
    int offset = 0;
    for (SourceLine srcLine : srcLines) {
      srcLine.setOffset(this.getOffset(offset));

      if (!srcLine.getMnemocode().isPresent()) {
        continue;
      }
      Mnemocode mnemocode = srcLine.getMnemocode().get();

      offset += mnemocode.getOffset();
    }
  }

  private String getOffset(int offset) {
    StringBuilder strOffset = new StringBuilder(Integer.toHexString(offset));
    while (strOffset.length() < 4) {
      strOffset.insert(0, "0");
    }
    return strOffset.toString();
  }

  ///////////////////////////////////////////////////////////

  ///////////////// SECOND GO THROUGH ///////////////////////

  /** this method adds byteCode in each SourceLine */
  private void secondGoThrough(List<SourceLine> srcLines) {}

  ///////////////////////////////////////////////////////////
}

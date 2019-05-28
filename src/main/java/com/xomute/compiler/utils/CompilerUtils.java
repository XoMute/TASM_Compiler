package com.xomute.compiler.utils;

import com.xomute.compiler.Compiler;
import com.xomute.utils.StringConstants;

public class CompilerUtils {

  public static String getOffset(int offset) {
    StringBuilder strOffset = new StringBuilder(Integer.toHexString(offset));
    while (strOffset.length() < 4) {
      strOffset.insert(0, "0");
    }
    return strOffset.toString().toUpperCase();
  }

  public static boolean isJumpBack(String label) {
    return !Compiler.getCurrentSegment()
        .getLabelOffset(label)
        .equals(StringConstants.NO_SUCH_LABEL);
  }

  public static String getLabelOffset(String label) {
    return Compiler.getCurrentSegment().getLabelOffset(label);
  }
}

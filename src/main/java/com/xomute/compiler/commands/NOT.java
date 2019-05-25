package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;
import com.xomute.utils.AssemblerHelper;

import java.util.HashMap;
import java.util.Map;

public class NOT implements Command {
  private String code = "";
  private int offset = 2;

  private String operand;

  public NOT(String operand) {
    this.operand = operand;
    if (AssemblerHelper.isRegister8(operand)) {
      code = "F6 ";
    } else if (AssemblerHelper.isRegister16(operand)) {
      code = "F7 ";
    } else {
      code = "ERROR";
    }
    code += AssemblerHelper.getModRM("DX", operand);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  @Deprecated
  public void updateCode() {

  }
}

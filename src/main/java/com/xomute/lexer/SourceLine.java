package com.xomute.lexer;

import com.xomute.utils.AssemblerHelper;
import com.xomute.utils.StringUtils;

import java.util.List;

public class SourceLine {

  private String line;

  private int nameIndex = -1;

  private int mnemocodeIndex = -1;
  private int mnemocodeLexemNumber = -1;

  private int firstOperandIndex = -1;
  private int firstOperandLexemNumber = -1;

  private int secondOperandIndex = -1;
  private int secondOperandLexemNumber = -1;

  public SourceLine(String line) {
    this.line = line;
    calculateIndexes();
  }

  public void calculateIndexes() {
    List<String> operands = StringUtils.splitByOperands(this.getLine());
    if (AssemblerHelper.isName(operands.get(0))) {
      nameIndex = 0;
    } else {
      mnemocodeIndex = 0;
      mnemocodeLexemNumber = StringUtils.split(operands.get(0)).size();
    }

    if (operands.size() > 1) {
      String firstOperand = operands.get(1);
      if (mnemocodeIndex == -1 && AssemblerHelper.isMnemocode(firstOperand)) {
        mnemocodeIndex = getIndex(firstOperand);
        mnemocodeLexemNumber = getSize(firstOperand);
      } else {
        firstOperandIndex = getIndex(firstOperand);
        firstOperandLexemNumber = getSize(firstOperand);
      }
    }

    if (operands.size() > 2) {
      String secondOperand = operands.get(2);
      if (firstOperandIndex == -1) {
        firstOperandIndex = getIndex(secondOperand);
        firstOperandLexemNumber = getSize(secondOperand);
      } else {
        secondOperandIndex = getIndex(secondOperand);
        secondOperandLexemNumber = getSize(secondOperand);
      }
    }
  }

  private int getIndex(String field) {
    return StringUtils.split(line).indexOf(StringUtils.split(field).get(0));
  }

  private int getSize(String field) {
    return StringUtils.split(field).size();
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public List<String> getLexems() {
    return StringUtils.split(line);
  }

  public int getNameIndex() {
    return nameIndex;
  }

  public int getMnemocodeIndex() {
    return mnemocodeIndex;
  }

  public int getMnemocodeLexemNumber() {
    return mnemocodeLexemNumber;
  }

  public int getFirstOperandIndex() {
    return firstOperandIndex;
  }

  public int getFirstOperandLexemNumber() {
    return firstOperandLexemNumber;
  }

  public int getSecondOperandIndex() {
    return secondOperandIndex;
  }

  public int getSecondOperandLexemNumber() {
    return secondOperandLexemNumber;
  }
}

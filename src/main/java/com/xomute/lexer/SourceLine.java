package com.xomute.lexer;

import com.xomute.compiler.interfaces.Mnemocode;
import com.xomute.utils.AssemblerHelper;
import com.xomute.utils.StringUtils;

import java.util.List;
import java.util.Optional;

public class SourceLine {

  // flag to skip processing this line (in case of error or macro processing)
  private boolean skipByCompiler = false;

  private Mnemocode mnemocode;

  private String line;

  private int nameIndex = -1;

  private int mnemocodeIndex = -1;
  private int mnemocodeLexemNumber = -1;

  private int firstOperandIndex = -1;
  private int firstOperandLexemNumber = -1;

  private int secondOperandIndex = -1;
  private int secondOperandLexemNumber = -1;

  private String offset = "";
  private String byteCode = "";

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

  public Optional<Mnemocode> getMnemocode() {
    if (mnemocodeIndex != -1 && mnemocode == null) {
    	String strMnemocode = StringUtils.split(this.line).get(mnemocodeIndex);
    	if (AssemblerHelper.isCommand(strMnemocode)) {
				this.mnemocode = AssemblerHelper.getCommand(strMnemocode, this.line);
	    } else if (AssemblerHelper.isDataIdentifier(strMnemocode)) {
        this.mnemocode = AssemblerHelper.getDataIdentifier(strMnemocode, this.line);
	    } else {
        this.mnemocode = AssemblerHelper.getDirective(strMnemocode);
	    }
    }
    return Optional.ofNullable(this.mnemocode);
  }

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getByteCode() {
    if (skipByCompiler) {
      return "";
    }

		Optional<Mnemocode> opt = getMnemocode();
		if (opt.isPresent()) {
		  return opt.get().getCode();
    }
		return "";
	}

  public boolean isSkipByCompiler() {
    return skipByCompiler;
  }

  public void setSkipByCompiler(boolean skipByCompiler) {
    this.skipByCompiler = skipByCompiler;
  }
}

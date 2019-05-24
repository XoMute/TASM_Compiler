package com.xomute.compiler.identifiers;

import com.xomute.compiler.interfaces.DataIdentifier;
import com.xomute.utils.AssemblerHelper;

public class DB extends DataIdentifier {

  public DB(String identifier, String value) {
    super(identifier, value);
    calculate();
  }

  protected void calculate() {
  	super.calculate();
  	if (AssemblerHelper.processLexem(value, true).equals(AssemblerHelper.Type.STRING_CONSTANT)) {
		  this.code = calculateCodeForString(value).toUpperCase();
		  this.offset = code.split(" ").length;
	  }
  	beautifyCode(2);
  }

	private String calculateCodeForString(String value) {
		StringBuilder code = new StringBuilder();
		for (int i = 1; i < value.length() - 1; i++) {
			char c = value.charAt(i);
			String hex = Integer.toHexString(c);
			code.append(hex).append(" ");
		}
		return code.toString();
	}

	@Override
	@Deprecated
	public void updateCode() {

	}
}

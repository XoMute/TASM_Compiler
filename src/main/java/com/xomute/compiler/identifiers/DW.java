package com.xomute.compiler.identifiers;

import com.xomute.compiler.interfaces.DataIdentifier;

public class DW extends DataIdentifier {
	private int offset = 2;

	public DW(String identifier, String value) {
		super(identifier, value);
		calculate();
	}

	protected void calculate() {
  	super.calculate();
		beautifyCode(4);
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

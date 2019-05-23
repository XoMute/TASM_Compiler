package com.xomute.compiler.identifiers;

import com.xomute.compiler.interfaces.DataIdentifier;

public class DD extends DataIdentifier {
	private int offset = 4;

	public DD(String identifier, String value) {
		super(identifier, value);
		calculate();
	}

	protected void calculate() {
  	super.calculate();
		beautifyCode(8);
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getOffset() {
		return offset;
	}
}

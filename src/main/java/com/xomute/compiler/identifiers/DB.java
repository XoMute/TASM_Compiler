package com.xomute.compiler.identifiers;

import com.xomute.compiler.interfaces.DataIdentifier;

@Deprecated
public class DB implements DataIdentifier {
	private String code;
	private int offset;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getOffset() {
		return offset;
	}
}

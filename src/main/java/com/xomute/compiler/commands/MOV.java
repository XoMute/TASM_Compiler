package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;

@Deprecated
public class MOV implements Command {
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

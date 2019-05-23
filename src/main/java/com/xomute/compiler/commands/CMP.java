package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;
import com.xomute.utils.AssemblerHelper;

public class CMP implements Command {
	private String code;
	private int offset = 2;

	private String operand1;
	private String operand2;

	public CMP(String operand1, String operand2) {
		this.operand1 = operand1;
		this.operand2 = operand2;
		if (AssemblerHelper.isRegister8(operand1)) {
			code = "3A ";
		} else if (AssemblerHelper.isRegister16(operand1)) {
			code = "3B ";
		} else {
			code = "ERROR";
			// todo: error
		}
		code += AssemblerHelper.getModRM(operand1, operand2);
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

package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;
import com.xomute.utils.AssemblerHelper;
import com.xomute.utils.StringUtils;

public class BTS implements Command {

	private String code = "";
	private int offset;

	private String operand1;
	private String operand2;

	public BTS(String operand1, String operand2) {
		this.operand1 = operand1;
		this.operand2 = operand2;
		code = "0F BA ";
		code += AssemblerHelper.getModRM("BP", operand1) + " ";

		String hexValue = "";
		String pureOperand2 = operand2.substring(0, operand2.length() - 1);
		if (AssemblerHelper.isHexConstant(operand2)) {
			hexValue = pureOperand2;
		} else if (AssemblerHelper.isDecConstant(operand2)) {
			hexValue = AssemblerHelper.decToHex(operand2);
		} else if (AssemblerHelper.isBinConstant(operand2)) {
			hexValue = AssemblerHelper.binToHex(pureOperand2);
		} else {
			code = "ERROR: Wrong 2nd operand";
		}

		code += StringUtils.beautifyCode(hexValue, 2);
		offset = 4;
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

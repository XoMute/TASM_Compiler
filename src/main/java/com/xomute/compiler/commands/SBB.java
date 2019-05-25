package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;
import com.xomute.lexer.lexems.Identifier;
import com.xomute.utils.AssemblerHelper;
import com.xomute.utils.StringUtils;

public class SBB implements Command {
	private String code = "";
	private int offset;

	private String operand1;
	private String operand2;

	public SBB(String operand1, String operand2) {
		this.operand1 = operand1;
		this.operand2 = operand2;
		code = AssemblerHelper.getPrefix(operand1);
		if (!code.isEmpty()) {
			offset++;
		}
		if (AssemblerHelper.isIdentifier16(operand1)) {
			code += "19 ";
		} else {
			code += "18 ";
		}
		offset++;

		code += AssemblerHelper.getModRM(operand2, operand1) + " ";
		code +=
				StringUtils.beautifyCode(
						Integer.toHexString(
								AssemblerHelper.getIdentifier(
										AssemblerHelper.getIdentifierNameFromOperand(operand1))
										.map(Identifier::getOffset)
										.orElse(0)), 4) + "r ";
		offset += 3;
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
	public void updateCode() {}
}

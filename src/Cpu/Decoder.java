package cpu;

import Structures.*;
import memory.*;

public class Decoder {
	RegisterFile regFile;
	private int number = 1;

	public Decoder(RegisterFile regFile) {
		super();
		this.regFile = regFile;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	protected DecoderResponse decode(int instruction) {
		System.out.println("Decoding instruction Number : " + (number));
		DecoderResponse response = identifyOperation(instruction);
		response.setInstructionnumber(number);
		if (response.getInstructionType().equals(InstructionType.iType)) {
			response = setOperandsIType(instruction, response);
		} else if (response.getInstructionType().equals(InstructionType.rType)) {
			response = setOperandsRType(instruction, response);
		} else {
			response = setOperandsJType(instruction, response);
		}
		number = number + 1;
		return response;

	}

	private DecoderResponse identifyOperation(int instruction) {// sets el response.type w response.operation enums
		int opcodetmp = instruction & 0b11110000000000000000000000000000;
		int opcode = opcodetmp >> 28;
		// System.out.println(opcode);
		if (opcode == -8) {
			opcode = 8;

		} else if (opcode == -7) {
			opcode = 9;

		} else if (opcode == -6) {
			opcode = 10;
		} else if (opcode == -5) {
			opcode = 11;
		}

		// System.out.println(opcode);
		DecoderResponse response = new DecoderResponse();
		switch (opcode) {
		case 0:
			response.setInstructionType(InstructionType.rType);
			response.setOperation(Operation.ADD);
			break;
		case 1:
			response.setInstructionType(InstructionType.rType);
			response.setOperation(Operation.SUB);
			break;

		case 2:
			response.setInstructionType(InstructionType.iType);
			response.setOperation(Operation.MULI);
			break;

		case 3:
			response.setInstructionType(InstructionType.iType);
			response.setOperation(Operation.ADDI);
			break;

		case 4:
			response.setInstructionType(InstructionType.iType);
			response.setOperation(Operation.BNE);
			response.setDoexecute(false);
			break;
		case 5:
			response.setInstructionType(InstructionType.iType);
			response.setOperation(Operation.ANDI);
			break;
		case 6:
			response.setInstructionType(InstructionType.iType);
			response.setOperation(Operation.ORI);
			break;
		case 7:
			response.setInstructionType(InstructionType.jType);
			//System.out.println("jump");
			response.setOperation(Operation.J);
			response.setDoexecute(false);

			break;
		case 8:
			response.setInstructionType(InstructionType.rType);
			response.setOperation(Operation.SLL);
			break;

		case 9:
			response.setInstructionType(InstructionType.rType);
			response.setOperation(Operation.SRL);
			break;

		case 10:
			response.setInstructionType(InstructionType.iType);
			response.setOperation(Operation.LW);
			//System.out.println("LW");

			break;
		case 11:
			response.setInstructionType(InstructionType.iType);
			response.setOperation(Operation.SW);
			break;

		default:
			System.out.println("The Given Opcode is wrong " + opcode);
			break;

		}
		return response;

	}

	private DecoderResponse setOperandsIType(int instruction, DecoderResponse response) {
		int destTemp = instruction & 0b00001111100000000000000000000000;
		int destRegnum = destTemp >> 23;
		int destRegValue = regFile.getValueFromReg(destRegnum);

		int source1Temp = instruction & 0b00000000011111000000000000000000;
		int source1num = source1Temp >> 18;
		int source1Value = regFile.getValueFromReg(source1num);

		int immValuetemp = instruction & 0b00000000000000111111111111111111;

		int significantbit = ((immValuetemp & (1 << (18 - 1))) >> (18 - 1));

		if (significantbit == 1) {
			int immValue = immValuetemp ^ 0b111111111111111111;

//			System.out.println(Integer.toBinaryString(immValue));
			immValue = immValue + 1;
//			System.out.println(Integer.toBinaryString(immValue));
			immValue = immValue * -1;
			response.setDestinationRegisterNumber(destRegnum);
			response.setDestinationRegisterValue(destRegValue);
			response.setFirstSourceValue(source1Value);
			response.setSecondSourceValue(immValue);
			System.out.println("The Instruction Type of " + number + " is " + response.getInstructionType());
			System.out.println("The Instruction Operation of " + number + " is " + response.getOperation());
			System.out.println("The Instruction First register number of " + number + " is " + source1num);
			System.out.println(
					"The Instruction First register value of " + number + " is " + response.getFirstSourceValue());
			System.out
					.println("The Instruction Immediate value of " + number + " is " + response.getSecondSourceValue());
			System.out.println("The Instruction Destination register number of " + number + " is "
					+ response.getDestinationRegisterNumber());
			System.out.println("The Instruction Destination register value of " + number + " is "
					+ response.getDestinationRegisterValue());
			System.out.println();
		} else {
			response.setDestinationRegisterNumber(destRegnum);
			response.setDestinationRegisterValue(destRegValue);
			response.setFirstSourceValue(source1Value);
			response.setSecondSourceValue(immValuetemp);
			System.out.println("The Instruction Type of " + number + " is " + response.getInstructionType());
			System.out.println("The Instruction Operation of " + number + " is " + response.getOperation());
			System.out.println("The Instruction First register number of " + number + " is " + source1num);
			System.out.println(
					"The Instruction First register value of " + number + " is " + response.getFirstSourceValue());
			System.out
					.println("The Instruction Immediate value of " + number + " is " + response.getSecondSourceValue());
			System.out.println("The Instruction Destination register number of " + number + " is "
					+ response.getDestinationRegisterNumber());
			System.out.println("The Instruction Destination register value of " + number + " is "
					+ response.getDestinationRegisterValue());
			System.out.println();

		}

		return response;

	}

	private DecoderResponse setOperandsRType(int instruction, DecoderResponse response) {
		int destTemp = instruction & 0b00001111100000000000000000000000;
		int destRegnum = destTemp >> 23;

		int source1Temp = instruction & 0b00000000011111000000000000000000;
		int source1num = source1Temp >> 18;
		int source1Value = regFile.getValueFromReg(source1num);

		int source2Temp = instruction & 0b00000000000000111110000000000000;
		int source2num = source2Temp >> 13;
		int source2Value = regFile.getValueFromReg(source2num);

		int shamtValue = instruction & 0b00000000000000000001111111111111;

		response.setDestinationRegisterNumber(destRegnum);
		response.setDestinationRegisterValue(regFile.getValueFromReg(destRegnum));
		response.setFirstSourceValue(source1Value);
		response.setSecondSourceValue(source2Value);
		response.setShamt(shamtValue);

		System.out.println("The Instruction Type of " + number + " is " + response.getInstructionType());
		System.out.println("The Instruction Operation of " + number + " is " + response.getOperation());
		System.out.println("The Instruction First register number of " + number + " is " + source1num);
		System.out
				.println("The Instruction First register value of " + number + " is " + response.getFirstSourceValue());
		System.out.println("The Instruction Second register number of " + number + " is " + source2num);

		System.out.println(
				"The Instruction Second register value of " + number + " is " + response.getSecondSourceValue());
		System.out.println("The Instruction Destination register number of " + number + " is "
				+ response.getDestinationRegisterNumber());
		System.out.println("The Instruction Destination register value of " + number + " is "
				+ response.getDestinationRegisterValue());
		System.out.println("The Instruction Shift value of " + number + " is " + response.getShamt());
System.out.println();
		return response;

	}

	private DecoderResponse setOperandsJType(int instruction, DecoderResponse response) {
		int address = instruction & 0b00001111111111111111111111111111;
		response.setAddress(address);
		System.out.println("The Instruction Type of " + number + " is " + response.getInstructionType());
		System.out.println("The Instruction Operation of " + number + " is " + response.getOperation());
		System.out.println("The Instruction Address value of " + number + " is " + response.getAddress());
		System.out.println();
		return response;

	}

	public static void main(String[] args) {
//		

		int instruction = 1085014015;
		int immValuetemp = instruction & 0b00000000000000111111111111111111;
		System.out.println(immValuetemp);

		int significantbit = ((immValuetemp & (1 << (18 - 1))) >> (18 - 1));
		//System.out.println(significantbit);
		if (significantbit == 1) {
			int immValue = immValuetemp ^ 0b111111111111111111;

			System.out.println(Integer.toBinaryString(immValue));
			immValue = immValue + 1;
			System.out.println(Integer.toBinaryString(immValue));
			immValue = immValue * -1;
			System.out.println(immValue);
		}
	}
}
package cpu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Structures.InstructionType;
import Structures.ParserResponse;
import memory.Memory;

public class Encoder {

	protected ParserResponse identifyOperation(ArrayList<String> Instruction) {
		int returned = 0;
		ParserResponse response = new ParserResponse();
		String operation = Instruction.get(0);
		switch (operation) {
		case "ADD":
			// do nothing bec opcode is 0000
			response.setInstructionType(InstructionType.rType);
			break;
		case "SUB":
			returned = returned | (1 << 28);
			response.setInstruction(returned);
			response.setInstructionType(InstructionType.rType);

			break;
		case "MULI":
			returned = returned | (1 << 29);
			response.setInstruction(returned);
			response.setInstructionType(InstructionType.iType);

			break;
		case "ADDI":
			returned = returned | (1 << 28);
			returned = returned | (1 << 29);
			// System.out.println(returned);
			response.setInstruction(returned);
			response.setInstructionType(InstructionType.iType);

			break;
		case "BNE":
			returned = returned | (1 << 30);
			response.setInstruction(returned);
			response.setInstructionType(InstructionType.iType);

			break;
		case "ANDI":
			returned = returned | (1 << 30);
			returned = returned | (1 << 28);
			response.setInstruction(returned);
			response.setInstructionType(InstructionType.iType);

			break;
		case "ORI":
			returned = returned | (1 << 30);
			returned = returned | (1 << 29);
			response.setInstruction(returned);
			response.setInstructionType(InstructionType.iType);

			break;
		case "J":
			returned = returned | (1 << 30);
			returned = returned | (1 << 29);
			returned = returned | (1 << 28);
			response.setInstruction(returned);
			response.setInstructionType(InstructionType.jType);

			break;

		case "SLL":
			returned = returned | (1 << 31);
			response.setInstruction(returned);
			response.setInstructionType(InstructionType.rType);

			break;
		case "SRL":
			returned = returned | (1 << 31);
			returned = returned | (1 << 28);

			response.setInstruction(returned);
			response.setInstructionType(InstructionType.rType);

			break;
		case "LW":
			returned = returned | (1 << 31);
			returned = returned | (1 << 29);

			response.setInstruction(returned);
			response.setInstructionType(InstructionType.iType);

			break;
		case "SW":
			returned = returned | (1 << 31);
			returned = returned | (1 << 29);
			returned = returned | (1 << 28);

			response.setInstruction(returned);
			response.setInstructionType(InstructionType.iType);

			break;

		default:
			System.out.println("The Given word  is wrong " + operation);
			break;

		}
		return response;

	}

	protected int setRType(ArrayList<String> Instruction, ParserResponse response) {
		String firstRegister = Instruction.get(1);
		String secondRegister = Instruction.get(2);
		String thirdRegister = Instruction.get(3);
		// System.out.println(firstRegister);
		// System.out.println(secondRegister);

		// System.out.println(thirdRegister);

		int returned1 = response.getInstruction();
		// System.out.println(returned1);
		boolean isShift = false;
		if (returned1 == 0b10000000000000000000000000000000 || returned1 == 0b10010000000000000000000000000000) {
			isShift = true;
		}

		response = setR1(response, firstRegister, returned1);
		int returned2 = response.getInstruction();
		response = setR2(response, secondRegister, returned2);
		int returned3 = response.getInstruction();
//		System.out.println(returned3);
		if (isShift == false) {
			response = setR3(response, thirdRegister, returned3);
		} else {
			System.out.println("it's a shift operation no need to set R3 , it's all 0's");

			System.out.println();
		}

		int returned4 = response.getInstruction();

		if (Instruction.get(0).equals("SLL") || Instruction.get(0).equals("SRL")) {
			int num = Integer.parseInt(Instruction.get(3));
			for (int i = 13; i > 0; i--) {
				int f = 5;
				f = ((num & (1 << (i - 1))) >> (i - 1));
				if (f == 1) {
					returned4 = returned4 | (1 << (i - 1));
					response.setInstruction(returned4);

				}

			}
			// int shamt = binaryToDecimal(num);

			// instruction = instruction & 0b00000000000000000001111111111111;
		}
		int instruction = response.getInstruction();
		// System.out.println(instruction);
		return instruction;

	}

	private ParserResponse setR1(ParserResponse response, String firstRegister, int returned) {
		// System.out.println(firstRegister);
		switch (firstRegister) {
		case "R0":
			response.setInstruction(returned);
			break;
		case "R1":
			returned = returned | (1 << 23);
			response.setInstruction(returned);
			break;
		case "R2":
			returned = returned | (1 << 24);
			response.setInstruction(returned);
			break;
		case "R3":
			returned = returned | (1 << 23);
			returned = returned | (1 << 24);
			response.setInstruction(returned);
			break;
		case "R4":
			returned = returned | (1 << 25);
			response.setInstruction(returned);
			break;
		case "R5":
			returned = returned | (1 << 25);
			returned = returned | (1 << 23);
			response.setInstruction(returned);
			break;
		case "R6":
			returned = returned | (1 << 24);
			returned = returned | (1 << 25);
			response.setInstruction(returned);
			break;
		case "R7":
			returned = returned | (1 << 23);
			returned = returned | (1 << 24);
			returned = returned | (1 << 25);
			response.setInstruction(returned);
			break;
		case "R8":
			returned = returned | (1 << 26);
			response.setInstruction(returned);
			break;
		case "R9":
			returned = returned | (1 << 23);
			returned = returned | (1 << 26);
			response.setInstruction(returned);
			break;
		case "R10":
			returned = returned | (1 << 24);
			returned = returned | (1 << 26);
			response.setInstruction(returned);
			break;
		case "R11":
			returned = returned | (1 << 23);
			returned = returned | (1 << 24);
			returned = returned | (1 << 26);
			response.setInstruction(returned);
			break;
		case "R12":
			returned = returned | (1 << 25);
			returned = returned | (1 << 26);
			response.setInstruction(returned);
			break;
		case "R13":
			returned = returned | (1 << 23);
			returned = returned | (1 << 25);
			returned = returned | (1 << 26);
			response.setInstruction(returned);
			break;
		case "R14":
			returned = returned | (1 << 24);
			returned = returned | (1 << 25);
			returned = returned | (1 << 26);
			response.setInstruction(returned);
			break;
		case "R15":
			returned = returned | (1 << 23);
			returned = returned | (1 << 24);
			returned = returned | (1 << 25);
			returned = returned | (1 << 26);
			response.setInstruction(returned);
			break;
		case "R16":
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R17":
			returned = returned | (1 << 23);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R18":
			returned = returned | (1 << 24);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R19":
			returned = returned | (1 << 23);
			returned = returned | (1 << 24);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R20":
			returned = returned | (1 << 25);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R21":
			returned = returned | (1 << 23);
			returned = returned | (1 << 25);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R22":
			returned = returned | (1 << 24);
			returned = returned | (1 << 25);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R23":
			returned = returned | (1 << 23);
			returned = returned | (1 << 24);
			returned = returned | (1 << 25);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R24":
			returned = returned | (1 << 26);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R25":
			returned = returned | (1 << 23);
			returned = returned | (1 << 26);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R26":
			returned = returned | (1 << 24);
			returned = returned | (1 << 26);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R27":
			returned = returned | (1 << 23);
			returned = returned | (1 << 24);
			returned = returned | (1 << 26);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R28":
			returned = returned | (1 << 25);
			returned = returned | (1 << 26);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R29":
			returned = returned | (1 << 23);
			returned = returned | (1 << 25);
			returned = returned | (1 << 26);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R30":
			returned = returned | (1 << 24);
			returned = returned | (1 << 25);
			returned = returned | (1 << 26);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		case "R31":
			returned = returned | (1 << 23);
			returned = returned | (1 << 24);
			returned = returned | (1 << 25);
			returned = returned | (1 << 26);
			returned = returned | (1 << 27);
			response.setInstruction(returned);
			break;
		default:
			System.out.println("The Given firstRegisterWord is wrong " + firstRegister);
			break;
		}
		return response;
	}

	private ParserResponse setR2(ParserResponse response, String firstRegister, int returned) {
		switch (firstRegister) {
		case "R0":
			response.setInstruction(returned);
			break;
		case "R1":
			returned = returned | (1 << 18);
			response.setInstruction(returned);
			break;
		case "R2":
			returned = returned | (1 << 19);
			response.setInstruction(returned);
			break;
		case "R3":
			returned = returned | (1 << 18);
			returned = returned | (1 << 19);
			response.setInstruction(returned);
			break;
		case "R4":
			returned = returned | (1 << 20);
			response.setInstruction(returned);
			break;
		case "R5":
			returned = returned | (1 << 20);
			returned = returned | (1 << 18);
			response.setInstruction(returned);
			break;
		case "R6":
			returned = returned | (1 << 19);
			returned = returned | (1 << 20);
			response.setInstruction(returned);
			break;
		case "R7":
			returned = returned | (1 << 18);
			returned = returned | (1 << 19);
			returned = returned | (1 << 20);
			response.setInstruction(returned);
			break;
		case "R8":
			returned = returned | (1 << 26);
			response.setInstruction(returned);
			break;
		case "R9":
			returned = returned | (1 << 18);
			returned = returned | (1 << 21);
			response.setInstruction(returned);
			break;
		case "R10":
			returned = returned | (1 << 19);
			returned = returned | (1 << 21);
			response.setInstruction(returned);
			break;
		case "R11":
			returned = returned | (1 << 18);
			returned = returned | (1 << 19);
			returned = returned | (1 << 21);
			response.setInstruction(returned);
			break;
		case "R12":
			returned = returned | (1 << 20);
			returned = returned | (1 << 21);
			response.setInstruction(returned);
			break;
		case "R13":
			returned = returned | (1 << 18);
			returned = returned | (1 << 20);
			returned = returned | (1 << 21);
			response.setInstruction(returned);
			break;
		case "R14":
			returned = returned | (1 << 19);
			returned = returned | (1 << 20);
			returned = returned | (1 << 21);
			response.setInstruction(returned);
			break;
		case "R15":
			returned = returned | (1 << 18);
			returned = returned | (1 << 19);
			returned = returned | (1 << 20);
			returned = returned | (1 << 21);
			response.setInstruction(returned);
			break;
		case "R16":
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R17":
			returned = returned | (1 << 18);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R18":
			returned = returned | (1 << 19);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R19":
			returned = returned | (1 << 18);
			returned = returned | (1 << 19);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R20":
			returned = returned | (1 << 20);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R21":
			returned = returned | (1 << 18);
			returned = returned | (1 << 20);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R22":
			returned = returned | (1 << 19);
			returned = returned | (1 << 20);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R23":
			returned = returned | (1 << 18);
			returned = returned | (1 << 19);
			returned = returned | (1 << 20);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R24":
			returned = returned | (1 << 21);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R25":
			returned = returned | (1 << 18);
			returned = returned | (1 << 21);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R26":
			returned = returned | (1 << 19);
			returned = returned | (1 << 21);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R27":
			returned = returned | (1 << 18);
			returned = returned | (1 << 19);
			returned = returned | (1 << 21);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R28":
			returned = returned | (1 << 20);
			returned = returned | (1 << 21);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R29":
			returned = returned | (1 << 19);
			returned = returned | (1 << 20);
			returned = returned | (1 << 21);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R30":
			returned = returned | (1 << 19);
			returned = returned | (1 << 20);
			returned = returned | (1 << 21);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		case "R31":
			returned = returned | (1 << 18);
			returned = returned | (1 << 19);
			returned = returned | (1 << 20);
			returned = returned | (1 << 21);
			returned = returned | (1 << 22);
			response.setInstruction(returned);
			break;
		default:
			System.out.println("The Given firstRegisterWord is wrong " + firstRegister);
			break;
		}
		return response;
	}

	private ParserResponse setR3(ParserResponse response, String firstRegister, int returned) {

		switch (firstRegister) {
		case "R0":
			response.setInstruction(returned);
			break;
		case "R1":
			returned = returned | (1 << 13);
			response.setInstruction(returned);
			break;
		case "R2":
			returned = returned | (1 << 14);
			response.setInstruction(returned);
			break;
		case "R3":
			returned = returned | (1 << 13);
			returned = returned | (1 << 14);
			response.setInstruction(returned);
			break;
		case "R4":
			returned = returned | (1 << 15);
			response.setInstruction(returned);
			break;
		case "R5":
			returned = returned | (1 << 15);
			returned = returned | (1 << 13);
			response.setInstruction(returned);
			break;
		case "R6":
			returned = returned | (1 << 14);
			returned = returned | (1 << 15);
			response.setInstruction(returned);
			break;
		case "R7":
			returned = returned | (1 << 13);
			returned = returned | (1 << 14);
			returned = returned | (1 << 15);
			response.setInstruction(returned);
			break;
		case "R8":
			returned = returned | (1 << 16);
			response.setInstruction(returned);
			break;
		case "R9":
			returned = returned | (1 << 13);
			returned = returned | (1 << 16);
			response.setInstruction(returned);
			break;
		case "R10":
			returned = returned | (1 << 14);
			returned = returned | (1 << 16);
			response.setInstruction(returned);
			break;
		case "R11":
			returned = returned | (1 << 13);
			returned = returned | (1 << 14);
			returned = returned | (1 << 16);
			response.setInstruction(returned);
			break;
		case "R12":
			returned = returned | (1 << 15);
			returned = returned | (1 << 16);
			response.setInstruction(returned);
			break;
		case "R13":
			returned = returned | (1 << 13);
			returned = returned | (1 << 15);
			returned = returned | (1 << 16);
			response.setInstruction(returned);
			break;
		case "R14":
			returned = returned | (1 << 14);
			returned = returned | (1 << 15);
			returned = returned | (1 << 16);
			response.setInstruction(returned);
			break;
		case "R15":
			returned = returned | (1 << 13);
			returned = returned | (1 << 14);
			returned = returned | (1 << 15);
			returned = returned | (1 << 16);
			response.setInstruction(returned);
			break;
		case "R16":
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R17":
			returned = returned | (1 << 13);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R18":
			returned = returned | (1 << 14);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R19":
			returned = returned | (1 << 13);
			returned = returned | (1 << 14);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R20":
			returned = returned | (1 << 15);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R21":
			returned = returned | (1 << 13);
			returned = returned | (1 << 15);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R22":
			returned = returned | (1 << 14);
			returned = returned | (1 << 15);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R23":
			returned = returned | (1 << 13);
			returned = returned | (1 << 14);
			returned = returned | (1 << 15);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R24":
			returned = returned | (1 << 16);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R25":
			returned = returned | (1 << 13);
			returned = returned | (1 << 16);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R26":
			returned = returned | (1 << 14);
			returned = returned | (1 << 16);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R27":
			returned = returned | (1 << 13);
			returned = returned | (1 << 14);
			returned = returned | (1 << 16);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R28":
			returned = returned | (1 << 15);
			returned = returned | (1 << 16);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R29":
			returned = returned | (1 << 13);
			returned = returned | (1 << 15);
			returned = returned | (1 << 16);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R30":
			returned = returned | (1 << 14);
			returned = returned | (1 << 15);
			returned = returned | (1 << 16);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		case "R31":
			returned = returned | (1 << 13);
			returned = returned | (1 << 14);
			returned = returned | (1 << 15);
			returned = returned | (1 << 16);
			returned = returned | (1 << 17);
			response.setInstruction(returned);
			break;
		default:
			System.out.println("The Given firstRegisterWord is wrong " + firstRegister);
			break;
		}
		return response;
	}

	protected int setIType(ArrayList<String> Instruction, ParserResponse response) {

		String firstRegister = Instruction.get(1);
		String secondRegister = Instruction.get(2);
		int returned1 = response.getInstruction();
		response = setR1(response, firstRegister, returned1);
		// .out.println(response.getInstruction());
		int returned2 = response.getInstruction();
		response = setR2(response, secondRegister, returned2);
		// System.out.println(response.getInstruction());
		int returned3 = response.getInstruction();
		int immNum = Integer.parseInt(Instruction.get(3));
		for (int i = 18; i > 0; i--) {
			int f = 5;// dummy value
			f = ((immNum & (1 << (i - 1))) >> (i - 1));
			if (f == 1) {
				returned3 = returned3 | (1 << (i - 1));
				response.setInstruction(returned3);

			}

		}
		int instruction = response.getInstruction();
//		System.out.println(instruction);
		// instruction = instruction & 0b00000000000000111111111111111111;
		return instruction;

	}

	protected int setJType(ArrayList<String> Instruction, ParserResponse response) {
		int addressnum = Integer.parseInt(Instruction.get(1));
		int returned = response.getInstruction();

		// int address = binaryToDecimal(addressnum);
		for (int i = 28; i > 0; i--) {
			int f = 5;// dummy value
			f = ((addressnum & (1 << (i - 1))) >> (i - 1));
			if (f == 1) {
				returned = returned | (1 << (i - 1));
				response.setInstruction(returned);

			}

		}
		int instruction = response.getInstruction();
		// instruction = instruction & 0b00001111111111111111111111111111;
		return instruction;
	}

	protected ArrayList<String> readAssemblyIntoList(String string) {
		String[] r = string.split("\\s+");
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < r.length; i++) {
			result.add(r[i]);
		}
		return result;
	}

	public int parseInstruction(String instruction) {
		ArrayList<String> result = readAssemblyIntoList(instruction);
		ParserResponse response = identifyOperation(result);
		if (response.getInstructionType().equals(InstructionType.iType)) {

			return setIType(result, response);
		} else {
			if (response.getInstructionType().equals(InstructionType.rType)) {
				return setRType(result, response);
			} else {

				return setJType(result, response);

			}
		}

	}

	private static int binaryToDecimal(int n) {
		int num = n;
		int dec_value = 0;
		int base = 1;

		int temp = num;
		while (temp > 0) {
			int last_digit = temp % 10;
			temp = temp / 10;

			dec_value += last_digit * base;

			base = base * 2;
		}

		return dec_value;
	}

	protected void readFileIntoList(String fileName, ArrayList<String> list) {
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(fileName);

			br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void loadinstructionsinMemory(Memory mem, String fileName) {
		ArrayList<String> list = new ArrayList<String>();
		readFileIntoList(fileName, list);
		for (int i = 0; i < list.size(); i++) {
			int x = parseInstruction(list.get(i));
			mem.setInstruction(i, x);
			// System.out.println(mem.getInstruction(i));
		}

	}

	public static void main(String[] args) {
//		String x = "-5";
//		int y= Integer.parseInt(x);
//		System.out.println(y);
//		int y = -1321039047;
//		int opcode = 0;
//		int opcodetmp = y & 0b11110000000000000000000000000000;
//		opcode = opcodetmp >> 28;
//		System.out.println(opcode);
//		String z = Integer.toBinaryString(y);
//		System.out.println(z);
		// int dfchdfh = Integer.parseInt(z);
		// System.out.println(dfchdfh);

		// int z = binaryToDecimal(y);
		// System.out.println(z);

	}

}

//8:24 AM  :) 

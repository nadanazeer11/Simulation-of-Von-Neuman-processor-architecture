package memory;

import java.util.ArrayList;

import Structures.Register;

public class RegisterFile {

	private ArrayList<Register> regList;

	private void fillRegisters() {
		for (int i = 0; i < 32; i++) {
			Register reg = new Register(0, (String) ("R" + i));
			regList.add(reg);
		}

	}

	public void printRegFile() {
		for (int i = 0; i < regList.size(); i++) {
			if (i==32) {
				System.out.println("Register name :" + "PC" + " Register Data : " + regList.get(i).getData());
			}
			else {
							System.out.println("Register name :" + i + " Register Data : " + regList.get(i).getData());

			}
		}
	}

	public RegisterFile() {
		this.regList = new ArrayList<Register>();
		fillRegisters();
		regList.add(new Register(0, "PC"));
	}

	public void setValueinRegister(int regNumber, int regValue) {
		if (regNumber == 0) {
			System.out.println("can't edit zero register ,check ur numbers");

		} else {
			Register reg = regList.get(regNumber);
			reg.setData(regValue);
		}

	}
	public int readPc() {
	return regList.get(32).getData();
		

	}

	public int getValueFromReg(int regNumber) {
		if (regNumber == 32) {
			int temp = regList.get(regNumber).getData();

			setValueinRegister(32, (temp + 1));
			return temp;
		} else {
			return regList.get(regNumber).getData();
		}

	}

}

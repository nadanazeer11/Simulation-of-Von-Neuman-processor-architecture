package cpu;

import memory.Memory;
//law jump aw branch el pc mabyedzawedsh
import memory.RegisterFile;

public class Fetcher {
	private Memory mem;
	private RegisterFile registerFile;
	private int pc = 0;
	private int number = 1;
	
	public RegisterFile getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getPc() {
		return pc;
	}

	public int fetch() {
		pc = registerFile.getValueFromReg(32);
		System.out.println("Fetching instruction Number : " + (number++) + " Pc equal " + pc + " of intruction "
				+ mem.getInstruction(pc));
		System.out.println();

		return mem.getInstruction(pc);

	}

	protected Fetcher(Memory mem, RegisterFile registerFile) {
		this.mem = mem;
		this.registerFile = registerFile;
	}

	public Memory getMem() {
		return mem;
	}

	public void setMem(Memory mem) {
		this.mem = mem;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public boolean thereIsNextInstruction() {
		return mem.getInstruction(registerFile.readPc()) != 0;
	}

	/////// its your duty to maintain mem integrity

}

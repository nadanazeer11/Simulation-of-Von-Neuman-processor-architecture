package Structures;

public class ExecuterResponse {
	private int destReg;
	private int result;
	private Operation operation;
	int newPc = -99;
	boolean doWriteBack = true;
	boolean doMemory = true;
	boolean JumpOrBranch = false;
	private int instrunumber;

	public int getInstrunumber() {
		return instrunumber;
	}

	public void setInstrunumber(int instrunumber) {
		this.instrunumber = instrunumber;
	}

	public boolean isJumpOrBranch() {
		return JumpOrBranch;
	}

	public void setJumpOrBranch(boolean jumpOrBranch) {
		JumpOrBranch = jumpOrBranch;
	}

	public boolean isDoMemory() {
		return doMemory;
	}

	public void setDoMemory(boolean doMemory) {
		this.doMemory = doMemory;
	}

	public ExecuterResponse(int destReg, int result, Operation operation) {
		super();
		this.destReg = destReg;
		this.result = result;
		this.operation = operation;
	}

	public boolean isDoWriteBack() {
		return doWriteBack;
	}

	public void setDoWriteBack(boolean doWriteBack) {
		this.doWriteBack = doWriteBack;
	}

	public int getDestReg() {
		return destReg;
	}

	public int getNewPc() {
		return newPc;
	}

	public void setNewPc(int newPc) {
		this.newPc = newPc;
	}

	public void setDestReg(int destReg) {
		this.destReg = destReg;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

}

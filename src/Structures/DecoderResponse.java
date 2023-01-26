package Structures;

public class DecoderResponse {
	private InstructionType instructionType;
	private int firstSourceValue;// 5
	private int secondSourceValue;// 7
	private int destinationRegisterNumber;// r5
	private int destinationRegisterValue;
	private Operation operation;
	private int instructionnumber;

	public int getInstructionnumber() {
		return instructionnumber;
	}

	public void setInstructionnumber(int instructionnumber) {
		this.instructionnumber = instructionnumber;
	}

	// private boolean passByMem=false; //ha3ady 3al mem wala la true in cases of
	// (store w load)
	// private boolean passBywb=true; //ha3ady 3al wb wala la true in all cases
	// except (store)
	private int shamt;
	private int address;
	private boolean doexecute = true;
	// private boolean isSw = false;

	public boolean isDoexecute() {
		return doexecute;
	}

	public void setDoexecute(boolean doexecute) {
		this.doexecute = doexecute;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public int getShamt() {
		return shamt;
	}

	public int getDestinationRegisterValue() {
		return destinationRegisterValue;
	}

	public void setDestinationRegisterValue(int destinationRegisterValue) {
		this.destinationRegisterValue = destinationRegisterValue;
	}

	public void setShamt(int shamt) {
		this.shamt = shamt;
	}

	public DecoderResponse() {
		super();

	}

	public InstructionType getInstructionType() {
		return instructionType;
	}

	public void setInstructionType(InstructionType instructionType) {
		this.instructionType = instructionType;
	}

	public int getFirstSourceValue() {
		return firstSourceValue;
	}

	public void setFirstSourceValue(int firstSourceValue) {
		this.firstSourceValue = firstSourceValue;
	}

	public int getSecondSourceValue() {
		return secondSourceValue;
	}

	public void setSecondSourceValue(int secondSourceValue) {
		this.secondSourceValue = secondSourceValue;
	}

	public int getDestinationRegisterNumber() {
		return destinationRegisterNumber;
	}

	public void setDestinationRegisterNumber(int destinationRegisterNumber) {
		this.destinationRegisterNumber = destinationRegisterNumber;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

}

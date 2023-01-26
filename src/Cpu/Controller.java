package cpu;

import java.util.ArrayList;

import Structures.DecoderResponse;
import Structures.ExecuterResponse;
import Structures.MemoryHandlerResponse;
import Structures.Operation;
import memory.Memory;
import memory.RegisterFile;

public class Controller {
	Fetcher fetcher;
	Decoder decoder;
	Executer executer;
	Memory mem;
	MemoryHandler memHandler;
	Writeback writeback;
	RegisterFile regFile;
	Encoder encoder;
	int fetched;
	DecoderResponse decoderResponse;
	ExecuterResponse executerResponse;
	MemoryHandlerResponse memResponse;
	int currentPc = 0;
	int jumpPc = -1;
	boolean indifferent = false;

	static int cycles = 1;

	private Controller() {
		super();
		this.mem = new Memory();
		this.regFile = new RegisterFile();
		this.fetcher = new Fetcher(mem, regFile);
		this.decoder = new Decoder(regFile);
		this.executer = new Executer();
		this.memHandler = new MemoryHandler(mem);
		this.writeback = new Writeback(regFile);
		this.encoder = new Encoder();

	}

	private void decodingPrint(int no) {
		System.out.println("Still Decoding instruction " + no);
	}

	private void executingPrint(int no) {
		System.out.println("Still Executing instruction " + no);
	}

	private void initializePipeline() {

		if (memResponse == null) {
			System.out.println("Cycle number : " + cycles);
			int firstInstruction = fetcher.fetch();
			currentPc = fetcher.getRegisterFile().readPc();

			System.out.println("----------------------------------------");
			// First cycle done
			cycles++;
			System.out.println("Cycle number : " + cycles);

			DecoderResponse decoderfirstResponse = decoder.decode(firstInstruction);
			if (decoderfirstResponse.getOperation().equals(Operation.BNE)
					|| decoderfirstResponse.getOperation().equals(Operation.J)) {

				jumpPc = fetcher.getRegisterFile().readPc();
			}
			System.out.println("----------------------------------------");
			// second cycle done
			cycles++;
			System.out.println("Cycle number : " + cycles);

			// u are still decoding
//			System.out.println("Still Decoding");

			int secondInstruction = fetcher.fetch();
			currentPc = fetcher.getRegisterFile().readPc();
			decodingPrint(decoderfirstResponse.getInstructionnumber());
			System.out.println("----------------------------------------");

			// third cycle done
			cycles++;
			System.out.println("Cycle number : " + cycles);

			DecoderResponse decoderSecondResponse = decoder.decode(secondInstruction);
			if (decoderSecondResponse.getOperation().equals(Operation.BNE)
					|| decoderSecondResponse.getOperation().equals(Operation.J)) {

				jumpPc = fetcher.getRegisterFile().readPc();
			}

			ExecuterResponse executerFirstResponse = executer.execute(decoderfirstResponse, jumpPc);

			currentPc = fetcher.getRegisterFile().readPc();
			System.out.println("----------------------------------------");
			// fourth cycle done
			cycles++;
			System.out.println("Cycle number : " + cycles);

//			System.out.println("Still Decoding");
//			System.out.println("Still Executing");

			int thirdInstruction = fetcher.fetch();
			if (executerFirstResponse.getNewPc() != -99) {
				// System.out.println("ana dakhalt");
				fetcher.getRegisterFile().setValueinRegister(32, executerFirstResponse.getNewPc());
			}
			currentPc = fetcher.getRegisterFile().readPc();

			decodingPrint(decoderSecondResponse.getInstructionnumber());
			executingPrint(executerFirstResponse.getInstrunumber());

			System.out.println("----------------------------------------");
			// fifth cycle done
			cycles++;
			System.out.println("Cycle number : " + cycles);

			// still decoding 2 and exec 1

			memResponse = memHandler.handle(executerFirstResponse);

			if (executerFirstResponse.isJumpOrBranch() == true) {

				executerFirstResponse.setJumpOrBranch(false);
				executerFirstResponse.setNewPc(-99);
				differentPipelines();
			} else {
				decoderResponse = decoder.decode(thirdInstruction);
				if (decoderResponse.getOperation().equals(Operation.BNE)
						|| decoderResponse.getOperation().equals(Operation.J)) {

					jumpPc = fetcher.getRegisterFile().readPc();
				}

				executerResponse = executer.execute(decoderSecondResponse, jumpPc);

				currentPc = fetcher.getRegisterFile().readPc();

				System.out.println("----------------------------------------");
				// 6th cycle done
				cycles++;
				System.out.println("Cycle number : " + cycles);
//				System.out.println("Still Executing");
//				System.out.println("Still Decoding");

				fetched = fetcher.fetch();
				if (executerResponse.getNewPc() != -99) {
					// System.out.println("ana dakhalt");
					fetcher.getRegisterFile().setValueinRegister(32, executerResponse.getNewPc());

				}
				currentPc = fetcher.getRegisterFile().readPc();
				decodingPrint(decoderResponse.getInstructionnumber());
				executingPrint(executerResponse.getInstrunumber());

				// System.out.println("abl el writeback " + memResponse.isDoWriteBack());
				writeback.writeBack(memResponse);
				System.out.println("----------------------------------------");
				// 7th done
				cycles++;
				System.out.println("Cycle Number :" + cycles);

				runPipeLine();

			}
		} else {
			// System.out.println("ana fel initialize else");
			System.out.println("----------------------------------------");
			cycles++;
			System.out.println("Cycle number : " + cycles);

			writeback.writeBack(memResponse);

			fetcher.setNumber(currentPc + 1);
			writeback.setNumber(currentPc + 1);
			decoder.setNumber(currentPc + 1);
			executer.setNumber(currentPc + 1);
			memHandler.setNumber(currentPc + 1);

			int firstInstruction = fetcher.fetch();
			currentPc = fetcher.getRegisterFile().readPc();

			System.out.println("----------------------------------------");

			cycles++;
			System.out.println("Cycle number : " + cycles);
			// First cycle done
			DecoderResponse decoderfirstResponse = decoder.decode(firstInstruction);
			if (decoderfirstResponse.getOperation().equals(Operation.BNE)
					|| decoderfirstResponse.getOperation().equals(Operation.J)) {

				jumpPc = fetcher.getRegisterFile().readPc();
			}
			System.out.println("----------------------------------------");

			cycles++;
			System.out.println("Cycle number : " + cycles);
			// second cycle done
			// u are still decoding

			int secondInstruction = fetcher.fetch();
			currentPc = fetcher.getRegisterFile().readPc();
			decodingPrint(decoderfirstResponse.getInstructionnumber());
			System.out.println("----------------------------------------");

			cycles++;
			System.out.println("Cycle number : " + cycles);

			// third cycle done
			DecoderResponse decoderSecondResponse = decoder.decode(secondInstruction);
			if (decoderSecondResponse.getOperation().equals(Operation.BNE)
					|| decoderSecondResponse.getOperation().equals(Operation.J)) {

				jumpPc = fetcher.getRegisterFile().readPc();
			}
			ExecuterResponse executerFirstResponse = executer.execute(decoderfirstResponse, jumpPc);

			currentPc = fetcher.getRegisterFile().readPc();

			System.out.println("----------------------------------------");
			cycles++;
			System.out.println("Cycle number : " + cycles);

			// fourth cycle done
			int thirdInstruction = fetcher.fetch();
			decodingPrint(decoderSecondResponse.getInstructionnumber());
			executingPrint(executerFirstResponse.getInstrunumber());

			if (executerFirstResponse.getNewPc() != -99) {
				// System.out.println("ana dakhalt");
				fetcher.getRegisterFile().setValueinRegister(32, executerFirstResponse.getNewPc());

//				fetcher.setNumber(currentPc + 1);
//				writeback.setNumber(currentPc + 1);
//				decoder.setNumber(currentPc + 1);
//				executer.setNumber(currentPc + 1);
//				memHandler.setNumber(currentPc + 1);
				// System.out.println(currentPc);
			}
			currentPc = fetcher.getRegisterFile().readPc();

			System.out.println("----------------------------------------");
			cycles++;
			System.out.println("Cycle number : " + cycles);

			// still decoding 2 and exec 1
			// fifth cycle done
			memResponse = memHandler.handle(executerFirstResponse);

			if (executerFirstResponse.isJumpOrBranch() == true) {

				executerFirstResponse.setJumpOrBranch(false);
				executerFirstResponse.setNewPc(-99);
				differentPipelines();
			} else {
				decoderResponse = decoder.decode(thirdInstruction);
				if (decoderResponse.getOperation().equals(Operation.BNE)
						|| decoderResponse.getOperation().equals(Operation.J)) {

					jumpPc = fetcher.getRegisterFile().readPc();
				}

				executerResponse = executer.execute(decoderSecondResponse, jumpPc);

				currentPc = fetcher.getRegisterFile().readPc();

				System.out.println("----------------------------------------");
				cycles++;
				System.out.println("Cycle number : " + cycles);
				// sixth done
				fetched = fetcher.fetch();
				if (executerResponse.getNewPc() != -99) {
					fetcher.getRegisterFile().setValueinRegister(32, executerResponse.getNewPc());
					;

				}
				currentPc = fetcher.getRegisterFile().readPc();
				decodingPrint(decoderResponse.getInstructionnumber());
				executingPrint(executerResponse.getInstrunumber());
				// System.out.println("abl el writeback " + memResponse.isDoWriteBack());
				writeback.writeBack(memResponse);

				System.out.println("----------------------------------------");

				cycles++;
				System.out.println("Cycle number : " + cycles);

				runPipeLine();

			}
		}

	}

	private void differentPipelines() {
//		System.out.println("ana fel different Pipelines");

		int instructionsleft = instructionsleft(currentPc);
//		System.out.println(instructionsleft);

		if (instructionsleft >= 4) {
			initializePipeline();
		} else {
			if (instructionsleft == 0) {
				System.out.println("There is no more instructions left to execute");
			} else {
				if (instructionsleft == 1) {
					// System.out.println("ana fel instructions = 1");

					if (memResponse == null) {
						System.out.println("----------------------------------------");

						System.out.println("Cycle number : " + cycles);
						// First cycle done
						int firstInstruction = fetcher.fetch();
						currentPc = fetcher.getRegisterFile().readPc();
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);
						// second cycle done

						DecoderResponse decoderfirstResponse = decoder.decode(firstInstruction);
						if (decoderfirstResponse.getOperation().equals(Operation.BNE)
								|| decoderfirstResponse.getOperation().equals(Operation.J)) {

							jumpPc = fetcher.getRegisterFile().readPc();
						}
						System.out.println("----------------------------------------");
						cycles++;
						// third cycle done
						System.out.println("Cycle number : " + cycles);
						decodingPrint(decoderfirstResponse.getInstructionnumber());
						System.out.println("----------------------------------------");
						cycles++;
						// fourth cycle done
						System.out.println("Cycle number : " + cycles);
						ExecuterResponse executerFirstResponse = executer.execute(decoderfirstResponse, jumpPc);
						if (executerFirstResponse.getNewPc() != -99)
							fetcher.getRegisterFile().setValueinRegister(32, executerFirstResponse.getNewPc());
						currentPc = fetcher.getRegisterFile().readPc();

						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);
						// fifth cycle done
						executingPrint(executerFirstResponse.getInstrunumber());
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);
						// sixth cycle done
						memResponse = memHandler.handle(executerFirstResponse);
						if (executerFirstResponse.isJumpOrBranch() == true) {
							executerFirstResponse.setJumpOrBranch(false);
							executerFirstResponse.setNewPc(-99);
							// System.out.println("ana dakhalt we current pc =" + currentPc);
							differentPipelines();

						} else {

							System.out.println("----------------------------------------");

							cycles++;
							System.out.println("Cycle number : " + cycles);
							// seventh done
							writeback.writeBack(memResponse);
							System.out.println("----------------------------------------");
						}

					} else {
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);

						writeback.writeBack(memResponse);

						fetcher.setNumber(currentPc + 1);
						writeback.setNumber(currentPc + 1);
						decoder.setNumber(currentPc + 1);
						executer.setNumber(currentPc + 1);
						memHandler.setNumber(currentPc + 1);

						int firstInstruction = fetcher.fetch();
						currentPc = fetcher.getRegisterFile().readPc();
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);
						// First cycle done
						DecoderResponse decoderfirstResponse = decoder.decode(firstInstruction);
						if (decoderfirstResponse.getOperation().equals(Operation.BNE)
								|| decoderfirstResponse.getOperation().equals(Operation.J)) {

							jumpPc = fetcher.getRegisterFile().readPc();
						}
						currentPc = fetcher.getRegisterFile().readPc();
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);
						decodingPrint(decoderfirstResponse.getInstructionnumber());
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);

						ExecuterResponse executerFirstResponse = executer.execute(decoderfirstResponse, jumpPc);

						if (executerFirstResponse.getNewPc() != -99)
							fetcher.getRegisterFile().setValueinRegister(32, executerFirstResponse.getNewPc());
						currentPc = fetcher.getRegisterFile().readPc();
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);

						executingPrint(executerFirstResponse.getInstrunumber());
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);
						memResponse = memHandler.handle(executerFirstResponse);

						if (executerFirstResponse.isJumpOrBranch() == true) {
							executerFirstResponse.setJumpOrBranch(false);
							executerFirstResponse.setNewPc(-99);

							differentPipelines();

						} else {
							System.out.println("----------------------------------------");
							cycles++;
							System.out.println("Cycle number : " + cycles);
							// sixth done
							writeback.writeBack(memResponse);

						}

					}

				} else if (instructionsleft == 2) {
					if (memResponse == null) {
						System.out.println("----------------------------------------");

						System.out.println("Cycle number : " + cycles);
						int firstInstruction = fetcher.fetch();
						currentPc = fetcher.getRegisterFile().readPc(); // First cycle done
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);

						DecoderResponse decoderfirstResponse = decoder.decode(firstInstruction);
						if (decoderfirstResponse.getOperation().equals(Operation.BNE)
								|| decoderfirstResponse.getOperation().equals(Operation.J)) {

							jumpPc = fetcher.getRegisterFile().readPc();
						}
						currentPc = fetcher.getRegisterFile().readPc(); // second cycle done
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);
						int secondInstruction = fetcher.fetch();
//						
						currentPc = fetcher.getRegisterFile().readPc(); // second cycle done

						decodingPrint(decoderfirstResponse.getInstructionnumber());
						// third cycle done
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);

						DecoderResponse decoderSecondResponse = decoder.decode(secondInstruction);
						if (decoderSecondResponse.getOperation().equals(Operation.BNE)
								|| decoderSecondResponse.getOperation().equals(Operation.J)) {

							jumpPc = fetcher.getRegisterFile().readPc();
						}
						currentPc = fetcher.getRegisterFile().readPc(); // second cycle done
						ExecuterResponse executerFirstResponse = executer.execute(decoderfirstResponse, jumpPc);
						if (executerFirstResponse.getNewPc() != -99)
							fetcher.getRegisterFile().setValueinRegister(32, executerFirstResponse.getNewPc());
						// System.out.println(executerFirstResponse.isJumpOrBranch());
						// fourth cycle done
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);
						decodingPrint(decoderSecondResponse.getInstructionnumber());
						executingPrint(executerFirstResponse.getInstrunumber());
						// fifth cycle done
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						memResponse = memHandler.handle(executerFirstResponse);
						if (executerFirstResponse.isJumpOrBranch() == true) {
							executerFirstResponse.setJumpOrBranch(false);
							executerFirstResponse.setNewPc(-99);
							// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
							differentPipelines();

						} else {
							ExecuterResponse executerSecondResponse = executer.execute(decoderSecondResponse, jumpPc);
							if (executerSecondResponse.getNewPc() != -99)
								fetcher.getRegisterFile().setValueinRegister(32, executerSecondResponse.getNewPc());
							// sixth cycle done
							System.out.println("----------------------------------------");

							cycles++;
							System.out.println("Cycle number : " + cycles);

							executingPrint(executerSecondResponse.getInstrunumber());
							writeback.writeBack(memResponse);
							// seventh cycle done
							System.out.println("----------------------------------------");

							cycles++;
							System.out.println("Cycle number : " + cycles);
							memResponse = memHandler.handle(executerSecondResponse);
							if (executerSecondResponse.isJumpOrBranch() == true) {
								executerSecondResponse.setJumpOrBranch(false);
								executerSecondResponse.setNewPc(-99);
								// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
								differentPipelines();

							} else {
								// eighth cycle done
								System.out.println("----------------------------------------");

								cycles++;
								System.out.println("Cycle number : " + cycles);
								writeback.writeBack(memResponse);
								// ninth cycle done
							}

						}

					} else {
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						writeback.writeBack(memResponse);

						fetcher.setNumber(currentPc + 1);
						writeback.setNumber(currentPc + 1);
						decoder.setNumber(currentPc + 1);
						executer.setNumber(currentPc + 1);
						memHandler.setNumber(currentPc + 1);

						int firstInstruction = fetcher.fetch();
						currentPc = fetcher.getRegisterFile().readPc(); // second cycle done
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);
						// First cycle done
						DecoderResponse decoderfirstResponse = decoder.decode(firstInstruction);
						if (decoderfirstResponse.getOperation().equals(Operation.BNE)
								|| decoderfirstResponse.getOperation().equals(Operation.J)) {

							jumpPc = fetcher.getRegisterFile().readPc(); // second cycle done

						}
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						decodingPrint(decoderfirstResponse.getInstructionnumber());
						int secondInstruction = fetcher.fetch();

						currentPc = fetcher.getRegisterFile().readPc(); // second cycle done
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);

						// third cycle done
						DecoderResponse decoderSecondResponse = decoder.decode(secondInstruction);
						if (decoderSecondResponse.getOperation().equals(Operation.BNE)
								|| decoderSecondResponse.getOperation().equals(Operation.J)) {

							jumpPc =fetcher.getRegisterFile().readPc(); // second cycle done

						}

						ExecuterResponse executerFirstResponse = executer.execute(decoderfirstResponse, jumpPc);
						if (executerFirstResponse.getNewPc() != -99)
							fetcher.getRegisterFile().setValueinRegister(32, executerFirstResponse.getNewPc());
						currentPc = fetcher.getRegisterFile().readPc(); // second cycle done
						System.out.println("----------------------------------------");
						cycles++;
						System.out.println("Cycle number : " + cycles);

						decodingPrint(decoderSecondResponse.getInstructionnumber());
						executingPrint(executerFirstResponse.getInstrunumber());
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						memResponse = memHandler.handle(executerFirstResponse);
						if (executerFirstResponse.isJumpOrBranch() == true) {
							executerFirstResponse.setJumpOrBranch(false);
							executerFirstResponse.setNewPc(-99);
							// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
							differentPipelines();

						} else {
							ExecuterResponse executerSecondResponse = executer.execute(decoderSecondResponse, jumpPc);
							if (executerSecondResponse.getNewPc() != -99)
								fetcher.getRegisterFile().setValueinRegister(32, executerSecondResponse.getNewPc());
							currentPc = fetcher.getRegisterFile().readPc(); // second cycle done
							System.out.println("----------------------------------------");

							cycles++;
							System.out.println("Cycle number : " + cycles);

							executingPrint(executerSecondResponse.getInstrunumber());

							writeback.writeBack(memResponse);
							System.out.println("----------------------------------------");

							cycles++;
							System.out.println("Cycle number : " + cycles);

							memResponse = memHandler.handle(executerSecondResponse);
							if (executerSecondResponse.isJumpOrBranch() == true) {
								executerSecondResponse.setJumpOrBranch(false);
								executerSecondResponse.setNewPc(-99);
								// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
								differentPipelines();

							} else {
								System.out.println("----------------------------------------");

								cycles++;
								System.out.println("Cycle number : " + cycles);
								writeback.writeBack(memResponse);
							}
						}

					}

				} else {

					// System.out.println("ana fel instructions = 3");
					if (memResponse == null) {
						System.out.println("----------------------------------------");

						System.out.println("Cycle number : " + cycles);

						int firstInstruction = fetcher.fetch();
						currentPc = fetcher.getRegisterFile().readPc(); // second cycle done
						// First cycle done
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						DecoderResponse decoderfirstResponse = decoder.decode(firstInstruction);
						if (decoderfirstResponse.getOperation().equals(Operation.BNE)
								|| decoderfirstResponse.getOperation().equals(Operation.J)) {

							jumpPc = fetcher.getRegisterFile().readPc();
						}
						// second cycle done
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);
						int secondInstruction = fetcher.fetch();
						currentPc = fetcher.getRegisterFile().readPc();
						decodingPrint(decoderfirstResponse.getInstructionnumber());
						// third cycle done
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);
						DecoderResponse decoderSecondResponse = decoder.decode(secondInstruction);
						if (decoderSecondResponse.getOperation().equals(Operation.BNE)
								|| decoderSecondResponse.getOperation().equals(Operation.J)) {

							jumpPc = fetcher.getRegisterFile().readPc();
						}
						ExecuterResponse executerFirstResponse = executer.execute(decoderfirstResponse, jumpPc);
						// System.out.println(executerFirstResponse.isJumpOrBranch());
						// fourth cycle done

						currentPc = fetcher.getRegisterFile().readPc();
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						decodingPrint(decoderSecondResponse.getInstructionnumber());
						executingPrint(executerFirstResponse.getInstrunumber());
						int thirdInstruction = fetcher.fetch();
						if (executerFirstResponse.getNewPc() != -99)
							fetcher.getRegisterFile().setValueinRegister(32, executerFirstResponse.getNewPc());
							currentPc = fetcher.getRegisterFile().readPc();
						// fifth cycle done
						System.out.println("----------------------------------------");

						cycles++;

						System.out.println("Cycle number : " + cycles);
						memResponse = memHandler.handle(executerFirstResponse);
						if (executerFirstResponse.isJumpOrBranch() == true) {
							executerFirstResponse.setJumpOrBranch(false);
							executerFirstResponse.setNewPc(-99);
							// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
							differentPipelines();

						} else {
							DecoderResponse decoderThirdResponse = decoder.decode(thirdInstruction);
							if (decoderThirdResponse.getOperation().equals(Operation.BNE)
									|| decoderThirdResponse.getOperation().equals(Operation.J)) {

								jumpPc = fetcher.getRegisterFile().readPc();
							}

							ExecuterResponse executerSecondResponse = executer.execute(decoderSecondResponse, jumpPc);
							if (executerSecondResponse.getNewPc() != -99)
								fetcher.getRegisterFile().setValueinRegister(32, executerSecondResponse.getNewPc());
							// currentPc = fetcher.getPc();
							// sixth cycle done
							currentPc = fetcher.getRegisterFile().readPc();
							System.out.println("----------------------------------------");

							cycles++;

							System.out.println("Cycle number : " + cycles);
							decodingPrint(decoderThirdResponse.getInstructionnumber());
							executingPrint(executerSecondResponse.getInstrunumber());
							writeback.writeBack(memResponse);
							// seventh cycle done
							System.out.println("----------------------------------------");

							cycles++;
							System.out.println("Cycle number : " + cycles);
							memResponse = memHandler.handle(executerSecondResponse);
							if (executerSecondResponse.isJumpOrBranch() == true) {
								executerSecondResponse.setJumpOrBranch(false);
								executerSecondResponse.setNewPc(-99);
								// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
								differentPipelines();

							} else {
								ExecuterResponse executerThirdResponse = executer.execute(decoderThirdResponse, jumpPc);
								if (executerThirdResponse.getNewPc() != -99)
									fetcher.getRegisterFile().setValueinRegister(32, executerThirdResponse.getNewPc());
									currentPc = fetcher.getRegisterFile().readPc();
								// eighth cycle done
								System.out.println("----------------------------------------");

								cycles++;
								System.out.println("Cycle number : " + cycles);

								executingPrint(executerThirdResponse.getInstrunumber());
								writeback.writeBack(memResponse);
								// ninth cycle done
								System.out.println("----------------------------------------");

								cycles++;
								System.out.println("Cycle number : " + cycles);
								memResponse = memHandler.handle(executerThirdResponse);
								if (executerThirdResponse.isJumpOrBranch() == true) {
									executerThirdResponse.setJumpOrBranch(false);
									executerThirdResponse.setNewPc(-99);
									// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
									differentPipelines();

								} else {
									// tenth cycle done
									System.out.println("----------------------------------------");

									cycles++;
									System.out.println("Cycle number : " + cycles);
									writeback.writeBack(memResponse);
									// eleventh cycle done
								}

							}

						}

					} else {
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						writeback.writeBack(memResponse);

						fetcher.setNumber(currentPc + 1);
						writeback.setNumber(currentPc + 1);
						decoder.setNumber(currentPc + 1);
						executer.setNumber(currentPc + 1);
						memHandler.setNumber(currentPc + 1);

						int firstInstruction = fetcher.fetch();
						currentPc = fetcher.getRegisterFile().readPc();
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);
						// First cycle done
						DecoderResponse decoderfirstResponse = decoder.decode(firstInstruction);
						if (decoderfirstResponse.getOperation().equals(Operation.BNE)
								|| decoderfirstResponse.getOperation().equals(Operation.J)) {

							jumpPc = fetcher.getRegisterFile().readPc();
						}
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						int secondInstruction = fetcher.fetch();
						decodingPrint(decoderfirstResponse.getInstructionnumber());

						currentPc = fetcher.getRegisterFile().readPc();
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						// third cycle done
						DecoderResponse decoderSecondResponse = decoder.decode(secondInstruction);
						if (decoderSecondResponse.getOperation().equals(Operation.BNE)
								|| decoderSecondResponse.getOperation().equals(Operation.J)) {

							jumpPc =fetcher.getRegisterFile().readPc();
						}
						ExecuterResponse executerFirstResponse = executer.execute(decoderfirstResponse, jumpPc);
						currentPc = fetcher.getRegisterFile().readPc();
						// System.out.println(executerFirstResponse.isJumpOrBranch());
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						decodingPrint(decoderSecondResponse.getInstructionnumber());
						executingPrint(executerFirstResponse.getInstrunumber());

						int thirdInstruction = fetcher.fetch();

						if (executerFirstResponse.getNewPc() != -99)
							fetcher.getRegisterFile().setValueinRegister(32, executerFirstResponse.getNewPc());
						currentPc = fetcher.getRegisterFile().readPc();
						System.out.println("----------------------------------------");

						cycles++;
						System.out.println("Cycle number : " + cycles);

						memResponse = memHandler.handle(executerFirstResponse);
						if (executerFirstResponse.isJumpOrBranch() == true) {
							executerFirstResponse.setJumpOrBranch(false);
							executerFirstResponse.setNewPc(-99);
							// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
							differentPipelines();

						} else {
							DecoderResponse decoderThirdResponse = decoder.decode(thirdInstruction);

							if (decoderThirdResponse.getOperation().equals(Operation.BNE)
									|| decoderThirdResponse.getOperation().equals(Operation.J)) {

								jumpPc = fetcher.getRegisterFile().readPc();
							}
							ExecuterResponse executerSecondResponse = executer.execute(decoderSecondResponse, jumpPc);
							if (executerSecondResponse.getNewPc() != -99)
								fetcher.getRegisterFile().setValueinRegister(32, executerSecondResponse.getNewPc());

							currentPc = fetcher.getRegisterFile().readPc();


							System.out.println("----------------------------------------");

							cycles++;
							System.out.println("Cycle number : " + cycles);

							decodingPrint(decoderThirdResponse.getInstructionnumber());
							executingPrint(executerSecondResponse.getInstrunumber());
							writeback.writeBack(memResponse);
							System.out.println("----------------------------------------");

							cycles++;
							System.out.println("Cycle number : " + cycles);

							memResponse = memHandler.handle(executerSecondResponse);
							if (executerSecondResponse.isJumpOrBranch() == true) {
								executerSecondResponse.setJumpOrBranch(false);
								executerSecondResponse.setNewPc(-99);
								// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
								differentPipelines();

							} else {

								ExecuterResponse executerThirdResponse = executer.execute(decoderThirdResponse, jumpPc);
								if (executerThirdResponse.getNewPc() != -99) {

									fetcher.getRegisterFile().setValueinRegister(32, executerThirdResponse.getNewPc());

								}
								currentPc = fetcher.getRegisterFile().readPc();
								System.out.println("----------------------------------------");

								cycles++;
								System.out.println("Cycle number : " + cycles);

								executingPrint(executerThirdResponse.getInstrunumber());

								writeback.writeBack(memResponse);
								System.out.println("----------------------------------------");

								cycles++;
								System.out.println("Cycle number : " + cycles);
								memResponse = memHandler.handle(executerThirdResponse);
								if (executerThirdResponse.isJumpOrBranch() == true) {
									executerThirdResponse.setJumpOrBranch(false);
									executerThirdResponse.setNewPc(-99);
									// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
									differentPipelines();

								} else {
									System.out.println("----------------------------------------");

									cycles++;
									System.out.println("Cycle number : " + cycles);
									writeback.writeBack(memResponse);
								}

							}

						}

					}

				}

			}

		}

	}

	private void exitPipeLine() {
//
//		System.out.println("ana fel exit pipeline");
//		System.out.println("----------------------------------------");
//
//		System.out.println("Cycle number : " + cycles);

		memResponse = memHandler.handle(executerResponse);
		if (executerResponse.isJumpOrBranch() == true) {
			executerResponse.setJumpOrBranch(false);
			executerResponse.setNewPc(-99);
			// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
			differentPipelines();

		} else {
			executerResponse = executer.execute(decoderResponse, jumpPc);
			if (executerResponse.getNewPc() != -99) {

				fetcher.getRegisterFile().setValueinRegister(32, executerResponse.getNewPc());

			}
			currentPc = fetcher.getRegisterFile().readPc();
			decoderResponse = decoder.decode(fetched);
			if (decoderResponse.getOperation().equals(Operation.BNE)
					|| decoderResponse.getOperation().equals(Operation.J)) {

				jumpPc =fetcher.getRegisterFile().readPc();

			}
			System.out.println("-----------------------------------");
			// System.out.println("Cycle Done");

			cycles++;
			System.out.println("Cycle number : " + cycles);
			writeback.writeBack(memResponse);

			decodingPrint(decoderResponse.getInstructionnumber());
			executingPrint(executerResponse.getInstrunumber());

			System.out.println("----------------------------------------");
			cycles++;
			System.out.println("Cycle number : " + cycles);

			memResponse = memHandler.handle(executerResponse);
			if (executerResponse.isJumpOrBranch() == true) {
				executerResponse.setJumpOrBranch(false);
				executerResponse.setNewPc(-99);
				// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
				differentPipelines();

			} else {
				executerResponse = executer.execute(decoderResponse, jumpPc);
				if (executerResponse.getNewPc() != -99) {

					fetcher.getRegisterFile().setValueinRegister(32, executerResponse.getNewPc());

				}
				currentPc = fetcher.getRegisterFile().readPc();


				System.out.println("----------------------------------------");
				cycles++;
				System.out.println("Cycle number : " + cycles);

				writeback.writeBack(memResponse);
				executingPrint(executerResponse.getInstrunumber());
				System.out.println("----------------------------------------");

				cycles++;
				System.out.println("Cycle number : " + cycles);

				memResponse = memHandler.handle(executerResponse);
				if (executerResponse.isJumpOrBranch() == true) {
					executerResponse.setJumpOrBranch(false);
					executerResponse.setNewPc(-99);
					// System.out.println("ana dakhalt we current pc =" + fetcher.getPc());
					differentPipelines();

				} else {
					System.out.println("----------------------------------------");

					cycles++;
					System.out.println("Cycle number : " + cycles);

					writeback.writeBack(memResponse);

				}

			}

		}

	}

	private void runPipeLine() {
		// System.out.println("ana fl runpipeline");

		// System.out.println("this is my pc " + fetcher.getPc());
		while (fetcher.thereIsNextInstruction()) {
			if (cycles % 2 == 0) {
				// System.out.println("----------------------------------------");

				memResponse = memHandler.handle(executerResponse);
				if (executerResponse.isJumpOrBranch() == true) {
					executerResponse.setJumpOrBranch(false);
					executerResponse.setNewPc(-99);
					// System.out.println("ana dakhalt we current pc =" + currentPc);
					differentPipelines();
					indifferent = true;
					break;// 3ayzah yetla3 khaless mayedkholsh fel exit
				}

				executerResponse = executer.execute(decoderResponse, jumpPc);

				currentPc = fetcher.getRegisterFile().readPc();
				decoderResponse = decoder.decode(fetched);// dec4
				if (decoderResponse.getOperation().equals(Operation.BNE)
						|| decoderResponse.getOperation().equals(Operation.J)) {

					jumpPc= fetcher.getRegisterFile().readPc();
				}
				// System.out.println("currentPc abl el exit " + currentPc);
				System.out.println("----------------------------------------");

				cycles++;
				System.out.println("Cycle number : " + cycles);
				// decode
				// execute
				// memoryhanlde
			} else {
				// System.out.println("Cycle Number : " + cycles);

				writeback.writeBack(memResponse);

				decodingPrint(decoderResponse.getInstructionnumber());
				executingPrint(executerResponse.getInstrunumber());

				fetched = fetcher.fetch();
				if (executerResponse.getNewPc() != -99) {
					// System.out.println("ana dakhalt");
					fetcher.getRegisterFile().setValueinRegister(32, executerResponse.getNewPc());
				}
				currentPc = fetcher.getRegisterFile().readPc();
				System.out.println("----------------------------------------");

				cycles++;
				System.out.println("Cycle Number :" + cycles);

			}

		}
		;
		if (indifferent == false) {
			exitPipeLine();
		}

	}

	public static void main(String[] args) {

		Controller controller = new Controller();
		// controller.testNormalPipe();
		controller.encoder.loadinstructionsinMemory(controller.mem, "test.txt");
//		controller.testNormalPipe();
		// controller.fetcher.getMem().printMemory();
		controller.regFile.setValueinRegister(1, 1);
		controller.regFile.setValueinRegister(2, 2);
		// controller.fetcher.getMem().setData(1024, 60);
		// controller.fetcher.getMem().setData(1025, 60);
		controller.differentPipelines();

		controller.fetcher.getMem().printMemory();
		controller.regFile.printRegFile();

	}

	private void testNormalPipe() {

		// Row i =new Row();
//		i.setFromTo(8,8);
//		i.setFromTo(12, 12);
//		i.setFromTo(17, 18);

		// mem.setInstruction(0, 8937472);
		// this.mem.setData(1024, 15);

		this.encoder.loadinstructionsinMemory(mem, "test.txt");
		int i = mem.getInstruction(0);
		System.out.println(i); // System.out.println(i);
		regFile.setValueinRegister(2, 4);
		regFile.setValueinRegister(3, 4);
		regFile.setValueinRegister(1, 2);
		DecoderResponse resp = decoder.decode(i);
		System.out.println(resp.getDestinationRegisterNumber());
		System.out.println(resp.getFirstSourceValue());
		System.out.println(resp.getSecondSourceValue());

//		Controller.regFile.setValueinRegister(4,5);
		ExecuterResponse obj = executer.execute(resp, 0);
		System.out.println(obj.getDestReg());
		System.out.println(obj.getResult());
		mem.printMemory();
		MemoryHandlerResponse memHandleResponse = memHandler.handle(obj);
		writeback.writeBack(memHandleResponse);

		regFile.printRegFile();
		mem.printMemory();

		System.out.println();
	}

	private int instructionsleft(int pccount) {
		int count = 0;
		for (int i = pccount; i < this.fetcher.getMem().getsize(); i++) {
			if (this.fetcher.getMem().getInstruction(i) != 0) {
				count = count + 1;
			} else {
				break;
			}

		}
		return count;
	}

}
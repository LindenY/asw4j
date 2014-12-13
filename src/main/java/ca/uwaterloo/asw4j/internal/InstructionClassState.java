package ca.uwaterloo.asw4j.internal;

import ca.uwaterloo.asw4j.Instruction;

/**
 * <p>
 * Basic representation for {@link Instruction} class
 * </p>
 * 
 * <p>
 * States:
 * <ul>
 * <li>Ready: the {@link Instruction} class is ready to run.</li>
 * <li>Blocked: the {@link Instruction} class is blocked.</li>
 * <li>Terminated: the {@link Instruction} class finished running.</li>
 * </ul>
 * </p>
 * 
 * @author Desmond Lin
 * @since 1.0.0
 */
public class InstructionClassState {

	protected static enum STATE {
		Ready, Blocked, Terminated
	}

	private final STATE state;
	private final String stateMessage;

	protected InstructionClassState(STATE state, String stateMessage) {
		this.state = state;
		this.stateMessage = stateMessage;
	}

	public String getState() {
		return state.toString();
	}

	public String getStateMessage() {
		return stateMessage;
	}

	private final static InstructionClassState Ready = new InstructionClassState(
			STATE.Ready, "Instruction is ready");
	private final static InstructionClassState BlockedBySingleton = new InstructionClassState(
			STATE.Blocked, "Instruction is blocked by singleton");
	private final static InstructionClassState Terminated = new InstructionClassState(
			STATE.Terminated, "Instruction is terminated");

	public final static InstructionClassState Ready() {
		return Ready;
	}

	public final static InstructionClassState BlockedBySingleton() {
		return BlockedBySingleton;
	}

	public final static InstructionClassState BlockedByException(Exception e) {
		return new InstructionClassState(STATE.Blocked,
				("Instruction is blocked by exception: ["
						+ e.getClass().getName() + "] " + e.getMessage()));
	}

	public final static InstructionClassState Terminated() {
		return Terminated;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof InstructionClassState) {
			InstructionClassState another = (InstructionClassState) obj;

			if (this.state == another.state
					&& this.stateMessage.equals(another.stateMessage)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return ("[" + state.toString() + ":" + stateMessage + "]");
	}

}

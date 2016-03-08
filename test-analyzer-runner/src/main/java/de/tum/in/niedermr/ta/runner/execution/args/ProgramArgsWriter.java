package de.tum.in.niedermr.ta.runner.execution.args;

/** Writer to generate the arguments array for a program invocation. */
public class ProgramArgsWriter extends AbstractProgramArgsManager {

	/** Constructor. */
	public ProgramArgsWriter(Class<?> programClass, int argsLength) {
		super(programClass, new String[argsLength]);
		initEmptyArgs();
	}

	/** Initialize the array with empty string entries. */
	private void initEmptyArgs() {
		for (int i = 0; i < m_args.length; i++) {
			m_args[i] = "";
		}
	}

	/** Get the arguments array. */
	public String[] getArgs() {
		return m_args;
	}

	/** Set an argument by its key. A key check is performed. */
	public void setValue(ProgramArgsKey key, String value) {
		checkProgramArgsKey(key);
		m_args[key.getIndex()] = value;
	}
}

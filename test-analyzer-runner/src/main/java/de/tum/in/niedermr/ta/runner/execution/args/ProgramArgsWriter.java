package de.tum.in.niedermr.ta.runner.execution.args;

/** Writer to generate the arguments array for a program invocation. */
public class ProgramArgsWriter extends AbstractProgramArgsManager {

	public ProgramArgsWriter(Class<?> programClass, int argsLength) {
		super(programClass, new String[argsLength]);
		initEmptyArgs();
	}

	private void initEmptyArgs() {
		for (int i = 0; i < m_args.length; i++) {
			m_args[i] = "";
		}
	}

	public String[] getArgs() {
		return m_args;
	}

	public void setValue(ProgramArgsKey key, String value) {
		checkProgramArgsKey(key);
		m_args[key.getIndex()] = value;
	}
}

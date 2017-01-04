package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base.AbstractReturnValueFactory;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/** Abstract test class for factories generating instances. */
public abstract class AbstractInstancesReturnValueFactoryTest {

	/** Factory. */
	protected AbstractReturnValueFactory m_factory;

	/** Constructor. */
	protected AbstractInstancesReturnValueFactoryTest(AbstractReturnValueFactory factory) {
		m_factory = factory;
	}

	/**
	 * Test: Wrapper classes and String should not be supported because they are already handled by the simple return
	 * value generators
	 */
	@Test
	public void factoryShouldNotSupportStringAndWrapperTypes() {
		assertClassIsUnsupported(String.class.getName());
		assertClassIsUnsupported(Double.class.getName());
	}

	protected void assertClassIsUnsupported(String returnType) {
		assertFalse(m_factory.supports(MethodIdentifier.EMPTY, returnType));
		assertNull(m_factory.get(MethodIdentifier.EMPTY.toString(), returnType));
	}
}

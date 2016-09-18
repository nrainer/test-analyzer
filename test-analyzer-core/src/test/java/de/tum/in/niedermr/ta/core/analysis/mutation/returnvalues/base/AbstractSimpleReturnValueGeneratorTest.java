package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

public class AbstractSimpleReturnValueGeneratorTest {
	@Test
	public void testCanHandleReturn() {
		AbstractSimpleReturnValueGenerator retValGenNoString = new AbstractSimpleRetValGenStub(false);
		AbstractSimpleReturnValueGenerator retValGenWithString = new AbstractSimpleRetValGenStub(true);

		final MethodIdentifier identifier = MethodIdentifier.EMPTY;
		Type type;

		type = Type.VOID_TYPE;
		assertFalse(retValGenNoString.checkReturnValueSupported(identifier, type));
		assertFalse(retValGenWithString.checkReturnValueSupported(identifier, type));

		type = Type.CHAR_TYPE;
		assertTrue(retValGenNoString.checkReturnValueSupported(identifier, type));
		assertTrue(retValGenWithString.checkReturnValueSupported(identifier, type));

		type = Type.getType("Ljava/lang/String;");
		assertFalse(retValGenNoString.checkReturnValueSupported(identifier, type));
		assertTrue(retValGenWithString.checkReturnValueSupported(identifier, type));

		type = Type.getType("Ljava/lang/Double;");
		assertFalse(retValGenNoString.checkReturnValueSupported(identifier, type));
		assertFalse(retValGenWithString.checkReturnValueSupported(identifier, type));

		type = Type.getType("Ljava/io/File;");
		assertFalse(retValGenNoString.checkReturnValueSupported(identifier, type));
		assertFalse(retValGenWithString.checkReturnValueSupported(identifier, type));
	}

	class AbstractSimpleRetValGenStub extends AbstractSimpleReturnValueGenerator {
		public AbstractSimpleRetValGenStub(boolean supportStringType) {
			super(supportStringType);
		}

		@Override
		public void handleBooleanReturn(MethodVisitor mv) {
			// NOP
		}

		@Override
		public void handleIntegerReturn(MethodVisitor mv) {
			// NOP
		}

		@Override
		public void handleCharReturn(MethodVisitor mv) {
			// NOP
		}

		@Override
		public void handleLongReturn(MethodVisitor mv) {
			// NOP
		}

		@Override
		public void handleFloatReturn(MethodVisitor mv) {
			// NOP
		}

		@Override
		public void handleDoubleReturn(MethodVisitor mv) {
			// NOP
		}

		@Override
		public void handleStringReturn(MethodVisitor mv) {
			// NOP
		}
	}
}

package de.tum.in.niedermr.ta.core.code.visitor;

import org.objectweb.asm.ClassVisitor;

/** Class visitor that does not change anything. */
public final class NoModificationClassVisitor extends ClassVisitor {

	public NoModificationClassVisitor(int version, ClassVisitor cv) {
		super(version, cv);
	}
}

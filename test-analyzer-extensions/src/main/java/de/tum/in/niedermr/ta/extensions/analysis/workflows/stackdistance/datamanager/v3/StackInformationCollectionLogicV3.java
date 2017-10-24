package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.v3;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.datamanager.AbstractThreadAwareStackInformationCollectionLogic;

/**
 * Logic to collect information about the test cases and methods under
 * test.<br/>
 * Parameterless constructor required.
 */
public class StackInformationCollectionLogicV3 extends AbstractThreadAwareStackInformationCollectionLogic {

	/** Constructor. */
	public StackInformationCollectionLogicV3() {
		super(new ThreadAwareStackDistanceManagerV3());
	}
}

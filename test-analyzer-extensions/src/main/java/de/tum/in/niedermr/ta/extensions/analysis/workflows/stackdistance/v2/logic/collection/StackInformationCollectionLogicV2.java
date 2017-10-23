package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.v2.logic.collection;

import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.common.logic.collection.AbstractThreadAwareStackInformationCollectionLogic;

/**
 * Logic to collect information about the test cases and methods under
 * test.<br/>
 * Parameterless constructor required.
 */
public class StackInformationCollectionLogicV2 extends AbstractThreadAwareStackInformationCollectionLogic {

	/** Constructor. */
	public StackInformationCollectionLogicV2() {
		super(new ThreadAwareStackDistanceManagerV2());
	}
}

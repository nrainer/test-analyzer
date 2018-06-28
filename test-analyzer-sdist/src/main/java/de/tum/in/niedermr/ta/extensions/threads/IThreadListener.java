package de.tum.in.niedermr.ta.extensions.threads;

/** Listener for {@link ThreadNotifier}. */
public interface IThreadListener {

	/**
	 * Event that a new Thread was started.
	 * 
	 * @param name
	 *            name of the new thread
	 */
	void threadIsAboutToStart(String name);
}

package de.tum.in.niedermr.ta.extensions.threads;

import java.util.ArrayList;
import java.util.List;

/** Notifier that sends events to registered listeners if a new thread is started. */
public class ThreadNotifier {

	/** Singleton instance. */
	public static final ThreadNotifier INSTANCE = new ThreadNotifier();

	/** Registered listeners. */
	private final List<IThreadListener> m_listeners = new ArrayList<>();

	/** Register a listener. */
	public synchronized void registerListener(IThreadListener listener) {
		m_listeners.add(listener);
	}

	/**
	 * Send an event to the listeners to notify them that a new thread is about to be started. Note that is <b>has not
	 * been started yet</b>, but will be started after the leaving of this method. <b>Only to be invoked by
	 * {@link Thread#start()}.</b>
	 */
	public synchronized void sendThreadStartedEvent(Thread thread) {
		String threadName = thread.getName();
		for (IThreadListener listener : m_listeners) {
			listener.threadIsAboutToStart(threadName);
		}
	}
}

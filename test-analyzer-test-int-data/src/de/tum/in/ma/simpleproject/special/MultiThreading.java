package de.tum.in.ma.simpleproject.special;

public class MultiThreading {

	public void computeMultiThreading() throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			new Thread(new ComputeThread()).start();
		}

		compute2(2);
		compute3(3);
		compute4(4);

		// let the threads complete
		Thread.sleep(100L);
	}

	public int compute2(int x) {
		return 2 + compute3(x);
	}

	public int compute3(int x) {
		return 4 + 3 * x;
	}

	private int compute4(int x) {
		return 4 + 3 * compute2(x);
	}

	class ComputeThread implements Runnable {

		@Override
		public void run() {
			compute2(8);
		}
	}
}

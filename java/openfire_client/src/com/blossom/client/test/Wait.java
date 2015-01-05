package com.blossom.client.test;

public class Wait {

	private Lock lock = new Lock();
	
	public static void main(String[] args) {
		Wait w = new Wait();
		w.getLock();
	}

	public Lock getLock() {
		synchronized (lock) {
			while(!lock.released) {
				try {
					lock.wait(6000L);
					System.out.println("wake up");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return lock;
	}
}

class Lock {
	protected boolean released;
	
	public Lock() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (Lock.this) {
					released = true;
					Lock.this.notifyAll();
					System.out.println("released");
				}
			}
		}).start();
	}
}

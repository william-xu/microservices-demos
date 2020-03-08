package org.xwl.demo.boot.tp.distlock.utils;

public interface DistributedLock {
	public static enum ExpireType {
		EX, // Set the specified expire time, in seconds.
		PX // Set the specified expire time, in milliseconds.
	}

	public static enum SetType {
		NX, // Only set the key if it does not already exist.
		XX // Only set the key if it already exist.
	}

	boolean lock(String key, String value, ExpireType expireType, int expirationTime, SetType setType);

	boolean unlock(String key, String value);
	
}

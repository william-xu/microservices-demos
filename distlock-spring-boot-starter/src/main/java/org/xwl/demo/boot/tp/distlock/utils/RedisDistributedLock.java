package org.xwl.demo.boot.tp.distlock.utils;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.types.Expiration;

/**
 * @author xwl
 */
public class RedisDistributedLock implements DistributedLock{

	private RedisConnectionFactory connectionFactory;

	public boolean lock(String key, String value, ExpireType expireType, int expirationTime, SetType setType) {
		boolean isLocked = false;
		Expiration exp = ExpireType.EX == expireType ? Expiration.seconds(expirationTime)
				: Expiration.milliseconds(expirationTime);
		SetOption opt = SetType.NX == setType ? SetOption.SET_IF_ABSENT : SetOption.SET_IF_PRESENT;
		RedisConnection conn = null;
		try {
			conn = connectionFactory.getConnection();
			isLocked = conn.set(key.getBytes(), value.getBytes(), exp, opt);
		} finally {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}

		return isLocked;
	}

	public boolean unlock(String key, String value) {
		boolean isUnlocked = false;
		// 将获取键和删除键放到脚本中一起执行
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		RedisConnection conn = null;
		try {
			conn = connectionFactory.getConnection();
			Long result = conn.eval(script.getBytes(), ReturnType.INTEGER, 1, key.getBytes(), value.getBytes());
			isUnlocked = !(result == 0);
		} finally {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return isUnlocked;
	}

	public RedisConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

}
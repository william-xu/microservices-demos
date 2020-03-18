package org.xwl.msd.canal.client;
import redis.clients.jedis.Jedis;

/**
 * @author lgz
 */
public class RedisUtil {

    private static Jedis jedis = null;

    public static synchronized Jedis getJedis() {
        if (jedis == null) {
            jedis = new Jedis("192.168.43.104", 6379);
            jedis.select(1);
        }
        return jedis;
    }

    public static boolean existKey(String key) {
        return getJedis().exists(key);
    }

    // todo 增删改操作需要清空所有filter数据
    //        getJedis().del(tableName+":filter");

    public static void delKey(String tableName,String key) {
        getJedis().hdel(tableName,key);
    }

    public static String stringGet(String tableName,String key) {
        return getJedis().hget(tableName, key);
    }

    public static String stringSet(String tableName,String key, String value) {
        getJedis().hset(tableName,key,value);
        return "";
//        return getJedis().set(key, value);
    }

    public static void hashSet(String key, String field, String value) {
        getJedis().hset(key, field, value);
    }
}
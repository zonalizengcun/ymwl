package com.yim.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisClient {

	private static final Logger logger = Logger.getLogger(RedisClient.class);
	private final static int KEY_EXPIRE_TIME = 5; // 分布式锁超时5秒自动解锁

	private int redis_pool_minIdle = 1;
	private int redis_pool_maxIdle = 20;
	private int redis_pool_maxTotal = 1000;
	
	private String jedisIp;
	
	private int port ;
	private String passWord;

	private JedisPool jedisPool;
	
	public RedisClient(String ip,int port,String password ){
		this.jedisIp = ip;
		this.port = port;
		this.passWord = password;
	}

	public void init() {
 		JedisPoolConfig config = new JedisPoolConfig();
		config.setMinIdle(redis_pool_minIdle);
		config.setMaxIdle(redis_pool_maxIdle);
		config.setMaxTotal(redis_pool_maxTotal);
		if (this.passWord == null || this.passWord.equals("")) {
			jedisPool = new JedisPool(config,jedisIp,port);
		}else{
			jedisPool = new JedisPool(config,jedisIp,port,Protocol.DEFAULT_TIMEOUT,passWord);
		}
	}

	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} finally {
			jedis.close();
		}
	}

	public void del(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 放入map
	 * 
	 * @param key
	 *            map对应的索引key
	 * @param entryKey
	 *            map中的key
	 * @param value
	 *            map中entryKey所对应的value
	 */

	public long hset(String key, String entryKey, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hset(key, entryKey, value);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 批量放入map
	 * 
	 * @param key
	 *            map对应的索引key
	 * @param map
	 *            所存入的map
	 */

	public String hmSet(String key, Map<String, String> map) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hmset(key, map);
		} finally {
			jedis.close();
		}
	}
	
	/**
	 * 从map中获取所有value
	 * @param key
	 * @return
	 */
	public List<String> hValues(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hvals(key);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 批量删除索引key所存储的map集合中对应entryKey
	 * 
	 * @param key
	 *            索引key
	 * @param entryKey
	 *            map集合的keys
	 */

	public long hdel(String key, String... entryKey) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hdel(key, entryKey);
		} finally {
			jedis.close();
		}
	}

	public List<String> hmGet(String key, String... entryKey) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hmget(key, entryKey);
		} finally {
			jedis.close();
		}
	}

	/**
	 * Set操作 添加一个set成员在对应的key上
	 */

	public long sadd(String key, String... members) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sadd(key, members);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 从SET中删除多个成员
	 */

	public long srem(String key, String... members) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.srem(key, members);
		} finally {
			jedis.close();
		}
	}

	public long incr(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.incr(key);
		} finally {
			jedis.close();
		}
	}

	public long setnx(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.setnx(key, value);
		} finally {
			jedis.close();
		}
	}

	public void expire(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.expire(key, seconds);
		} finally {
			jedis.close();
		}
	}

	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.get(key);
		} finally {
			jedis.close();
		}
	}

	public Set<String> keys(String param) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.keys(param);
		} finally {
			jedis.close();
		}
	}

	public boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} finally {
			jedis.close();
		}
	}

	public String hget(String key, String entryKey) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hget(key, entryKey);
		} finally {
			jedis.close();
		}
	}

	public Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hgetAll(key);
		} finally {
			jedis.close();
		}
	}

	public long hincrBy(String key, String field, Integer value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hincrBy(key, field, value);
		} finally {
			jedis.close();
		}
	}

	public Set<String> smembers(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.smembers(key);
		} finally {
			jedis.close();
		}
	}

	public boolean sismember(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sismember(key, member);
		} finally {
			jedis.close();
		}
	}

	public boolean hexiste(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hexists(key, field);
		} finally {
			jedis.close();
		}
	}

	public List<String> lrange(String key, int start, int stop) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.lrange(key, start, stop);
		} finally {
			jedis.close();
		}
	}

	public void lpush(String key, String... values) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.lpush(key, values);
		} finally {
			jedis.close();
		}
	}

	public long lrem(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.lrem(key, 0, value);
		} finally {
			jedis.close();
		}
	}

	public long lLen(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.llen(key);
		} finally {
			jedis.close();
		}
	}

	public String rPop(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.rpop(key);
		} finally {
			jedis.close();
		}

	}

	public long hlen(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hlen(key);
		} finally {
			jedis.close();
		}
	}
	
	/**
	 * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
	 * @param key
	 * @param score
	 * @param member
	 */
	public void zAdd(String key,double score,String member){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.zadd(key, score, member);
		} finally {
			jedis.close();
		}
	}
	
	/**
	 * 有序集合中对指定成员的分数加上增量 score
	 * @param key
	 * @param score
	 * @param member
	 */
	public void zIncrby(String key,double score,String member){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.zincrby(key, score, member);
		} finally {
			jedis.close();
		}
	}
	
	/**
	 * 通过索引区间返回有序集合成指定区间内的成员 从低到高
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zRange(String key,long start,long end){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> members = jedis.zrange(key, start, end);
			return members;
		} finally {
			jedis.close();
		}
	}
	/**
	 * 通过索引区间返回有序集合成指定区间内的成员 从低到高
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zReverRange(String key,long start,long end){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> members = jedis.zrevrange(key, start, end);
			return members;
		} finally {
			jedis.close();
		}
	}
	
	
	/**
	 * 获取有序集合的成员数
	 * @param key
	 * @return
	 */
	public long zCard(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zcard(key);
		} finally {
			jedis.close();
		}
	}
	
	/**
	 * 命令返回有序集中成员的排名
	 * @param key
	 * @param member
	 * @return
	 */
	public Long zRevrank(String key,String member){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zrevrank(key, member);
		} finally {
			jedis.close();
		}
	}
	
	public double zScore(String key,String member){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.zscore(key, member);
		} finally {
			jedis.close();
		}
	}
	
	
	
	/**
	 * 移除有序集合中给定的排名区间的所有成员
	 * @param key
	 * @param start
	 * @param end
	 */
	public void zRemRangeByRank(String key,long start,long end){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.zremrangeByRank(key, start, end);
		} finally {
			jedis.close();
		}
		
	}
	
	
	
	

	public void lock(String key) {
		try {
			do {
				key += "_LOCK";
				long result = this.setnx(key, "lock");
				if (result == 1) {
					this.expire(key, KEY_EXPIRE_TIME);
					return;
				}
				Thread.sleep(20);
			} while (true);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void unlock(String key) {
		try {
			key += "_LOCK";
			this.del(key);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void setRedis_pool_minIdle(int redis_pool_minIdle) {
		this.redis_pool_minIdle = redis_pool_minIdle;
	}

	public void setRedis_pool_maxIdle(int redis_pool_maxIdle) {
		this.redis_pool_maxIdle = redis_pool_maxIdle;
	}

	public void setRedis_pool_maxTotal(int redis_pool_maxTotal) {
		this.redis_pool_maxTotal = redis_pool_maxTotal;
	}


}

package fi.silverskin.secureproxy.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * Common base class for all Redis related classes. No other reason than share
 * share connection for single Redis instance.
 */
public class BaseRedis {
    // TODO: Maybe put these into some fancy enum?
    public static final int LINK_DB = 0;
    public static final int RESPONSE_CACHE = 1;
    
    // TODO: Read redis address from config file.
    protected static JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
}

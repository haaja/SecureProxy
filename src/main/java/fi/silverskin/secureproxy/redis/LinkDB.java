package fi.silverskin.secureproxy.redis;

import java.util.Map;
import java.util.Map.Entry;
import redis.clients.jedis.Jedis;

public class LinkDB extends BaseRedis {

    private final int type = LINK_DB;

    /**
     * Insert link pair into global, not-expiring storage.
     *
     * Link pair is inserted only if it doesn't exist already, more importantly:
     * original link is used as key and its existence is tested.
     *
     * @param key
     * @param value Modified link
	 * @return true if something was inserted, false otherwise.
     */
    public boolean addLink(String key, String value) {
        Jedis jedis = pool.getResource();
        try {
            jedis.select(type);
            boolean retval = insertLink(jedis, "GLOBAL", key, value);
			return retval;
        } finally {
            pool.returnResource(jedis);
        }
    }

    /**
     * Add link pair into id hash, will create id if it doesn't exist.
     *
     * Notice that timeout key id given to id, not to individual link. This
     * method also makes sure that links are inserted into id only once.
     *
     * @param key Original link
     * @param value Modified link
     * @param id Session key
     * @param timeout Timeout in seconds until SESSION will expire.
	 * @return true if something was inserted, false otherwise.
     */
    public boolean addLink(String key, String value, String id, int timeout) {
        Jedis jedis = pool.getResource();
        try {
            jedis.select(type);
            boolean retval = insertLink(jedis, id, key, value);
            jedis.expire(id, timeout);
			return retval;
        } finally {
            pool.returnResource(jedis);
        }
    }

    private boolean insertLink(Jedis con, String id, String key, String values) {
        Long retval = con.hsetnx(id, key, values);
		System.out.println(retval);

		if (retval == 0) {
			System.out.println("Field already existed.");
			return false;
		}
		else {
			System.out.println("Field was inserted.");
			return true;
		}
    }

    
    /**
     * Fetch original link associated with modified link from global storage.
     * 
     * @param value Modified link
     * @return Unmodified link or empty String
     */
    public String fetchValue(String value) {
        return fetchValue(value, "GLOBAL");
    }

    /**
     * Fetch original link associated with modified link in given session.
     * 
     * @param value Modified link
     * @param id Session key
     * @return Unmodified link or empty String
     */
    public String fetchValue(String value, String id) {
        Jedis jedis = pool.getResource();
        try {
            jedis.select(type);
            Map<String, String> values = jedis.hgetAll(id);
            
            if (values.containsValue(value))
                return iterateValues(values, value);
            else 
                return "";
        } finally {
            pool.returnResource(jedis);
        }
    }

    private String iterateValues(Map<String, String> values, String val) {
        for (Entry<String, String> pair : values.entrySet()) {
            if (pair.getValue().equalsIgnoreCase(val)) {
                return pair.getKey();
            }
        }
        return "";
    }


    /**
     * Flush LinkDB data.
     */
    public void flushAll() {
        Jedis jedis = pool.getResource();
        try {
            jedis.select(type);
            jedis.flushDB();
        } finally {
            pool.returnResource(jedis);
        }        
    }
}

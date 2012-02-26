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
     * @param original Original link
     * @param modified Modified link
     */
    public void addLink(String original, String modified) {
        Jedis jedis = pool.getResource();
        try {
            jedis.select(type);
            insertLink(jedis, "GLOBAL", original, modified);

        } finally {
            pool.returnResource(jedis);
        }
    }

    /**
     * Add link pair into session hash, will create session if it doesn't exist.
     *
     * Notice that timeout key is given to session, not to individual link. This
     * method also makes sure that links are inserted into session only once.
     *
     * @param original Original link
     * @param modified Modified link
     * @param session Session key
     * @param timeout Timeout in seconds until SESSION will expire.
     */
    public void addLink(String original, String modified, String session, int timeout) {
        Jedis jedis = pool.getResource();
        try {
            jedis.select(type);
            insertLink(jedis, session, original, modified);
            jedis.expire(session, timeout);
        } finally {
            pool.returnResource(jedis);
        }
    }

    private void insertLink(Jedis con, String key, String original, String modified) {
        con.hsetnx(key, original, modified);
    }

    
    /**
     * Fetch original link associated with modified link from global storage.
     * 
     * @param modified Modified link
     * @return Unmodified link or empty String
     */
    public String fetchOriginal(String modified) {
        return fetchOriginal(modified, "GLOBAL");
    }

    /**
     * Fetch original link associated with modified link in given session.
     * 
     * @param modified Modified link
     * @param session Session key
     * @return Unmodified link or empty String
     */
    public String fetchOriginal(String modified, String session) {
        Jedis jedis = pool.getResource();
        try {
            jedis.select(type);
            Map<String, String> values = jedis.hgetAll(session);
            
            if (values.containsValue(modified))
                return iterateValues(values, modified);
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

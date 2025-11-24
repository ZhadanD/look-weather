package look.weather.services;

import java.util.Properties;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import look.weather.utils.PropertyUtil;

public class RedisService {
    private RedisClient redisClient;

    private StatefulRedisConnection<String, String> connection;

    private RedisCommands<String, String> syncCommands;
    
    public RedisService() {
        PropertyUtil propertyUtil = new PropertyUtil();

        Properties properties = propertyUtil.loadProperties();

        redisClient = RedisClient.create(
            properties.getProperty("redis.url")
        );

        connection = redisClient.connect();

        syncCommands = connection.sync();
    }
    
    public void setWithExpiry(String key, String value, long seconds) {
        syncCommands.setex(key, seconds, value);
    }

    public String getString(String key) {
        return syncCommands.get(key);
    }

    public boolean exists(String key) {
        return syncCommands.exists(key) > 0;
    }
}

package com.superspeed.lock.disLocks;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;

public class SimpleRedisLock {

    private Jedis jedis = new Jedis("127.0.0.1" , 6379);

    public String getlock(String lockName, int timeout) {
        String uuid = UUID.randomUUID().toString();

        while (true) {
            try {
                if (jedis.setnx(lockName, uuid) == 1) {
                    jedis.expire(lockName, timeout);
                    return uuid;
                }
                if (jedis.ttl(lockName) == -1) {
                    jedis.expire(lockName, timeout);
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public boolean releaseLock(String key, String value) {
        while (true) {
            try {
                String status = jedis.watch(key);
                System.out.println("status->" + status);
                if (value.equals(jedis.get(key))) {
                    Transaction transaction = jedis.multi();
                    transaction.del(value);
                    List<Object> list = transaction.exec();
                    if (list == null) {
                        continue;
                    }
                    return true;
                }
                jedis.unwatch();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static void main(String[] args) {
        String lockName = "test_lock";
        SimpleRedisLock simpleRedisLock = new SimpleRedisLock();
        String uuid = simpleRedisLock.getlock(lockName, 10);
        if (uuid != null) {
            System.out.println("获得锁");
            if (simpleRedisLock.releaseLock(lockName, uuid)) {
                System.out.println("释放锁成功");
            }
        } else {
            System.out.println("未获取到锁");
        }
    }

}

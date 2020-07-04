--[[
    key1 : 订阅topic
    key2 : key(topic)
    key3 : offset
    key4 : size
    key5 : status
    key6 : check_status
    argv1 : value
    argv2 : value.toString()
--]]


if redis.call('hsetnx', KEYS[2], KEYS[3], 0)==1 then
    redis.call('hset', KEYS[2], KEYS[4], 1)
    redis.call('hset', KEYS[2], 0, ARGV[1])
else
    local num=redis.call('hget', KEYS[2], KEYS[4])
    redis.call('hincrby', KEYS[2], KEYS[4], 1)
    redis.call('hset', KEYS[2], num, ARGV[1])
end
redis.call('hsetnx', KEYS[2], KEYS[5], 1)
redis.call('hdel', KEYS[2],KEYS[6])
return redis.call('publish', KEYS[1], ARGV[2])



if redis.call('hget', KEYS[1], KEYS[3]) then

else
    redis.call('hset', KEYS[1], KEYS[3], 1)
end

local num=redis.call('hget', KEYS[1], KEYS[4])
if num then
    redis.call('hincrby', KEYS[1], KEYS[4], 1)
    redis.call('hset', KEYS[1], num, ARGV[1])
else
    redis.call('hset', KEYS[1], KEYS[4], 1)
    redis.call('hset', KEYS[1], 0, ARGV[1])
end

return redis.call('publish', KEYS[2], ARGV[2])



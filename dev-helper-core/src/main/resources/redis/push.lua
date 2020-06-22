if redis.call('hget', KEYS[1], ARGV[1]) then

else
    redis.call('hset', KEYS[1], ARGV[1], 1)
end

local num=redis.call('hget', KEYS[1], ARGV[2])
if num then
    redis.call('hincrby', KEYS[1], ARGV[2], 1)
    redis.call('hset', KEYS[1], num, ARGV[3])
else
    redis.call('hset', KEYS[1], ARGV[2], 1)
    redis.call('hset', KEYS[1], 0, ARGV[3])
end

return redis.call('publish', KEYS[2], ARGV[4])



// key1 : key(topic)
// key2 : size
// key3 : offset
// key4 : queue
// argv1 : value

local size=redis.call('hget', KEYS[1], KEYS[2])
local offset=redis.call('hget', KEYS[1], KEYS[3])
if size and offset and tonumber(size)>tonumber(offset) then
    if ARGV[1]=='false' then
    else
        redis.call('hset', KEYS[1]..KEYS[4], offset, ARGV[1])
        redis.call('hincrby', KEYS[1], KEYS[3], 1)
    end
    return tonumber(offset)
else

    return -1
end
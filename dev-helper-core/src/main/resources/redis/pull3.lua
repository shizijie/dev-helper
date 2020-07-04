--[[
    key1 : key(topic)
    key2 : key(topic_queue)
    key3 : key(topic_queue_bak)
    key4 : offset
    key5 : size
    key6 : status
    argv1 : value
--]]

local function pull(key,offset,size,value)
    if redis.call('hsetnx', key, offset, 0)==1 then
        redis.call('hset', key, size, 1)
        redis.call('hset', key, 0, value)
    else
        local num=redis.call('hget', key, size)
        redis.call('hincrby', key, size, 1)
        redis.call('hset', key, num, value)
    end
end

local status=redis.call('hget', KEYS[1], KEYS[6])
local size
local offset
if status=='1' then
    offset=redis.call('hget', KEYS[1], KEYS[4])
    size=redis.call('hget', KEYS[1], KEYS[5])
elseif status=='2' then
    offset=redis.call('hget', KEYS[1]..KEYS[2], KEYS[4])
    size=redis.call('hget', KEYS[1]..KEYS[2], KEYS[5])
elseif status=='3' then
    offset=redis.call('hget', KEYS[1]..KEYS[3], KEYS[4])
    size=redis.call('hget', KEYS[1]..KEYS[3], KEYS[5])
else
    return tonumber(status)
end

if size and offset and tonumber(size)>tonumber(offset) then
    if ARGV[1]=='false' then
    else
        if status=='1' or status='3' then
            pull(KEYS[1]..KEYS[2], KEYS[4], KEYS[5], offset)
        elseif status=='2' then
            pull(KEYS[1]..KEYS[3], KEYS[4], KEYS[5], offset)
        end
    end
    if status=='1' then
        return tonumber(offset)
    elseif status=='2' then
        return tonumber(redis.call('hget', KEYS[1]..KEYS[2], offset))
    elseif status=='3' then
        return tonumber(redis.call('hget', KEYS[1]..KEYS[3], offset))
    end
else
    return -1
end
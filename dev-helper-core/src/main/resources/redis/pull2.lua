--[[
    key1 : key(topic)
    key2 : key(topic_queue)
    key3 : key(topic_queue_bak)
    key4 : offset
    key5 : size
    key6 : status
    argv1 : value
--]]

local status=redis.call('hget', KEYS[1], KEYS[6])
if status=='1' then
    local offset=redis.call('hget', KEYS[1], KEYS[4])
    local size=redis.call('hget', KEYS[1], KEYS[5])
    if size and offset and tonumber(size)>tonumber(offset) then
        if ARGV[1]=='false' then
            return tonumber(offset)
        else
            redis.call('hincrby', KEYS[1], KEYS[4], 1)
            redis.call('lpush', KEYS[1]..KEYS[2], offset)
            return tonumber(offset)
        end
    else
        return -1
    end
elseif status=='2' then
    local size=redis.call('llen', KEYS[1]..KEYS[2])
    if size>0 then
        if ARGV[1]=='false' then
            return size
        else
            local value=redis.call('rpop', KEYS[1]..KEYS[2])
            redis.call('lpush', KEYS[1]..KEYS[3], value)
            return tonumber(value)
        end
    else
        return -2
    end
elseif status=='3' then
    local size=redis.call('llen', KEYS[1]..KEYS[3])
    if size>0 then
        if ARGV[1]=='false' then
            return size
        else
            local value=redis.call('rpop', KEYS[1]..KEYS[3])
            redis.call('lpush', KEYS[1]..KEYS[2], value)
            return tonumber(value)
        end
    else
        return -3
    end
else
    return -4
end

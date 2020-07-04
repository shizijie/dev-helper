--[[
    key1 : key(topic)
    key2 : key(topic_queue)
    key3 : key(topic_queue_bak)
    key4 : value
    key5 : status
--]]

local status=redis.call('hget', KEYS[1], KEYS[5])
if status=='1' then
    redis.call('lrem', KEYS[1]..KEYS[2], 0, KEYS[4])
elseif status=='2' then
    redis.call('lrem', KEYS[1]..KEYS[3], 0, KEYS[4])
elseif status=='3' then
    redis.call('lrem', KEYS[1]..KEYS[2], 0, KEYS[4])
else
    return -1
end
return 1
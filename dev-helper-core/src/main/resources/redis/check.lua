--[[
    key1 : key(topic)
    key2 : key(topic_queue)
    key3 : key(topic_queue_bak)
    key4 : status
    key5 : check_status
    key6 : keys*

    -1 : 正在运行中
    -2 : 运行结束，校验结束（无需校验）
    1  : 完全结束，切换成status=1
    2  : offset未消费完，切换成status=1
    3  : queue未消费完，切换成status=2
    4  : queue_bak未消费完，切换成status=3
--]]

if unpack(redis.call('keys', KEYS[6])) then
    return -1
else
    if redis.call('hsetnx', KEYS[1],KEYS[5],1)==1 then
        local offset=redis.call('hget', KEYS[1], KEYS[4])
        local size=redis.call('hget', KEYS[1], KEYS[5])
        if size and offset and tonumber(size)>tonumber(offset) then
            redis.call('hset', KEYS[1], KEYS[4],1)
            return 2
        elseif redis.call('llen', KEYS[1]..KEYS[2])>0 then
            redis.call('hset', KEYS[1], KEYS[4],2)
            return 3
        elseif redis.call('llen', KEYS[1]..KEYS[3])>0 then
            redis.call('hset', KEYS[1], KEYS[4],3)
            return 4
        else
            redis.call('hset', KEYS[1], KEYS[4],1)
            return 1
        end
    end
end
return -2
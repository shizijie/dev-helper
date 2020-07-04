if unpack(redis.call('keys', KEYS[1])) then
    return 1
else
    return -1
end
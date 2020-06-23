local num=0
if redis.call('get', KEYS[1]) then

else
    redis.call('set', KEYS[1], ARGV[1])
    num=redis.call('expire',KEYS[1], KEYS[2])
end
return num
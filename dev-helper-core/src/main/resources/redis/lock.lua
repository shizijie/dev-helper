local num=0
if redis.call('setnx', KEYS[1],ARGV[1])==1 then
    num=redis.call('expire',KEYS[1], KEYS[2])
end
return num
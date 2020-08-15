-- insert
insert into pfw_t_listener (ID,loginName,loginCount,status,done,ip,lastLoginTime,sessionId) values (#{ID},#{loginName},#{loginCount},#{status},#{done},#{ip},#{lastLoginTime},#{sessionId});

-- select
select ID,loginName,loginCount,status,done,ip,lastLoginTime,sessionId from pfw_t_listener;

-- update
update pfw_t_listener set ID = #{ID},loginName = #{loginName},loginCount = #{loginCount},status = #{status},done = #{done},ip = #{ip},lastLoginTime = #{lastLoginTime},sessionId = #{sessionId};
-- insert
insert into pfw_t_authority (ID,name,displayName) values (#{ID},#{name},#{displayName});

-- select
select ID,name,displayName from pfw_t_authority;

-- update
update pfw_t_authority set ID = #{ID},name = #{name},displayName = #{displayName};
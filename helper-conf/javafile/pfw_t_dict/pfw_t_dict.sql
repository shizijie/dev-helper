-- insert
insert into pfw_t_dict (ID,dm,name,sfyx,remark,type,parent) values (#{ID},#{dm},#{name},#{sfyx},#{remark},#{type},#{parent});

-- select
select ID,dm,name,sfyx,remark,type,parent from pfw_t_dict;

-- update
update pfw_t_dict set ID = #{ID},dm = #{dm},name = #{name},sfyx = #{sfyx},remark = #{remark},type = #{type},parent = #{parent};
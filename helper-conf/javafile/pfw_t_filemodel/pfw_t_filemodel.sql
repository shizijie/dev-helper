-- insert
insert into pfw_t_filemodel (ID,path,createDate,creator,title,type,remark) values (#{ID},#{path},#{createDate},#{creator},#{title},#{type},#{remark});

-- select
select ID,path,createDate,creator,title,type,remark from pfw_t_filemodel;

-- update
update pfw_t_filemodel set ID = #{ID},path = #{path},createDate = #{createDate},creator = #{creator},title = #{title},type = #{type},remark = #{remark};
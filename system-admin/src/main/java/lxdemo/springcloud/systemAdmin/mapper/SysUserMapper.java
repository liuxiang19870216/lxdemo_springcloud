package lxdemo.springcloud.systemAdmin.mapper;

import lxdemo.springcloud.systemAdmin.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

@Mapper
public interface SysUserMapper {
	@Select("SELECT * FROM spring_cloud_framework.sys_user where user_name = #{name}")
    SysUser getSysUserByUserName(@Param("name") String name);
}

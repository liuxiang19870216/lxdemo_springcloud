package lxdemo.springcloud.systemAdmin.service;

import lxdemo.springcloud.systemAdmin.entity.SysUser;
import lxdemo.springcloud.systemAdmin.mapper.SysUserMapper;
import lxdemo.springcloud.systemAdmin.req.UserLoginReqVO;
import lxdemo.springcloud.systemAdmin.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class AuthService {
	
	@Autowired
    private SysUserMapper sysUserMapper;

	public String login(@Valid UserLoginReqVO userLoginReqVO) {
		SysUser sysUser = sysUserMapper.getSysUserByUserName(userLoginReqVO.getNickName());
		if(sysUser == null) {
			return null;
		}
		String pass = MD5Util.str2Md5(userLoginReqVO.getPassword() + sysUser.getPasswordSalt());
		if(sysUser.getUserPassword().equals(pass)) {
			return sysUser.getUserId().toString();
		}
		return null;
	}

	public boolean hasPermission(String userId, String apiUrl) {
		// TODO 权限判断接口预留
		return true;
	}

}

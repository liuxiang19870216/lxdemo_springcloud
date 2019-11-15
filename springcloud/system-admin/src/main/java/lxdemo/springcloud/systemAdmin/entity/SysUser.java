package lxdemo.springcloud.systemAdmin.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysUser {
	private Integer userId;
	private String userName;
	private String userPassword;
	private String passwordSalt;
}

/*
 * Project: admin-platform
 * 
 * File Created at 2019年1月10日
 * 
 * Copyright 2016 CMCC Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ZYHY Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license.
 */
package lxdemo.springcloud.systemAdmin.req;

import javax.validation.constraints.NotNull;

/**
 * @author guoyangyong
 * @Type UserLoginReqVO.java
 * @Desc
 * @date 2019年1月10日 下午2:48:49
 */
public class UserLoginReqVO {

	@NotNull(message = "密码不能为空")
    private String password;

	@NotNull(message = "用户名不能为空")
    private String nickName;

	@NotNull(message = "图形验证码不能为空")
    private String imgCode;
    
	@NotNull(message = "图形验证码不能为空")
    private String imgCodeUUID;

    public UserLoginReqVO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgCode() {
        return imgCode;
    }

    public void setImgCode(String imgCode) {
        this.imgCode = imgCode;
    }

	public String getImgCodeUUID() {
		return imgCodeUUID;
	}

	public void setImgCodeUUID(String imgCodeUUID) {
		this.imgCodeUUID = imgCodeUUID;
	}

}


/**
 * Revision history
 * -------------------------------------------------------------------------
 * <p>
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2019年1月10日 cmhi001 creat
 */
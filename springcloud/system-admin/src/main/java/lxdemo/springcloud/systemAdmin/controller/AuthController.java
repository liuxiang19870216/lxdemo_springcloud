package lxdemo.springcloud.systemAdmin.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lxdemo.springcloud.systemAdmin.req.UserLoginReqVO;
import lxdemo.springcloud.systemAdmin.service.AuthService;
import lxdemo.springcloud.systemAdmin.service.JwtService;
import lxdemo.springcloud.systemAdmin.utils.Constants;
import lxdemo.springcloud.systemAdmin.utils.ResMsgUtil;
import lxdemo.springcloud.systemAdmin.utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@RefreshScope
public class AuthController {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtService jwtService;

	@GetMapping("/getCode")
	@ApiOperation(value = "获取图片验证码(有效期为60秒)，返回数据为jpg图片", consumes = "application/x-www-form-urlencoded")
	public void getCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
		String verifyCode = VerifyCodeUtil.generateVerifyCode(4);
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		stringRedisTemplate.opsForHash().put(Constants.REDIS_PREFIX_IMG_CODE_UUID + uuid, "code", verifyCode);
		stringRedisTemplate.expire(Constants.REDIS_PREFIX_IMG_CODE_UUID + uuid, 60, TimeUnit.SECONDS);
		response.addHeader("imgcode-uuid", uuid);
		VerifyCodeUtil.outputImage(214, 80, response.getOutputStream(), verifyCode);
	}

	/**
	 * 登录授权，生成JWT
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	@PostMapping("/login")
	@ApiOperation(value = "登录")
	public Map<String, Object> login(
			@RequestBody @ApiParam(value = "nickName:用户昵称<br/>" + "password:密码</br>" + "imgCode:图形验证码</br>"
					+ "imgCodeUUID:图形验证码uuid") @Valid UserLoginReqVO userLoginReqVO,
			HttpServletRequest request, HttpServletResponse response) {

		String uuid = userLoginReqVO.getImgCodeUUID();
		String sCode = (String) stringRedisTemplate.opsForHash().get(Constants.REDIS_PREFIX_IMG_CODE_UUID + uuid,
				"code");
		if (sCode == null || (!userLoginReqVO.getImgCode().toLowerCase().equals(sCode.toLowerCase()))) {
			return ResMsgUtil.getFailResp("登录失败，验证码错误");
		}
		String userId = authService.login(userLoginReqVO);
		if (userId != null) {
			Map<String, Object> dataMap = jwtService.addJwtToken(userId);
			return ResMsgUtil.getSuccessResp(dataMap);
		}
		return ResMsgUtil.getFailResp("登录失败，用户名或密码错误");
	}
	
	@GetMapping("/logout")
	@ApiOperation(value = "logout")
	public Map<String, Object> logout(HttpServletRequest httpServletReuqest) {
		jwtService.invaldToken(httpServletReuqest.getHeader("Authorization"));
		return ResMsgUtil.getSuccessResp("logout成功");
	}

	/**
	 * 刷新JWT
	 * 
	 * @param refreshToken
	 * @return
	 */
	@GetMapping("/refreshToken")
	@ApiOperation(value = "刷新token")
	public Map<String, Object> refreshToken(@RequestParam String clientId) {
		String newToken = jwtService.refreshJwtToken(clientId);
		if (newToken == null) {
			return ResMsgUtil.getFailResp("刷新token失败");
		}
		return ResMsgUtil.getSuccessResp(newToken);
	}
	
	@GetMapping("/checkPermission")
	@ApiOperation(value = "刷新token")
	public String checkPermission(@RequestParam String token, @RequestParam String apiUrl) {
		String userId = jwtService.verifyJWT(token);
		if (userId == null) {
			return null;
		}
		if(authService.hasPermission(userId, apiUrl)) {
			return userId;
		}
		return "NoPermission";
	}

}
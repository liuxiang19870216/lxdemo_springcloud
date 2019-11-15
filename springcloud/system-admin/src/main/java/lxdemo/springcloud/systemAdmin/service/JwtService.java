package lxdemo.springcloud.systemAdmin.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import lxdemo.springcloud.systemAdmin.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JwtService {
	
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${token.expire.time}")
    private long tokenExpireTime;

    @Value("${refresh.token.expire.time}")
    private long refreshTokenExpireTime;
    
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	public Map<String, Object> addJwtToken(String userId) {
		String token = buildJWT(userId);
		String clientId = UUID.randomUUID().toString().replaceAll("-","");
		String refreshTokenKey = Constants.REDIS_PREFIX_JWT_CLIENT + clientId;
		stringRedisTemplate.opsForHash().put(refreshTokenKey,
		        "token", token);
		stringRedisTemplate.opsForHash().put(refreshTokenKey,
		        "userId", userId);
		stringRedisTemplate.expire(refreshTokenKey,
		        refreshTokenExpireTime, TimeUnit.MILLISECONDS);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("token", token);
		dataMap.put("clientId", clientId);
		return dataMap;
	}
	
	public String refreshJwtToken(String clientId) {
		String clientIdRedis = Constants.REDIS_PREFIX_JWT_CLIENT + clientId;
		String userId = (String) stringRedisTemplate.opsForHash().get(clientIdRedis, "userId");
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		String newToken = buildJWT(userId);
		// 替换当前token，并将旧token添加到黑名单
		String oldToken = (String) stringRedisTemplate.opsForHash().get(clientIdRedis, "token");
		stringRedisTemplate.opsForHash().put(clientIdRedis, "token", newToken);
		stringRedisTemplate.opsForValue().set(Constants.REDIS_PREFIX_JWT_BLACKLIST + oldToken, "", tokenExpireTime,
				TimeUnit.MILLISECONDS);
		return newToken;
	}
	
    private String buildJWT(String userId){
        //生成jwt
        Date now = new Date();
        Algorithm algo = Algorithm.HMAC256(secretKey);
        String token = JWT.create()
                .withIssuer("cmcc")
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + tokenExpireTime))
                .withClaim("userId", userId)//保存身份标识
                .sign(algo);
        return token;
    }
    
    /**
     * JWT验证
     * @param token
     * @return userName
     */
    public String verifyJWT(String token){
    	if(stringRedisTemplate.hasKey(Constants.REDIS_PREFIX_JWT_BLACKLIST + token)) {
    		return null;
    	}
        String userId = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("cmcc")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            userId = jwt.getClaim("userId").asString();
        } catch (JWTVerificationException e){
        	log.error(e.getLocalizedMessage(), e);
            return null;
        }
        return userId;
    }

	public void invaldToken(String clientId) {
		stringRedisTemplate.opsForValue().set(Constants.REDIS_PREFIX_JWT_BLACKLIST + clientId, "", tokenExpireTime,
				TimeUnit.MILLISECONDS);
	}
}

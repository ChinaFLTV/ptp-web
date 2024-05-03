package pfp.fltv.common.utils;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/2 下午 9:23:33
 * @description JWT的工具类库
 * @filename JwtUtils.java
 */

public class JwtUtils {


    private static final String PRIVATE_KEY = "CouldYouPleaseGiveMeATimeForASong?";
    private static final Long ONE_WEEK_DURATION = 7 * 24 * 60 * 60 * 1000L;
    private static final String OBJECT_INFO_KEY = "object_info";
    private static final String OBJECT_JWT_KEY_ID = "ptp-object-jwt";


    /**
     * @param object 待转换为TOKEN的对象
     * @return 加密生成的Token
     * @author Lenovo/LiGuanda
     * @date 2024/4/2 下午 9:24:10
     * @version 1.0.0
     * @description 生成对应对象的Token
     * @filename JwtUtils.java
     */
    public static String encode(Object object) {

        if (object == null) {

            return null;

        }

        Map<String, Object> header = new HashMap<>();
        header.put("type", "jwt");
        header.put("alg", "HS256");

        return JWT.create()
                .withHeader(header)
                .withSubject("ptp-user-login-info")
                .withClaim(OBJECT_INFO_KEY, JSON.toJSONString(object))
                .withIssuer("ISS")
                .withIssuedAt(Instant.now())
                .withExpiresAt(new Date(System.currentTimeMillis() + ONE_WEEK_DURATION))
                .withJWTId(OBJECT_JWT_KEY_ID)
                .sign(Algorithm.HMAC256(PRIVATE_KEY));

    }


    /**
     * @param token 用户传递的Token
     * @return 解密后的文本内容
     * @throws JWTDecodeException 因用户给出的TOKEN格式非法或解码异常而导致的异常
     * @author Lenovo/LiGuanda
     * @date 2024/4/3 下午 7:57:04
     * @version 1.0.0
     * @description 用于根据所给的Token解析出对应的文本内容(不做对象解析处理)
     * @filename JwtUtils.java
     */
    public static String decode(String token) throws JWTDecodeException {

        if (!StringUtils.hasLength(token)) {

            return null;

        }

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(PRIVATE_KEY))
                .withJWTId(OBJECT_JWT_KEY_ID)
                .build()
                .verify(token);

        return decodedJWT.getClaim(OBJECT_INFO_KEY).asString();

    }


}

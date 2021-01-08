package cn.jboost.base.common.util.security;

import cn.hutool.core.bean.BeanUtil;
import cn.jboost.base.common.exception.ExceptionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @Author ronwxy
 * @Date 2020/5/22 18:35
 * @Version 1.0
 */
public class JwtUtil {
    private static Clock clock = DefaultClock.INSTANCE;
    public static final String USER_ID = "uid";
    public static final String USER_TYPE = "uType";
    public static final String USER_DETAILS = "uDetails";

    /**
     * 生成jwt token
     *
     * @param username
     * @param secret
     * @param expire
     * @return
     */
    public static String generateToken(Serializable userId, String username, String userType, String secret, long expire) {
        final Date createdTime = clock.now();
        final Date expiredTime = new Date(createdTime.getTime() + expire);

        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID, userId);
        claims.put(USER_TYPE, userType);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdTime)
                .setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成包含用户详细信息的token
     * @param userType
     * @param jwtUser
     * @param secret
     * @param expire
     * @return
     */
    public static String generateUserToken(String userType, JwtUser jwtUser, String secret, long expire) {
        final Date createdTime = clock.now();
        final Date expiredTime = new Date(createdTime.getTime() + expire);

        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_TYPE, userType);
        claims.put(USER_ID, jwtUser.getId());
        jwtUser.setPassword(null); //脱敏
        claims.put(USER_DETAILS, jwtUser);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(jwtUser.getUsername())
                .setIssuedAt(createdTime)
                .setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    public static JwtUser getJwtUser(String token, String secret) throws JsonProcessingException {
        final Claims claims = getAllClaimsFromToken(token, secret);
        Map<String, Object> valueMap = claims.get(USER_DETAILS, Map.class);
        return BeanUtil.mapToBean(valueMap, JwtUser.class, false);
    }

    public static Serializable getUserId(String token, String secret) {
        final Claims claims = getAllClaimsFromToken(token, secret);
        return claims.get(USER_ID, Serializable.class);
    }

    /**
     * 获取userType
     *
     * @param token
     * @param secret
     * @return
     */
    public static String getUserType(String token, String secret) {
        final Claims claims = getAllClaimsFromToken(token, secret);
        return claims.get(USER_TYPE, String.class);
    }

    /**
     * 获取username
     *
     * @param token
     * @param secret
     * @return
     */
    public static String getUsername(String token, String secret) {
        return getClaim(token, secret, Claims::getSubject);
    }

    /**
     * token是否过期
     *
     * @param token
     * @param secret
     * @return
     */
    public static boolean isTokenExpired(String token, String secret) {
        final Date expiration = getClaim(token, secret, Claims::getExpiration);
        return expiration.before(clock.now());
    }

    /**
     * 验证token是否有效
     *
     * @param token
     * @param secret
     * @param userDetails
     * @return
     */
    public static boolean validateToken(String token, String secret, UserDetails userDetails) {
        //用户被禁用，则立即禁止授权操作
        return userDetails.isEnabled() && !isTokenExpired(token, secret);
    }
    public static boolean validateToken(String token, String secret) {
        //用户被禁用，则立即禁止授权操作
        return  !isTokenExpired(token, secret);
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token, String secret, UserDetails userDetails) {
        if (!canTokenRefreshed(token, secret, userDetails)) {
            ExceptionUtil.rethrowUnauthorizedException("登录凭证无效或已过期，请重新登录");
        }
        final Date createdTime = clock.now();
        final Date expiredTime = getClaim(token, secret, Claims::getExpiration);

        final Claims claims = getAllClaimsFromToken(token, secret);
        claims.setIssuedAt(createdTime);
        claims.setExpiration(expiredTime);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 是否可以刷新token
     *
     * @param token
     * @param secret
     * @param userDetails
     * @return
     */
    private static boolean canTokenRefreshed(String token, String secret, UserDetails userDetails) {
        return validateToken(token, secret, userDetails);
    }

    private static <T> T getClaim(String token, String secret, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token, secret);
        return claimsResolver.apply(claims);
    }

    private static Claims getAllClaimsFromToken(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }


}

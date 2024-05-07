package com.vk.common.core.utils;


import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.constant.TokenConstants;
import com.vk.common.core.text.Convert;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.ZIP.GZIP;


public class TokenUtils {
    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    /**
     * 从id生成令牌
     *
     * @param id 用户id
     * @return 令牌
     */
    public static String createToken(Long id)
    {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.DETAILS_USER_ID,id);
        return createToken(claims);
    }

    /**
     * 从用户名中声明生成令牌
     *
     * @param username 用户名
     * @return 令牌
     */
    public static String createToken(String username)
    {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.DETAILS_USERNAME,username);
        return createToken(claims);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims)
    {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .expiration(new Date(currentTime + TokenConstants.TOKEN_TIME_OUT))
                .issuedAt(new Date(currentTime))
                .subject(TokenConstants.SUBJECT)
                .signWith(getSigningKey(),Jwts.SIG.HS256)
                .compressWith(GZIP)
                .compact();
    }

    /**
     * 签名转Key
     * @return
     */
    public static SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(TokenConstants.SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token)
    {
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (JwtException e) {
            log.error("token 解析失败 : {}",e.getMessage());
        }
        return claims;
    }

    /**
     * 根据令牌获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserId(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }


    /**
     * 根据身份信息获取用户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserId(Claims claims)
    {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据身份信息获取用户名
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getUserName(Claims claims)
    {
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取用户名
     *
     * @param token token
     * @return 用户名
     */
    public static String getUserName(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }
    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key 键
     * @return 值
     */
    public static String getValue(Claims claims, String key)
    {
        return Convert.toStr(claims.get(key), "");
    }

}

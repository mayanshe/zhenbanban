/*
 * Copyright (C) 2025 zhangxihai<mail@sniu.com>，All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * WARNING: This code is licensed under the GPL. Any derivative work or
 * distribution of this code must also be licensed under the GPL. Failure
 * to comply with the terms of the GPL may result in legal action.
 */
package com.zhenbanban.core.infrastructure.util;

import com.zhenbanban.core.shared.exception.UnauthorizedException;
import com.zhenbanban.core.shared.valueobj.JwtSetting;
import com.zhenbanban.core.shared.valueobj.Token;
import com.zhenbanban.core.shared.valueobj.UserClaims;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.Map;

/**
 * Util : Jwt身份验证工具类接口
 *
 * @author zhangxihai 2025/8/5
 */
@Component
@RequiredArgsConstructor
public class JwtAuthUtils<TUser> {
    private final JwtSetting setting;                       // JWT设置

    private final RedisUtils redisUtils;                    // Redis工具类

    private TUser user;                                     // 当前请求的用户信息

    /**
     * 获取当前请求的Token
     *
     * @return 当前请求的Token，如果没有则返回null
     */
    public String getScope() {
        String token = getToken();

        Claims claims = getClaims(token);
        return claims.get("scope", String.class);
    }

    /**
     * 获取当前请求的用户的ID
     *
     * @return 当前请求的用户ID，如果没有则返回null
     */
    public Long getUserId() {
        String token = getToken();

        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    /**
     * 获取当前请求的用户名
     *
     * @return 当前请求的用户名，如果没有则返回null
     */
    public String getUserName() {
        String token = getToken();

        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }

    /**
     * 获取当前请求的Token
     *
     * @return 当前请求的Token，如果没有则返回null
     */
    public String getToken() {
        String requestToken = HttpUtils.getAuthorization();

        if (requestToken.isEmpty() || !requestToken.startsWith(setting.getType())) {
            return null;
        }

        return requestToken.replace(setting.getType(), "").trim();
    }

    /**
     * 验证Token是否有效
     *
     * @param token   JWT Token
     * @param message 错误消息
     */
    private void verifyToken(String token, String message) {
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException(message);
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(setting.getSecret().getBytes());

            // 解析Token并验证签名
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            // 验证Token是否已经作废
            long userId = getUserId();
            String scope = getScope();
            String cacheKey = CacheKeyGenerator.getAccountTokenKey(scope);
            String cachedToken = redisUtils.hget(cacheKey, String.valueOf(userId)).toString();
            if (cachedToken == null || !cachedToken.equals(token)) {
                throw new UnauthorizedException("Token已失效或已被销毁，请重新登录");
            }
        } catch (Exception e) {
            throw new UnauthorizedException(message);
        }
    }

    /**
     * 生成Token
     *
     * @param claims     用户声明信息
     * @param rememberMe 是否记住我模式
     * @return Token对象
     */
    public Token generate(UserClaims claims, boolean rememberMe) {
        SecretKey key = Keys.hmacShaKeyFor(setting.getSecret().getBytes());
        long timestamp = System.currentTimeMillis();                                                                     // 当前时间戳
        long expireAt = timestamp + setting.getExpire() * 60 * 1000;                                                     // Token过期时间戳
        long rememberExpireAt = timestamp + setting.getRememberExpire() * 60 * 1000;                                     // 记住我模式下的Token过期时间
        long refreshExpireAt = timestamp + setting.getRefreshExpire() * 60 * 1000;                                       // 刷新Token过期时间
        long actualExpireAt = rememberMe ? rememberExpireAt : expireAt;                                                  // 实际的Token过期时间
        Date now = new Date();

        Map<String, Object> claimsMap = Map.of(
                "id", claims.getId(),
                "username", claims.getUsername(),
                "scope", claims.getScope()
        );
        String token = Jwts.builder()
                .claims(claimsMap)
                .issuedAt(now)
                .expiration(new Date(actualExpireAt))
                .signWith(key)
                .compact();
        cacheToken(claims.getScope(), claims.getId(), token);

        Map<String, Object> refrhClaimsMapes = Map.of(
                "id", claims.getId(),
                "username", claims.getUsername(),
                "scope", claims.getScope() + "-REFRESH"
        );
        String refreshToken = Jwts.builder()
                .claims(claimsMap)
                .issuedAt(now)
                .expiration(new Date(refreshExpireAt))
                .signWith(key)
                .compact();
        cacheToken(claims.getScope() + "-REFRESH", claims.getId(), refreshToken);

        return new Token(
                setting.getType(),
                token,
                actualExpireAt - timestamp,
                refreshToken,
                setting.getRefreshExpire() * 60 * 1000,
                timestamp
        );
    }

    /**
     * 刷新token
     *
     * @param refreshToken 刷新Token
     */
    public void refresh(String refreshToken) {
        verifyToken(refreshToken, "用户凭证已失效，请重新登录");

        UserClaims userClaims = new UserClaims();
        userClaims.setId(getUserId());
        userClaims.setUsername(getUserName());
        userClaims.setScope(getScope());

        this.generate(userClaims, false);
    }

    private Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(setting.getSecret().getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 销毁登陆Token
     */
    public void clearToken() {
        String token = getToken();
        verifyToken(token, "您尚未登陆或登陆已失效。");
        String scope = getScope();
        long userId = getUserId();

        String cacheKey = CacheKeyGenerator.getAccountTokenKey(scope);
        redisUtils.hdel(cacheKey, String.valueOf(userId));

        String refreshCacheKey = CacheKeyGenerator.getAccountTokenKey(scope + "-REFRESH");
        redisUtils.hdel(refreshCacheKey, String.valueOf(userId));

        String userCacheKey = CacheKeyGenerator.getAuthUserKey(scope, getUserId());
        redisUtils.del(userCacheKey);
    }

    /**
     * 存储登陆token到缓存中
     *
     * @param scope  作用域
     * @param userId 用户ID
     * @param token  登陆Token
     */
    @Async
    private void cacheToken(String scope, long userId, String token) {
        String cacheKey = CacheKeyGenerator.getAccountTokenKey(scope);
        redisUtils.hset(cacheKey, String.valueOf(userId), token);
    }

    /**
     * 验证token
     */
    public void validate() {
        verifyToken(getToken(), "您尚未登陆或登陆已失效.");
    }

    @Async
    public boolean setUser(String token, TUser user) {
        String scope = getClaims(token).get("scope", String.class);
        Long userId = getClaims(token).get("id", Long.class);
        String cacheKey = CacheKeyGenerator.getAuthUserKey(scope, userId);
        return redisUtils.set(cacheKey, user, setting.getExpire() * 60);
    }

    public TUser getUser(Class<TUser> clazz) {
        if (this.user == null) {
            String token = getToken();
            verifyToken(token, "用户凭证已失效，请重新登录1");

            String cacheKey = CacheKeyGenerator.getAuthUserKey(getScope(), getUserId());
            if (cacheKey == null || cacheKey.isEmpty()) {
                throw new UnauthorizedException("用户凭证已失效，请重新登录2");
            }

            TUser user = redisUtils.get(cacheKey, clazz, null);
            if (user == null) {
                throw new UnauthorizedException("用户凭证已失效，请重新登录3");
            }

            Long ttl = redisUtils.getExpire(cacheKey);
            if (ttl != null && ttl <= 0) {
                throw new UnauthorizedException("用户凭证已失效，请重新登录4");
            }

            this.user = user;
        }

        return this.user;
    }

}
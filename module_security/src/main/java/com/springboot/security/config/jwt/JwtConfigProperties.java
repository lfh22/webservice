package com.springboot.security.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gx
 * @since 2017/5/22
 */
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {
    private String headToken;
    private String secret;
    private Integer expiration;
    private String header;

    public String getHeadToken() {
        return headToken;
    }

    public void setHeadToken(String headToken) {
        this.headToken = headToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

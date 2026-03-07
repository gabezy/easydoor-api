package br.com.gabezy.easydoorapi.infra.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@StaticInitSafe
@ConfigMapping(prefix = "jwt")
public interface JwtProperties {

    AccessToken accessToken();

    RefreshToken refreshToken();

    interface AccessToken {
        int ttlSeconds();
    }

    interface RefreshToken {
        int ttlSeconds();
    }


}

package com.argusnft.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtService {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("mysupersecret");

    // https://github.com/auth0/java-jwt
    public Optional<String> createToken(String baseAddress, Optional<String> adaHandle) {
        try {
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("base_address", baseAddress);

            if (adaHandle.isPresent()) {
                builder = builder.withClaim("ada_handle", adaHandle.get());
            }
            return Optional.of(builder.sign(ALGORITHM));
        } catch (JWTCreationException exception) {
            return Optional.empty();
        }
    }

}

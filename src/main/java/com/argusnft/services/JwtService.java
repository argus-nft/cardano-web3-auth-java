package com.argusnft.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtService {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("mysupersecret");

    private static final JWTVerifier verifier = JWT.require(ALGORITHM).withIssuer("auth0").build();

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

    public Optional<DecodedJWT> validate(String token) {
        try {
            return Optional.of(verifier.verify(token));
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }

}

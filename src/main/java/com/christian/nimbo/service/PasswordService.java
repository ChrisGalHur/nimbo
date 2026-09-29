package com.christian.nimbo.service;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PasswordService {

    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    private final SecureRandom secureRandom =
            new SecureRandom();

    //region Hash
    public String hash(String password) {

        byte[] salt =
                new byte[SALT_LENGTH];

        secureRandom.nextBytes(salt);

        byte[] hash =
                deriveKey(
                        password,
                        salt,
                        ITERATIONS
                );

        return ITERATIONS +
                ":" +
                Base64.getEncoder().encodeToString(salt) +
                ":" +
                Base64.getEncoder().encodeToString(hash);
    }
    //endregion

    //region Verify
    public boolean matches(
            String password,
            String storedHash) {

        try {

            String[] parts =
                    storedHash.split(":");

            if (parts.length != 3) {
                return false;
            }

            int iterations =
                    Integer.parseInt(parts[0]);

            byte[] salt =
                    Base64.getDecoder()
                            .decode(parts[1]);

            byte[] expected =
                    Base64.getDecoder()
                            .decode(parts[2]);

            byte[] actual =
                    deriveKey(
                            password,
                            salt,
                            iterations
                    );

            return MessageDigest.isEqual(
                    actual,
                    expected
            );

        } catch (Exception e) {

            return false;
        }
    }
    //endregion

    //region Helpers
    private byte[] deriveKey(
            String password,
            byte[] salt,
            int iterations) {

        try {

            PBEKeySpec spec =
                    new PBEKeySpec(
                            password.toCharArray(),
                            salt,
                            iterations,
                            KEY_LENGTH
                    );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256"
                    );

            return factory
                    .generateSecret(spec)
                    .getEncoded();

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to hash password.",
                    e
            );
        }
    }
    //endregion
}

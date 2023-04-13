package org.example.data;

import lombok.Data;
import org.example.utils.crypt.Cryptographer;
import org.example.request_processing.exceptions.InvalidInteractionKeyException;
import org.example.utils.jwt.JwtUtil;

@Data
public class SecurityData {

    private byte[] secretKey;

    private byte[] interactionKey;

    private final Cryptographer cryptographer;

    private final JwtUtil jwtUtil;

    public SecurityData(Cryptographer cryptographer, String secret, JwtUtil jwtUtil) {
        this.cryptographer = cryptographer;
        this.secretKey = cryptographer.encrypt(secret.getBytes());
        this.jwtUtil = jwtUtil;
    }

    public void setInteractionKey(String secretKey, String interactionKey) throws InvalidInteractionKeyException {

        if(!checkSecretKey(secretKey))
            throw new InvalidInteractionKeyException();

        jwtUtil.setKey(interactionKey.getBytes());
    }

    public boolean checkSecretKey(String secretKey) {

        return secretKey != null && cryptographer.matches(this.secretKey, secretKey);
    }
}
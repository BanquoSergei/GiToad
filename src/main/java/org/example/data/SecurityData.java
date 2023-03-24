package org.example.data;

import lombok.Data;
import org.example.crypt.Cryptographer;
import org.example.exceptions.InvalidInteractionKeyException;
import org.example.utils.JwtUtil;

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

        jwtUtil.setKey(cryptographer.decrypt(interactionKey.getBytes()));
    }

    public boolean checkSecretKey(String secretKey) {

        return new String(cryptographer.decrypt(this.secretKey)).equals(secretKey);
    }
}
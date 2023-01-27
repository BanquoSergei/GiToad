package org.example.data;

import lombok.Data;
import org.example.crypt.Cryptographer;
import org.example.exceptions.InvalidInteractionKeyException;

@Data
public class SecurityData {

    private byte[] secretKey;

    private byte[] interactionKey;

    private Cryptographer cryptographer;

    public SecurityData(Cryptographer cryptographer, String secret) {
        this.cryptographer = cryptographer;
        this.secretKey = cryptographer.encrypt(secret.getBytes());
    }

    public void setInteractionKey(String secretKey, String interactionKey) throws InvalidInteractionKeyException {

        if(!checkSecretKey(secretKey))
            throw new InvalidInteractionKeyException();

        this.interactionKey = cryptographer.encrypt(interactionKey.getBytes());
    }

    public boolean checkSecretKey(String secretKey) {

        return new String(cryptographer.decrypt(this.secretKey)).equals(secretKey);
    }

    public boolean checkInteractionKey(String interactionKey){

        return new String(cryptographer.decrypt(this.interactionKey)).equals(interactionKey);
    }
}
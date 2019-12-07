package com.example.schoolevents.Models;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.exceptions.CognitoInternalErrorException;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser;

import java.util.Date;

public class AccessToken extends UserToken {

    private static final int SECS = 1000;
    /**
     * Constructor.
     *
     * @param token the user token.
     */
    public AccessToken(String token) {
        super(token);
    }

    /**
     * Returns the access token formatted as JWT.
     *
     * @return JWT as a {@link String}.
     */
    public String getJWTToken() {
        return super.getToken();
    }

    /**
     * Returns expiration of this access token.
     *
     * @return access token expiration in UTC as java.util.Date object.
     */
    public Date getExpiration() {
        try {
            final String claim = CognitoJWTParser.getClaim(super.getToken(), "exp");
            if (claim == null) {
                return null;
            }
            final long timeSec = Long.parseLong(claim);
            final long timeMilliSec = timeSec * SECS;
            return new Date(timeMilliSec);
        } catch (final Exception e) {
            throw new CognitoInternalErrorException(e.getMessage());
        }
    }

    /**
     * Returns the username set in the access token.
     * @return Username.
     */
    public String getUsername() {
        return CognitoJWTParser.getClaim(super.getToken(), "username");
    }
}

package com.example.schoolevents.Models;

import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoIdentityProviderClientConfig;

import java.util.Date;

public class UserSession {
    private static final int SECS_CONVERSION = 1000;

    /**
     * Cognito access token.
     */
    private final AccessToken accessToken;

    /**
     * Constructs a new Cognito session.
     *
     * @param accessToken           REQUIRED: Access Token for this session.
     */
    public UserSession(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Returns Access Token.
     *
     * @return token as a String.
     */
    public AccessToken getAccessToken() {
        return accessToken;
    }

    /**
     * Returns if the access and id tokens has not expired.
     *
     * @return boolean to indicate if the access and id tokens has not expired.
     */
    public boolean isValid() {
        final Date currentTimeStamp = new Date();

        try {
            return currentTimeStamp.before(accessToken.getExpiration());
        } catch (final Exception e) {
            return false;
        }
    }

    public boolean isValidForThreshold() {
        try {
            final long currentTime = System.currentTimeMillis()
                    - SDKGlobalConfiguration.getGlobalTimeOffset() * SECS_CONVERSION;
            final long expiresInMilliSeconds = accessToken.getExpiration().getTime() - currentTime;
            return (expiresInMilliSeconds > CognitoIdentityProviderClientConfig.getRefreshThreshold());
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Returns username contained in this session.
     * <p>
     *     Reads the username from Access Tokens.
     *     Returns null on Exceptions - This would mean that the contained tokens are not parsable
     *     and hence are not valid.
     * </p>
     * @return Username of the user to whom these tokens belong.
     */
    public String getUsername() {
        if (this.accessToken != null) {
            try {
                return this.accessToken.getUsername();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}

package org.example.config;

import com.phonepe.sdk.pg.Env;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type App properties.
 */
@ConfigurationProperties(prefix = "spring.payments.phonepe")
public class PhonePeProperties {

    /**
     * @param merchantId
     */
    private final String merchantId;
    /**
     * @param saltKey
     */
    private final String saltKey;
    /**
     * @param saltIndex
     */
    private final Integer saltIndex;
    /**
     * @param env
     */
    private final Env env;
    /**
     * @param shouldPublishEvents
     */
    private final boolean shouldPublishEvents;
    /**
     * @param callbackUrl
     */
    private final String callbackUrl;

    /**
     * Constructor PhonePeProperties.
     * @param theMerchantId
     * @param theEnv
     * @param theSaltIndex
     * @param theSaltKey
     * @param theCallbackUrl
     * @param theShouldPublishEvents
     */
    public PhonePeProperties(final String theMerchantId,
                             final String theSaltKey,
                             final Integer theSaltIndex, final Env theEnv,
                             final boolean theShouldPublishEvents,
                             final String theCallbackUrl) {
        this.merchantId = theMerchantId;
        this.saltKey = theSaltKey;
        this.saltIndex = theSaltIndex;
        this.env = theEnv;
        this.shouldPublishEvents = theShouldPublishEvents;
        this.callbackUrl = theCallbackUrl;
    }

    /**
     * gets the getMerchantId.
     * @return merchantId
     */
    public String getMerchantId() {
        return merchantId;
    }
    /**
     * gets the getSaltKey.
     * @return saltKey
     */
    public String getSaltKey() {
        return saltKey;
    }
    /**
     * gets the getSaltIndex.
     * @return saltIndex
     */
    public Integer getSaltIndex() {
        return saltIndex;
    }
    /**
     * gets the getEnv.
     * @return env
     */
    public Env getEnv() {
        return env;
    }
    /**
     * gets the isShouldPublishEvents.
     * @return shouldPublishEvents
     */
    public boolean isShouldPublishEvents() {
        return shouldPublishEvents;
    }
    /**
     * gets the getCallbackUrl.
     * @return callbackUrl
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }
}

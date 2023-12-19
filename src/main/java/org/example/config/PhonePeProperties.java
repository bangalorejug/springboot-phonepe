package org.example.config;

import com.phonepe.sdk.pg.Env;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type App properties.
 */
@ConfigurationProperties(prefix = "spring.payments.phonepe")
public class PhonePeProperties {

    private final String merchantId;

    private final String saltKey;

    private final Integer saltIndex;

    private final Env env;

    private final boolean shouldPublishEvents;

    private final String callbackUrl;

    public PhonePeProperties(String merchantId, String saltKey,
                             Integer saltIndex, Env env,
                             boolean shouldPublishEvents,
                             String callbackUrl) {
        this.merchantId = merchantId;
        this.saltKey = saltKey;
        this.saltIndex = saltIndex;
        this.env = env;
        this.shouldPublishEvents = shouldPublishEvents;
        this.callbackUrl = callbackUrl;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getSaltKey() {
        return saltKey;
    }

    public Integer getSaltIndex() {
        return saltIndex;
    }

    public Env getEnv() {
        return env;
    }

    public boolean isShouldPublishEvents() {
        return shouldPublishEvents;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}

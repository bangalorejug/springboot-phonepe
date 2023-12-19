package org.example.config;

import com.phonepe.sdk.pg.Env;
import com.phonepe.sdk.pg.payments.v1.PhonePePaymentClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhonePeConfig {
    /**
     * Builds PhonePePaymentClient.
     * @param merchantId
     * @param saltKey
     * @param saltIndex
     * @param shouldPublishEvents
     * @return
     */
    @Bean
    PhonePePaymentClient phonePePaymentClient(
            @Value("${spring.payments.phonepe.merchantId}")
            final String merchantId,
            @Value("${spring.payments.phonepe.saltKey}")
            final String saltKey,
            @Value("${spring.payments.phonepe.saltIndex}")
            final Integer saltIndex,
            @Value("${spring.payments.phonepe.shouldPublishEvents}")
            final boolean shouldPublishEvents) {
        return new PhonePePaymentClient(merchantId, saltKey, saltIndex,
                Env.UAT, shouldPublishEvents);
    }
}

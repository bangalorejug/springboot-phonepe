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
     * @param phonePeProperties
     * @return PhonePePaymentClient
     */
    @Bean
    PhonePePaymentClient phonePePaymentClient(
            final PhonePeProperties phonePeProperties) {
        return new PhonePePaymentClient(phonePeProperties.getMerchantId(),
                phonePeProperties.getSaltKey(), phonePeProperties.getSaltIndex(),
                phonePeProperties.getEnv(), phonePeProperties.isShouldPublishEvents());
    }
}

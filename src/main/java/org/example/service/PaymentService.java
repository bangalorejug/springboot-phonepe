package org.example.service;

import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.PhonePePaymentClient;
import com.phonepe.sdk.pg.payments.v1.models.request.PgPayRequest;
import com.phonepe.sdk.pg.payments.v1.models.response.PayPageInstrumentResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgTransactionStatusResponse;
import org.example.config.PhonePeProperties;
import org.example.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    /**
     * Represents the PhonePe payment client used for processing payments.
     * This client is expected to be initialized only once and remains constant
     * throughout the lifecycle of the class.
     */
    @Autowired
    private PhonePePaymentClient phonepeClient;
    /**
     * Represents the PhonePe payment properties used for processing payments.
     * This client is expected to be initialized only once and remains constant
     * throughout the lifecycle of the class.
     */
    @Autowired
    private PhonePeProperties phonePeProperties;

    /**
     * Initiates payment process by generating a redirect URL for the pay page.
     *                    redirect request. Should not be null.
     * @return A RedirectView pointing to the generated Pay Page URL.
     * @see PgPayRequest
     * @see PgPayResponse
     * @see PayPageInstrumentResponse
     */
    public URL pay(final Payment payment) throws MalformedURLException {

        payment.setTransactionId(UUID.randomUUID().toString());

        PgPayRequest pgPayRequest = PgPayRequest.PayPagePayRequestBuilder()
                .amount(payment.getAmount())
                .merchantUserId(payment.getUserName())
                .merchantId(phonePeProperties.merchantId())
                .merchantTransactionId(payment.getTransactionId())
                .callbackUrl(this.phonePeProperties.callbackUrl())
                .redirectUrl(this.phonePeProperties.callbackUrl())
                .redirectMode("POST")
                .build();

        PhonePeResponse<PgPayResponse> payResponse =
                this.phonepeClient.pay(pgPayRequest);
        PayPageInstrumentResponse payPageInstrumentResponse =
                (PayPageInstrumentResponse) payResponse.getData()
                        .getInstrumentResponse();
        return new URL(payPageInstrumentResponse.getRedirectInfo().getUrl());
    }

    /**
     * Handles the return URL callback for payment notifications.
     * @return The name of the view to be rendered, typically "index".
     */
    public String handlePaymentNotification(final Map<String, String> map) {
        if (map.get("code").equals("PAYMENT_SUCCESS")
                && map.get("merchantId").equals(this.phonePeProperties
                .merchantId())
                && map.containsKey("transactionId")
                && map.containsKey("providerReferenceId")) {
            PhonePeResponse<PgTransactionStatusResponse> statusResponse
                    = this.phonepeClient.checkStatus(map.get("transactionId"));
            return statusResponse.getData().toString();
        }
        return "My Title";
    }
}

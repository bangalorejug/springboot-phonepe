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
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {

    private final Map<String, Payment> payments ;

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

    public PaymentService() {
        this.payments = new ConcurrentHashMap<>();
    }

    /**
     * Initiates payment process by generating a redirect URL for the pay page.
     *                    redirect request. Should not be null.
     * @return A RedirectView pointing to the generated Pay Page URL.
     * @param payment
     * @see PgPayRequest
     * @see PgPayResponse
     * @see PayPageInstrumentResponse
     */
    public URL pay(final Payment payment) throws MalformedURLException {

        payment.setTransactionId(UUID.randomUUID().toString());

        this.payments.put(payment.getTransactionId(), payment);

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
     * @param code
     * @param merchantId
     * @param providerReferenceId
     * @param transactionId
     * @return The name of the view to be rendered, typically "index".
     */
    public Object getStatus(final String code,
                            final String transactionId,
                            final String merchantId,
                            final String providerReferenceId,
                            final long amount) {
        if (code.equals("PAYMENT_SUCCESS")
                && merchantId.equals(this.phonePeProperties.merchantId())
                && transactionId != null
                && providerReferenceId != null) {
            Payment payment = this.payments.get(transactionId);
            if (amount == payment.getAmount()) {
                PhonePeResponse<PgTransactionStatusResponse> statusResponse
                        = this.phonepeClient.checkStatus(transactionId);
                return statusResponse.getData();
            } else {
                throw new IllegalArgumentException("Payment invalid " + transactionId);
            }

        }
        return null;
    }
}

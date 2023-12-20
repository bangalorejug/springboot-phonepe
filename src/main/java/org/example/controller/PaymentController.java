package org.example.controller;

import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.PhonePePaymentClient;
import com.phonepe.sdk.pg.payments.v1.models.request.PgPayRequest;
import com.phonepe.sdk.pg.payments.v1.models.response.PayPageInstrumentResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgTransactionStatusResponse;
import org.example.config.PhonePeProperties;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Controller
public class PaymentController {

    /**
     *  The max merchant transaction id characters.
     */
    private static final int MAX_MERCHANT_TRANSACTION_ID = 34;
    /**
     *  The default amount for a transaction.
     */
    private static final long DEFAULT_AMOUNT = 100;

    /**
     * Represents the PhonePe payment client used for processing payments.
     * This client is expected to be initialized only once and remains constant
     * throughout the lifecycle of the class.
     */
    private final PhonePePaymentClient phonepeClient;
    /**
     * Represents the PhonePe payment properties used for processing payments.
     * This client is expected to be initialized only once and remains constant
     * throughout the lifecycle of the class.
     */
    private final PhonePeProperties phonePeProperties;

    /**
     * Constructs a new PaymentController with the specified
     * PhonePePaymentClient and PhonePeProperties.
     *
     * @param thePhonepeClient      The PhonePePaymentClient used for processing
     *                              payments.Should not be null.
     * @param thePhonePeProperties  The PhonePeProperties containing
     *                              configuration settings for the PhonePe
     *                              integration. Should not be null.
     */
    PaymentController(final PhonePePaymentClient thePhonepeClient,
                      final PhonePeProperties thePhonePeProperties) {
        this.phonepeClient = thePhonepeClient;
        this.phonePeProperties = thePhonePeProperties;
    }

    /**
     * Initiates payment process by generating a redirect URL for the pay page.
     * @param attributes The RedirectAttributes used to add attributes to the
     *                    redirect request. Should not be null.
     * @return A RedirectView pointing to the generated Pay Page URL.
     * @see PgPayRequest
     * @see PgPayResponse
     * @see PayPageInstrumentResponse
     */
    @GetMapping(value = "/pay")
    public RedirectView pay(final RedirectAttributes attributes) {
        String merchantTransactionId = UUID.randomUUID().toString()
                .substring(0, MAX_MERCHANT_TRANSACTION_ID);
        long amount = DEFAULT_AMOUNT;
        String merchantUserId = "MUID123";
        PgPayRequest pgPayRequest = PgPayRequest.PayPagePayRequestBuilder()
                .amount(amount)
                .merchantId(phonePeProperties.merchantId())
                .merchantTransactionId(merchantTransactionId)
                .callbackUrl(this.phonePeProperties.callbackUrl())
                .redirectUrl(this.phonePeProperties.callbackUrl())
                .redirectMode("POST")
                .merchantUserId(merchantUserId)
                .build();

        PhonePeResponse<PgPayResponse> payResponse =
                                        this.phonepeClient.pay(pgPayRequest);
        PayPageInstrumentResponse payPageInstrumentResponse =
                (PayPageInstrumentResponse) payResponse.getData()
                                                  .getInstrumentResponse();
        String url = payPageInstrumentResponse.getRedirectInfo().getUrl();
        return new RedirectView(url);
    }
    /**
     * Handles the return URL callback for payment notifications.
     * @param model      The Model object for adding attributes to the view.
     *                   Should not be null.
     * @param httpEntity The HttpEntity representing the HTTP request entity.
     *                   Should not be null.
     * @return The name of the view to be rendered, typically "index".
     * @see PgTransactionStatusResponse
     */
    @RequestMapping(value = "/pay-return-url")
    public String paymentNotification(final Model model,
                                      final HttpEntity<String> httpEntity) {
        Map<String, String> map = getMap(httpEntity);

        if (map.get("code").equals("PAYMENT_SUCCESS")
                && map.get("merchantId").equals(this.phonePeProperties
                .merchantId())
                && map.containsKey("transactionId")
                && map.containsKey("providerReferenceId")) {
            PhonePeResponse<PgTransactionStatusResponse> statusResponse
                    = this.phonepeClient.checkStatus(map.get("transactionId"));
            model.addAttribute("title", statusResponse.getData().toString());
            return "index";
        }

        model.addAttribute("title", "My Title");
        return "index";
    }

    /**
     * Handles the get Map.
     * @param httpEntity The HttpEntity representing the HTTP request entity.
     *                   Should not be null.
     * @return data.
     */
    public static Map<String, String> getMap(final HttpEntity<String>
                                                         httpEntity) {

        HashMap<String, String> data = new HashMap<>();

        final String[] arrParameters = Objects.requireNonNull(httpEntity
                        .getBody())
                        .split("&");
        for (final String tempParameterString : arrParameters) {

            final String[] arrTempParameter = tempParameterString
                    .split("=");

            if (arrTempParameter.length >= 2) {
                final String parameterKey = arrTempParameter[0];
                final String parameterValue = arrTempParameter[1];
                data.put(parameterKey, parameterValue);
            } else {
                final String parameterKey = arrTempParameter[0];
                data.put(parameterKey, "");
            }
        }
        return data;
    }

}

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

    private final PhonePePaymentClient phonepeClient;
    private final PhonePeProperties phonePeProperties;

    PaymentController(PhonePePaymentClient phonepeClient,
                      final PhonePeProperties phonePeProperties) {
        this.phonepeClient = phonepeClient;
        this.phonePeProperties = phonePeProperties ;
    }

    @GetMapping(value = "/pay")
    public RedirectView pay(RedirectAttributes attributes) {
        String merchantTransactionId = UUID.randomUUID().toString().substring(0,34);
        long amount = 100;
        String merchantUserId = "MUID123";
        PgPayRequest pgPayRequest = PgPayRequest.PayPagePayRequestBuilder()
                .amount(amount)
                .merchantId(phonePeProperties.getMerchantId())
                .merchantTransactionId(merchantTransactionId)
                .callbackUrl(this.phonePeProperties.getCallbackUrl())
                .redirectUrl(this.phonePeProperties.getCallbackUrl())
                .redirectMode("POST")
                .merchantUserId(merchantUserId)
                .build();

        PhonePeResponse<PgPayResponse> payResponse = this.phonepeClient.pay(pgPayRequest);
        PayPageInstrumentResponse payPageInstrumentResponse = (PayPageInstrumentResponse) payResponse.getData().getInstrumentResponse();
        String url = payPageInstrumentResponse.getRedirectInfo().getUrl();
        return new RedirectView(url);
    }

    @RequestMapping(value = "/pay-return-url")
    public String paymentNotification(final Model model, HttpEntity<String> httpEntity) {
        Map<String, String> map = getMap(httpEntity);

        if (map.get("code").equals("PAYMENT_SUCCESS")
                && map.get("merchantId").equals(this.phonePeProperties.getMerchantId())
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

    public static Map<String, String> getMap(HttpEntity<String> httpEntity) {

        HashMap<String, String> data = new HashMap<>();

        final String[] arrParameters = Objects.requireNonNull(httpEntity.getBody())
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

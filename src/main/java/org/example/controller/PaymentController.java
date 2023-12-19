package org.example.controller;

import com.phonepe.sdk.pg.Env;
import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.PhonePePaymentClient;
import com.phonepe.sdk.pg.payments.v1.models.request.PgPayRequest;
import com.phonepe.sdk.pg.payments.v1.models.response.PayPageInstrumentResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgTransactionStatusResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class PaymentController {

    private final PhonePePaymentClient phonepeClient;
    private final String merchantId;

    PaymentController(PhonePePaymentClient phonepeClient,
                      @Value("${spring.payments.phonepe.merchantId}")
                      final String merchantId) {
        this.phonepeClient = phonepeClient;
        this.merchantId = merchantId ;
    }

    @GetMapping(value = {"/", "/index"})
    public String index(final Model model) {
        model.addAttribute("title", "My Title");
        return "index";
    }

    @GetMapping(value = "/pay")
    public RedirectView pay(RedirectAttributes attributes) {

        String merchantTransactionId = UUID.randomUUID().toString().substring(0,34);
        long amount = 100;
        String callbackurl = "http://localhost:8080/pay-return-url";
        String merchantUserId = "MUID123";

        PgPayRequest pgPayRequest = PgPayRequest.PayPagePayRequestBuilder()
                .amount(amount)
                .merchantId(merchantId)
                .merchantTransactionId(merchantTransactionId)
                .callbackUrl(callbackurl)
                .redirectUrl(callbackurl)
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
        Map<String, String> map = getAsMap(httpEntity);

        if (map.get("code").equals("PAYMENT_SUCCESS")
                && map.containsKey("merchantId")
                && map.containsKey("transactionId")
                && map.containsKey("providerReferenceId")) {

            PhonePeResponse<PgTransactionStatusResponse> statusResponse = this.phonepeClient.checkStatus(map.get("transactionId"));


            model.addAttribute("title", statusResponse.getData().toString());
            return "index";
        }

        model.addAttribute("title", "My Title");
        return "index";
    }

    public static HashMap<String, String> getAsMap(HttpEntity<String> httpEntity) {

        HashMap<String, String> data = new HashMap<>();

        final String[] arrParameters = httpEntity.getBody().split("&");
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

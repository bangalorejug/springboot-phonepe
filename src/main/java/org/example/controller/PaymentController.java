package org.example.controller;

import com.phonepe.sdk.pg.Env;
import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.PhonePePaymentClient;
import com.phonepe.sdk.pg.payments.v1.models.request.PgPayRequest;
import com.phonepe.sdk.pg.payments.v1.models.response.PayPageInstrumentResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.UUID;

@Controller
public class PaymentController {

    @GetMapping(value = {"/", "/index"})
    public String index(final Model model) {
        model.addAttribute("title", "My Title");
        return "index";
    }

    @GetMapping(value = "/pay")
    public RedirectView pay(RedirectAttributes attributes) {
        String merchantId = "PGTESTPAYUAT";
        String saltKey = "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399";
        Integer saltIndex = 1;
        Env env = Env.UAT;
        boolean shouldPublishEvents = true;
        PhonePePaymentClient phonepeClient = new PhonePePaymentClient(merchantId, saltKey, saltIndex, env, shouldPublishEvents);

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

        PhonePeResponse<PgPayResponse> payResponse = phonepeClient.pay(pgPayRequest);
        PayPageInstrumentResponse payPageInstrumentResponse = (PayPageInstrumentResponse) payResponse.getData().getInstrumentResponse();
        String url = payPageInstrumentResponse.getRedirectInfo().getUrl();
        return new RedirectView(url);
    }

    @RequestMapping(value = "/pay-return-url")
    public String paymentNotification(final Model model, HttpEntity<String> httpEntity) {
        model.addAttribute("title", "My Title");
        return "index";
    }

}

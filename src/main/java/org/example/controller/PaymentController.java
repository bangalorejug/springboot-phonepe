package org.example.controller;

import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class PaymentController {

    /**
     * Represents the PhonePe payment service used for processing payments.
     * This client is expected to be initialized only once and remains constant
     * throughout the lifecycle of the class.
     */
    @Autowired
    private PaymentService paymentService;

    /**
     * Initiates payment process by generating a redirect URL for the pay page.
     * @param attributes The RedirectAttributes used to add attributes to the
     *                    redirect request. Should not be null.
     * @return A RedirectView pointing to the generated Pay Page URL.
     */
    @GetMapping(value = "/pay")
    public RedirectView pay(final RedirectAttributes attributes) {
        String url = paymentService.payAmount();
        return new RedirectView(url);
    }
    /**
     * Handles the return URL callback for payment notifications.
     * @param model      The Model object for adding attributes to the view.
     *                   Should not be null.
     * @param httpEntity The HttpEntity representing the HTTP request entity.
     *                   Should not be null.
     * @return The name of the view to be rendered, typically "index".
     */
    @RequestMapping(value = "/pay-return-url")
    public String paymentNotification(final Model model,
                                      final HttpEntity<String> httpEntity) {
        Map<String, String> map = getMap(httpEntity);

        String title = paymentService.handlePaymentNotification(map);

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

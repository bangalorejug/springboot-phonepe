package org.example.controller;

import org.example.model.Payment;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.net.MalformedURLException;

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
     *  The default amount for a transaction.
     */
    private static final long DEFAULT_AMOUNT = 100;

    /**
     * Initiates payment process by generating a redirect URL for the pay page.
     * @param attributes The RedirectAttributes used to add attributes to the
     *                    redirect request. Should not be null.
     * @return A RedirectView pointing to the generated Pay Page URL.
     */
    @GetMapping(value = "/pay")
    public RedirectView pay(final RedirectAttributes attributes)
                                     throws MalformedURLException {
        Payment payment = new Payment();
        payment.setUserName("Sathish");
        payment.setAmount(DEFAULT_AMOUNT);
        return new RedirectView(paymentService.pay(payment).toString());
    }
    /**
     * Handles the return URL callback for payment notifications.
     * @param model      The Model object for adding attributes to the view.
     *                   Should not be null.
     * @param code
     * @param merchantId
     * @param providerReferenceId
     * @param transactionId
     * @return The name of the view to be rendered, typically "index".
     */
    @RequestMapping(value = "/pay-return-url")
    public String paymentNotification(final Model model,
                                      @RequestParam(name = "code") final String code,
                                      @RequestParam(name = "transactionId") final String transactionId,
                                      @RequestParam(name = "merchantId") final String merchantId,
                                      @RequestParam(name = "providerReferenceId")
                                          final String providerReferenceId,
                                      @RequestParam(name = "amount") final long amount,
                                      HttpEntity<String> httpEntity) {

        model.addAttribute("response", paymentService.getStatus(code,
                                          transactionId, merchantId,
                                          providerReferenceId, amount));
        return "payment_success";
    }

}

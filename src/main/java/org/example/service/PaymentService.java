/**
 * Info about this package doing something for package-info.java file.
 */
package org.example.service;

import org.example.model.Payment;

import java.net.URL;
import java.util.Map;

public interface PaymentService {

    /**
     * Pay amount method.
     * @return url theUrl.
     */
    URL pay(Payment payment);

    /**
     * handling payment notifications method.
     * @param map
     * @return status of payment.
     */
    String handlePaymentNotification(Map<String, String> map);
}

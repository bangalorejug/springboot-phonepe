# Spring Boot Payment Gateway Integration
Spring Boot PhonePe Payment Gateway Integration Example

## How it works
1. User initiates a payment to Our Application
2. Our Application Calls [PhonePe Pay API](https://developer.phonepe.com/v1/reference/pay-api/) with Transaction ID and Call Me Back with CallbackURL.
2. PhonePe returns a payment Collector URL Our Application redirects the User to the Collector URL
4. User enters his Payment Details and Submit to PhonePe.
5. PhonePe calls our application the CallbackURL that we gave on Step 1 with transaction id 
6. Our Application Can now check the status ofd the payment with [Status API](https://developer.phonepe.com/v1/reference/check-status-api-1).

## References
1. https://github.com/Code-180/PhonePe-Nodejs-Integration
2. https://www.baeldung.com/spring-redirect-and-forward

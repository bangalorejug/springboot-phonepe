# Spring Boot Payment Gateway Integration
Spring Boot PhonePe Payment Gateway Integration Example

## How it works
1. User initiates a payment to PhonePe Pay API with Call Me Back URL.
2. PhonePe returns a payment Collector URL
3. Our Application redirects the User to the Collector URL
4. User enters  his Payment Details and Submit to PhonePe.
5. PhonePe calls our application the Call Me Back URL that we gave on Step 1 with transaction id 
6. Our Application Can now check the status ofd the payment with Status API.

## References
1. https://github.com/Code-180/PhonePe-Nodejs-Integration
2. https://www.baeldung.com/spring-redirect-and-forward

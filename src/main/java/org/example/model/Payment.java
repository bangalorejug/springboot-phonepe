package org.example.model;

import java.time.LocalDateTime;

public class Payment {

    /**
     * String Variable transactionId.
     */
    private String transactionId;
    /**
     * String Variable userName.
     */
    private String userName;
    /**
     * long Variable amount.
     */
    private long amount;

    private String status;

    private String response;

    private LocalDateTime createdAt;
    /**
     * get Transaction Id.
     * @return transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }
    /**
     * set Transaction Id.
     * @param theTransactionId
     */
    public void setTransactionId(final String theTransactionId) {
        this.transactionId = theTransactionId;
    }
    /**
     * get Username.
     * @return userName
     */
    public String getUserName() {
        return userName;
    }
    /**
     * set Username.
     * @param theUserName
     */
    public void setUserName(final String theUserName) {
        this.userName = theUserName;
    }
    /**
     * get amount.
     * @return amount
     */
    public long getAmount() {
        return amount;
    }
    /**
     * set amount.
     * @param anAmount
     */
    public void setAmount(final long anAmount) {
        this.amount = anAmount;
    }
}

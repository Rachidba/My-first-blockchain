package io.rachidba.model;

import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey recipient; // Also known as the new owner of these coins
    public float value;
    public String parentTransaction; // The id of the transaction this output was created in

    // Constructor
    public TransactionOutput(PublicKey recipient, float value, String parentTransaction) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransaction = parentTransaction;
    }

    // check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == this.recipient);
    }

}

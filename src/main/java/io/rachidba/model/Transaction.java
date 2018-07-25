package io.rachidba.model;

import io.rachidba.App;
import io.rachidba.util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {

    public String transactionId; // This is also the hash of the transaction
    public PublicKey sender; // Senders address
    public PublicKey recipient; // Recipients address
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; // a rough count of how many transactions have been generated

    // Constructor
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    // This calculates the transaction hash (wish will be used as its Id)
    private String calculateHash() {
        this.sequence++; // Increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
            StringUtil.getStringFromKey(this.sender) +
                    StringUtil.getStringFromKey(this.recipient) +
                    Float.toString(this.value) +
                    this.sequence
        );
    }

    // Signs all the data we dont wish to be tamped with
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) +
                StringUtil.getStringFromKey(recipient) +
                Float.toString(value);
        this.signature = StringUtil.applyECDSASig(privateKey, data);
    }

    // verifies the data we signed hasnt been tampered with
    public boolean verifiySignature() {
        String data =  StringUtil.getStringFromKey(this.sender) +
                StringUtil.getStringFromKey(this.recipient) +
                Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    // Return true if new transaction could be created
    public boolean processTransaction() {
        if (this.verifiySignature() == false) {
            System.out.println("#Transaction signature failed to verify");
            return false;
        }
        // Gather transaction inputs (Make sure they are unspent
        for (TransactionInput input: inputs) {
            input.UTXO = App.UTXOs.get(input.transactionOutputId);
        }
        // Check if transaction is valid
        // if (getInputsValue())

        return true;
    }

    // Return sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        for (TransactionInput input: inputs) {
            if (input.UTXO == null) continue;
            total += input.UTXO.value;
        }
        return total;
    }

    // Return sum of outputs value
    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput output: outputs) {
            total += output.value;
        }
        return total;
    }

}

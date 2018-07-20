package io.rachidba.model;

import io.rachidba.util.StringUtil;

import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data; // Our data will be a simpme message
    private long timeStamp; // As number of milliseconds since 1/1/1970

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    private String calculateHash() {
        String calculatedHash = StringUtil.applySha256(
                this.previousHash +
                        Long.toString(timeStamp) +
                        data
                    );
        return calculatedHash;
    }
}

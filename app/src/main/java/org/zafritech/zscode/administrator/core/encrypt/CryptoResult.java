package org.zafritech.zscode.administrator.core.encrypt;

public class CryptoResult {

    private String vector;

    private String cypher;

    public CryptoResult(String vector, String cypher) {

        this.vector = vector;
        this.cypher = cypher;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public String getCypher() {
        return cypher;
    }

    public void setCypher(String cypher) {
        this.cypher = cypher;
    }
}

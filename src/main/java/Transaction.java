package main.java;//import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.Instant;

/**
 * The Transaction class is a JavaBean that represents transactions. Each transaction contains a payer, points, and timestamp for the transaction.
 */
public class Transaction {
    private String payer;
    private int points;
    private Instant timestamp;

    public Transaction() {

    }

    public Transaction(String payer, int points, Instant timeStamp) {
        this.payer = payer;
        this.points = points;
        this.timestamp = timeStamp;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "payer='" + payer + '\'' +
                ", points=" + points +
                ", timeStamp=" + timestamp +
                '}';
    }

    public boolean valid() {
        if (payer == null || payer.isEmpty()) {
            return false;
        }

        if (timestamp == null) {
            return false;
        }

        return true;
    }

}

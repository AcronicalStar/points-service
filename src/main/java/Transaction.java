package main.java;

import java.time.Instant;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return points == that.points && payer.equals(that.payer) && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payer, points, timestamp);
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

package main.java;

import java.util.Objects;

/**
 * The PointsAfterSpending class is a JavaBean that contains the payer and points. It is used to store the number of
 * points left after the spendPoints endpoint is called.
 */
public class PointsAfterSpending {
    private String payer;
    private int points;

    public PointsAfterSpending(String payer, int points) {
        this.payer = payer;
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointsAfterSpending pointsAfterSpending = (PointsAfterSpending) o;
        return points == pointsAfterSpending.points && payer.equals(pointsAfterSpending.payer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payer, points);
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

    @Override
    public String toString() {
        return "Points{" +
                "payer='" + payer + '\'' +
                ", points=" + points +
                '}';
    }
}

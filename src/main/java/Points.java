package main.java;

/**
 * The Points class is a JavaBean that represents points.
 */
public class Points {
    private int points;

    public Points() {

    }

    public Points(int points) {
        this.points = points;
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
                "points=" + points +
                '}';
    }
}

package test.java;

import main.java.PointsAfterSpending;
import main.java.PointsManager;
import main.java.Transaction;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertThrows;

import java.time.Instant;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

class PointsManagerTest {

    public static final String DANNON = "DANNON";
    public static final String UNILEVER = "UNILEVER";
    public static final String MILLER_COORS = "MILLER COORS";
    private PointsManager pointsManager = new PointsManager();
    private int points;

    @BeforeEach
    void setUp() {
        Instant instant = Instant.parse("2020-11-02T14:00:00Z");
        Instant instant2 = Instant.parse("2020-10-31T11:00:00Z");
        Instant instant3 = Instant.parse("2020-10-31T15:00:00Z");
        Instant instant4 = Instant.parse("2020-11-01T14:00:00Z");
        Instant instant5 = Instant.parse("2020-10-31T10:00:00Z");

        Transaction transaction = new Transaction(DANNON, 1000, instant);
        Transaction transaction2 = new Transaction(UNILEVER, 200, instant2);
        Transaction transaction3 = new Transaction(DANNON, -200, instant3);
        Transaction transaction4 = new Transaction(MILLER_COORS, 10000, instant4);
        Transaction transaction5 = new Transaction(DANNON, 300, instant5);

        pointsManager.addTransaction(transaction);
        pointsManager.addTransaction(transaction2);
        pointsManager.addTransaction(transaction3);
        pointsManager.addTransaction(transaction4);
        pointsManager.addTransaction(transaction5);

        points = 5000;
    }

    @AfterEach
    void tearDown() {
        points = 0;
    }

    @Test
    void spendPoints() {
        // prepare expected data
        List<PointsAfterSpending> expectedPoints = new ArrayList<>();

        expectedPoints.add(new PointsAfterSpending(DANNON, -100));
        expectedPoints.add(new PointsAfterSpending(UNILEVER, -200));
        expectedPoints.add(new PointsAfterSpending(MILLER_COORS, -4700));
        List<PointsAfterSpending> actualPointsList = pointsManager.spendPoints(points);
        assertThat(actualPointsList, contains(expectedPoints.toArray()));
    }

    @Test
    void spendNegativePoints() {
        List<PointsAfterSpending> actualPointsList = pointsManager.spendPoints(-points);
        assertThat(actualPointsList.isEmpty(), is(true));
    }

    @Test
    void spendZeroPoints() {
        List<PointsAfterSpending> actualPointsList = pointsManager.spendPoints(0);
        assertThat(actualPointsList.isEmpty(), is(true));
    }

    @Test
    void getPayersPointBalance() {
        // prepare expected data
        Map<String, Integer> expectedPayersPointBalance = new HashMap<>();

        expectedPayersPointBalance.put(DANNON, 1000);
        expectedPayersPointBalance.put(UNILEVER, 0);
        expectedPayersPointBalance.put(MILLER_COORS, 5300);
        Map<String, Integer> actualPayersPointBalance = pointsManager.getPayersPointBalance();
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(DANNON, 1100));
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(UNILEVER, 200));
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(MILLER_COORS, 10000));

        // now spend points
        pointsManager.spendPoints(points);
        actualPayersPointBalance = pointsManager.getPayersPointBalance();

        // assert to ensure that points have been taken away
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(DANNON, 1000));
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(UNILEVER, 0));
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(MILLER_COORS, 5300));

        // now spend points again
        pointsManager.spendPoints(points);
        actualPayersPointBalance = pointsManager.getPayersPointBalance();

        // assert to ensure that points have been taken away
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(DANNON, 1000));
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(UNILEVER, 0));
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(MILLER_COORS, 300));

        // now spend points again and make sure IllegalArgumentException is thrown
        spendAndAssert(points);
    }

    @Test
    void exceedAvailablePoints() {
        spendAndAssert(20000);
    }

    private void spendAndAssert(int points) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pointsManager.spendPoints(points);
        });

        String expectedMessage = "Points to spend has exceeded available points";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getPayersPointBalanceNoTransactions() {
//        Map<String, Integer> actualPayersPointBalance = pointsManager.getPayersPointBalance(Collections.emptyList());
//        assertThat(actualPayersPointBalance.isEmpty(), is(true));
    }

}
package test.java;

import main.java.PointsAfterSpending;
import main.java.PointsManager;
import main.java.Transaction;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

class PointsManagerTest {

    public static final String DANNON = "DANNON";
    public static final String UNILEVER = "UNILEVER";
    public static final String MILLER_COORS = "MILLER COORS";
    private PointsManager pointsManager = new PointsManager();
    private List<Transaction> transactions = new ArrayList<>();
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

        transactions.add(transaction);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);

        points = 5000;
    }

    @AfterEach
    void tearDown() {
        transactions.clear();
        points = 0;
    }

    @Test
    void spendPoints() {
        // prepare expected data
        List<PointsAfterSpending> expectedPoints = new ArrayList<>();

        expectedPoints.add(new PointsAfterSpending(DANNON, -100));
        expectedPoints.add(new PointsAfterSpending(UNILEVER, -200));
        expectedPoints.add(new PointsAfterSpending(MILLER_COORS, -4700));
        List<PointsAfterSpending> actualPointsList = pointsManager.spendPoints(points, transactions);
        assertThat(actualPointsList, contains(expectedPoints.toArray()));
    }

    @Test
    void spendNegativePoints() {
        List<PointsAfterSpending> actualPointsList = pointsManager.spendPoints(-points, transactions);
        assertThat(actualPointsList.isEmpty(), is(true));
    }

    @Test
    void spendZeroPoints() {
        List<PointsAfterSpending> actualPointsList = pointsManager.spendPoints(0, transactions);
        assertThat(actualPointsList.isEmpty(), is(true));
    }

    @Test
    void getPayersPointBalance() {
        // prepare expected data
        Map<String, Integer> expectedPayersPointBalance = new HashMap<>();

        expectedPayersPointBalance.put(DANNON, 1000);
        expectedPayersPointBalance.put(UNILEVER, 0);
        expectedPayersPointBalance.put(MILLER_COORS, 5300);
        Map<String, Integer> actualPayersPointBalance = pointsManager.getPayersPointBalance(transactions);
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(DANNON, 1100));
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(UNILEVER, 200));
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(MILLER_COORS, 10000));

        // now spend points
        pointsManager.spendPoints(points, transactions);
        actualPayersPointBalance = pointsManager.getPayersPointBalance(transactions);

        // assert to ensure that points have been taken away
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(DANNON, 1000));
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(UNILEVER, 0));
        assertThat(actualPayersPointBalance, IsMapContaining.hasEntry(MILLER_COORS, 5300));
    }

    @Test
    void getPayersPointBalanceNoTransactions() {
        Map<String, Integer> actualPayersPointBalance = pointsManager.getPayersPointBalance(Collections.emptyList());
        assertThat(actualPayersPointBalance.isEmpty(), is(true));
    }

}
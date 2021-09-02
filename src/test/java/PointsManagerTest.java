package test.java;

import main.java.PointsSpentByPayer;
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
import static org.junit.Assert.assertThrows;
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

        Transaction transaction5 = new Transaction(DANNON, 300, instant5);
        Transaction transaction2 = new Transaction(UNILEVER, 200, instant2);
        Transaction transaction3 = new Transaction(DANNON, -200, instant3);
        Transaction transaction4 = new Transaction(MILLER_COORS, 10000, instant4);
        Transaction transaction = new Transaction(DANNON, 1000, instant);

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
        List<PointsSpentByPayer> expectedPoints = new ArrayList<>();

        expectedPoints.add(new PointsSpentByPayer(DANNON, -100));
        expectedPoints.add(new PointsSpentByPayer(UNILEVER, -200));
        expectedPoints.add(new PointsSpentByPayer(MILLER_COORS, -4700));
        List<PointsSpentByPayer> actualPointsList = pointsManager.spendPoints(points);
        assertThat(actualPointsList, contains(expectedPoints.toArray()));
    }

    @Test
    void addDuplicateTransaction() {
        Instant instant2 = Instant.parse("2020-10-31T11:00:00Z");
        Transaction transaction2 = new Transaction(UNILEVER, 200, instant2);
        assertThat(pointsManager.getTransactions().size(), is(5));
        pointsManager.addTransaction(transaction2);
        assertThat(pointsManager.getTransactions().size(), is(6));
    }

    @Test
    void spend200Points_ensureDannonIsNotSpent() {
        pointsManager.getTransactions().clear();

        Instant instant = Instant.parse("2020-11-02T14:00:00Z");
        Instant instant2 = Instant.parse("2020-10-31T11:00:00Z");
        Instant instant3 = Instant.parse("2020-10-31T15:00:00Z");
        Instant instant4 = Instant.parse("2020-11-01T14:00:00Z");
        Instant instant5 = Instant.parse("2020-10-31T10:00:00Z");


        Transaction transaction5 = new Transaction(DANNON, 200, instant5);
        Transaction transaction2 = new Transaction(UNILEVER, 200, instant2);
        Transaction transaction3 = new Transaction(DANNON, -200, instant3);
        Transaction transaction4 = new Transaction(MILLER_COORS, 10000, instant4);
        Transaction transaction = new Transaction(DANNON, 1000, instant);

        pointsManager.addTransaction(transaction);
        pointsManager.addTransaction(transaction2);
        pointsManager.addTransaction(transaction3);
        pointsManager.addTransaction(transaction4);
        pointsManager.addTransaction(transaction5);

        // prepare expected data
        List<PointsSpentByPayer> expectedPoints = new ArrayList<>();

        expectedPoints.add(new PointsSpentByPayer(DANNON, 0));
        expectedPoints.add(new PointsSpentByPayer(UNILEVER, -200));
        List<PointsSpentByPayer> actualPointsList = pointsManager.spendPoints(200);
        assertThat(actualPointsList, contains(expectedPoints.toArray()));
    }

    @Test
    void spendNegativePoints() {
        List<PointsSpentByPayer> actualPointsList = pointsManager.spendPoints(-points);
        assertThat(actualPointsList.isEmpty(), is(true));
    }

    @Test
    void spendZeroPoints() {
        List<PointsSpentByPayer> actualPointsList = pointsManager.spendPoints(0);
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
    public void spend100_Dannon200_Unilever300_DannonNeg100_Miller10000_Dannon300() {
        pointsManager.getTransactions().clear();

        Instant instant1 = Instant.parse("2020-10-31T10:00:00Z");
        Instant instant2 = Instant.parse("2020-10-31T11:00:00Z");
        Instant instant3 = Instant.parse("2020-10-31T15:00:00Z");
        Instant instant4 = Instant.parse("2020-11-01T14:00:00Z");
        Instant instant5 = Instant.parse("2020-11-02T14:00:00Z");

        Transaction transaction1 = new Transaction(DANNON, 200, instant1);
        Transaction transaction2 = new Transaction(UNILEVER, 300, instant2);
        Transaction transaction3 = new Transaction(DANNON, -100, instant3);
        Transaction transaction4 = new Transaction(MILLER_COORS, 10000, instant4);
        Transaction transaction5 = new Transaction(DANNON, 300, instant5);

        pointsManager.addTransaction(transaction1);
        pointsManager.addTransaction(transaction2);
        pointsManager.addTransaction(transaction3);
        pointsManager.addTransaction(transaction4);
        pointsManager.addTransaction(transaction5);

        // prepare expected data
        List<PointsSpentByPayer> expectedPoints = new ArrayList<>();

        expectedPoints.add(new PointsSpentByPayer(DANNON, -100));
        List<PointsSpentByPayer> actualPointsList = pointsManager.spendPoints(100);
        assertThat(actualPointsList, contains(expectedPoints.toArray()));
    }

    @Test
    public void spend500_Dannon200_Unilever300_DannonNeg100_Miller10000_Dannon300() {
        pointsManager.getTransactions().clear();

        Instant instant1 = Instant.parse("2020-10-31T10:00:00Z");
        Instant instant2 = Instant.parse("2020-10-31T11:00:00Z");
        Instant instant3 = Instant.parse("2020-10-31T15:00:00Z");
        Instant instant4 = Instant.parse("2020-11-01T14:00:00Z");
        Instant instant5 = Instant.parse("2020-11-02T14:00:00Z");

        Transaction transaction1 = new Transaction(DANNON, 200, instant1);
        Transaction transaction2 = new Transaction(UNILEVER, 300, instant2);
        Transaction transaction3 = new Transaction(DANNON, -100, instant3);
        Transaction transaction4 = new Transaction(MILLER_COORS, 10000, instant4);
        Transaction transaction5 = new Transaction(DANNON, 300, instant5);

        pointsManager.addTransaction(transaction1);
        pointsManager.addTransaction(transaction2);
        pointsManager.addTransaction(transaction3);
        pointsManager.addTransaction(transaction4);
        pointsManager.addTransaction(transaction5);

        // prepare expected data
        List<PointsSpentByPayer> expectedPoints = new ArrayList<>();

        expectedPoints.add(new PointsSpentByPayer(DANNON, -100));
        expectedPoints.add(new PointsSpentByPayer(UNILEVER, -300));
        expectedPoints.add(new PointsSpentByPayer(MILLER_COORS, -100));

        List<PointsSpentByPayer> actualPointsList = pointsManager.spendPoints(500);
        assertThat(actualPointsList, contains(expectedPoints.toArray()));
    }

    @Test
    public void spend500_andagain_Dannon200_Unilever300_DannonNeg100_Miller10000_Dannon300() {
        pointsManager.getTransactions().clear();

        Instant instant1 = Instant.parse("2020-10-31T10:00:00Z");
        Instant instant2 = Instant.parse("2020-10-31T11:00:00Z");
        Instant instant3 = Instant.parse("2020-10-31T15:00:00Z");
        Instant instant4 = Instant.parse("2020-11-01T14:00:00Z");
        Instant instant5 = Instant.parse("2020-11-02T14:00:00Z");

        Transaction transaction1 = new Transaction(DANNON, 200, instant1);
        Transaction transaction2 = new Transaction(UNILEVER, 300, instant2);
        Transaction transaction3 = new Transaction(DANNON, -100, instant3);
        Transaction transaction4 = new Transaction(MILLER_COORS, 10000, instant4);
        Transaction transaction5 = new Transaction(DANNON, 300, instant5);

        pointsManager.addTransaction(transaction1);
        pointsManager.addTransaction(transaction2);
        pointsManager.addTransaction(transaction3);
        pointsManager.addTransaction(transaction4);
        pointsManager.addTransaction(transaction5);

        // prepare expected data
        List<PointsSpentByPayer> expectedPoints = new ArrayList<>();

        expectedPoints.add(new PointsSpentByPayer(DANNON, -100));
        expectedPoints.add(new PointsSpentByPayer(UNILEVER, -300));
        expectedPoints.add(new PointsSpentByPayer(MILLER_COORS, -100));

        List<PointsSpentByPayer> actualPointsList = pointsManager.spendPoints(500);
        assertThat(actualPointsList, contains(expectedPoints.toArray()));

        Map<String, Integer> payersPointBalance = pointsManager.getPayersPointBalance();
        assertThat(payersPointBalance.size(), is(3));
        assertThat(payersPointBalance.get(UNILEVER), is(0));
        assertThat(payersPointBalance.get(DANNON), is(300));
        assertThat(payersPointBalance.get(MILLER_COORS), is(9900));

        // spend again
        expectedPoints.clear();
        expectedPoints.add(new PointsSpentByPayer(MILLER_COORS, -9900));

        actualPointsList = pointsManager.spendPoints(9900);
        assertThat(actualPointsList, contains(expectedPoints.toArray()));
        payersPointBalance = pointsManager.getPayersPointBalance();
        assertThat(payersPointBalance.size(), is(3));
        assertThat(payersPointBalance.get(DANNON), is(300));
        assertThat(payersPointBalance.get(UNILEVER), is(0));
        assertThat(payersPointBalance.get(MILLER_COORS), is(0));

        // spend again
        expectedPoints.clear();
        expectedPoints.add(new PointsSpentByPayer(DANNON, -300));

        actualPointsList = pointsManager.spendPoints(300);
        assertThat(actualPointsList, contains(expectedPoints.toArray()));
        payersPointBalance = pointsManager.getPayersPointBalance();
        assertThat(payersPointBalance.size(), is(3));
        assertThat(payersPointBalance.get(DANNON), is(0));
        assertThat(payersPointBalance.get(UNILEVER), is(0));
        assertThat(payersPointBalance.get(MILLER_COORS), is(0));
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
        pointsManager.getTransactions().clear();
        Map<String, Integer> actualPayersPointBalance = pointsManager.getPayersPointBalance();
        assertThat(actualPayersPointBalance.isEmpty(), is(true));
    }

    @Test
    void spendPointsWithLeftoverNegativePoints() {
        pointsManager.getTransactions().clear();

        Instant instant = Instant.parse("2020-11-02T14:00:00Z");
        Instant instant2 = Instant.parse("2020-10-31T11:00:00Z");
        Instant instant3 = Instant.parse("2020-10-31T15:00:00Z");
        Instant instant4 = Instant.parse("2020-11-01T14:00:00Z");
        Instant instant5 = Instant.parse("2020-10-31T10:00:00Z");

        Transaction transaction5 = new Transaction(DANNON, 100, instant5);
        Transaction transaction2 = new Transaction(UNILEVER, 200, instant2);
        Transaction transaction3 = new Transaction(DANNON, -200, instant3);
        Transaction transaction4 = new Transaction(MILLER_COORS, 10000, instant4);
        Transaction transaction = new Transaction(DANNON, 1000, instant);

        pointsManager.addTransaction(transaction);
        pointsManager.addTransaction(transaction2);
        pointsManager.addTransaction(transaction3);
        pointsManager.addTransaction(transaction4);
        pointsManager.addTransaction(transaction5);

        // prepare expected data
        List<PointsSpentByPayer> expectedPoints = new ArrayList<>();

        expectedPoints.add(new PointsSpentByPayer(DANNON, 0));
        expectedPoints.add(new PointsSpentByPayer(UNILEVER, -200));
        expectedPoints.add(new PointsSpentByPayer(MILLER_COORS, -4800));

        List<PointsSpentByPayer> actualPointsList = pointsManager.spendPoints(5000);
        assertThat(actualPointsList, contains(expectedPoints.toArray()));
    }
}
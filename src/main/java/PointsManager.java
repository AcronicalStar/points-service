package main.java;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

/**
 * Class contains all the logic for spending points and getting point balance for all payers.
 */
public class PointsManager {
    private final List<Transaction> transactions = new ArrayList<>();

    /**
     * This method adds the specified transaction to the list of transactions
     * @param transaction - contains payer, points, and timestamp and is added to the list of transactions
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * This method returns a list of points by payer after spending the specified points
     * @param points The points to spend
     * @return List of points by payer after spending the specified points
     */
    public List<PointsSpentByPayer> spendPoints(int points) {
        validateExceededPoints(points);

        // Sorts the transaction list by timestamp
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });

        List<Transaction> positiveTransactions = getPositiveTransactions();

        List<PointsSpentByPayer> pointsSpentByPayerList = new ArrayList<>();

        for (int i = 0; i < positiveTransactions.size() && points > 0; i++) {
            Transaction transaction = positiveTransactions.get(i);

            // Check to see if the payer is present in the list of points after spending. If it's not then add the payer
            // with a starting value of zero
            Optional<PointsSpentByPayer> optionalPointsAfterSpending = pointsSpentByPayerList.stream().filter(t -> t.getPayer().equals(transaction.getPayer())).findAny();
            if (optionalPointsAfterSpending.isEmpty()) {
                pointsSpentByPayerList.add(new PointsSpentByPayer(transaction.getPayer(), 0));
            }

            // Find the min between the points to spend and the points for the current transaction and subtract
            // the min from points to spend

            int minPoints = 0;

            if (transaction.getPoints() > points) {
                minPoints = points;
                transaction.setPoints(transaction.getPoints() - points);
                points = 0;
            } else {
                minPoints = transaction.getPoints();
                points -= transaction.getPoints();
                transaction.setPoints(0);
            }

            // Subtract the min points from the current payer's points
            int finalMinPoints = minPoints;
            pointsSpentByPayerList
                    .stream()
                    // Filters the final result by the current transaction's payer
                    .filter(pointsSpentByPayer -> pointsSpentByPayer.getPayer().equals(transaction.getPayer()))
                    .findAny()
                    // If it finds the payer, it subtracts the transaction's points from the payer's points
                    .ifPresent(pointsSpentByPayer ->
                            pointsSpentByPayer.setPoints(pointsSpentByPayer.getPoints() - finalMinPoints));
        }

        return pointsSpentByPayerList;
    }

    private void validateExceededPoints(int points) {
        // Count total points and check to make sure it doesn't exceed points to spend
        int totalPoints = transactions
                .stream()
                .map(t -> t.getPoints())
                .reduce(0, Integer::sum);

        if (points > totalPoints) {
            throw new IllegalArgumentException("Points to spend has exceeded available points");
        }
    }

    /**
     * This method returns a list of positive transactions
     * @return List of positive transactions
     */
    private List<Transaction> getPositiveTransactions() {
        // Creates a list with only positive transactions
        List<Transaction> positiveTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            // If the number of points is positive then add the transaction to the list of positive transactions
            if (transaction.getPoints() > 0) {
                positiveTransactions.add(transaction);
            } else if (transaction.getPoints() < 0) {
                // Turn points to positive points
                int absPoints = -transaction.getPoints();

                // Filter out payer transactions associated with the negative points and add them to a list
                List<Transaction> currentPayerTransactions = positiveTransactions
                        .stream()
                        .filter(t -> t.getPayer().equals(transaction.getPayer()))
                        .collect(Collectors.toList());

                for (int i = 0; i < currentPayerTransactions.size() && absPoints > 0; i++) {
                    // Find the minimum between the current transaction points and absPoints and subtract from absPoints
                    int minPoints = Math.min(absPoints, currentPayerTransactions.get(i).getPoints());
                    absPoints -= minPoints;

                    // Subtract the minimum points from the current transaction's points
                    currentPayerTransactions.get(i).setPoints(currentPayerTransactions.get(i).getPoints() - minPoints);

                    // Add them back to the negative points
                    transaction.setPoints(transaction.getPoints() + minPoints);
                }
            }
        }
        return positiveTransactions;
    }

    /**
     * This method returns a map of balances by payer when given a list of transactions
     * @return Map of balances by payer
     */
    public Map<String, Integer> getPayersPointBalance() {
        return transactions
                .stream()
                // groups by payer
                .collect(groupingBy(
                Transaction::getPayer,
                // creates a new linked hashmap keyed by payer with value which is the sum of all payer points
                LinkedHashMap::new, summingInt(Transaction::getPoints)));
    }
}

package main.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * The controller class for the REST API endpoints.
 */
@RestController
public class TransactionController {

    private final PointsManager pointsManager = new PointsManager();
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping(value = "/")
    public ResponseEntity index() {
        return ResponseEntity.ok("Welcome to the Point Service");
    }

    @PostMapping(value = "/add")
    public void addTransaction(@RequestBody Transaction transaction) {
        logger.info("Received new transaction: {}", transaction);
        if (!transaction.valid()) {
            throw new IllegalArgumentException("Invalid transaction");
        }
        pointsManager.addTransaction(transaction);
    }

    @PostMapping(value = "/spend-points")
    public List<PointsSpentByPayer> spendPoints(@RequestBody Points points) {
        logger.info("Spent points: {}", points);
        return pointsManager.spendPoints(points.getPoints());
    }

    @GetMapping(value = "/get-balance")
    public Map<String, Integer> getBalance() {
        logger.info("Getting balance.");
        return pointsManager.getPayersPointBalance();
    }
}

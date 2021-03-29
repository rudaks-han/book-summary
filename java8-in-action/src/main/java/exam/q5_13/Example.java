package exam.q5_13;

import exam.q5_10.Trader;
import exam.q5_10.Transaction;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Example {

    public static void main(String[] args) {
        List<Transaction> transactions = Transaction.getTransactionList();

        List<Trader> results = transactions.stream()
                .map(Transaction::getTrader)
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());

        results.stream().forEach(System.out::println);
    }
}

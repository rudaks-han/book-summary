package exam.q5_11;

import exam.q5_10.Trader;
import exam.q5_10.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class Example {

    public static void main(String[] args) {
        List<Transaction> transactions = Transaction.getTransactionList();

        List<Transaction> results = transactions.stream()
                .filter(transaction -> transaction.getTrader() != null)
                .distinct()
                .collect(Collectors.toList());

        results.stream().forEach(System.out::println);
    }
}

package exam.q5_10;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Example {

    public static void main(String[] args) {
        List<Transaction> transactions = Transaction.getTransactionList();

        List<Transaction> results = transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparingInt(Transaction::getValue))
                .collect(Collectors.toList());

        results.stream().forEach(System.out::println);
    }
}

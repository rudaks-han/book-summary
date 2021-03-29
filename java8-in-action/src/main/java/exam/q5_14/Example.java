package exam.q5_14;

import exam.q5_10.Transaction;

import java.util.List;

public class Example {

    public static void main(String[] args) {
        List<Transaction> transactions = Transaction.getTransactionList();

        boolean exists = transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));

        System.out.println(exists);
    }
}

package exam.q5_17;

import exam.q5_10.Transaction;

import java.util.List;

public class Example {

    public static void main(String[] args) {
        List<Transaction> transactions = Transaction.getTransactionList();

        long min = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::min).get();

        System.out.println(min);
    }
}

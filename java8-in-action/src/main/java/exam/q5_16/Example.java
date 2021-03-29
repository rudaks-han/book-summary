package exam.q5_16;

import exam.q5_10.Transaction;

import java.util.List;

public class Example {

    public static void main(String[] args) {
        List<Transaction> transactions = Transaction.getTransactionList();

        long max = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max).get();

        System.out.println(max);
    }
}

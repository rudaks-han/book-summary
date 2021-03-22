package rudaks;

import rudaks.ch04.Dish;
import rudaks.ch05.Trader;
import rudaks.ch05.Transaction;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {


        //System.out.println(results);
        Optional<Integer> sum = Transaction.getTransactionList().stream()
            .map(Transaction::getValue)
            .reduce(Integer::min);

        System.out.println(sum);
    }
}

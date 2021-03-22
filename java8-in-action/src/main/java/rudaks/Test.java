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
        List<Trader> results = Transaction.getTransactionList().stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .sorted(Comparator.comparing(Trader::getName))
                .distinct()
                .collect(Collectors.toList());
        System.out.println(results);
    }
}

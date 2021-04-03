package study.ch_2_4;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Money {
    private BigDecimal amount;
    private String currency;

    public Money(BigDecimal amount, String currency) {
        if (currency == null)
            throw new IllegalArgumentException(currency);

        this.amount = amount;
        this.currency = currency;
    }

    public Money add(Money arg) {
        if (arg == null)
            throw new IllegalArgumentException("arg : " + arg);
        if (currency != arg.currency)
            throw new IllegalArgumentException("화폐 단위가 다름: " + currency + ", " + arg.currency);

        return new Money(amount.add(arg.getAmount()), currency);
    }

    public static void main(String[] args) {
        Money myMoney = new Money(new BigDecimal(1000), "KRW");
        Money allowance = new Money(new BigDecimal(3000), "KRW");
        Money result = myMoney.add(allowance);
        System.out.println("result : " + result.getAmount());

        Money krw = new Money(new BigDecimal(1000), "KRW");
        Money usd = new Money(new BigDecimal(10), "USD");
        Money result2 = krw.add(usd);
        System.out.println("result2 : " + result2.getAmount());

        BigDecimal bigDecimal = new BigDecimal(1000);
        BigDecimal bigDecimal2 = new BigDecimal(1000);
        bigDecimal.add(bigDecimal2);

    }
}

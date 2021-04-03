package study.ch_4_5;

public class PhysicalDistributionBase {

    public Baggage ship(Baggage baggage) {
        // 생략
        return null;
    }

    public void receive(Baggage baggage) {
        // 생략
    }

    public void transport(PhysicalDistributionBase to, Baggage baggage) {
        Baggage shippedBaggage = ship(baggage);
        to.receive(shippedBaggage);

        // 운송 기록 같은 것도 필요할 것이다.
    }
}

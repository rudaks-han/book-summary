package study.ch_4_5;

public class TransportService {
    public void tranport(PhysicalDistributionBase from ,PhysicalDistributionBase to, Baggage baggage) {
        Baggage shippedBaggage = from.ship(baggage);
        to.receive(baggage);

        // 운송 기록 남김
    }
}

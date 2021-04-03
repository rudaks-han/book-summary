package study.ch_13_1.specification;

public class CircleMembersFullSpecification {
    public boolean isSatisfiedBy(CircleMembers members) {
        long premiumUserNumeber = members.countPremiumMembers(false);
        long circleUpperLimit = premiumUserNumeber < 10 ? 30 : 50;
        return members.countMembers() >= circleUpperLimit;
    }
}

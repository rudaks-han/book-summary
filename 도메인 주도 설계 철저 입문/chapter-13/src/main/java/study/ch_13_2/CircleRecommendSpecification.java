package study.ch_13_2;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CircleRecommendSpecification implements ISpecification<Circle> {
    private long executeDateTime;

    public CircleRecommendSpecification(long executeDateTime) {
        this.executeDateTime = executeDateTime;
    }

    public boolean isSatisfiedBy(Circle circle) {
        if (circle.countMembers() < 10) {
            return false;
        }

        return circle.getCreated() > executeDateTime - 60*1000*60*24*30;
    }
}

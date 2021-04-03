package study.ch_13_2;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import study.ch_13_1.IUserRepository;
import study.ch_13_1.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class CircleApplicationService
{
    private final ICircleRepository circleRepository;
    private final IUserRepository userRepository;
    private long now;

    public CircleGetRecommendResult getRecomend(CircleGetRecommendRequest request) {
        /*
        // 리포지토리에 모두 맡기면 된다.
        List<Circle> recommendedCircles = circleRepository.findRecommended(now);
        return new CircleGetRecommendResult(recommendedCircles);*/

        CircleRecommendSpecification recommendSpecification = new CircleRecommendSpecification(now);

        List<Circle> circles = circleRepository.findAll();
        List<Circle> recommendCircles = circles.stream()
                .filter(circle -> new CircleRecommendSpecification().isSatisfiedBy(circle))
                .limit(10)
                .collect(Collectors.toList());

        return new CircleGetRecommendResult(recommendCircles);
    }

    public CircleGetRecommendResult getRecommend(CircleGetRecommendRequest request) throws SQLException {
        CircleRecommendSpecification circleRecommendSpecification = new CircleRecommendSpecification(now);
        // 리포지토리에 명세를 전달해 추천 서클을 추려냄(필터링)
        List<Circle> recommentCircles = circleRepository.find(circleRecommendSpecification).stream()
                .limit(10).collect(Collectors.toList());

        return new CircleGetRecommendResult(recommentCircles);
    }

    public CircleGetSummariesResult getSummaries(CircleGetSummariesCommand command) {
        // 모든 서클의 목록을 받아옴
        List<Circle> all = circleRepository.findAll();
        // 페이징 처리
        List<Circle> circles = all.stream()
                .skip((command.getPage() -1) * command.getSize())
                .limit(command.getSize())
                .collect(Collectors.toList());

        List<CircleSummaryData> summaries = new ArrayList<>();

        for(Circle circle: circles) {
            // 각 서클의 서클장에 해당하는 사용자 정보 검색
            User owner = userRepository.find(circle.getOwner());
            summaries.add(new CircleSummaryData(circle.getId().getValue(), owner.getName().getValue()));
        }

        return new CircleGetSummariesResult(summaries);
    }
}

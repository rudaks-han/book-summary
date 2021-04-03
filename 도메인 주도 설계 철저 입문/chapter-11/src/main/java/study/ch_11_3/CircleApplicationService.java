package study.ch_11_3;

public class CircleApplicationService {
    private final ICircleFactory circleFactory;
    private final ICircleRepository circleRepository;
    private final CircleService circleService;
    private final IUserRepository userRepository;

    public CircleApplicationService(
            ICircleFactory circleFactory,
            ICircleRepository circleRepository,
            CircleService circleService,
            IUserRepository userRepository) {
        this.circleFactory = circleFactory;
        this.circleRepository = circleRepository;
        this.circleService = circleService;
        this.userRepository = userRepository;
    }

    public void create(CircleCreateCommand command) {
        UserId ownerId = new UserId(command.getUserId());
        User owner = userRepository.find(ownerId);

        if (owner == null) {
            throw new UserNotFoundException("user not found: " + ownerId);
        }

        CircleName name = new CircleName(command.getName());
        Circle circle = circleFactory.create(name, owner);

        if (circleService.exists(circle)) {
            throw new CanNotRegisterCircleException("이미 등록된 서클임: " + circle.getName());
        }

        circleRepository.save(circle);
    }

    public void join(CircleJoinCommand command) {
        UserId memberId = new UserId(command.getUserId());
        User member = userRepository.find(memberId);
        if (member == null) {
            throw new UserNotFoundException("서클에 가입할 사용자를 찾지 못했음: " + member.getName());
        }

        CircleId id = new CircleId(command.getCircleId());
        Circle circle = circleRepository.find(id);

        if (circle == null) {
            throw new UserNotFoundException("가입할 서클을 찾지 못했음: " + circle.getName());
        }

        // 서클에 소속된 사용자가 서클장을 포함 30명 이하인지 확인
        if (circle.getMembers().size() >= 29) {
            throw new CircleFullException(id + "");
        }

        circle.getMembers().add(member);
        circleRepository.save(circle);
    }

    public void invite(CircleInviteCommand command) {
        UserId fromUserId = new UserId(command.getFromUserId());
        User fromUser = userRepository.find(fromUserId);
        if (fromUser == null) {
            throw new UserNotFoundException("초대한 사용자를 찾지 못했음: " + fromUser);
        }

        UserId invitedUserId = new UserId(command.getInvitedUserId());
        User invitedUser = userRepository.find(invitedUserId);
        if (invitedUser == null) {
            throw new UserNotFoundException("초대받은 사용자를 찾지 못했음: " + invitedUser);
        }

        CircleId circleId = new CircleId(command.getCircleId());
        Circle circle = circleRepository.find(circleId);

        if (circle == null) {
            throw new CircleNotFoundException("초대받은 서클을 찾지 못했음: " + circle.getName());
        }

        // 서클에 소속된 사용자가 서클장을 포함 30명 미만인지 확인
        if (circle.getMembers().size() >= 29) {
            throw new CircleFullException(circleId + "");

            /*CircleInvitation circleInvitation = new CircleInvitation(circle, fromUser, invitedUser);
            circleInvitationRepository.save(circleInvitation);*/
        }
    }
}

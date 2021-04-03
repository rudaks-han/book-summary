package study.ch_13_1;

import java.util.List;

public class EFUserRepository implements IUserRepository {
    @Override
    public void save(User user) {
        // 노티피케이션 객체를 전달했다가 다시 회수해 내부 데이터를 입수한다.
        UserDataModelBuilder userDataModelBuilder = new UserDataModelBuilder();
        user.notify(userDataModelBuilder);

        // 전달받은 내부 데이터로 데이터 모델을 생성
        UserDataModel userDataModel = userDataModelBuilder.build();

        // 데이터 모델을 ORM에 전달한다.
        /*context.getUsers().add(userDataModel);
        context.saveChanges();*/
    }

    @Override
    public List<User> find(List<UserId> members)
    {
        return null;
    }

    @Override
    public User find(UserId id) {
        return null;
    }

    @Override
    public User find(User user) {
        return null;
    }
}

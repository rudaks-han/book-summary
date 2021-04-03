package study.ch_12_1;

public class UserDataModelBuilder implements IUserNotification {
    private UserId id;
    private UserName name;

    @Override
    public void id(UserId id) {
        this.id = id;
    }

    @Override
    public void name(UserName name) {
        this.name = name;
    }

    // 전달받은 데이터로 데이터 모델을 생성하는 메서드
    public UserDataModel build() {
        return new UserDataModel(
                this.id,
                this.name
        );
    }
}

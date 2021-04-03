package study.layered;

import java.util.List;

public class UserIndexResponseModel {
    List<UserResponseModel> users;
    public UserIndexResponseModel(List<UserResponseModel> users) {
        this.users = users;
    }
}

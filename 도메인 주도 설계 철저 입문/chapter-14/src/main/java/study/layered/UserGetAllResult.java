package study.layered;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserGetAllResult {
    private List<UserData> userModels;
}

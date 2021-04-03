package study.layered;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserApplicationService userApplicationService;

    @GetMapping
    public UserIndexResponseModel index() {
        UserGetAllResult result = userApplicationService.getAll();
        List<UserResponseModel> users = result.getUserModels().stream()
                .map(userData -> new UserResponseModel(userData.getUser()))
                .collect(Collectors.toList());
        return new UserIndexResponseModel(users);
    }

    @GetMapping("${id}")
    public UserGetResponseModel get(@PathVariable String id) {
        UserGetCommand command = new UserGetCommand(id);
        UserGetResult result = userApplicationService.get(command);

        UserResponseModel userModel = new UserResponseModel(result.getUser());
        return new UserGetResponseModel(userModel);
    }

    @PostMapping
    public UserPostResponseModel post(@RequestBody UserPostRequestModel request) {
        UserRegisterCommand command = new UserRegisterCommand(request.getUserName());
        UserRegisterResult result = userApplicationService.register(command);

        return new UserPostResponseModel(result.getCreatedUserId());
    }

    @PutMapping("${id}")
    public void put(@PathVariable String id, @RequestBody UserPutRequestModel request) {
        UserUpdateCommand command = new UserUpdateCommand(id, request.getName());
        userApplicationService.update(command);
    }

    @DeleteMapping("${id}")
    public void delete(@PathVariable String id) {
        UserDeleteCommand command = new UserDeleteCommand(id);
        userApplicationService.delete(command);
    }
}

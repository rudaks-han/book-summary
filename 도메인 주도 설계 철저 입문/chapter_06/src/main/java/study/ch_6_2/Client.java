package study.ch_6_2;

public class Client {
    private UserApplicationService userApplicationService;

    public void changeName(String id, String name) {
        UserData target = userApplicationService.get(id);
        UserName newName = new UserName(name);
        //target.changeName(newName);
    }
}

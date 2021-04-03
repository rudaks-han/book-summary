package study.ch_6_2;

import lombok.Getter;

@Getter
public class UserData {
    private String id;
    private String name;

    public UserData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserData(User source) {
        this.id = source.getId().getValue();
        this.name = source.getName().getValue();
    }
}

package study.ch_11_3;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Circle {
    private CircleId id;
    private CircleName name;
    private User owner;
    private List<User> members;

    public Circle(CircleId id, CircleName name, User owner, List<User> members) {
        if (id == null)
            throw new IllegalArgumentException("id : " + id);
        if (name == null)
            throw new IllegalArgumentException("name : " + name);
        if (owner == null)
            throw new IllegalArgumentException("owner : " + owner);
        if (members == null)
            throw new IllegalArgumentException("members : " + members);

        this.id = id;
        this.name = name;
        this.owner = owner;
        this.members = members;
    }
}

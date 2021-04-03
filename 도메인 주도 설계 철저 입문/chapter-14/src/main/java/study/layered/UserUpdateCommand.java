
package study.layered;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateCommand {
    private String id;
    private String name;
}

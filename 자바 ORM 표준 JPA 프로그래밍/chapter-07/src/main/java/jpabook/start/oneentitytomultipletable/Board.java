package jpabook.start.oneentitytomultipletable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@SecondaryTable(
        name = "board_detail",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "board_id")
)
public class Board {
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String title;

    @Column(table = "board_detail")
    private String content;

    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

package jpabook.start.key;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@TableGenerator(
    name = "board_seq_generator",
    table = "my_sequence",
    pkColumnValue = "board_seq", allocationSize = 5
)
public class BoardTable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "board_seq_generator")
    private Long id;

    @Column
    private String username;

    public BoardTable(String username) {
        this.username = username;
    }
}

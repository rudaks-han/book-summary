package jpabook.start.key;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
    name = "board_seq_generator",
    sequenceName = "board_seq",
    initialValue = 1, allocationSize = 1
)
public class BoardSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq_generator")
    private Long id;

    @Column
    private String username;

    public BoardSequence(String username) {
        this.username = username;
    }
}

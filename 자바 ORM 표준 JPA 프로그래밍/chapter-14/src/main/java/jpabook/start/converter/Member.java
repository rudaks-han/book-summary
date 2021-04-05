package jpabook.start.converter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Convert(converter = BooleanToYNConverter.class, attributeName = "vip")
public class Member {
    @Id
    private Long id;

    private String username;

    private boolean vip;
}

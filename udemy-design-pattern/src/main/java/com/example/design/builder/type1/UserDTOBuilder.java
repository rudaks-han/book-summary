package com.example.design.builder.type1;

import java.time.LocalDate;

// Abstract Builder
public interface UserDTOBuilder {

    UserDTOBuilder withFirstName(String fname);

    UserDTOBuilder withLastName(String lname);

    UserDTOBuilder withBirthday(LocalDate date);

    UserDTOBuilder withAddress(Address address);

    // 최종 Product를 구성할 때 사용하는 메소드
    UserDTO build();

    // 이미 만들어진 객체를 가져올 때 사용하는 메소드
    UserDTO getUserDTO();
}

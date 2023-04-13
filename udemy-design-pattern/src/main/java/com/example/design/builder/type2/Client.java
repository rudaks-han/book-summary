package com.example.design.builder.type2;

import java.time.LocalDate;

import com.example.design.builder.type1.Address;
import com.example.design.builder.type1.User;


public class Client {

    public static void main(String[] args) {
        User user = createUser();
        UserDTO dto = directBuild(UserDTO.getBuilder(), user);
        System.out.println(dto);
    }

    // Director
    private static UserDTO directBuild(UserDTO.UserDTOBuilder builder, User user) {
        return builder
            .withFirstName(user.getFirstName())
            .withLastName(user.getLastName())
            .withAddress(user.getAddress())
            .withBirthday(user.getBirthday())
            .build();
    }

    // sample User를 리턴
    public static User createUser() {
        User user = new User();
        user.setBirthday(LocalDate.of(1960, 5, 6));
        user.setFirstName("Ron");
        user.setLastName("Swanson");
        Address address = new Address();
        address.setHouseNumber("100");
        address.setStreet("State Street");
        address.setCity("Pawnee");
        address.setState("Indiana");
        address.setZipcode("12345");
        user.setAddress(address);

        return user;
    }
}

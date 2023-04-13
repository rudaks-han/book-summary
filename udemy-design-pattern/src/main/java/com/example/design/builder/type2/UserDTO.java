package com.example.design.builder.type2;

import java.time.LocalDate;
import java.time.Period;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Product 클래스
@Getter
@Setter
@ToString
public class UserDTO {

	private String name;

	private String address;

	private String age;

	public static UserDTOBuilder getBuilder() {
		return new UserDTOBuilder();
	}

	public static class UserDTOBuilder {
		private String firstName;

		private String lastName;

		private String age;

		private String address;

		private UserDTO userDTO;

		public UserDTOBuilder withFirstName(String fname) {
			this.firstName = fname;
			return this;
		}

		public UserDTOBuilder withLastName(String lname) {
			this.lastName = lname;
			return this;
		}

		public UserDTOBuilder withBirthday(LocalDate date) {
			Period ageInYears = Period.between(date, LocalDate.now());
			this.age = Integer.toString(ageInYears.getYears());
			return this;
		}

		public UserDTOBuilder withAddress(com.example.design.builder.type1.Address address) {
			this.address = address.getHouseNumber() + ", " + address.getStreet() + "\n"
				+ address.getCity() + "\n"
				+ address.getState() + " " + address.getZipcode();
			return this;
		}

		public UserDTO build() {
			this.userDTO = new UserDTO();
			userDTO.setName(firstName + " " + lastName);
			userDTO.setAddress(address);
			userDTO.setAge(age);

			return this.userDTO;
		}

		public UserDTO getUserDTO() {
			return this.userDTO;
		}
	}
}

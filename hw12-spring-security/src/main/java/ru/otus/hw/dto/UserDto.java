package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.hw.models.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String username;

    private String password;

    private String fullname;

    private String street;

    private String city;

    private String phone;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(id, username, passwordEncoder.encode(password), fullname, street, city, phone);
    }

}
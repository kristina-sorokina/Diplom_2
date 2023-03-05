package model;

import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class User {
    private String email;
    private String password;
    private String name;
}

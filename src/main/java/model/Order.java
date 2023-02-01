package model;

import java.util.List;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class Order {
    private List<String> ingredients;
}

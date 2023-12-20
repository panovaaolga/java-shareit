package ru.practicum.shareit.user;

import lombok.*;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    private String name;
    @Column(unique = true)
    private String email;
}

package ru.abfitness.persist.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.abfitness.persist.entities.User;

public final class UserSpecification {

    public static Specification<User> trueLiteral() {
        return (root, query, builder) -> builder.isTrue(builder.literal(true));
    }

    public static Specification<User> findByEmail(String email) {
        return (root, query, builder) -> builder.like(root.get("email"), email);
    }

    public static Specification<User> emailContains(String email) {
        return (root, query, builder) -> builder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<User> idNotEqual(Integer id) {
        return (root, query, builder) -> builder.notEqual(root.get("id"), id);
    }
}

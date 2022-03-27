package ru.abfitness.persist.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "email")
    @NotBlank(message = "Укажите email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "password")
    @NotBlank(message = "Укажите password")
    private String password;
    @Transient
    private String matchingPassword;


    @Column
    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Set<Role> roles;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_create", updatable = false)
    private Date dtCreate;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_modify", nullable = false)
    private Date dtModify;



    public Date getDtCreate() {
        return dtCreate;
    }

    @PrePersist
    public void setDtCreate() {
        this.dtCreate = this.dtModify = new Date();
    }

    public Date getDtModify() {
        return dtModify;
    }

    @PreUpdate
    public void setDtModify() {
        this.dtModify = new Date();
    }


    public User( String email, String password) {
        this.password = password;
        this.email = email;
        this.roles = new HashSet<>();
    }

    @Override
    public String toString() {
        StringBuilder cb = new StringBuilder("User{");
        cb.append("id=").append(id).append(", ");
        cb.append("password=").append(password).append(", ");
        cb.append("matchingPassword=").append(matchingPassword).append(", ");
        cb.append("email=").append(email).append(", ");
        if (dtCreate != null) {
            cb.append("dtCreate=").append(new SimpleDateFormat("dd MMMM yyyy").format(dtCreate)).append(", ");
        }
        if (dtModify != null) {
            cb.append("dtModify=").append(new SimpleDateFormat("dd MMMM yyyy").format(dtModify)).append(", ");
        }
        cb.append("}\n");

        return cb.toString();
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}

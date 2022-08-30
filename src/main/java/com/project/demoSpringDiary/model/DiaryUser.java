package com.project.demoSpringDiary.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiaryUser implements UserDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String password;
    private String userName;
    private Boolean isFemale;
    private Integer age;
    private Integer height;
    private Integer weight;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;
    private Double basalMetabolicRate;
    private Integer goalWeight;
    private Integer daysForResult;
    private Double caloriesForResult;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "diary_user_role", joinColumns = @JoinColumn(name = "diary_user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "day_id")
    private Day day;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Meal> meal;

    @Autowired
    public DiaryUser(Boolean isFemale, Integer age, Integer height, Integer weight, ActivityType activityType) {
        this.isFemale = isFemale;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.activityType = activityType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

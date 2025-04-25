package com.nexusfc.api.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Users")
public class User implements UserDetails {
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("email")
    private String email;

    @Field("password")
    private String password;

    @Field("created_at")
    private Instant createdAt = Instant.now();

    @Field("last_rewarded_login")
    private Instant lastRewardedLogin;

    @Field("coins")
    private Float coins = 120f;

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public void addCoins(Float coins) {
        this.coins += coins;
    }

    public void decreaseCoins(Float coins) {
        this.coins -= coins;
    }

    public boolean isFirstDailyLogin() {
        if (lastRewardedLogin == null || Duration.between(lastRewardedLogin, Instant.now()).toHours() >= 24) {
            lastRewardedLogin = Instant.now();
            return true;
        }

        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}

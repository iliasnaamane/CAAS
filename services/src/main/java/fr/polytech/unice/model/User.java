package fr.polytech.unice.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.NoArgsConstructor;

import static com.google.common.base.Preconditions.checkNotNull;


@Entity
@NoArgsConstructor
public class User {

    public static final int BRONZE_OFFER = 1;
    public static final int SILVER_OFFER = 2;
    public static final int GOLD_OFFER = 3;

    @Id public Long id;
    @Index public String username;
    public String mail;
    public int offer;

    public User(String username, String mail,int offer) {
        this.username = username;
        this.mail =mail;
        this.offer = offer;
    }
}

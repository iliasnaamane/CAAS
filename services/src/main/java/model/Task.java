package model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@NoArgsConstructor
public class Task {

    public static final int PENDING_STATE = 0;
    public static final int RUNNING_STATE = 1;
    public static final int DONE_STATE = 2;

    @Parent Key<User> user;
    @Id public Long id;
    @Index public int state;
    public String original;
    public String converted;
    public String format;
    @Index public Date created;
    @Index public Date expired;
    public boolean purged = false;

    public Task(Key<User> user, String original, String converted, String format) {
        this.user = user;
        this.original =original;
        this.converted =converted;
        this.format = format;
        this.created = new Date();
        this.state = PENDING_STATE;
    }

}

package com.sg.bierkasse.entities;

import com.sg.bierkasse.utils.security.Permissions;
import com.vaadin.flow.component.template.Id;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
public class UserEntity implements BaseEntity {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String username;

    private String password;

    private Set<Permissions> roles = EnumSet.of(Permissions.USER);

    private boolean enabled = true;

    private Instant createdAt;
}

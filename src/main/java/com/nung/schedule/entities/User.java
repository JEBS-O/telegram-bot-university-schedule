package com.nung.schedule.entities;

import com.nung.schedule.entities.enums.BotStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;
    private String groupName;
    @Enumerated(EnumType.STRING)
    private BotStatus botStatus;
}

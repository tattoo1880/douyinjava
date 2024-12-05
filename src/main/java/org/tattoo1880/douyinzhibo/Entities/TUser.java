package org.tattoo1880.douyinzhibo.Entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "t_user")
public class TUser {
    @Id
    private Long id;
    private String uid;
    private String shortId;
    private String nickname;
}

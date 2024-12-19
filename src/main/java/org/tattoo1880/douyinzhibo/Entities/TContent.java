package org.tattoo1880.douyinzhibo.Entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("t_content")
public class TContent {

  @Id
  private String id = UUID.randomUUID().toString();
  private String roomId;
  private String uid;
  private String nickname;
  private String content;
  private Instant createTime = Instant.now();

}

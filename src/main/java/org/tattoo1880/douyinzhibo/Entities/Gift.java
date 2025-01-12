package org.tattoo1880.douyinzhibo.Entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gift {
    String Type;
    String UserName;
    String UserId;
    String GiftUrl;
    String count;
}

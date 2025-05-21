package dev.pawan.sharemate.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDTO {
    private Integer userId;
    private Integer friendId;
}

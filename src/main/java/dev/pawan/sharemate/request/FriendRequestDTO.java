package dev.pawan.sharemate.request;

import dev.pawan.sharemate.enums.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDTO {
    private Integer userId;
    private Integer friendId;
    private FriendStatus status = FriendStatus.PENDING;
}

package dev.pawan.sharemate.request;

import java.util.List;

import lombok.Data;

@Data
public class AddMemberRequestDTO {
	Integer groupId;
	List<Integer> friendIdList;
}

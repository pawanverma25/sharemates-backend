package dev.pawan.sharemate.request;

import java.util.List;

import lombok.Data;

@Data
public class GroupRequestDTO {
	private Integer id;
	private String name;
	private String description;
	private Integer createdBy;
	private List<Integer> friendIdList;
}

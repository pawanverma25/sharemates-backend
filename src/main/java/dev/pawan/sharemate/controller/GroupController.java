package dev.pawan.sharemate.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pawan.sharemate.model.Group;
import dev.pawan.sharemate.request.AddMemberRequestDTO;
import dev.pawan.sharemate.request.GroupRequestDTO;
import dev.pawan.sharemate.response.GroupDTO;
import dev.pawan.sharemate.response.UserDTO;
import dev.pawan.sharemate.service.GroupService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/getGroups/{userId}")
    public ResponseEntity<List<GroupDTO>> getGroupsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupsByUserId(userId));
    }
    

    @GetMapping("/getGroupDetails/{groupId}")
    public ResponseEntity<GroupDTO> getGroupDetails(@PathVariable Integer groupId) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupDetailsByGroupId(groupId));
    }

    @GetMapping("/getGroupMembers/{groupId}")
    public ResponseEntity<List<UserDTO>> getGroupMembersByGroupId(@PathVariable Integer groupId) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupMembersByGroupId(groupId));
    }

    @PostMapping("/createGroup")
    public ResponseEntity<Group> createGroup(@RequestBody GroupRequestDTO groupRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.createGroup(groupRequest));
    }

    @PostMapping("/addMembersToGroup")
    public ResponseEntity<Map<String, String>> addMembersToGroup(@RequestBody AddMemberRequestDTO addMemberRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.addMembersToGroup(addMemberRequestDTO));
    }
    
    @PostMapping("/updateGroup")
    public ResponseEntity<Map<String,Boolean>> updateGroup(@RequestBody GroupRequestDTO groupRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("updated",groupService.updateGroup(groupRequestDTO)));
    }

}

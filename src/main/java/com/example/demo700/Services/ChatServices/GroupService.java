package com.example.demo700.Services.ChatServices;

import java.util.List;
import com.example.demo700.Models.ChatModels.Group;
import com.example.demo700.Models.ChatModels.GroupMessage;
import com.example.demo700.DTOFiles.GroupMessageResponse;

public interface GroupService {
    
    // গ্রুপ ম্যানেজমেন্ট
    Group createGroup(Group group, String creatorId);
    Group updateGroup(String groupId, Group group, String userId);
    boolean deleteGroup(String groupId, String userId);
    Group getGroupById(String groupId);
    public GroupMessage getGroupMessageById(String id);
    List<Group> getUserGroups(String userId);
    boolean addMemberToGroup(String groupId, String memberId, String adminId);
    boolean removeMemberFromGroup(String groupId, String memberId, String adminId);
    
    // গ্রুপ মেসেজিং
    GroupMessage sendGroupMessage(GroupMessage message);
    List<GroupMessageResponse> getGroupMessages(String groupId, String userId, int page, int size);
    GroupMessage editGroupMessage(String messageId, String newContent, String userId);
    boolean deleteGroupMessage(String messageId, String userId);
    
    // রিড রিসিপ্ট
    void markMessageAsRead(String messageId, String userId);
    int getUnreadCount(String groupId, String userId);
}
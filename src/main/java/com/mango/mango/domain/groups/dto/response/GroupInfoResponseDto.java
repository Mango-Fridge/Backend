package com.mango.mango.domain.groups.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoResponseDto {
    private Long groupId;
    private String groupCode;
    private String groupName;
    private Long groupOwnerId;
    private List<GroupUser> groupUsers;
    private List<GroupHopeUser> groupHopeUsers;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupUser {
        private Long userId;
        private String username;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupHopeUser {
        private Long userId;
        private String username;
    }
}

package com.mango.mango.domain.groups.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupExistResponseDto {
    private Long groupId;
    private String groupName;
    private String groupOwnerName;
    private int groupMemberCount;
}

package com.mango.mango.domain.groups.dto.reqeust;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequestDto {
    private Long userId;
    private Long groupId;
}
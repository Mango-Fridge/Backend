package com.mango.mango.domain.groups.dto.reqeust;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupRequestDto {
    private Long userId;
    private String groupName;
}

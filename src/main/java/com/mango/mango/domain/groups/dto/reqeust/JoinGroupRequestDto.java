package com.mango.mango.domain.groups.dto.reqeust;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinGroupRequestDto {
    private Long userId;
    private Long groupId;
}
package org.grace.matjibbacked.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO { // DTO는 내가 원하는 데이터를 뽑아내기 위한 객체
    private Long tno;
    private String title;
    private boolean complete;
    private String writer;
    private LocalDate dueDate;

}

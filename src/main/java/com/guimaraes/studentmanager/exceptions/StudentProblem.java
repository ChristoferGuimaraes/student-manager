package com.guimaraes.studentmanager.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class StudentProblem {
    private String title;
    private Integer status;
    private String message;
}

package com.EducCore.EduCore.domain.User;

import lombok.Data;
import java.util.List;

@Data
public class GroupRequestDTO {
    private String name;
    private List<CourseRequestDTO> courses;
}
package com.workhub.mapper;

import com.workhub.dto.ProjectDto;
import com.workhub.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDto projectToProjectDTO(Project project);

    Project projectDTOToProject(ProjectDto projectDTO);

    void updateProjectFromDTO(ProjectDto projectDto, @MappingTarget Project project);
}

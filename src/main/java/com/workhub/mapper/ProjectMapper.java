package com.workhub.mapper;

import com.workhub.dto.ProjectDto;
import com.workhub.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectDto projectToProjectDTO(Project project);

    Project projectDTOToProject(ProjectDto projectDTO);
}

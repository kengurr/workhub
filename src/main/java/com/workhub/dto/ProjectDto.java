package com.workhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Technology is required.")
    private Set<Technology> technology = new HashSet<>();
}

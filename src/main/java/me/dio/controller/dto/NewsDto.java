package me.dio.controller.dto;

import jakarta.validation.constraints.NotBlank;
import me.dio.domain.model.News;

public record NewsDto(Long id, @NotBlank String icon, @NotBlank String description) {

    public NewsDto(News model) {
        this(model.getId(), model.getIcon(), model.getDescription());
    }

    public News toModel() {
        News model = new News();
        model.setId(this.id);
        model.setIcon(this.icon);
        model.setDescription(this.description);
        return model;
    }
}


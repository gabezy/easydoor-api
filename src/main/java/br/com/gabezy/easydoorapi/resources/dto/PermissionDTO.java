package br.com.gabezy.easydoorapi.resources.dto;

/**
 * Permission DTO for REST responses
 */
public class PermissionDTO {
    public Long id;
    public String code;
    public String description;

    public PermissionDTO() {
    }

    public PermissionDTO(Long id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }
}


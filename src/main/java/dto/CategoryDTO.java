package dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private String categoryId;
    private String name;
    private String description;
    private String categoryIcon;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private UserDTO user;
}
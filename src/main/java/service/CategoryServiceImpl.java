package service;

import dto.CategoryDTO;
import dto.UserDTO;
import entity.CategoryEntity;
import entity.User;
import exceptions.ItemExistsException;
import exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import mappers.CategoryMapper;
import org.springframework.stereotype.Service;
import repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    private final CategoryMapper categoryMapper;

    /**
     * This is for reading the categories from database
     * @return list
     * */
    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> list = categoryRepository.findByUserId(userService.getLoggedInUser().getId());
        return list.stream().map(categoryEntity -> categoryMapper.mapToCategoryDTO(categoryEntity)).collect(Collectors.toList());
    }

    /**
     * This is for creating the new category
     * @param categoryDTO
     * @return CategoryDTO
     * */
    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        boolean isCategoryPresent = categoryRepository.existsByNameAndUserId(categoryDTO.getName(), userService.getLoggedInUser().getId());
        if (isCategoryPresent) {
            throw new ItemExistsException("Category is already present for the name "+categoryDTO.getName());
        }
        CategoryEntity newCategory = categoryMapper.mapToCategoryEntity(categoryDTO);
        newCategory.setCategoryId(UUID.randomUUID().toString());
        newCategory.setUser(userService.getLoggedInUser());
        newCategory = categoryRepository.save(newCategory);
        return categoryMapper.mapToCategoryDTO(newCategory);
    }

    /**
     * This is for deleting the category from database
     * @param categoryId
     * */
    @Override
    public void deleteCategory(String categoryId) {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), categoryId);
        if (!optionalCategory.isPresent()) {
            throw new ResourceNotFoundException("Category not found for the id "+categoryId);
        }
        categoryRepository.delete(optionalCategory.get());
    }

    /**
     * Mapper method to convert User entity to User DTO
     * @param user
     * @return UserDTO
     * */
    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}

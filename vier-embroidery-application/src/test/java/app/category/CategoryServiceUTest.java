package app.category;

import app.category.model.Category;
import app.category.repository.CategoryRepository;
import app.category.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceUTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void whenGetAllCategories_thenReturnCategoryList() {

        Category category = Category.builder()
                .name("Men")
                .build();

        List<Category> categoryList = List.of(category);

        when(categoryRepository.findAll()).thenReturn(categoryList);

        List<Category> categories = categoryService.getAllCategories();

        assertEquals(1, categories.size());
        assertEquals("Men", categories.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }
}

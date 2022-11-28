package shoppingmall.bookshop.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.bookshop.entity.Category;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegisterParentCategoryDto {

    private String categoryName;

    public Category toEntity() {
        return Category.builder()
                .categoryName(categoryName)
                .isParent(true)
                .childCategories(new ArrayList<>())
                .itemCategories(new ArrayList<>())
                .build();
//        category.getChildCategories().add(new RegisterChildCategoryDto("testchild", category).toEntity());
//        return category;
    }

}

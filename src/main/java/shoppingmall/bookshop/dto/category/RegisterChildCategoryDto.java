package shoppingmall.bookshop.dto.category;

import lombok.*;
import shoppingmall.bookshop.entity.Category;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterChildCategoryDto {
    private String categoryName;
    private Category parent;

    public Category toEntity() {
        return Category.builder()
                .categoryName(categoryName)
                .itemCategories(new ArrayList<>())
                .parent(parent)
                .isParent(false)
                .build();
    }

}

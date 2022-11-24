package shoppingmall.bookshop.dto.category;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import shoppingmall.bookshop.entity.Category;

@RequiredArgsConstructor
@Builder
public class RegisterParentCategoryDto {

    private final String categoryName;

    public Category toEntity() {

        return Category.builder().categoryName(categoryName).isParent(true).build();

    }




}

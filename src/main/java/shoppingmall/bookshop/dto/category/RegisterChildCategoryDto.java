package shoppingmall.bookshop.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shoppingmall.bookshop.entity.Category;

@Getter
@Builder
@RequiredArgsConstructor
public class RegisterChildCategoryDto {
    private final String categoryName;
    private final Category parent;

    public Category toEntity() {
        return Category.builder()
                .categoryName(categoryName)
                .parent(parent)
                .build();
    }

}

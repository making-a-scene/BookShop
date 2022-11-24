package shoppingmall.bookshop.dto.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shoppingmall.bookshop.entity.Category;

@RequiredArgsConstructor
@Getter
public class ChangeParentCategoryDto {
    private final Long childId;
    private final Category parent;

}

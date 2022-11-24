package shoppingmall.bookshop.dto.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoryNameUpdateDto {
    private final Long id;
    private final String categoryName;
}

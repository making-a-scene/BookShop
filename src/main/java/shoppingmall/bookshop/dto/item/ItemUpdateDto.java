package shoppingmall.bookshop.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shoppingmall.bookshop.entity.Category;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.entity.ItemCategory;

@Getter
@AllArgsConstructor
public class ItemUpdateDto {

    private Long id;

    private String title;

    private String author;

    private String summery;

    private int price;

    private ItemCategory category;

}

package shoppingmall.bookshop.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shoppingmall.bookshop.entity.ItemCategory;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemUpdateDto {

    private Long id;

    private String title;

    private String author;

    private String summery;

    private int price;

    private List<ItemCategory> itemCategories;

}

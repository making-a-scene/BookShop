package shoppingmall.bookshop.dto.item;

import lombok.RequiredArgsConstructor;
import shoppingmall.bookshop.entity.Category;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.entity.ItemCategory;
import shoppingmall.bookshop.entity.User;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class ItemRegisterDto {

    private final String title;

    private final String author;

    private final String summery;

    private final Date publishedAt;

    private final int price;

    private final User admin;

    private final List<ItemCategory> itemCategories;

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .author(author)
                .summery(summery)
                .salesCount(0)
                .publishedAt(publishedAt)
                .price(price)
                .admin(admin)
                .itemCategories(itemCategories)
                .build();
    }


}

package shoppingmall.bookshop.dto;

import lombok.RequiredArgsConstructor;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.entity.User;

import java.util.Date;

@RequiredArgsConstructor
public class ItemRegisterDto {

    private final String title;

    private final String author;

    private final String summery;

    private final Date publishedAt;

    private final int price;

    private final User admin;

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .author(author)
                .summery(summery)
                .salesCount(0)
                .publishedAt(publishedAt)
                .price(price)
                .admin(admin)
                .build();
    }


}

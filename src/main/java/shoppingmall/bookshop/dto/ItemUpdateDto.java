package shoppingmall.bookshop.dto;

import lombok.Getter;
import shoppingmall.bookshop.entity.Item;

@Getter
public class ItemUpdateDto {

    private Long id;

    private String title;

    private String author;

    private String summery;

    private int price;

    public Item toEntity() {
        return Item.builder()
                .id(id)
                .title(title)
                .author(author)
                .summery(summery)
                .price(price)
                .build();
    }
}

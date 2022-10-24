package shoppingmall.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shoppingmall.bookshop.entity.Item;

@Getter
@AllArgsConstructor
public class ItemUpdateDto {

    private Long id;

    private String title;

    private String author;

    private String summery;

    private int price;

}

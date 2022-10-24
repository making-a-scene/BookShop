package shoppingmall.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.bookshop.dto.ItemUpdateDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String author;

    private String summery;

    private int salesCount;

    private Date publishedAt;

    private int price;

    @ManyToOne
    private User admin;

    public void update(ItemUpdateDto itemUpdateDto) {
        this.title = itemUpdateDto.getTitle();
        this.author = itemUpdateDto.getAuthor();
        this.summery = itemUpdateDto.getSummery();
        this.price = itemUpdateDto.getPrice();
    }
}

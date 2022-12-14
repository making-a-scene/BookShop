package shoppingmall.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import shoppingmall.bookshop.dto.item.ItemUpdateDto;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "seq_item", allocationSize = 1)
public class Item {

    @Id @GeneratedValue(generator = "seq_item")
    @Column(name = "item_id")
    private Long id;

    private String title;

    private String author;

    private String summery;

    private int salesCount;

    private Date publishedAt;

    private int price;

    @ManyToOne
    private User admin;

    @OneToMany(mappedBy="item")
    @JsonIgnore
    private List<ItemCategory> itemCategories = new ArrayList<>();

    public void update(ItemUpdateDto itemUpdateDto) {
        this.title = itemUpdateDto.getTitle();
        this.author = itemUpdateDto.getAuthor();
        this.summery = itemUpdateDto.getSummery();
        this.price = itemUpdateDto.getPrice();
    }

    public void setItemCategories(List<ItemCategory> itemCategories) {
        if (!this.itemCategories.isEmpty()) {
            this.itemCategories.clear();
        }
        this.itemCategories = itemCategories;
    }
}

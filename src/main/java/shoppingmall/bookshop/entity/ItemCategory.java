package shoppingmall.bookshop.entity;

import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@IdClass(ItemCategoryId.class)
@Getter
public class ItemCategory {

    @Id
    @ManyToOne @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;

    @Id
    @ManyToOne @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    public void setItem(Item item) {
        this.item = item;
        item.getItemCategories().add(this);
    }

    public void setChildCategory(Category child) {
        this.category = child;
        child.getItemCategories().add(this);
    }
}

package shoppingmall.bookshop.entity;

import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Getter
public class ItemCategory {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;

    @ManyToOne @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    public void setItem(Item item) {
        if (item.getItemCategories().contains(this)) {
            item.getItemCategories().remove(this);
        }

        this.item = item;
        item.getItemCategories().add(this);
    }

    public void setChildCategory(Category child) {
        if (child.getChildCategories().contains(this)) {
            child.getChildCategories().remove(this);
        }

        this.category = child;
        child.getItems().add(this);
    }
}

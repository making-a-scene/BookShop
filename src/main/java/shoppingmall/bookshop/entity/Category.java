package shoppingmall.bookshop.entity;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String categoryName;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<ItemCategory> itemCategories = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    @JsonIgnore
    private Category parent;

    private boolean isParent;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    @JsonIgnore
    private List<Category> childCategories = new ArrayList<>();

    public void updateCategoryName(String name) {
        this.categoryName = name;
    }

    // 연관 관계 편의 메서드
    public void setCategoryRelationship(Category parent) {
        // this : 자식 카테고리
        if (this.parent != null) {
            this.parent.getChildCategories().remove(this);
        }
        this.parent = parent;

        parent.getChildCategories().add(this);
    }

}

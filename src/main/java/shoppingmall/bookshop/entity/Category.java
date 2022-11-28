package shoppingmall.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    @JsonIgnore
    private List<ItemCategory> itemCategories = new ArrayList<>();

    @ManyToOne(fetch = EAGER)
    @ToString.Exclude
    private Category parent;


    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @JsonIgnore
    private List<Category> childCategories = new ArrayList<>();

    private boolean isParent;

    public void updateCategoryName(String name) {
        this.categoryName = name;
    }

    // 연관 관계 편의 메서드
    public void setCategoryRelationship(Category parent) {
        // this : 자식 카테고리
        parent.getChildCategories().remove(this);
        this.parent = parent;
        parent.getChildCategories().add(this);
    }

    public boolean getWhetherParentOrNot() {
        return isParent;
    }

}

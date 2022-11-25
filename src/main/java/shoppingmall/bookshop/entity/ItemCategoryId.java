package shoppingmall.bookshop.entity;

import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class ItemCategoryId implements Serializable {

    private String item; // ItemCategory.item과 연결
    private String category; // ItemCategory.category와 연결

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

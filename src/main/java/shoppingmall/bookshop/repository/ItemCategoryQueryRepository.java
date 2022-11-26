package shoppingmall.bookshop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shoppingmall.bookshop.entity.Category;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.entity.ItemCategory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemCategoryQueryRepository {

    private final EntityManager em;

    public List<Item> findAllItemsByCategory(Category category) {
        List<ItemCategory> itemCategories =
                em.createQuery("select i from Item i" +
                                " join fetch i.itemCategories").getResultList();

        List<Item> foundItems = new ArrayList<>();
        for (ItemCategory itemCategory : itemCategories) {
            if (itemCategory.getCategory().equals(category)) {
                foundItems.add(itemCategory.getItem());
            }
        }
        return foundItems;
    }
}

package shoppingmall.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shoppingmall.bookshop.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}

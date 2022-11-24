package shoppingmall.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shoppingmall.bookshop.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

}

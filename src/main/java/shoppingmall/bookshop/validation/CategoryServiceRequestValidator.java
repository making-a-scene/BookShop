package shoppingmall.bookshop.validation;

import shoppingmall.bookshop.entity.Category;

import java.util.NoSuchElementException;
import java.util.Optional;

public class CategoryServiceRequestValidator {

    public static Category validateExistOrNot(Optional<Category> foundResult) {
        try {
            return foundResult.get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("해당 카테고리가 존재하지 않습니다.");
        }
    }

    public static void validateDuplicatedOrNot(Optional<Category> foundResult) {
        if (foundResult.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }
    }

    public static boolean validateParentOrNot(Category category) {
        return category.isParent();
    }

    public static void validateEmptyParentOrNot(Category parent) {
        if (!parent.getChildCategories().isEmpty()) {
            throw new IllegalStateException("해당 카테고리는 하위 카테고리가 존재하므로 삭제할 수 없습니다. 빈 카테고리만 삭제 가능합니다.");
        }
    }

    public static void validateEmptyChildOrNot(Category child) {
        if (!child.getItemCategories().isEmpty()) {
            throw new IllegalStateException("해당 카테고리에 상품이 등록되어 있어 삭제할 수 없습니다. 빈 카테고리만 삭제 가능합니다.");
        }
    }



}

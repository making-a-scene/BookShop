package shoppingmall.bookshop.validation;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.entity.User;

import java.util.NoSuchElementException;
import java.util.Optional;

public class ItemServiceRequestValidator {

    public static Item validateExistOrNot(Optional<Item> foundResult) {
        try {
            return foundResult.get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("존재하지 않는 상품입니다.");
        }

    }

    public static void validateAuthorization(Item item, User subject) {
        if (item.getAdmin().equals(subject)) {
            return;
        }
        throw new InsufficientAuthenticationException("권한이 없습니다.");
    }
}

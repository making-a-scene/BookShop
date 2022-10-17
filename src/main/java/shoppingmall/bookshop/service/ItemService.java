package shoppingmall.bookshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.bookshop.dto.ItemUpdateDto;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.repository.ItemRepository;

import java.util.NoSuchElementException;

@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public boolean authenticationCheck(User subject) {
        return subject.getRole().value().equals("ROLE_SUPER");
    }

    @Transactional(readOnly = true)
    public Item findItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> {throw new NoSuchElementException("해당 상품이 존재하지 않습니다.");});
    }

    public Long registerNewItem(User subject, Item item) throws InsufficientAuthenticationException {
        if(authenticationCheck(subject)) {
            itemRepository.save(item);
            return item.getId();
        } else {
            throw new InsufficientAuthenticationException("권한이 없습니다.");
        }
    }

    public void deleteItem(User subject, Item item) throws InsufficientAuthenticationException {
        if(authenticationCheck(subject)) {
            if(item.getAdmin().equals(subject)) {
                itemRepository.delete(item);
                return;
            }
        }
        throw new InsufficientAuthenticationException("권한이 없습니다.");
    }

    public Long updateItem(User subject, ItemUpdateDto itemUpdateDto) {
        if(authenticationCheck(subject)) {
            Item updatedItem = itemRepository.save(itemUpdateDto.toEntity());
            return updatedItem.getId();

        } else {
            throw new InsufficientAuthenticationException("권한이 없습니다.");
        }
    }
}

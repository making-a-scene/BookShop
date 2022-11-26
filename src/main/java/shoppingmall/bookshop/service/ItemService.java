package shoppingmall.bookshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.bookshop.dto.item.ItemRegisterDto;
import shoppingmall.bookshop.dto.item.ItemUpdateDto;
import shoppingmall.bookshop.entity.Category;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.entity.ItemCategory;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.repository.ItemCategoryQueryRepository;
import shoppingmall.bookshop.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

import static shoppingmall.bookshop.validation.CategoryServiceRequestValidator.*;
import static shoppingmall.bookshop.validation.ItemServiceRequestValidator.*;

@Transactional
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryQueryRepository itemCategoryRepository;
    private final CategoryService categoryService;

    private static Long setRelationship(Item item, List<ItemCategory> itemCategories) {
        for (ItemCategory itemCategory : itemCategories) {
            itemCategory.setItem(item);
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Item findItemById(Long id) {
        return validateExistOrNot(itemRepository.findById(id));
    }

    // 책 제목으로 item 엔티티 검색
    @Transactional(readOnly = true)
    public Item findItemByTitle(String title) {
        return validateExistOrNot(itemRepository.findItemByName(title));
    }

    //  카테고리 내에 있는 모든 상품들 출력
    @Transactional(readOnly = true)
    public List<Item> findAllItemsFromCategory(Long categoryId) {
        return itemCategoryRepository.findAllItemsByCategory(categoryService.findById(categoryId));
    }

    // 새로운 상품 등록
    public Long registerNewItem(ItemRegisterDto itemRegisterDto) {
        Item item = itemRepository.save(itemRegisterDto.toEntity());
        List<ItemCategory> itemCategories = itemRegisterDto.getItemCategories();

        return setRelationship(item, itemCategories);
    }

    // 상품 삭제
    public void deleteItem(User subject, Long itemId) throws InsufficientAuthenticationException {
        Item itemToBeDeleted = findItemById(itemId);
        validateAuthorization(itemToBeDeleted, subject);
        itemRepository.delete(itemToBeDeleted);
    }

    // 상품 수정
    public Long updateItem(User subject, ItemUpdateDto itemUpdateDto) {
        Item updateItem = findItemById(itemUpdateDto.getId());
        validateAuthorization(updateItem, subject);

        updateItem.update(itemUpdateDto);

        List<ItemCategory> itemCategories = itemUpdateDto.getItemCategories();
        return setRelationship(updateItem, itemCategories);
    }
}

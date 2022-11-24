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
import shoppingmall.bookshop.repository.ItemRepository;
import shoppingmall.bookshop.validation.ItemServiceRequestValidator;

import java.util.ArrayList;
import java.util.List;

import static shoppingmall.bookshop.validation.CategoryServiceRequestValidator.*;
import static shoppingmall.bookshop.validation.ItemServiceRequestValidator.*;

@Transactional
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryService categoryService;

    // item의 카테고리 이동
    @Transactional(readOnly = true)
    public Item findItemById(Long id) {
        return ItemServiceRequestValidator.validateExistOrNot(itemRepository.findById(id));
    }

    @Transactional(readOnly = true)
    public Item findItemByTitle(String title) {
        return ItemServiceRequestValidator.validateExistOrNot(itemRepository.findItemByName(title));
    }

    @Transactional(readOnly = true)
    public List<Item> findAllItemsFromCategory(Long categoryId) {
        Category category = validateExistOrNot(categoryService.findById(categoryId));
        List<ItemCategory> foundItemCategories = new ArrayList<>();
        List<ItemCategory> allItemCategories = getAllItemCategories(category, foundItemCategories);
        return new ArrayList<>(allItemCategories.stream().map(ItemCategory::getItem).toList());
    }
    private List<ItemCategory> getAllItemCategories(Category category, List<ItemCategory> itemCategories) {
        if (validateParentOrNot(category)) {
            List<List<ItemCategory>> allItems = category.getChildCategories().stream().map(Category::getItems).toList();
            for (List<ItemCategory> itemCategoryList : allItems) {
                itemCategories.addAll(itemCategoryList);
            }
            return itemCategories;
        }
        itemCategories.addAll(category.getItems());
        return itemCategories;
    }

    @Transactional(readOnly = true)
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Long registerNewItem(ItemRegisterDto itemRegisterDto) throws InsufficientAuthenticationException {
        Item item = itemRepository.save(itemRegisterDto.toEntity());
        return item.getId();
    }

    public void deleteItem(User subject, Long itemId) throws InsufficientAuthenticationException {
        Item itemToBeDeleted = findItemById(itemId);
        validateAuthorization(itemToBeDeleted, subject);
        itemRepository.delete(itemToBeDeleted);
    }

    public Long updateItem(User subject, ItemUpdateDto itemUpdateDto) {
        Item updateItem = findItemById(itemUpdateDto.getId());
        validateAuthorization(updateItem, subject);
        updateItem.update(itemUpdateDto);
        return updateItem.getId();
    }
}

package shoppingmall.bookshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shoppingmall.bookshop.authentication.PrincipalDetails;
import shoppingmall.bookshop.dto.item.ItemRegisterDto;
import shoppingmall.bookshop.dto.item.ItemUpdateDto;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.service.ItemService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록
    @RequestMapping(value = "/admin/item/register", method = POST)
    public ResponseEntity<Item> registerItem(@RequestBody ItemRegisterDto itemRegisterDto) {
        Item newItem = itemRegisterDto.toEntity();
        itemService.findItemByTitle(newItem.getTitle());
        itemService.registerNewItem(itemRegisterDto);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(newItem, httpHeaders, HttpStatus.OK);
    }

    // 상품 삭제
    @RequestMapping(value = "/admin/item/delete", method = DELETE)
    public void deleteItem(@RequestParam("id") Long itemId, @AuthenticationPrincipal PrincipalDetails principal) {
        itemService.deleteItem(principal.getUser(), itemId);
    }

    // 상품 정보 수정
    @RequestMapping(value = "/admin/item/update", method = POST)
    public Long updateItemInfo(@RequestBody ItemUpdateDto itemUpdateDto, @AuthenticationPrincipal PrincipalDetails principal) {
        return itemService.updateItem(principal.getUser(), itemUpdateDto);
    }

    // 등록되어 있는 전체 상품 엔티티 get
    @RequestMapping(value = "/v1/item", method = GET)
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> itemList = itemService.findAll();

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(itemList, httpHeaders, HttpStatus.OK);
    }

    // 특정 상품 엔티티 get
    @RequestMapping(value = "/v1/item/{id}", method = GET)
    public ResponseEntity<Item> getItemById(@PathVariable("id") Long itemId) {
        Item item = itemService.findItemById(itemId);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(item, httpHeaders,HttpStatus.OK);
    }

    // 카테고리에 있는 모든 상품 엔티티 get
    @RequestMapping(value = "v1/item/category", method = GET)
    public ResponseEntity<List<Item>> getAllItemsInCategory(@RequestParam Long categoryId) {
        List<Item> itemList = itemService.findAllItemsFromCategory(categoryId);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(itemList, httpHeaders, HttpStatus.OK);

    }
}

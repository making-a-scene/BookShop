package shoppingmall.bookshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.dto.AdminRegisterDto;
import shoppingmall.bookshop.dto.ItemRegisterDto;
import shoppingmall.bookshop.dto.ItemUpdateDto;
import shoppingmall.bookshop.dto.UserRegisterDto;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.service.ItemService;
import shoppingmall.bookshop.service.UserService;

import java.util.Date;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ItemServiceTest {

    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemServiceTest(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @Test
    public void ItemTest() {

        /* given */

        // admin 계정 생성
        AdminRegisterDto adminRegisterDto = new AdminRegisterDto("testAdminId", "testAdminPw", "testPassword", Role.ROLE_SUPER);
        User admin = adminRegisterDto.toEntity();
        userService.register(admin);

        // 일반 유저 계정 생성
        UserRegisterDto userRegisterDto = new UserRegisterDto("testUserId", "", "testUserPw", "userNickname", "userEmail");
        User user = userRegisterDto.toEntity();
        userService.register(user);


        /* when */

        // admin 유저가 상품 등록
        Long itemId = itemService.registerNewItem( new ItemRegisterDto("title", "author", "summery", new Date(221018), 10000, admin));
        Assertions.assertThat(itemService.findItemById(itemId)).isInstanceOf(Item.class);

        // 일반 유저는 상품을 등록할 수 없음.
        assertThrows(InsufficientAuthenticationException.class,
                () -> {itemService.registerNewItem(new ItemRegisterDto("title2", "author2", "summery2", new Date(221018), 10000, user));});


        /* then */

        // admin의 상품 정보 수정
        Long newItemId = itemService.updateItem(admin, new ItemUpdateDto(itemId, "newTitle", "newAuthor", "newSummery", 20000));
        Item newItem = itemService.findItemById(newItemId);

        Assertions.assertThat(newItemId).isSameAs(itemId);
        Assertions.assertThat(newItem.getTitle()).isEqualTo("newTitle");
        Assertions.assertThat(newItem.getAuthor()).isEqualTo("newAuthor");
        Assertions.assertThat(newItem.getSummery()).isEqualTo("newSummery");
        Assertions.assertThat(newItem.getPrice()).isEqualTo(20000);

        // 일반 유저는 상품을 수정할 수 없음
        assertThrows(InsufficientAuthenticationException.class,
                () -> {itemService.updateItem(user, new ItemUpdateDto(itemId, "newTitle2", "newAuthor2", "newSummery2", 30000));});



        // 일반 유저는 상품을 삭제할 수 없음
        assertThrows(InsufficientAuthenticationException.class,
                () -> {itemService.deleteItem(user, itemId);});

        // admin의 상품 삭제
        assertThrows(NoSuchElementException.class,
                () -> {itemService.deleteItem(admin, itemId);
                        itemService.findItemById(itemId);});


        // 삭제되어 존재하지 않는 상품 찾을 시 예외 발생
        assertThrows(NoSuchElementException.class,
                () -> {itemService.findItemById(itemId);});


    }
}

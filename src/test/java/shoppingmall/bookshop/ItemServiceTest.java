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
import shoppingmall.bookshop.dto.account.AdminRegisterDto;
import shoppingmall.bookshop.dto.category.RegisterChildCategoryDto;
import shoppingmall.bookshop.dto.category.RegisterParentCategoryDto;
import shoppingmall.bookshop.dto.item.ItemRegisterDto;
import shoppingmall.bookshop.dto.item.ItemUpdateDto;
import shoppingmall.bookshop.dto.account.UserRegisterDto;
import shoppingmall.bookshop.entity.Category;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.service.CategoryService;
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
    private final CategoryService categoryService;

    @Autowired
    public ItemServiceTest(ItemService itemService, UserService userService, CategoryService categoryService) {
        this.itemService = itemService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Test
    public void ItemTest() {

        /* given */

        // admin 계정 생성
        AdminRegisterDto adminRegisterDto = new AdminRegisterDto("testAdminId", "testAdminPw", "testPassword", Role.ROLE_ADMIN);
        User admin = adminRegisterDto.toEntity();
        userService.register(admin);

        // 일반 유저 계정 생성
        UserRegisterDto userRegisterDto = new UserRegisterDto("testUserId", "", "testUserPw", "userNickname", "userEmail");
        User user = userRegisterDto.toEntity();
        userService.register(user);


        /* when */

        // 부모 카테고리 생성
        RegisterParentCategoryDto parentCategoryDto = new RegisterParentCategoryDto("testParentCategory");
        Category parentCategory = parentCategoryDto.toEntity();

        // 자식 카테고리 생성, 부모 카테고리를 parentCategory로 설정.
        RegisterChildCategoryDto childCategoryDto = new RegisterChildCategoryDto("testChildCategory", parentCategory);
        Category childCategory = childCategoryDto.toEntity();

        // 부모, 자식 카테고리 간의 양방향 연관 관계가 잘 설정되었는지 확인.
        Assertions.assertThat(childCategory.getParent()).isEqualTo(parentCategory);
        Assertions.assertThat(childCategory).isIn(parentCategory.getChildCategories());


        // admin 유저가 상품 등록, childCategory에 저장.
        Long itemId = itemService.registerNewItem( new ItemRegisterDto("title", "author", "summery", new Date(221018),10000, admin, childCategory));
        Item newItem = itemService.findItemById(itemId);
        Assertions.assertThat(newItem).isInstanceOf(Item.class);
        Assertions.assertThat(newItem).isIn(childCategory.getIncludingItems());
        Assertions.assertThat(newItem.getIncludedCategory()).isEqualTo(childCategory);

        /* then */

        // admin의 상품 정보 수정
        Long updatedItemId = itemService.updateItem(admin, new ItemUpdateDto(itemId, "newTitle", "newAuthor", "newSummery", 20000, childCategory));
        Item updatedItem = itemService.findItemById(updatedItemId);

        Assertions.assertThat(updatedItemId).isSameAs(itemId);
        Assertions.assertThat(newItem.getTitle()).isEqualTo("newTitle");
        Assertions.assertThat(newItem.getAuthor()).isEqualTo("newAuthor");
        Assertions.assertThat(newItem.getSummery()).isEqualTo("newSummery");
        Assertions.assertThat(newItem.getPrice()).isEqualTo(20000);


        // admin의 상품 삭제
        assertThrows(NoSuchElementException.class,
                () -> {itemService.deleteItem(admin, itemId);
                        itemService.findItemById(itemId);});


        // 삭제되어 존재하지 않는 상품 찾을 시 예외 발생
        assertThrows(NoSuchElementException.class,
                () -> {itemService.findItemById(itemId);});


    }


}

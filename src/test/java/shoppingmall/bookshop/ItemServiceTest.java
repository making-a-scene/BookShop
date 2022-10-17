package shoppingmall.bookshop;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.dto.AdminRegisterDto;
import shoppingmall.bookshop.entity.Item;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.repository.ItemRepository;
import shoppingmall.bookshop.service.ItemService;

public class ItemServiceTest {
    private final ItemRepository itemRepository = new SimpleJpaRepository<Item, Long>();

    private final ItemService itemService = new ItemService();

    @Test
    public void ItemServiceTest() {

        AdminRegisterDto registerDto = new AdminRegisterDto("testId", "password", "testPassword", Role.ROLE_SUPER);

        User admin = registerDto.toEntity();





    }
}

package shoppingmall.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shoppingmall.bookshop.dto.category.CategoryNameUpdateDto;
import shoppingmall.bookshop.dto.category.RegisterChildCategoryDto;
import shoppingmall.bookshop.dto.category.RegisterParentCategoryDto;
import shoppingmall.bookshop.entity.Category;
import shoppingmall.bookshop.service.CategoryService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    // 전체 부모 카테고리 조회
    @RequestMapping(value = "/category/all", method = GET)
    public ResponseEntity<List<Category>> getAllParents() {
        List<Category> allParents = categoryService.findAllParents();

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(allParents, httpHeaders, OK);
    }

    // 특정 자식 카테고리의 부모 카테고리 조회
    @RequestMapping(value = "/category/parent", method = GET)
    public ResponseEntity<Category> getChildsParent(@RequestParam("id") Long childId) {
        Category parent = categoryService.findById(childId);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        log.info("부모 카테고리 조회 결과를 반환합니다.");
        return new ResponseEntity<>(parent, httpHeaders, OK);

    }

    // 특정 부모 카테고리의 자식 카테고리 모두 조회
    @RequestMapping(value = "/category/all/child", method = GET)
    public ResponseEntity<List<Category>> getAllChildFromParent(@RequestParam Long parentId) {
        List<Category> allChildByParent = categoryService.findAllChildByParent(parentId);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(allChildByParent, httpHeaders, OK);
    }

    // 부모 카테고리 생성8
    @RequestMapping(value = "/admin/category/new/parent", method = POST)
    public ResponseEntity<Category> makeNewParent(@RequestBody RegisterParentCategoryDto registerParentDto) {
        Long categoryId = categoryService.registerParentCategory(registerParentDto);
        Category category = categoryService.findById(categoryId);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(category, httpHeaders, OK);
    }

    // 자식 카테고리 생성
    @RequestMapping(value = "/admin/category/new/child", method = POST)
    public ResponseEntity<Category> makeNewChild(@RequestBody RegisterChildCategoryDto registerChildDto) {
        Long categoryId = categoryService.registerChildCategory(registerChildDto);
        Category category = categoryService.findById(categoryId);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(category, httpHeaders, OK);
    }

    // 카테고리 삭제
    @RequestMapping(value = "/admin/category/remove", method = DELETE)
    public String deleteCategory(@RequestParam(name = "id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return "삭제 완료";
    }

    // 카테고리명 변경
    @RequestMapping(value = "/admin/category/update/name", method = POST)
    public ResponseEntity<Category> changeCategoryName(@RequestBody CategoryNameUpdateDto nameUpdateDto) {
        Long id = categoryService.updateCategoryName(nameUpdateDto);
        Category changedCategory = categoryService.findById(id);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(changedCategory, httpHeaders, OK);
    }
}

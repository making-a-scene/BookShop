package shoppingmall.bookshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.bookshop.dto.category.CategoryNameUpdateDto;
import shoppingmall.bookshop.dto.category.ChangeParentCategoryDto;
import shoppingmall.bookshop.dto.category.RegisterChildCategoryDto;
import shoppingmall.bookshop.dto.category.RegisterParentCategoryDto;
import shoppingmall.bookshop.entity.Category;

import shoppingmall.bookshop.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static shoppingmall.bookshop.validation.CategoryServiceRequestValidator.*;
import static shoppingmall.bookshop.validation.CategoryServiceRequestValidator.validateExistOrNot;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findAllParents() {
        List<Category> all = findAll();
        return all.stream().filter(Category::isParent).collect(Collectors.toList());
    }

    public List<Category> findAllChildByParent(Long parentId) {
        Category parent = validateExistOrNot(categoryRepository.findById(parentId));
        return parent.getChildCategories();
    }

    // 카테고리 등록(이미 존재하는 카테고리인지 확인) - 부모, 자식 카테고리 따로
    // 부모 카테고리 등록
    @Transactional
    public Long registerParentCategory(RegisterParentCategoryDto parentCategoryDto) {
        Category newParentCategory = parentCategoryDto.toEntity();

        validateDuplicatedOrNot(categoryRepository.findById(newParentCategory.getId()));
        return categoryRepository.save(newParentCategory).getId();
    }
    // 자식 카테고리 등록
    @Transactional
    public Long registerChildCategory(RegisterChildCategoryDto childCategoryDto) {
        Category newChildCategory = childCategoryDto.toEntity();

        validateDuplicatedOrNot(categoryRepository.findById(newChildCategory.getId()));
        validateExistOrNot(findById(newChildCategory.getParent().getId()));
        newChildCategory.setCategoryRelationship(childCategoryDto.getParent());

        return categoryRepository.save(newChildCategory).getId();
    }

    // 카테고리 삭제
    @Transactional
    public void deleteCategory(Long id) {
        Category category = validateExistOrNot(categoryRepository.findById(id));

        if (validateParentOrNot(category)) {
            validateEmptyParentOrNot(category);
        } else {
            validateEmptyChildOrNot(category);
        }
        categoryRepository.delete(category);
    }

    // 카테고리 이름 수정
    @Transactional
    public Long updateCategoryName(CategoryNameUpdateDto nameUpdateDto) {
        Category category = validateExistOrNot(findById(nameUpdateDto.getId()));
        category.updateCategoryName(nameUpdateDto.getCategoryName());

        return setRelationship(category);
    }
    private static Long setRelationship(Category category) {
        if (validateParentOrNot(category)) {
            List<Category> childCategories = category.getChildCategories();
            for (Category child : childCategories) {
                child.setCategoryRelationship(category);
            }
        } else {
            category.setCategoryRelationship(category.getParent());
        }
        return category.getId();
    }

    // 자식 카테고리가 속해 있는 부모 카테고리 변경
    @Transactional
    public void changeParent(ChangeParentCategoryDto changeParentCategoryDto) {
        Category child = validateExistOrNot(categoryRepository.findById(changeParentCategoryDto.getChildId()));
        if (validateParentOrNot(child)) {
            throw new IllegalStateException("상위 카테고리 변경은 하위 카테고리를 대상으로만 가능합니다.");
        }
        Category parent = changeParentCategoryDto.getParent();
        if(validateParentOrNot(parent)) {
            throw new IllegalStateException("하위 카테고리의 경우 상위 카테고리로 등록할 수 없습니다.");
        }
        child.setCategoryRelationship(parent);
    }


}

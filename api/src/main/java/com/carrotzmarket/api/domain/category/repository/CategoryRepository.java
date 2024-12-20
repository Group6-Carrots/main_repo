package com.carrotzmarket.api.domain.category.repository;

import com.carrotzmarket.db.category.CategoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 모든 카테고리를 조회하며, 부모-자식 관계를 한 번에 로딩합니다.
     * @return 카테고리 목록
     */
    public List<CategoryEntity> findAll() {
        return entityManager.createQuery(
                "SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.children", CategoryEntity.class)
                .getResultList();
    }

    /**
     * ID로 카테고리를 조회합니다.
     * @param id 조회할 카테고리의 ID
     * @return CategoryEntity
     */
    public Optional<CategoryEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(CategoryEntity.class, id));
    }

    /**
     * 카테고리명을 기준으로 카테고리를 조회합니다.
     * @param categoryName 카테고리명
     * @return CategoryEntity
     */
    public CategoryEntity findByName(String categoryName) {
        return entityManager.createQuery(
                "SELECT c FROM CategoryEntity c WHERE c.name = :categoryName", CategoryEntity.class)
                .setParameter("categoryName", categoryName)
                .getSingleResult();
    }

    /**
     * 키워드를 포함하는 카테고리를 조회합니다.
     * @param keyword 검색 키워드
     * @return 카테고리 목록
     */
    public List<CategoryEntity> findByNameContainingIgnoreCase(String keyword) {
        TypedQuery<CategoryEntity> query = entityManager.createQuery(
                "SELECT c FROM CategoryEntity c WHERE LOWER(c.name) LIKE :keyword", CategoryEntity.class);
        query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        return query.getResultList();
    }

    /**
     * 카테고리를 저장 또는 수정합니다.
     * @param category 저장할 카테고리 엔티티
     */
    @Transactional
    public void save(CategoryEntity category) {
        if (category.getId() == null) {
            entityManager.persist(category);
        } else {
            entityManager.merge(category);
        }
    }

    /**
     * 카테고리를 삭제합니다.
     * @param category 삭제할 카테고리 엔티티
     */
    @Transactional
    public void delete(CategoryEntity category) {
        entityManager.remove(entityManager.contains(category) ? category : entityManager.merge(category));
    }
}

package io.everyonecodes.deliciousnessness.repository;

import io.everyonecodes.deliciousnessness.dto.CategoryDto;
import io.everyonecodes.deliciousnessness.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<CategoryDto> findAllByOrderByNameAsc();

    Optional<Category> findByNameIgnoreCase(String name);
}

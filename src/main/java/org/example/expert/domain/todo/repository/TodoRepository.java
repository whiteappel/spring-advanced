package org.example.expert.domain.todo.repository;

import jakarta.persistence.Entity;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDateTime;
import java.util.Optional;

/*기존꺼 fetch join 기반의 n+1 해결하고 있다.
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    int countById(Long todoId);
}

 */

public interface TodoRepository extends JpaRepository<Todo, Long> {
    //유저에서 가져오는 거엿으니 속성으로 유저를 넣어주면된다.
    @EntityGraph(attributePaths ={"user"})
    @Query("SELECT t FROM Todo t ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths ={"user"})
    @Query("SELECT t FROM Todo t WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    int countById(Long todoId);
    @EntityGraph
    @Query("SELECT t FROM Todo t " +
            "WHERE (:weather is null or t.weather= :weather) " +
            "AND (:startAt is null or t.modifiedAt >= :startAt) " +
            "AND (:endAt is null or t.modifiedAt <= :endAt) " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findtodobyweather(
            @Param("weather") String weather,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            Pageable pageable
            );

}


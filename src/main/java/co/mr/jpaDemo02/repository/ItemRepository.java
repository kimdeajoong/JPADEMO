package co.mr.jpaDemo02.repository;

import co.mr.jpaDemo02.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
// jpaRepository는 CRUD/ Paging 위한 메소드가 정의 되어 있다.

// 지원 메소드 예
// save : 엔티티 저장 /수정
// delete : 엔티티 삭제
// count: 엔티티 총개수
// findAll : 모든 엔티티 조회

// ** QuerydslPredicateExecutor 상속
//        Predicate : 자바코드로 조건문 표현할 수 있고 조건문들을 조합가능하다.
//        Optional<T> findOne(Predicate predicate);
//        Iterable<T> findAll(Predicate predicate);
//        Iterable<T> findAll(Predicate predicate, Sort sort);
//        Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);
//        Iterable<T> findAll(OrderSpecifier<?>... orders);
//        Page<T> findAll(Predicate predicate, Pageable pageable);
//        long count(Predicate predicate);
//        boolean exists(Predicate predicate);


                                                // 엔티티타입, 기본키타입
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
    // 쿼리메소드는 Spring Data JPA의 핵심 기능 중 하나
    // Repository 인터페이스에 간단한 네이밍 규칙을 이용하여 메소드를 작성하면
    // 쿼리를 실행 할 수 있다. find 가장 많이 사용
    // find + (엔티티명) + By + 변수명(필드명) : 엔티티명은 보통 생략한다.
    // findItemByItemName ----> findByItemName

    // 쿼리메소드 단점
    // 조건이 많아지면 메소드명이 길어져서, 해석하기가 힘들다

    // 주로 간단한 처리시에 유용하게 사용, 복잡한쿼리는 부적합
    List<Item> findByItemName(String itemName);

    // Or 조건
    List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail);

    // LessThan 조건 (<), LessThanEqual (<=)
    List<Item> findByPriceLessThan(Integer price);

    // OrderBy desc : OrderBy+속성명+Desc(Asc)
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // @Query
    // 쿼리메소드의 단점을 보완하기 위해서 Spring Data JPA에서 제공
    // 이 어노테이션을 이용하면 SQL과 유사한 JPQL(Java Persistence Query Language)을
    // 사용할 수 있다. JPQL(객체지향쿼리언어)

    // SQL은 데이터베이스의 테이블을 대상으로 쿼리를 수행
    // JPQL은 엔티티 객체를 대상으로 쿼리 수행
    // JPQL로 쿼리 작성시 데이터베이스가 변경되어도 애플리케이션은 영향을 받지 않는다.

    // @Query 안에 JPQL로 작성한 쿼리문을 넣어준다.
    // from 뒤에는 엔티티 클래스명, Item을 i 별칭으로 사용
    // Item 객체로 부터 Item 데이터를 select하겠다는 의미

    // @Param은 넘어오는 파라미터값("sample 상품 상세")을
    // 지정한 변수(itemDetail)로 JPQL에 전달

    @Query("select i from Item i where i.itemDetail like "+
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    // 네이티브 쿼리
    // @Query의 nativeQuery 속성을 사용하면 기존의 데이터베이스에서 사용하던
    // 쿼리를 그대로 사용 가능, 이 경우 특정데이터베이스에 종속된다.(독립성 상실)
    // 통계용 쿼리처럼 복잡한 쿼리를 그대로 사용해야 하는 경우 활용 할 수 있다.

    @Query(value="select * from item i where i.item_detail like " +
            "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailNative(@Param("itemDetail") String itemDetail);

}

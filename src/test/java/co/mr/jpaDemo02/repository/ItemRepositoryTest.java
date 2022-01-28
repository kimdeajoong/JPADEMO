package co.mr.jpaDemo02.repository;

import co.mr.jpaDemo02.constant.ItemSellStatus;
import co.mr.jpaDemo02.entity.Item;
import co.mr.jpaDemo02.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// 통합테스트를 위해 스프링 부트에서 제공하는 어노테이션
// 실제 애플리케이션을 구동할 때처럼 모든 Bean을 IoC 컨테이너에 등록한다.
// 애플리케이션 규모가 크면 속도가 느려질 수 있다.

@SpringBootTest
@TestPropertySource(locations = "classpath:application02.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    //@PersistenceContext을 이용하면 EntityManager 빈을 주입 받는다.
    @PersistenceContext
    EntityManager em;

    @Test // 테스트할 메소드 위에 선언하면 해당 메소드를 테스트 대상으로 지정한다.
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("sample 상품 상세");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item saveItem = itemRepository.save(item);
        System.out.println(saveItem.toString());
    }

    // 10개의 상품 데이터 생성
    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemName("Sample 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("sample 상품 상세" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item saveItem = itemRepository.save(item);
        }
    }

    // 쿼리메소드 이용
    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemTest() {
        this.createItemList(); // 테스트 상품 저장
        List<Item> itemList = itemRepository.findByItemName("Sample 상품7");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    // 상품명과 상품상세를 OR조건으로 조회하는 쿼리메소드
    @Test
    @DisplayName("상품명, 상품상세 OR 조건 테스트")
    public void findByItemNameOrItemDetailTest() {
        this.createItemList(); // 테스트 상품 저장
        List<Item> itemList = itemRepository.findByItemNameOrItemDetail("Sample 상품3", "Sample 상품 상세7");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList(); // 테스트 상품 저장
        List<Item> itemList = itemRepository.findByPriceLessThan(10007);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList(); // 테스트 상품 저장
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query을 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("sample 상품 상세");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery를 이용한 상품 조회 테스트")
    public void findByItemDetailNative() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailNative("Sample 상품 상세");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    // @Query 어노테이션에도 단점은 동적쿼리 작성이 안됨

    // Querydsl
    // 조건에 맞게 동적으로 쿼리를 생성
    // 문자열이 아닌 자바 소스코드로 작성하기 때문에 컴파일 시점에 오류를 발견
    // 자바 소스코드로 작성하면 자동완성기능 지원으로 생산성 향상

    // Querydsl 기본 셋팅
    // pom.xml 설정
    // Qdomain이라는 자바코드를 생성하는 플러그인을 추가
    // Querydsl을 통해서 쿼리를 생성할 때 Qdomain객체를 사용한다.

    // maven compile 후 빌드완료(BUILD SUCCESS)후 target 폴더 생성확인
    // generated-sources 폴더에 QItem클래스 생성확인
    // QItem 클래스에는 Item 클래스의 모든 필드들에 대해서 사용가능한 opertation을 호출할 수
    // 있는 메서드가 정의됨.

    @Test
    @DisplayName("Querydsl 조회 테스트")
    public void querydslTest() {
        this.createItemList();
        // Querydsl을 이용해서 쿼리를 생성하기 위해서는 JPAQueryFactory 필요함.
        // JPAQueryFactory사용하면 EntityManager를 통해서 쿼리를 처리할 수 있다.(JPQL사용)
        // SQLQueryFactory는 JDBC를 통해서 쿼리를 처리한다. (SQL 사용)
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        // selectFrom()호출하면 JPAQuery를 리턴하게되고
        // 쿼리를 build를 시작하게된다.
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"Sample 상품 상세"+"%"))
                .orderBy(qItem.price.desc());

        // JPAQuery 데이터 반환 메소드
        // List<T> fetch() : 조회결과 리스트 반환
        // T fetchOne() : 조회대상이 1건인 경우 제네릭으로 지정한 타입반환
        // T fetchFirst() : 조회대상 중 1건만 반환
        // Long fetchCount() : 조회대상 개수 반환
        // QueryResult<T> fetchResults() : 조회한 리스트와 전체 개수를 포함한 QueryResults 반환

        List<Item> itemList = query.fetch();
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    public void createItemList2() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemName("Sample 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("sample 상품 상세" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item saveItem = itemRepository.save(item);
        }

        for (int i = 11; i <= 15; i++) {
            Item item = new Item();
            item.setItemName("Sample 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("sample 상품 상세" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item saveItem = itemRepository.save(item);
        }
    }
    @Test
    @DisplayName("QueryDsl 조회 테스트 2")
    public void queryDslTest2() {
        this.createItemList2();

        // 쿼리에 들어갈(where문) 조건을 넣어주는 컨테이너라고 생각하면됨
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "Sample 상품 상세";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        // 상품의 판매상태가 SELL일 때만 BooleanBuilder에 판매상태를 동적으로 추가한다.
        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }
        // 데이터를 페이징처리해서 0 부터 5개를 보여주는 Pageable객체를 생성
        Pageable pageable = PageRequest.of(0, 5);

        Page<Item> itemPaginResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPaginResult.getTotalElements());

        List<Item> resultItemList = itemPaginResult.getContent();
        for (Item resultItem : resultItemList) {
            System.out.println(resultItem.toString());
        }
    }
}
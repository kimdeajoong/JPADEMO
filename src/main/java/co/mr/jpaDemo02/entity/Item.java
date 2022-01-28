package co.mr.jpaDemo02.entity;

import co.mr.jpaDemo02.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter @Setter
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id; // 상품코드

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber; // 재고수량

    // CLOB(Character Large Object) : 사이즈가 큰 데이터를 외부파일로 저장하기 위한 데이터 타입,
    // 문자형 대용량 파일을 저장하는데 사용하는 데이터 타입
    // BLOB(Binary Large Object) : 바이너리 데이터를 DB외부에 저장하기 위한 타입
    // (이미지, 사운드, 비디오 같은 멀티미디어 데이터를 다룰 때 사용)

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    // EnumType.ORDINAL,EnumType.STRING 둘중의 하나가 지정된다.
    // ORDINAL은 순서가 저장, STRING은 문자열이 저장된다.
    // 예> EnumType.ORDINAL 지정후
    //     ItemSellStatus.SELL은 DB에 저장되는 값이 1
    //    EnumType.STRING 지정후
    //     ItemSellStatus.SELL은 DB에 저장되는 값이 "SELL"문자열 자체로 저장

    // ORDINAL은 순서가 바뀌면 문제의 소지가 많다.
    // STRING으로 지정하는 것이 순서에 상관없이 명확하다. 주로 사용
    @Enumerated(EnumType.STRING) // enum 타입 매핑
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    private LocalDateTime regTime; // 등록 시간

    private LocalDateTime updateTime; // 수정시간
}

package co.mr.jpaDemo02.dto;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ItemDto {

    private Long id;
    private String name;
    private Integer price;
    private String detail;
    private String sellStatCd;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;


}

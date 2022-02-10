package co.mr.jpaDemo02.model;


import lombok.Data;

@Data
public class EventInfo {

    private String eventSeq; // 행사일련번호

    private String placeCdNm; // 장소명
    private String targetCdNm; // 행사 대상연령대
    private String themeCdNm; // 테마명
    private String title; // 행사명
    private String beginDt; // 행사시작일
    private String endDt; // 행사종료일
    private String themeCd; // 테마코드값
    private String placeCd; // 장소코드값
    private String beginTm; // 행사시작시간
    private String endTm; // 행사종료시간
    private String placeDetail; // 세부장소
    private String opmtnInstt; // 주최기관
    private String homepageAdd; // 홈페이지 주소
    private String prpleHoldYn; // 주차장 보유여부

    private String targetCd; // 대상코드값
    private String managementCdNm; // 행사주관값

}

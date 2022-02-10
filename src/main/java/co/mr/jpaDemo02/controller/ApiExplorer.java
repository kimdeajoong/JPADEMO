package co.mr.jpaDemo02.controller;/* Java 1.8 샘플 코드 */


import co.mr.jpaDemo02.model.EventInfo;
import com.google.gson.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ApiExplorer {

    @GetMapping("/apiExplorer")
    public String apiExplorer(@RequestParam(value="pageNo", required = false, defaultValue = "1") String pageNo, Model model) throws IOException {
//        String date = "2022011010";
//
//        LocalDateTime now = LocalDateTime.now();
//
//        String curDateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
//
//        System.out.println(curDateTime);

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6300000/eventDataService/eventDataListJson"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=cYA00vVd%2BKZRqX39OholhY63hC93QWNDfhMc5HQOQfKbIgevHk9HtNXokfmYl2bWrukvSS%2FT8OCnYNCOOVtZ%2Fg%3D%3D"); /*Service Key*/
//        urlBuilder.append("&" + URLEncoder.encode("","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /**/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8")); /* 페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10","UTF-8")); /*페이지당 레코드 수*/
//        urlBuilder.append("&" + URLEncoder.encode("title","UTF-8") + "=" + URLEncoder.encode("피아노","UTF-8")); /* 행사 제목 검색키워드*/
//        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON","UTF-8"));
        /*urlBuilder.append("&" + URLEncoder.encode("beginDt","UTF-8") + "=" + URLEncoder.encode("2017-01-01","UTF-8")); *//*행사시작일*//*
        urlBuilder.append("&" + URLEncoder.encode("endDt","UTF-8") + "=" + URLEncoder.encode("2017-01-02","UTF-8")); *//*행사 종료일*//*
        urlBuilder.append("&" + URLEncoder.encode("searchCondition","UTF-8") + "=" + URLEncoder.encode("C0101","UTF-8")); *//*테마분류코드*//*
        urlBuilder.append("&" + URLEncoder.encode("searchCondition1","UTF-8") + "=" + URLEncoder.encode("C0205001","UTF-8")); *//* 장소분류 코드*//*
        urlBuilder.append("&" + URLEncoder.encode("searchCondition2","UTF-8") + "=" + URLEncoder.encode("C0306","UTF-8")); *//* 대상분류코드*//*
        urlBuilder.append("&" + URLEncoder.encode("searchCondition3", "UTF-8") + "=" + URLEncoder.encode("C0445","UTF-8")); *//* 주관분류코드*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
//        System.out.println(sb.toString());

        // 문자열 객체로 만들어준다 네트워크를 통해 들어오는 데이터는 일반 텍스트
        String jsonData = sb.toString();

        // 필요 데이터만을 처리하기 위한 파싱 작업
        JsonElement jsonElement = JsonParser.parseString(jsonData);

        // 자바 json 객체
        JsonObject object = jsonElement.getAsJsonObject();

        JsonObject msgHeader = object.get("msgHeader").getAsJsonObject();
//        System.out.println(msgHeader.get("pageNo"));

        String totalCount = msgHeader.get("totalCount").toString();
//        System.out.println(totalCount);
        int intTotalCount = Integer.parseInt(totalCount);

        String numOfRows = msgHeader.get("numOfRows").toString();
        int intNumOfRows = Integer.parseInt(numOfRows);

//        System.out.println(msgHeader);

        JsonArray arrayData = object.get("msgBody").getAsJsonArray();

        Gson gson = new Gson();
        List<EventInfo> eventInfos = new ArrayList<EventInfo>();

        for (int i=0; i < arrayData.size(); i++){
            System.out.println(arrayData.get(i));

            EventInfo eventInfo = gson.fromJson(arrayData.get(i), EventInfo.class);
            eventInfos.add(eventInfo);
        }

        // 페이징
        // 전체페이지 수
        int totalPages = (int) Math.ceil(intTotalCount * 1.0 / intNumOfRows);

        int blockSize = 10;
        int intPageNo = Integer.parseInt(pageNo);

        // 현재 블럭의 위치
        int blockNum = (intPageNo - 1) / blockSize;

        // 블럭의 시작값 : 1, 6, 11, 16..
        int blockStart = (blockSize * blockNum)+1;

        // 블럭의 마지막 값 : 5, 10, 15, 20...
        int blockEnd = blockStart + (blockSize - 1);

        if(blockEnd > totalPages) blockEnd = totalPages;

        int prevPage = blockStart - 1;
        int nextPage = blockEnd + 1;
        if(nextPage>totalPages) nextPage = totalPages;

        model.addAttribute("pageNo",intPageNo);
        model.addAttribute("prevPage",prevPage);
        model.addAttribute("nextPage",nextPage);
        model.addAttribute("blockStart",blockStart);
        model.addAttribute("blockEnd",blockEnd);

        model.addAttribute("totalCount",totalCount);
        model.addAttribute("totalPages",totalPages);

        model.addAttribute("eventInfos",eventInfos);

        return "performance";

    }
}
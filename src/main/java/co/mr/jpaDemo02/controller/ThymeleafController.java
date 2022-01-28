package co.mr.jpaDemo02.controller;


import co.mr.jpaDemo02.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/thymeEx")
public class ThymeleafController  {

    @GetMapping("/test01")
    public String test01(Model model){
        model.addAttribute("data","타임리프 스터디");
        return "thymeEx/test01";
    }

    @GetMapping("/test02")
    public String test02(Model model){
        ItemDto itemDto = new ItemDto();
        itemDto.setDetail("상품 상세 설명");
        itemDto.setName("상품 1");
        itemDto.setPrice(100000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto",itemDto);
        return "thymeEx/test02";
    }

    @GetMapping("/test03")
    public String test03(Model model){
        ArrayList<ItemDto> itemList = new ArrayList<>();

        for(int i = 1; i <= 10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setDetail("상품상세"+i);
            itemDto.setName("상품"+i);
            itemDto.setPrice(10000+i);
            itemDto.setRegTime(LocalDateTime.now());

            itemList.add(itemDto);
        }
        model.addAttribute("itemList",itemList);
        return "thymeEx/test03";

    }

    @GetMapping("/test04")
    public String test04(Model model){
        ArrayList<ItemDto> itemList = new ArrayList<>();

        for(int i = 1; i <= 10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setDetail("상품상세"+i);
            itemDto.setName("상품"+i);
            itemDto.setPrice(10000+i);
            itemDto.setRegTime(LocalDateTime.now());

            itemList.add(itemDto);
        }
        model.addAttribute("itemList",itemList);
        return "thymeEx/test04";

    }

    @GetMapping("/test05")
    public String test05(){
        return "thymeEX/test05";
    }

    @GetMapping("/test06")
    public String test06(String param1, String param2, Model model){
        model.addAttribute("param1",param1);
        model.addAttribute("param2",param2);
        return "thymeEx/test06";
    }

    @GetMapping("/test07")
    public String test07(){
        return "thymeEX/test07";
    }


}

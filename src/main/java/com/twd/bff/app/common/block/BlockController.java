package com.twd.bff.app.common.block;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/block")
public class BlockController {

    @GetMapping("/hidden-key")
    public String setHiddenKey(HttpServletRequest request, HttpServletResponse response) {

    	String key = request.getParameter("key");


    	Cookie setCookie = new Cookie("block_hidden_key", key);

    	setCookie.setMaxAge(60*60*24); // 기간을 하루로 지정
    	setCookie.setPath("/");

    	response.addCookie(setCookie);
        return "common/block/hidden-key";
    }
}

package pers.penglan.mail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 页面控制入口
 *
 * @Author PENGL
 * 2020-03-29
 */
@Controller
@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
public class PagesController {

    @RequestMapping(path = {"/", "/main"})
    public String main(HttpServletRequest request) {
        return "main";
    }
}

package com.superspeed.controller;

import com.superspeed.controller.base.BaseController;
import com.superspeed.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "demo")
public class DemoController extends BaseController {

    @Autowired
    private DemoService demoService;

    /**
     * 测试方法
     * @author xc.yanww
     * @date 2017/9/12 10:51
     * @return String
     */
    @RequestMapping(value = "sayHello")
    @ResponseBody
    public String service(String name) {
        return demoService.sayHello(name);
    }

}

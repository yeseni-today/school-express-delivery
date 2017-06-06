package com.delivery.web.manage;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.delivery.web.ManageContants.URL_MANAGE_PASSEXAMINE;

/**
 * Created by Finderlo on 2016/12/8.
 */
@Controller
public class ManageController {


    @RequestMapping("/sed/examine")
    public String hello() {
        return "sed/examine";
    }

    @RequestMapping("/sed/complaints")
    public String hello2() {
        return "sed/complaints";
    }
    @RequestMapping("/message/new")
    public String hello3() {
        return "message/new";
    }
    @RequestMapping("/sed/query")
    public String hello4() {
        return "sed/query";
    }

}



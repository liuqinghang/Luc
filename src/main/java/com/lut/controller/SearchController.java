package com.lut.controller;

import com.lut.domain.FileModel;
import com.lut.service.IKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@RequestMapping("/search")
@Controller
@ResponseBody
public class SearchController {

    @Autowired
    private IKService ikService;

    @GetMapping("/index")
    public String hello(){
        return "app/searchPage";
    }

    @GetMapping("/search")
    public String getTopDoc() {
        ArrayList<FileModel> resList =  ikService.getTopDoc("科技", "D:\\work\\javawork\\tika\\indexPath", 4);
        return resList.toString();
    }


}

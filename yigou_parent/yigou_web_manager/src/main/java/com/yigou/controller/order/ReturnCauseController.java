package com.yigou.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yigou.entity.PageResult;
import com.yigou.entity.Result;
import com.yigou.pojo.order.ReturnCause;
import com.yigou.service.order.ReturnCauseService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/returnCause")
public class ReturnCauseController {

    @Reference
    private ReturnCauseService returnCauseService;

    @GetMapping("/findAll")
    public List<ReturnCause> findAll(){
        return returnCauseService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<ReturnCause> findPage(int page, int size){
        return returnCauseService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<ReturnCause> findList(@RequestBody Map<String,Object> searchMap){
        return returnCauseService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<ReturnCause> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  returnCauseService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public ReturnCause findById(Integer id){
        return returnCauseService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody ReturnCause returnCause){
        returnCauseService.add(returnCause);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody ReturnCause returnCause){
        returnCauseService.update(returnCause);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        returnCauseService.delete(id);
        return new Result();
    }

}

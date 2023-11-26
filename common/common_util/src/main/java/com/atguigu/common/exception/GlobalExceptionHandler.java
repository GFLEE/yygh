package com.atguigu.common.exception;

import com.atguigu.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/*
* @ControllerAdvice一般和三个以下注解一块使用，起到不同的作用,

@ExceptionHandler: 该注解作用于方法上，，可以捕获到controller中抛出的一些自定义异常，统一进行处理，一般用于进行一些特定的异常处理。
@InitBinder:该注解作用于方法上,用于将前端请求的特定类型的参数在到达controller之前进行处理，从而达到转换请求参数格式的目的。
@ModelAttribute：该注解作用于方法和请求参数上，在方法上时设置一个值，可以直接在进入controller后传入该参数。*/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        log.error("捕捉到异常："+e.getMessage());
        return Result.fail();
    }


    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        return Result.fail().message("运算异常，执行了特定异常");
    }

    //自定义异常
    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e){
        e.printStackTrace();
        return Result.fail().message(e.getMsg()).code(e.getCode());
    }

}
package com.atguigu.controller;

import com.atguigu.common.result.Result;
import com.atguigu.common.utils.MD5;
import com.atguigu.hospital.model.hosp.HospitalSet;
import com.atguigu.hospital.vo.HospitalSetQueryVo;
import com.atguigu.service.HospitalSetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//医院设置接口
@Api(description = "医院设置接口")
@RestController
@CrossOrigin //跨域
@RequestMapping("/admin/hosp/hospitalSet")
@Slf4j
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //查询所有医院设置
    @ApiOperation(value = "医院设置列表")
    @GetMapping("findAll")
    public Result findAll() {
        log.error("测试打印错误日志");
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    @DeleteMapping("removeById/{id}")
    @ApiOperation(value = "根据ID删除")
    public Result removeById(@PathVariable String id) {
        var res = hospitalSetService.removeById(id);
        return Result.ok(res);
    }


    @ApiOperation(value = "分页医院设置列表")
    @GetMapping("pageList/{page}/{limit}")
    public Result pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {

//        int a = 10/0;
        Page<HospitalSet> pageParam = new Page<>(page, limit);

        hospitalSetService.page(pageParam, null);
        List<HospitalSet> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("total", total);
        res.put("rows", records);

        return Result.ok(res);
    }


    @ApiOperation(value = "分页条件医院设置列表")
    @PostMapping("pageQuery/{page}/{limit}")
    public Result pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "hospitalSetQueryVo", value = "查询对象", required = false)
            @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {

        Page<HospitalSet> pageParam = new Page<>(page, limit);

        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();

        if (hospitalSetQueryVo == null) {
            hospitalSetService.page(pageParam, queryWrapper);
        } else {
            String hosname = hospitalSetQueryVo.getHosname();
            String hoscode = hospitalSetQueryVo.getHoscode();

            if (!StringUtils.isEmpty(hosname)) {
                queryWrapper.like("hosname", hosname);
            }

            if (!StringUtils.isEmpty(hoscode)) {
                queryWrapper.eq("hoscode", hoscode);
            }
            hospitalSetService.page(pageParam, queryWrapper);
        }

        List<HospitalSet> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("total", total);
        res.put("rows", records);

        return Result.ok(res);
    }


    @ApiOperation(value = "新增医院设置")
    @PostMapping("saveHospSet")
    public Result save(
            @ApiParam(name = "hospitalSet", value = "医院设置对象", required = true)
            @RequestBody HospitalSet hospitalSet) {

        //设置状态 1 使用 0 不能使用
        hospitalSet.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));

        hospitalSetService.save(hospitalSet);
        return Result.ok();
    }

    @ApiOperation(value = "根据ID查询医院设置")
    @GetMapping("getHospSet/{id}")
    public Result getById(
            @ApiParam(name = "id", value = "医院设置ID", required = true)
            @PathVariable String id) {

        HospitalSet teacher = hospitalSetService.getById(id);

        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("item", teacher);
        return Result.ok(res);
    }

    @ApiOperation(value = "根据ID修改医院设置")
    @PostMapping("updateHospSet")
    public Result updateById(@ApiParam(name = "hospitalSet", value = "医院设置对象", required = true)
                             @RequestBody HospitalSet hospitalSet) {
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }


    //批量删除医院设置
    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }


    // 医院设置锁定和解锁
    @ApiOperation(value = "医院设置锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }


}

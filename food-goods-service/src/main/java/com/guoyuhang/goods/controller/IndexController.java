package com.guoyuhang.goods.controller;


import com.guoyuhang.enums.YesOrNo;
import com.guoyuhang.goods.service.CarouselService;
import com.guoyuhang.goods.service.CategoryService;
import com.guoyuhang.goods.utils.JsonUtils;
import com.guoyuhang.pojo.Carousel;
import com.guoyuhang.pojo.Category;
import com.guoyuhang.pojo.vo.CategoryVO;
import com.guoyuhang.pojo.vo.NewItemsVO;
import com.guoyuhang.goods.utils.GUOGEJSONResult;
import com.guoyuhang.goods.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public GUOGEJSONResult carousel() {
        List<Carousel> list = new ArrayList<>();

        String carousel = redisOperator.get("carousel");
        if(StringUtils.isBlank(carousel)){
           list = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        }else {
            list=JsonUtils.jsonToList(carousel,Carousel.class);

        }
        return GUOGEJSONResult.ok(list);

    }
    //删除的话 可以定时任务删除缓存   每个轮播图都是广告 每个广告都有一个过期时间，过期了再重置。（定时任务）

    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类，渲染展示到首页
     * 2. 如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类(一级分类)", notes = "获取商品分类(一级分类)", httpMethod = "GET")
    @GetMapping("/cats")
    public GUOGEJSONResult cats() {
        List<Category> list = categoryService.queryAllRootLevelCat();
        return GUOGEJSONResult.ok(list);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public GUOGEJSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {

        //防止被攻击
        if (rootCatId == null) {
            return GUOGEJSONResult.errorMsg("分类不存在");
        }

        List<CategoryVO> list = categoryService.getSubCatList(rootCatId);
        return GUOGEJSONResult.ok(list);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public GUOGEJSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return GUOGEJSONResult.errorMsg("分类不存在");
        }

        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return GUOGEJSONResult.ok(list);
    }

}

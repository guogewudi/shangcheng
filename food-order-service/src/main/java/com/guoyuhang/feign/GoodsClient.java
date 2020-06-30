package com.guoyuhang.feign;

import com.guoyuhang.feign.hystrix.GoodsClientHystrix;
import com.guoyuhang.feign.hystrix.UserClientHystrix;
import com.guoyuhang.pojo.Items;
import com.guoyuhang.pojo.ItemsSpec;
import com.guoyuhang.pojo.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-06-20 20:10
 */
@FeignClient(name = "goods",
        fallback = GoodsClientHystrix.class)
@Component
public interface GoodsClient {
    @GetMapping(value = "/items/queryItemById")
    public Items queryItemById(
            @RequestParam String itemId) ;

    @GetMapping(value = "/items/queryItemMainImgById")
    public String queryItemMainImgById(
            @RequestParam String itemId) ;

    @RequestMapping(value = "/items/decreaseItemSpecStock",method = RequestMethod.GET)
    public void decreaseItemSpecStock(@RequestParam  String specId,@RequestParam int buyCounts) ;

    @RequestMapping(value = "/items/itemsSpecs",method = RequestMethod.GET)
    public ItemsSpec queryItemSpecById(@RequestParam String specId) ;
}

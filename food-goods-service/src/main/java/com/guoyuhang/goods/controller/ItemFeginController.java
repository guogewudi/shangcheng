package com.guoyuhang.goods.controller;

import com.guoyuhang.goods.service.impl.ItemServiceImpl;
import com.guoyuhang.goods.utils.GUOGEJSONResult;
import com.guoyuhang.pojo.Items;
import com.guoyuhang.pojo.ItemsSpec;
import com.guoyuhang.pojo.vo.CommentLevelCountsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-06-20 20:54
 */

@Api(value = "商品接口feign", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemFeginController {
    @Autowired
    ItemServiceImpl itemService;


    @GetMapping("/queryItemById")
    public Items queryItemById(@RequestParam String itemId) {
        Items items = itemService.queryItemById(itemId);
        return items;
    }

    @GetMapping("/queryItemMainImgById")
    public String queryItemMainImgById(@RequestParam String itemId) {
        String s = itemService.queryItemMainImgById(itemId);
        return s;
    }

    @GetMapping("/decreaseItemSpecStock")
    public void decreaseItemSpecStock(@RequestParam String specId,@RequestParam int buyCounts) {
        itemService.decreaseItemSpecStock(specId,buyCounts);
    }

    @GetMapping("/itemsSpecs")
    public ItemsSpec queryItemSpecById(@RequestParam String specId) {
        ItemsSpec itemsSpec = itemService.queryItemSpecById(specId);
        return itemsSpec;
    }
}

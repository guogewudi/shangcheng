package com.guoyuhang.feign.hystrix;

import com.guoyuhang.feign.GoodsClient;
import com.guoyuhang.pojo.Items;
import com.guoyuhang.pojo.ItemsSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-06-20 20:28
 */
@Component
@Slf4j
public class GoodsClientHystrix implements GoodsClient {
    @Override
    public Items queryItemById(String itemId) {
        log.info("queryItemById error,go hystrix");
        return null;
    }

    @Override
    public String queryItemMainImgById(String itemId) {
        log.info("queryItemMainImgById error,go hystrix");
        return null;
    }

    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {
        log.info("decreaseItemSpecStock error,go hystrix");
    }

    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        log.info("queryItemSpecById error,go hystrix");
        return null;
    }
}

package com.guoyuhang.service;

import com.guoyuhang.pojo.OrderStatus;
import com.guoyuhang.pojo.bo.SubmitOrderBO;
import com.guoyuhang.pojo.vo.OrderVO;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-04-24 1:08
 */
@Service
public interface OrderService {

    /**
     * 用于创建订单相关信息
     * @param submitOrderBO
     */
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    public OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    public void closeOrder();
}

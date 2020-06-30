package com.guoyuhang.controller;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-04-24 0:41
 */


import com.guoyuhang.enums.OrderStatusEnum;
import com.guoyuhang.enums.PayMethod;
import com.guoyuhang.goods.utils.GUOGEJSONResult;
import com.guoyuhang.pojo.OrderStatus;
import com.guoyuhang.pojo.bo.SubmitOrderBO;
import com.guoyuhang.pojo.vo.OrderVO;
import com.guoyuhang.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 订单结算  前往支付 ->待付款或->取消订单   支付成功，待发货
 * 支付成功则 订单创建完成
 *
 *
 * 复杂订单状态设计
 * 往往有异步回调 ，显示付款中，有延时
 * 待付款 -- 付款中 -- 取消订单（金额回退）--交易关闭
 * 代付款 --  付款中 -- 付款成功  -- 取消订单，待退款（需要人工审核）--已退款
 *                              -- 已发货 -- 确认收货 --取消订单，待退款（需要人工审核）--已退款
 *                              -- 已发货 -- 确认收货 -- 订单完成
 *
 * ！注意 订单中的地址是用户自己填写的快照，不可以关联 address 表。因为订单中地址是不可以更改的。
 *
 *
 * 订单状态表 10 待付款 20已付款 30 已发货 40交易成功 50 交易关闭
 *
 */

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
public class OrdersController extends BaseController {

    final static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrderService orderService;

//    @Autowired
//    private RestTemplate restTemplate;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public GUOGEJSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type ) {
            return GUOGEJSONResult.errorMsg("支付方式不支持！");
        }

        System.out.println(submitOrderBO.toString());

        // 1. 创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
//        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
//        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
//        merchantOrdersVO.setReturnUrl(payReturnUrl);
//
//        // 为了方便测试购买，所以所有的支付金额都统一改为1分钱
//        merchantOrdersVO.setAmount(1);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("imoocUserId","imooc");
//        headers.add("password","imooc");
//
//        HttpEntity<MerchantOrdersVO> entity =
//                new HttpEntity<>(merchantOrdersVO, headers);
//
//        ResponseEntity<GUOGEJSONResult> responseEntity =
//                restTemplate.postForEntity(paymentUrl,
//                        entity,
//                        GUOGEJSONResult.class);
//        GUOGEJSONResult paymentResult = responseEntity.getBody();
//        if (paymentResult.getStatus() != 200) {
//            logger.error("发送错误：{}", paymentResult.getMsg());
//            return GUOGEJSONResult.errorMsg("支付中心订单创建失败，请联系管理员！");
//        }

        System.out.println(orderId);
        return GUOGEJSONResult.ok(orderId);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public GUOGEJSONResult getPaidOrderInfo(String orderId) {

        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return GUOGEJSONResult.ok(orderStatus);
    }
}

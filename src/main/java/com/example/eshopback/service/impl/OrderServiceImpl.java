package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.Order;
import com.example.eshopback.model.entity.Position;
import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.enums.PaymentType;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.model.request.OrderRequest;
import com.example.eshopback.model.request.PositionRequest;
import com.example.eshopback.model.response.*;
import com.example.eshopback.repository.OrderRepository;
import com.example.eshopback.repository.PositionRepository;
import com.example.eshopback.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.eshopback.model.enums.PaymentType.CASH;
import static com.example.eshopback.model.enums.PaymentType.ELECTRONIC;
import static java.util.Calendar.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final SalesPointService salesPointService;
    private final UserService userService;
    private final RemainService remainService;
    private final ProductService productService;
    private final PositionRepository positionRepository;
    private final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    @Override
    public void createOrder(OrderRequest orderRequest) {
        //check product remains
        List<PositionRequest> products = orderRequest.getProducts();
        SalesPoint salesPoint = salesPointService.getSalesPoint(orderRequest.getSalesPoint());

        if (!salesPoint.isOpened()) {
            throw ErrorException.builder()
                    .message("Смена не открыта")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        remainService.checkOrder(products, salesPoint);

        Order order = Order.builder()
                .salesPoint(salesPoint)
                .clientName(orderRequest.getClientName())
                .cashier(userService.getUserById(orderRequest.getCashier()))
                .sum(orderRequest.getSum())
                .paymentSum(orderRequest.getPaymentSum())
                .paymentType(orderRequest.getPaymentType())
                .build();

        orderRepository.save(order);

        List<Product> productList = new ArrayList<>();
        for (PositionRequest positionRequest : products) {
            Product product = productService.getProduct(positionRequest.getProduct());
            Position position = Position.builder()
                    .order(order)
                    .amount(positionRequest.getAmount())
                    .product(product)
                    .build();
            positionRepository.save(position);
            productList.add(product);
        }

        //Write off
        remainService.writeOffOrder(products, salesPoint);
    }

    @Override
    public List<OrderResponse> getLast(Long salesPointId) {
        List<Order> orders = getTodayOrders(salesPointId);
        List<OrderResponse> orderResponses = new LinkedList<>();

        for (Order order : orders) {
            List<PositionResponse> positions = positionRepository.findByOrder(order).parallelStream()
                    .map(position -> PositionResponse.builder()
                            .productName(position.getProduct().getName())
                            .price(position.getProduct().getPrice())
                            .amount(position.getAmount())
                            .productType(position.getProduct().getType())
                            .build())
                    .collect(Collectors.toList());

            OrderResponse orderResponse = OrderResponse.builder()
                    .id(order.getId())
                    .salesPoint(order.getSalesPoint().getId())
                    .clientName(order.getClientName())
                    .cashier(order.getCashier().getFirstName())
                    .sum(order.getSum())
                    .paymentSum(order.getPaymentSum())
                    .paymentType(order.getPaymentType())
                    .positions(positions)
                    .build();
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    private List<Order> getTodayOrders(Long salesPointId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);

        SalesPoint salesPoint = salesPointService.getSalesPoint(salesPointId);

        Date date = calendar.getTime();

       return orderRepository.findByCreatedAtAfterAndSalesPointAndDeletedAtNull(date, salesPoint);
    }

    @Override
    public RevenueResponse getRevenues(Long salesPointId) {
        List<Order> todayOrders = getTodayOrders(salesPointId);
        List<Order> weeklyOrders = getWeeklyOrders(salesPointId);
        List<Order> weekAgoOrders = getWeekAgoOrder(salesPointId);

        double todaySum = todayOrders.parallelStream().mapToDouble(Order::getSum).sum();
        double weeklySum = weeklyOrders.parallelStream().mapToDouble(Order::getSum).sum();

        int todayAmount = todayOrders.size();
        int todayCash = (int) todayOrders.parallelStream()
                .filter(order -> order.getPaymentType() == CASH).count();
        int todayElectroic = (int) todayOrders.parallelStream()
                .filter(order -> order.getPaymentType() ==ELECTRONIC).count();

        List<HourlyRevenueResponse> todayHourlyOrders = getHourlyOrders(todayOrders);
        List<HourlyRevenueResponse> weekAgoHourlyOrders = getHourlyOrders(weekAgoOrders);

        return RevenueResponse.builder()
                .todaySum(todaySum)
                .weeklySum(weeklySum)
                .todayAmount(todayAmount)
                .todayCash(todayCash)
                .todayElectronic(todayElectroic)
                .todayOrders(todayHourlyOrders)
                .weekAgoOrders(weekAgoHourlyOrders)
                .build();
    }

    private List<Order> getWeekAgoOrder(Long salesPointId) {
        Date firstDate = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        Date secondDate = new Date(System.currentTimeMillis() - (6 * DAY_IN_MS));

        Calendar calendar = getInstance();
        calendar.setTime(firstDate);
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);
        firstDate = calendar.getTime();

        calendar.setTime(secondDate);
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);
        secondDate = calendar.getTime();

        SalesPoint salesPoint = salesPointService.getSalesPoint(salesPointId);

        return orderRepository.findBySalesPointAndCreatedAtBetweenAndDeletedAtNull(salesPoint, firstDate, secondDate);
    }

    private List<HourlyRevenueResponse> getHourlyOrders(List<Order> orders) {
        Map<String, Double> map = new HashMap<>();

        for (Order order : orders) {
            Date date = order.getCreatedAt();

            Calendar calendar = getInstance();
            calendar.setTime(date);

            String hour = calendar.get(HOUR_OF_DAY) + ":00";
            if (map.containsKey(hour)) {
                map.put(hour, map.get(hour) + 1);
            } else {
                map.put(hour, 1.0);
            }
        }

        return map.entrySet().parallelStream().map(entry -> new HourlyRevenueResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<Order> getWeeklyOrders(Long salesPointId) {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date date = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));

        Calendar calendar = getInstance();
        calendar.setTime(date);
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);


        SalesPoint salesPoint = salesPointService.getSalesPoint(salesPointId);

        return orderRepository.findByCreatedAtAfterAndSalesPointAndDeletedAtNull(calendar.getTime(), salesPoint);
    }

    @Override
    public List<OrderReportResponse> getReports(Optional<String> dateOptional, Long salesPointId, Optional<PaymentType> paymentTypeOptional) {
        SalesPoint salesPoint = salesPointService.getSalesPoint(salesPointId);

        if (!dateOptional.isPresent() && !paymentTypeOptional.isPresent()) {
            return orderRepository.findBySalesPointAndDeletedAtNull(salesPoint).parallelStream().map(order -> OrderReportResponse.builder()
                    .orderId(order.getId())
                    .orderSum(order.getSum())
                    .paymentType(order.getPaymentType())
                    .build()).collect(Collectors.toList());
        }

        if (!dateOptional.isPresent()) {
            return orderRepository.findBySalesPointAndPaymentTypeAndDeletedAtNull(salesPoint, paymentTypeOptional.get())
                    .parallelStream().map(order -> OrderReportResponse.builder()
                            .orderId(order.getId())
                            .orderSum(order.getSum())
                            .paymentType(order.getPaymentType())
                            .build()).collect(Collectors.toList());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy");
        String orderDate = dateOptional.get();
        Date dateBegin;
        Date dateEnd;
        try {
             dateBegin = simpleDateFormat.parse(orderDate);
             dateEnd = new Date(dateBegin.getTime() + DAY_IN_MS);
        } catch (ParseException e) {
            throw ErrorException.builder()
                    .message("Формат даты неправильная, должен быть как 13.08.22")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if (!paymentTypeOptional.isPresent()) {
            return orderRepository.findBySalesPointAndCreatedAtBetweenAndDeletedAtNull(salesPoint, dateBegin, dateEnd)
                    .parallelStream().map(order -> OrderReportResponse.builder()
                            .orderId(order.getId())
                            .orderSum(order.getSum())
                            .paymentType(order.getPaymentType())
                            .build()).collect(Collectors.toList());
        }
        PaymentType paymentType = paymentTypeOptional.get();
        return orderRepository.findBySalesPointAndPaymentTypeAndCreatedAtBetweenAndDeletedAtNull(salesPoint,
                        paymentType, dateBegin, dateEnd)
                .parallelStream().map(order -> OrderReportResponse.builder()
                        .orderId(order.getId())
                        .orderSum(order.getSum())
                        .paymentType(order.getPaymentType())
                        .build()).collect(Collectors.toList());
    }
}

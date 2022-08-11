package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.User;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.model.request.SalesPointRequest;
import com.example.eshopback.repository.SalesPointRepository;
import com.example.eshopback.service.SalesPointService;
import com.example.eshopback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesPointServiceImpl implements SalesPointService {
    private final SalesPointRepository salesPointRepository;
    private final UserService userService;

    private final String SALES_POINT_DOES_NOT_EXIST = "Торговая точка не существует";

    @Override
    public void createSalePoint(SalesPointRequest salesPointRequest) {
        SalesPoint salesPoint = new SalesPoint();
        salesPoint.setPointName(salesPointRequest.getPointName());

        List<User> users = new LinkedList<>();
        for (Long employeeId : salesPointRequest.getEmployees()) {
            User user = userService.getUserById(employeeId);
            user.setSalesPoint(salesPoint);
            users.add(user);
        }

        salesPoint.setEmployees(users);
        salesPointRepository.save(salesPoint);
    }

    @Override
    public List<SalesPoint> getSalesPoints() {
        return salesPointRepository.findAll();
    }

    @Override
    public SalesPoint getSalesPoint(Long salesPointId) {
        return salesPointRepository.findById(salesPointId).orElseThrow(
                () -> ErrorException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(SALES_POINT_DOES_NOT_EXIST)
                        .build()
        );
    }

    @Override
    public SalesPoint openShift(Long salesPointId) {
        SalesPoint salesPoint = getSalesPoint(salesPointId);
        salesPoint.setOpened(true);
        return salesPointRepository.save(salesPoint);
    }

    @Override
    public SalesPoint closeShift(Long salesPointId) {
        SalesPoint salesPoint = getSalesPoint(salesPointId);
        salesPoint.setOpened(false);
        return salesPointRepository.save(salesPoint);
    }
}

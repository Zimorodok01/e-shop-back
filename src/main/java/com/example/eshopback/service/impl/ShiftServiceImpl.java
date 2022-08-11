package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.Order;
import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.Shift;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.repository.ShfitRepository;
import com.example.eshopback.service.SalesPointService;
import com.example.eshopback.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {
    private final ShfitRepository shfitRepository;
    private final SalesPointService salesPointService;

    @Override
    public boolean getStatus(Long salesPointId) {
        SalesPoint salesPoint = salesPointService.getSalesPoint(salesPointId);
        return salesPoint.isOpened();
    }

    @Override
    public void openShift(Long salesPointId) {
        SalesPoint salesPoint = salesPointService.openShift(salesPointId);
        Shift shift = Shift.builder()
                .openedAt(new Date())
                .salesPoint(salesPoint)
                .build();
        shfitRepository.save(shift);
    }

    @Override
    public void closeShift(Long salesPointId) {
        SalesPoint salesPoint = salesPointService.closeShift(salesPointId);
        Shift shift = shfitRepository.findFirstBySalesPointAndClosedAtNullOrderByCreatedAtDesc(salesPoint).orElseThrow(
                () -> ErrorException.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("Смена не закрылся. Она и так уже закрыта")
                        .build()
        );
        shift.setClosedAt(new Date());
        shfitRepository.save(shift);
    }
}

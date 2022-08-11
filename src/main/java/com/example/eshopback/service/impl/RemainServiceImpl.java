package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.*;
import com.example.eshopback.model.exception.ErrorException;
import com.example.eshopback.model.request.PositionRequest;
import com.example.eshopback.model.response.RemainResponse;
import com.example.eshopback.repository.RemainRepository;
import com.example.eshopback.service.ProductService;
import com.example.eshopback.service.RemainService;
import com.example.eshopback.service.SalesPointService;
import com.example.eshopback.service.WriteOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;

@Service
@RequiredArgsConstructor
public class RemainServiceImpl implements RemainService {
    private final RemainRepository remainRepository;
    private final ProductService productService;
    private final SalesPointService salesPointService;
    private final WriteOffService writeOffService;

    private final String NO_PRODUCT_IN_STOCK = "%s нету в складе";
    private final String DOES_NOT_HAVE_ENOUGH = "%s не хватает в складе";

    @Override
    public void checkOrder(List<PositionRequest> positions, SalesPoint salesPoint) {
        for (PositionRequest position : positions) {
            Remain remain = getRemainByProductAndSalesPoint(position.getProduct(), salesPoint);
            if (remain.getAmount() < position.getAmount()) {
                throw ErrorException.builder()
                        .message(String.format(DOES_NOT_HAVE_ENOUGH, remain.getProduct().getName()))
                        .build();
            }
        }
    }

    @Override
    public void writeOff(List<PositionRequest> products, SalesPoint salesPoint) {
        for (PositionRequest positionRequest : products) {
            Remain remain = getRemainByProductAndSalesPoint(positionRequest.getProduct(), salesPoint);;
            remain.setAmount(remain.getAmount() - positionRequest.getAmount());
            remainRepository.save(remain);

            WriteOff writeOff = WriteOff.builder()
                    .productName(remain.getProduct().getName())
                    .amount(positionRequest.getAmount())
                    .product(remain.getProduct())
                    .salesPoint(salesPoint)
                    .build();
            writeOffService.save(writeOff);
        }
    }

    private Remain getRemainByProductAndSalesPoint(Long productId, SalesPoint salesPoint) {
        Product product = productService.getProduct(productId);
        Optional<Remain> remainOptional = remainRepository.findBySalesPointAndProduct(salesPoint, product);

        if (!remainOptional.isPresent()) {
            throw ErrorException.builder()
                    .message(String.format(NO_PRODUCT_IN_STOCK, product.getName()))
                    .status(I_AM_A_TEAPOT)
                    .build();
        }

        return remainOptional.get();
    }

    @Override
    public void addProduct(Product product) {
        List<SalesPoint> salesPoints = salesPointService.getSalesPoints();

        for (SalesPoint salesPoint : salesPoints) {
            Remain remain = Remain.builder()
                    .product(product)
                    .salesPoint(salesPoint)
                    .build();

            remainRepository.save(remain);
        }
    }

    @Override
    public void getRemainBySalesPointAndProduct(Supply supply) {
        Optional<Remain> remainOptional =
                remainRepository.findBySalesPointAndProduct(supply.getSalesPoint(), supply.getProduct());

        Remain remain;
        if (remainOptional.isPresent()) {
            remain = remainOptional.get();
            remain.setAmount(supply.getAmount());
        } else {
            remain = Remain.builder()
                    .amount(supply.getAmount())
                    .product(supply.getProduct())
                    .salesPoint(supply.getSalesPoint())
                    .build();
        }

        remainRepository.save(remain);
    }

    @Override
    public List<RemainResponse> getRemains(Long salesPointId) {
        SalesPoint salesPoint = salesPointService.getSalesPoint(salesPointId);
        List<Remain> remains =
                remainRepository.findBySalesPointAndDeletedAtNull(salesPoint);
        return remains.parallelStream().map(remain -> RemainResponse.builder()
                        .productName(remain.getProduct().getName())
                        .productType(remain.getProduct().getType())
                        .amount(remain.getAmount())
                        .productPrice(remain.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<RemainResponse> getRemainsByProductName(String productName, Long salesPointId) {
        List<Product> products = productService.getProductsByName(productName);
        SalesPoint salesPoint = salesPointService.getSalesPoint(salesPointId);
        List<Remain> remains =
                remainRepository.findBySalesPointAndProductInAndDeletedAtNull(salesPoint, products);
        return remains.parallelStream().map(remain -> RemainResponse.builder()
                        .productName(remain.getProduct().getName())
                        .productType(remain.getProduct().getType())
                        .amount(remain.getAmount())
                        .productPrice(remain.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());
    }
}

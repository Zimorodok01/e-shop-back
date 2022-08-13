package com.example.eshopback.service.impl;

import com.example.eshopback.model.entity.Product;
import com.example.eshopback.model.entity.SalesPoint;
import com.example.eshopback.model.entity.WriteOff;
import com.example.eshopback.model.entity.WriteOffOrder;
import com.example.eshopback.model.request.WriteOffRequest;
import com.example.eshopback.model.response.WriteOffResponse;
import com.example.eshopback.repository.RemainRepository;
import com.example.eshopback.repository.WriteOffOrderRepository;
import com.example.eshopback.repository.WriteOffRepository;
import com.example.eshopback.service.ProductService;
import com.example.eshopback.service.RemainService;
import com.example.eshopback.service.SalesPointService;
import com.example.eshopback.service.WriteOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WriteOffServiceImpl implements WriteOffService {
    private final SalesPointService salesPointService;
    private final RemainService remainService;
    private final ProductService productService;
    private final WriteOffRepository writeOffRepository;

    @Override
    public void writeOff(List<WriteOffRequest> writeOffRequests, Long salesPointId) {
        SalesPoint salesPoint = salesPointService.getSalesPoint(salesPointId);
        for (WriteOffRequest writeOffRequest : writeOffRequests) {
            Product product = productService.getProduct(writeOffRequest.getProductId());

            WriteOff writeOff = WriteOff.builder()
                    .product(product)
                    .productName(product.getName())
                    .amount(writeOffRequest.getAmount())
                    .comments(writeOffRequest.getComments())
                    .cause(writeOffRequest.getCause())
                    .salesPoint(salesPoint)
                    .build();
            writeOffRepository.save(writeOff);
            remainService.writeOff(product, salesPoint, writeOffRequest.getAmount());
        }
    }

    @Override
    public List<WriteOffResponse> getWriteOffs(Long salesPointId) {
        SalesPoint salesPoint = salesPointService.getSalesPoint(salesPointId);
        List<WriteOff> writeOffs = writeOffRepository.findBySalesPointAndDeletedAtNull(salesPoint);

        return writeOffs.parallelStream().map(writeOff -> WriteOffResponse.builder()
                .date(writeOff.getCreatedAt())
                .product(writeOff.getProductName())
                .comments(writeOff.getComments())
                .cause(writeOff.getCause())
                .amount(writeOff.getAmount())
                .build()).collect(Collectors.toList());
    }
}


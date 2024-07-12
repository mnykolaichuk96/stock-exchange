package pl.mnykolaichuk.sellOffer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mnykolaichuk.sellOffer.constants.SellOfferConstants;
import pl.mnykolaichuk.sellOffer.dto.ResponseDto;
import pl.mnykolaichuk.sellOffer.dto.SellOfferDto;
import pl.mnykolaichuk.sellOffer.service.impl.SellOfferService;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SellOfferController {

    private SellOfferService sellOfferService;

    @PostMapping("sell-offer/create")
    public ResponseEntity<ResponseDto> createSellOffer(@RequestBody SellOfferDto sellOfferDto) {
        sellOfferService.addSellOffer(sellOfferDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseDto.builder()
                        .statusCode(SellOfferConstants.STATUS_201)
                        .statusMsg(SellOfferConstants.MESSAGE_201)
                        .build()
        );
    }
}

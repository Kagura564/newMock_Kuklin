package com.example.newMock.Controller;

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
public class MainController {

    private final Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalance(@RequestBody RequestDTO requestDTO) {
        try {
            String clientID = requestDTO.getClientId();
            char firstDigit = clientID.charAt(0);
            BigDecimal maxLimit;
            String rqUI = requestDTO.getRqUID();
            String valueCurrency = "RUB";



            if (firstDigit == '8'){
                maxLimit = BigDecimal.valueOf(2000.00);
                valueCurrency  = "US";
            } else if(firstDigit == '9') {
                maxLimit = BigDecimal.valueOf(1000.00);
                valueCurrency  = "EU";
            } else {
                maxLimit = BigDecimal.valueOf(10000.00);
            }

            Random random = new Random();
            BigDecimal randomBalance = BigDecimal.valueOf(random.nextDouble()).multiply(maxLimit).setScale(2, RoundingMode.HALF_UP);

            ResponseDTO responseDTO = new ResponseDTO();


            responseDTO.setRqUID(rqUI);
            responseDTO.setClientId(clientID);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(valueCurrency);
            responseDTO.setBalance(randomBalance);
            responseDTO.setMaxLimit(maxLimit);

            log.error("*************************** RequestDTO ***************************" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("*************************** RequestDTO ***************************" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

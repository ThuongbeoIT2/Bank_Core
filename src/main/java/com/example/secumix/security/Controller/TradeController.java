package com.example.secumix.security.Controller;


import com.example.secumix.security.modelapp.UserUtils;
import com.example.secumix.security.modelapp.entities.Category;
import com.example.secumix.security.modelapp.entities.SpendingLimit;
import com.example.secumix.security.modelapp.entities.Trade;
import com.example.secumix.security.modelapp.repository.Categoryrepo;
import com.example.secumix.security.modelapp.repository.SpendingLimitRepo;
import com.example.secumix.security.modelapp.repository.TradeRepo;
import com.example.secumix.security.modelapp.response.TradeResponse;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class TradeController {
    @Autowired
    private TradeRepo tradeRepo;
    @Autowired
    private Categoryrepo categoryrepo;
    @Autowired
    private SpendingLimitRepo spendingLimitRepo;
    @Autowired
    private UserRepository userRepository;
    @GetMapping(value = "/account/trade/getall")
    ResponseEntity<List<TradeResponse>> getallbyUser(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<TradeResponse> tradeResponses= tradeRepo.findTradeByUser(email).stream().map(
                trade -> {
                    TradeResponse tradeResponse= new TradeResponse();
                    tradeResponse.setTradeId(trade.getTradeId());
                    tradeResponse.setCost(trade.getCost());
                    tradeResponse.setCateName(trade.getCategory().getCateName());
                    tradeResponse.setTitle(trade.getTitle());
                    tradeResponse.setDate(trade.getDate());
                    return tradeResponse;
                }
        ).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(tradeResponses);
    }
    @PostMapping(value = "/account/trade/insert")
    ResponseEntity<String> Insert(@RequestParam long cost,
                                  @RequestParam int cateid,
                                  @RequestParam String title,
                                  @RequestParam String date){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user= userRepository.findByEmail(email).get();
        Optional<Category> category= categoryrepo.findById(cateid);
        if (category.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại cateid="+ cateid);
        }
        Optional<SpendingLimit> spendingLimit= spendingLimitRepo.findByUserEmailAndCategoryName(email,category.get().getCateName());
        if (spendingLimit.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại quỹ cho "+category.get().getCateName());
        }

        Trade trade= Trade.builder()
                .category(category.get())
                .cost(cost)
                .title(title)
                .user(user)
                .createdAt(com.example.secumix.security.Utils.UserUtils.getCurrentDay())
                .date(date)
                .build();
        tradeRepo.save(trade);

        spendingLimit.get().setExpenditure(spendingLimit.get().getExpenditure()+cost);
        spendingLimitRepo.save(spendingLimit.get());

        return ResponseEntity.status(HttpStatus.OK).body("Thành công");
    }
    @GetMapping(value = "/account/trade/lognow")
    ResponseEntity<List<TradeResponse>> getLogTradebyUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<TradeResponse> tradeResponses = tradeRepo.findTradeByUserAndCurrentMonth(email)
                .stream()
                .map(trade -> {
                    TradeResponse tradeResponse = new TradeResponse();
                    tradeResponse.setTradeId(trade.getTradeId());
                    tradeResponse.setCost(trade.getCost());
                    tradeResponse.setCateName(trade.getCategory().getCateName());
                    tradeResponse.setTitle(trade.getTitle());
                    tradeResponse.setDate(trade.getDate());
                    return tradeResponse;
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(tradeResponses);
    }
    @GetMapping("/account/trade/logtrade")
    public ResponseEntity<List<TradeResponse>> getTradesByUserAndMonthYear(
            @RequestParam int year,
            @RequestParam int month) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email= auth.getName();
        try {
            if (year < 2000 || year > 9999 || month < 1 || month > 12) {
                return ResponseEntity.badRequest().build();
            }
            List<TradeResponse> tradeResponses= tradeRepo.findTradeByUserAndYearMonth(email, year, month)
                    .stream()
                    .map(trade -> {
                        TradeResponse tradeResponse = new TradeResponse();
                        tradeResponse.setTradeId(trade.getTradeId());
                        tradeResponse.setCost(trade.getCost());
                        tradeResponse.setCateName(trade.getCategory().getCateName());
                        tradeResponse.setTitle(trade.getTitle());
                        tradeResponse.setDate(trade.getDate());
                        return tradeResponse;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(tradeResponses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

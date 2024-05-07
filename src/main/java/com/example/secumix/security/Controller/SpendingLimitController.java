package com.example.secumix.security.Controller;


import com.example.secumix.security.modelapp.UserUtils;
import com.example.secumix.security.modelapp.entities.Category;
import com.example.secumix.security.modelapp.entities.SpendingLimit;
import com.example.secumix.security.modelapp.repository.Categoryrepo;
import com.example.secumix.security.modelapp.repository.SpendingLimitRepo;
import com.example.secumix.security.modelapp.response.SpendingLimitResponse;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SpendingLimitController {
    @Autowired
    private SpendingLimitRepo spendingLimitRepo;
    @Autowired
    private Categoryrepo categoryrepo;
    @Autowired
    private UserRepository userRepository;
    @GetMapping(value = "/account/getallspendinglimit")
    ResponseEntity<List<SpendingLimitResponse>> getall(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<SpendingLimitResponse> spendingLimitResponses= spendingLimitRepo.getAllByEmail(email).stream().map(
                spendingLimit -> {
                    SpendingLimitResponse spendingLimitResponse= new SpendingLimitResponse();
                    spendingLimitResponse.setSpendingLimit(spendingLimit.getSpendingLimit());
                    spendingLimitResponse.setCateName(spendingLimit.getCategory().getCateName());
                    spendingLimitResponse.setSpendId(spendingLimit.getSpendId());
                    spendingLimitResponse.setExpenditure(spendingLimit.getExpenditure());
                    spendingLimitResponse.setRemainingAmount(spendingLimit.getSpendingLimit()-spendingLimit.getExpenditure());
                    spendingLimitResponse.setDate(spendingLimit.getDate());
                    return spendingLimitResponse;
                }
        ).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(spendingLimitResponses);
    }
    @GetMapping(value = "/account/infodetail/spendinglimit/{catename}")
    ResponseEntity<Optional<SpendingLimitResponse>> getDetailSpendingByCateAndUser(@PathVariable String catename){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<SpendingLimitResponse> spendingLimitResponses= spendingLimitRepo.findByUserEmailAndCategoryName(email,catename).map(
                spendingLimit -> {
                    SpendingLimitResponse spendingLimitResponse= new SpendingLimitResponse();
                    spendingLimitResponse.setSpendingLimit(spendingLimit.getSpendingLimit());
                    spendingLimitResponse.setCateName(spendingLimit.getCategory().getCateName());
                    spendingLimitResponse.setSpendId(spendingLimit.getSpendId());
                    spendingLimitResponse.setExpenditure(spendingLimit.getExpenditure());
                    spendingLimitResponse.setRemainingAmount(spendingLimit.getSpendingLimit()-spendingLimit.getExpenditure());
                    spendingLimitResponse.setDate(spendingLimit.getDate());
                    return spendingLimitResponse;
                }
        );
        return ResponseEntity.status(HttpStatus.OK).body(spendingLimitResponses);
    }
    @PostMapping(value = "/account/spendinglimit/insert")
    ResponseEntity<String> Insert(@RequestParam long spendingLimit,
                                  @RequestParam int cateid){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user= userRepository.findByEmail(email).get();
        Optional<Category> category = categoryrepo.findById(cateid);
        if (category.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Loai quỹ không tônf tại");
        }
        if (spendingLimit<0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Quỹ tháng phải dương");
        }
        Optional<SpendingLimit> optionalSpendingLimit=spendingLimitRepo.findByUserEmailAndCategoryName(email,category.get().getCateName());
        if (optionalSpendingLimit.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Đã tồn tại.Vui lòng sửa vào đầu tháng t");
        }
        SpendingLimit newspendingLimit= new SpendingLimit();
        newspendingLimit.setCategory(category.get());
        newspendingLimit.setSpendingLimit(spendingLimit);
        newspendingLimit.setDate(SpendingLimit.processDate(UserUtils.getCurrentDay()));
        newspendingLimit.setUser(user);
        spendingLimitRepo.save(newspendingLimit);
        return ResponseEntity.status(HttpStatus.OK).body("Thành công");
    }
    @PostMapping(value = "/account/spendinglimit/update/{spenid}")
    ResponseEntity<String> Update(@RequestParam long spendingLimitValue,
                                  @PathVariable int spenid){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<SpendingLimit> spendingLimit= spendingLimitRepo.findById(spenid);
        if (spendingLimit.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại");
        }
        User userspen= spendingLimit.get().getUser();
        if (!email.equals(userspen.getEmail())){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Không phải quỹ của bạn");
        }
        if (spendingLimitValue<0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Quỹ tháng phải dương");
        }
        spendingLimit.get().setSpendingLimit(spendingLimitValue);
        spendingLimit.get().setDate(SpendingLimit.processDate(UserUtils.getCurrentDay()));
        spendingLimitRepo.save(spendingLimit.get());
        return ResponseEntity.status(HttpStatus.OK).body("Thành công");
    }
    @GetMapping(value = "/account/spendinglimit/delete/{spenid}")
    ResponseEntity<String> Update(@PathVariable int spenid){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<SpendingLimit> spendingLimit= spendingLimitRepo.findById(spenid);
        if (spendingLimit.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tồn tại");
        }
        User userspen= spendingLimit.get().getUser();
        if (!email.equals(userspen.getEmail())){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Không phải quỹ của bạn");
        }
        spendingLimitRepo.deleteById(spenid);
        return ResponseEntity.status(HttpStatus.OK).body("Thành công");
    }
}

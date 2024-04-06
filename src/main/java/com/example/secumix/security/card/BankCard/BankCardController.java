package com.example.secumix.security.card.BankCard;

import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.auth.AuthenticationResponse;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/bank")
public class BankCardController {
    @Autowired
    private BankCardService bankCardService;
    @Autowired
    private  UserService userService;
    @GetMapping(value = "/bankcard/{Represent}/list")
    ResponseEntity<ResponseObject> GetBankCard(@PathVariable("Represent") String Represent){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.FindByEmail(authentication.getName());
        List<BankCard> RS = bankCardService.listCardByUser(user.get().getUsername(),Represent);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Thanh Cong",RS)
        );
    }
}

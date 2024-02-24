package com.example.secumix.security.bankentity.Bank;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.bankentity.BankIntroduce.BankIntroduceRequest;
import com.example.secumix.security.bankentity.BankIntroduce.IBankIntroduceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bank")
public class BankController {
    @Autowired
    private IBankService service;
    private final Cloudinary cloudinary;
    @Autowired
    private IBankIntroduceService bankIntroduceService;

    public Map upload(MultipartFile file)  {
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }

    @Operation(summary = "Bank")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping(value = "/")
    ResponseEntity<ResponseObject> GetAllBank(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Get All Bank Successlly",service.getAllBank())
        );
    }
    @PostMapping(value = "/insert")
    ResponseEntity<ResponseObject> Insert(@RequestParam("bankName")String  bankName,
                                          @RequestParam("represent")String represent,
                                          @RequestParam MultipartFile logo)
    {
        Optional<Bank> rs1 = service.findBankByName(bankName);
        Optional<Bank> rs2 = service.findBankByRepresent(represent);
        if (rs1.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILD","BankName already exist","")
            );
        }else if (rs2.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILD","Represent already exist","")
            );
        }else if (logo.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILD","Logo image error","")
            );
        }else {
            try {
                Map<String, Object> uploadResult = upload(logo);
                BankRequest bankRequest = new BankRequest();
                bankRequest.setBankName(bankName.toUpperCase());
                bankRequest.setRepresent(represent.toUpperCase());
                bankRequest.setLogo(uploadResult.get("secure_url").toString());
                System.out.println(bankRequest.getLogo());
                service.Insert(bankRequest);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("SUCCESS", "Bank inserted successfully", "")
                );
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseObject("FAILED", "Failed to insert bank", "")
                );
            }
        }
    }
    @PostMapping(value = "/update/{id}")
    ResponseEntity<ResponseObject> Update(@RequestParam("bankName")String  bankName,
                                          @RequestParam("represent")String represent,
                                          @RequestParam MultipartFile logo,
                                          @PathVariable("id") int id)
    {
        Optional<Bank> rs1 = service.findBankByName(bankName);
        Optional<Bank> rs2 = service.findBankByRepresent(represent);
        if (rs1.isPresent() && rs1.get().getBankID() != id){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILD","BankName already exist","")
            );
        }else if (rs2.isPresent()&& rs2.get().getBankID() != id){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILD","Represent already exist","")
            );
        }else if (logo.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILD","Logo image error","")
            );
        }else {
            try {
                Map<String, Object> uploadResult = upload(logo);
                BankRequest bankRequest = new BankRequest();
                bankRequest.setBankName(bankName.toUpperCase());
                bankRequest.setRepresent(represent.toUpperCase());
                bankRequest.setLogo(uploadResult.get("secure_url").toString());
                System.out.println(bankRequest.getLogo());
                service.Update(bankRequest,id);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("SUCCESS", "Bank updated successfully", "")
                );
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseObject("FAILED", "Failed to update bank", "")
                );
            }
        }
    }
    @PostMapping(value = "/{bankrepresent}/insertintroduce")
    ResponseEntity<ResponseObject> InsertIntroduct( @RequestParam("title") String title ,
                                                    @RequestParam("description") String description,
                                                    @RequestParam MultipartFile avatar,
                                                    @PathVariable String bankrepresent)
    {
        Optional<Bank> bank = service.findBankByRepresent(bankrepresent);
        if (bank.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILD","Not found Bank","")
            );
        }else {
            try {
                Map<String, Object> uploadResult = upload(avatar);
                BankIntroduceRequest bankIntroduceRequest= new BankIntroduceRequest();
                bankIntroduceRequest.setTitleIntroduce(title);
                bankIntroduceRequest.setDescription(description);
                bankIntroduceRequest.setAvatarIntroduce(uploadResult.get("secure_url").toString());
                bankIntroduceService.Insert(bankIntroduceRequest);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("SUCCESS", "Bank Introduce inserted successfully", "")
                );
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseObject("FAILED", "Failed to insert bank introduce", "")
                );
            }
        }
    }
}

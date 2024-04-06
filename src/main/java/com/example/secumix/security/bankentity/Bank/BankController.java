package com.example.secumix.security.bankentity.Bank;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.bankentity.ATMBank.ATMBankRequest;
import com.example.secumix.security.bankentity.ATMBank.ATMBankResponse;
import com.example.secumix.security.bankentity.ATMBank.IATMBankService;
import com.example.secumix.security.bankentity.BankBranch.BankBranchRequest;
import com.example.secumix.security.bankentity.BankBranch.BankBranchResponse;
import com.example.secumix.security.bankentity.BankBranch.IBankBranchService;
import com.example.secumix.security.bankentity.BankIntroduce.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth/bank")
public class BankController {
    @Autowired
    private IBankService service;
    private final Cloudinary cloudinary;
    @Autowired
    private IBankIntroduceService bankIntroduceService;
    @Autowired
    private IATMBankService iatmBankService;
    @Autowired
    private IBankBranchService bankBranchService;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private BankIntroduceRepository bankIntroduceRepository;
    public Map upload(MultipartFile file)  {
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }


    @GetMapping(value = "/getall")
    List<BankRequest_Response> GetAllBank(){
       return service.getAllBank();
    } @GetMapping(value = "/{Represent}")
    ResponseEntity<ResponseObject> GetBankByRepresent(@PathVariable String Represent){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Get All Bank Successlly",service.findBankByRepresent(Represent))
        );
    }
    @PostMapping(value = "/insert")
    ResponseEntity<String> Insert(@RequestParam("bankName")String  bankName,
                                          @RequestParam("represent")String represent,
                                          @RequestParam MultipartFile logo)
    {
        Optional<BankRequest_Response> rs1 = service.findBankByName(bankName);
        Optional<BankRequest_Response> rs2 = service.findBankByRepresent(represent);
        if (rs1.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Bank Name already exist");
        }else if (rs2.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Represent already exist");
        }else if (logo.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Logo image error");
        }else {
            try {
                Map<String, Object> uploadResult = upload(logo);
                BankRequest_Response bankRequest = new BankRequest_Response();
                bankRequest.setBankName(bankName.toUpperCase());
                bankRequest.setRepresent(represent.toUpperCase());
                bankRequest.setLogo(uploadResult.get("secure_url").toString());
                System.out.println(bankRequest.getLogo());
                service.Insert(bankRequest);

                return ResponseEntity.status(HttpStatus.OK).body("Bank inserted successfully");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert bank");
            }
        }
    }
    @PostMapping(value = "/update/{id}")
    ResponseEntity<String> Update(@RequestParam MultipartFile logo,
                                          @PathVariable("id") int id)
    {
            try {
                Map<String, Object> uploadResult = upload(logo);
                BankRequest_Response bankRequest = new BankRequest_Response();
                bankRequest.setLogo(uploadResult.get("secure_url").toString());
                System.out.println(bankRequest.getLogo());
                service.Update(bankRequest,id);

                return ResponseEntity.status(HttpStatus.OK).body("Bank updated successfully");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update bank");
            }

    }
    @PostMapping(value = "/{bankrepresent}/insertintroduce")
    ResponseEntity<String> InsertIntroduct( @RequestParam("title") String title ,
                                                    @RequestParam("description") String description,
                                                    @RequestParam MultipartFile avatar,
                                                    @PathVariable String bankrepresent)
    {
        Optional<Bank> bank= bankRepository.findBankByRepresent(bankrepresent);
        if (bank.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found Bank");
        }else {
            try {
                Map<String, Object> uploadResult = upload(avatar);
                BankIntroduceRequest bankIntroduceRequest= new BankIntroduceRequest();
                bankIntroduceRequest.setTitleIntroduce(title);
                bankIntroduceRequest.setDescription(description);
                bankIntroduceRequest.setBankID(bank.get().getBankID());
                bankIntroduceRequest.setAvatarIntroduce(uploadResult.get("secure_url").toString());
                bankIntroduceService.Insert(bankIntroduceRequest);

                return ResponseEntity.status(HttpStatus.OK).body("Bank Introduce inserted successfully");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert bank introduce");
            }
        }
    }
    @PutMapping(value = "/{bankrepresent}/updateintroduce")
    ResponseEntity<String> UpdateIntroduct( @RequestParam("title") String title ,
                                                    @RequestParam("description") String description,
                                                    @RequestParam MultipartFile avatar,
                                                    @PathVariable String bankrepresent)
    {
        Optional<Bank> bank = bankRepository.findBankByRepresent(bankrepresent);
        if (bank.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found Bank");
        }else {
            try {
                Map<String, Object> uploadResult = upload(avatar);
               BankIntroduce bankIntroduce= bankIntroduceRepository.findBankIntroduceByBankID(bank.get().getBankID());
                bankIntroduce.setTitleIntroduce(title);
                bankIntroduce.setDescription(description);
                bankIntroduce.setAvatarIntroduce(uploadResult.get("secure_url").toString());

                bankIntroduceService.Save(bankIntroduce);

                return ResponseEntity.status(HttpStatus.OK).body("Bank Introduce updated successfully");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update bank introduce");
            }
        }
    }
    @GetMapping(value = "/atmbank")
    ResponseEntity<List<ATMBankResponse>> GetAllATMBank(){
        return ResponseEntity.status(HttpStatus.OK).body(iatmBankService.getAll());
    }
    @GetMapping(value = "/{bankrepresent}/atmbank")
    ResponseEntity<List<ATMBankResponse>> GetAllATMBankByRepresent(@PathVariable String bankrepresent){
        Optional<Bank> bank = bankRepository.findBankByRepresent(bankrepresent);
        if (bank.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(iatmBankService.GetAllATMByBank(bankrepresent));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PostMapping(value = "/{bankrepresent}/insertatm")
    ResponseEntity<String> InsertATM(@PathVariable String bankrepresent,
                                             @RequestParam String Provine,
                                             @RequestParam String District,
                                             @RequestParam String Ward){
        Optional<Bank> bank = bankRepository.findBankByRepresent(bankrepresent);
        if (bank.isPresent()){
            ATMBankRequest atmBankRequest=new ATMBankRequest();
            atmBankRequest.setBankID(bank.get().getBankID());
            atmBankRequest.setWard(Ward);
            atmBankRequest.setDistrict(District);
            atmBankRequest.setProvince(Provine);
            iatmBankService.Insert(atmBankRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Insert ATMBank successlly");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
    }
    @GetMapping(value = "/{bankrepresent}/bankbranch")
    ResponseEntity<List<BankBranchResponse>> GetAllBankBranchByRepresent(@PathVariable String bankrepresent){
        Optional<Bank> bank = bankRepository.findBankByRepresent(bankrepresent);
        if (bank.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(bankBranchService.GetAllBankBranchByBank(bankrepresent));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }

    }
    @PostMapping(value = "/{bankrepresent}/insertbankbranch")
    ResponseEntity<ResponseObject> InsertBankBanch(@PathVariable String bankrepresent,
                                                   @RequestParam String BankBranchName,
                                                    @RequestParam String Provine,
                                                   @RequestParam String District,
                                                   @RequestParam String Ward){
        Optional<Bank> bank = bankRepository.findBankByRepresent(bankrepresent);
        if (bank.isPresent()){
            BankBranchRequest bankBranchRequest=new BankBranchRequest();
            bankBranchRequest.setBankID(bank.get().getBankID());
            bankBranchRequest.setWard(Ward);
            bankBranchRequest.setDistrict(District);
            bankBranchRequest.setProvince(Provine);
            bankBranchRequest.setBankBranchName(BankBranchName.trim().toUpperCase());

            bankBranchService.Insert(bankBranchRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Insert BankBranch successlly","")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("OK","Not found","")
            );
        }
    }
}

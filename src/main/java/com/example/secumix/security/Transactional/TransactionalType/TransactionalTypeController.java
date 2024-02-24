package com.example.secumix.security.Transactional.TransactionalType;

import com.example.secumix.security.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/transactionaltype")
public class TransactionalTypeController {
    @Autowired
    private ITranTypeService service;
    @Operation(summary = "TransactionalType")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")}
    )
            @GetMapping(value="/")
    ResponseEntity<ResponseObject> GetAllType(){
        List<TransactionalType> rs = service.getAllTranType();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Get All TransactionalType successly",rs)
        );
    }
    @PostMapping(value = "/insert")
    ResponseEntity<ResponseObject> Insert(@RequestBody @Valid @NotNull TransactionalTypeRequest transactionalTypeRequest){
        Optional<TransactionalType> rs= service.findTypeByName(transactionalTypeRequest.getTypeName());
        System.out.println(transactionalTypeRequest);
        if (rs.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("FAILD","TypeName already exist.","")
            );
        }else {
            service.Insert(transactionalTypeRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Insert TransactionalType successly","")
            );
        }
    }
    @PutMapping(value = "/update/{id}")
    ResponseEntity<ResponseObject> UpdateById(@PathVariable("id") int id,@RequestBody @Valid @NotNull TransactionalTypeRequest transactionalTypeRequest){
        Optional<TransactionalType> rs= service.findTypeByName(transactionalTypeRequest.getTypeName());
        if (rs.isPresent()&& rs.get().getTypeID()!=id){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("FAILD","TypeName already exist.","")
            );
        }else {
            service.UpdateTransactionalType(transactionalTypeRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Update successly","")
            );
        }
    }
}

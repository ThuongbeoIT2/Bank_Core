package com.example.secumix.security.Transactional.TransactionalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TransactionalTypeController {
    @Autowired
    private TransactionalService service;

}

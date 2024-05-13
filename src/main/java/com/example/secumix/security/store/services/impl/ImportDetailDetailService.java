package com.example.secumix.security.store.services.impl;

import com.example.secumix.security.store.model.entities.ImportDetail;
import com.example.secumix.security.store.repository.ImportDetailRepo;
import com.example.secumix.security.store.services.IImportDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportDetailDetailService implements IImportDetailService {
    private final ImportDetailRepo importDetailRepo;

    @Override
    public Optional<ImportDetail> findByStore(int storeid) {
        return importDetailRepo.findById(storeid);
    }
    @Override
    public List<ImportDetail> findByStoreandProduct(int storeid, String productname) {
        return importDetailRepo.findByStoreandProduct(storeid,productname);
    }
}

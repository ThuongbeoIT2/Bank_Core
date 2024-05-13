package com.example.secumix.security.store.services;

import com.example.secumix.security.store.model.entities.ImportDetail;
import jdk.dynalink.linker.LinkerServices;

import java.util.List;
import java.util.Optional;

public interface IImportDetailService {
    Optional<ImportDetail> findByStore(int storeid);

    List<ImportDetail> findByStoreandProduct(int storeid, String productname);
}

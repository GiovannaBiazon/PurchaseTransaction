package com.brief.transaction.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.brief.transaction.vo.PurchaseVO;

@Repository
public interface PurchaseRepository extends CrudRepository<PurchaseVO, Long> {

}

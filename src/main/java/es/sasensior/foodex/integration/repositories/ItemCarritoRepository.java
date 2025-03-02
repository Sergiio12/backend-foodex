package es.sasensior.foodex.integration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.sasensior.foodex.integration.dao.ItemCarritoIdPL;
import es.sasensior.foodex.integration.dao.ItemCarritoPL;

public interface ItemCarritoRepository extends JpaRepository<ItemCarritoPL, ItemCarritoIdPL> {

}

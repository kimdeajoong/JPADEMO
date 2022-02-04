package co.mr.jpaDemo02.repository;

import co.mr.jpaDemo02.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {


}

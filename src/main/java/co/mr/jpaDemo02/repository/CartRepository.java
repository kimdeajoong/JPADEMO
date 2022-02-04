package co.mr.jpaDemo02.repository;

import co.mr.jpaDemo02.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}

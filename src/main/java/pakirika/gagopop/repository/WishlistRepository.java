package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.Wishlist;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUserEntity_Username(String username);

}

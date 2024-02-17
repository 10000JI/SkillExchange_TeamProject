package place.skillexchange.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import place.skillexchange.backend.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);


}

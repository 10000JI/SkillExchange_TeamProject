package place.skillexchange.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import place.skillexchange.backend.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    //활성화된 사용자(계정) 반환
    User findByIdAndActiveIsTrue(String id);

//    User findByIdAndActiveIsFalse(String id);
//
//    User findByEmailAndActiveIsFalse(String Email);

    Optional<User> findByEmailAndIdAndActiveIsFalse(String email, String id);

}

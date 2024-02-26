package place.skillexchange.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.User;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File,Integer> {
    Optional<File> findByUser(User user);

    boolean existsByGenerateHash(String generateHash);
}

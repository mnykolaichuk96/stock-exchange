package pl.mnykolaichuk.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mnykolaichuk.users.entity.UserDetail;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetail, Long> {
}

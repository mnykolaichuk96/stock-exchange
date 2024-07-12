package pl.mnykolaichuk.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mnykolaichuk.users.entity.UserDetail;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    UserDetail getUserDetailByUserDetailId(Long userId);
}

package pl.mnykolaichuk.users.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mnykolaichuk.users.dto.UserDetailDto;
import pl.mnykolaichuk.users.entity.UserDetail;
import pl.mnykolaichuk.users.mapper.UserDetailMapper;
import pl.mnykolaichuk.users.repository.UserDetailRepository;
import pl.mnykolaichuk.users.service.IUserDetailService;

@Service
@AllArgsConstructor
public class UserDetailService implements IUserDetailService {

    private UserDetailRepository userDetailRepository;

    /**
     * @param userDetailDto - Data Transfer Object with all required fields
     */
    @Override
    public void saveUserDetail(UserDetailDto userDetailDto) {
        UserDetail userDetail = UserDetailMapper.mapToUserDetail(userDetailDto, new UserDetail());

        userDetailRepository.save(userDetail);

    }
}

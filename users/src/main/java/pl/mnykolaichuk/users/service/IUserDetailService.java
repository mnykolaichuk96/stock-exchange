package pl.mnykolaichuk.users.service;

import pl.mnykolaichuk.users.dto.UserDetailDto;

public interface IUserDetailService {

    /**
     *
     * @param userDetailDto - Data Transfer Object with all required fields
     *
     */
    void saveUserDetail(UserDetailDto userDetailDto);
}

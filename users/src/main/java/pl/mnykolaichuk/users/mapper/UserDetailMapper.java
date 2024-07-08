package pl.mnykolaichuk.users.mapper;

import pl.mnykolaichuk.users.dto.UserDetailDto;
import pl.mnykolaichuk.users.entity.UserDetail;

public class UserDetailMapper {

    /**
     *
     * @param userDetailDto from UserDetailDto object
     * @param userDetail to UserDetail object
     * @return UserDetail object
     */
    public static UserDetail mapToUserDetail(UserDetailDto userDetailDto, UserDetail userDetail){

        userDetail.setUserId(userDetailDto.getUserId());
        userDetail.setBalance(userDetailDto.getBalance());
        userDetail.setFirstName(userDetailDto.getFirstName());
        userDetail.setLastName(userDetailDto.getLastName());
        userDetail.setEmail(userDetailDto.getEmail());

        return userDetail;
    }

    /**
     *
     * @param userDetail from UserDetail object
     * @param userDetailDto to UserDetailDto object
     * @return CardsDto object
     */
    public static UserDetailDto mapToUserDetailDto(UserDetail userDetail, UserDetailDto userDetailDto) {

        userDetailDto.setUserId(userDetail.getUserId());
        userDetailDto.setBalance(userDetail.getBalance());
        userDetailDto.setFirstName(userDetail.getFirstName());
        userDetailDto.setLastName(userDetail.getLastName());
        userDetailDto.setEmail(userDetail.getEmail());

        return userDetailDto;
    }
}

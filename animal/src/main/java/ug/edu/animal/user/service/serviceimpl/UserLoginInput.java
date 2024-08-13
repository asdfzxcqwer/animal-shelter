package ug.edu.animal.user.service.serviceimpl;

import lombok.Builder;

@Builder
public record UserLoginInput(
        String firstName,
        String lastName,
        String email,
        String password
){

}

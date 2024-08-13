package ug.edu.animal.user.service.serviceimpl;

import lombok.Builder;

@Builder
public record UserOutput(
        Long id,
        String email) {
}

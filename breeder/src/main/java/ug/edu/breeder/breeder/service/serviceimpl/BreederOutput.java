package ug.edu.breeder.breeder.service.serviceimpl;

import lombok.Builder;

@Builder
public
record BreederOutput(
        Long id,
        String email) {
}

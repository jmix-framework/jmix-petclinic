package io.jmix.petclinic.service;


import io.jmix.petclinic.dto.PetDto;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@GraphQLApi
public class PetDtoService {

    @GraphQLQuery
    public List<PetDto> petDtoList() {
        return Collections.emptyList();
    }

}

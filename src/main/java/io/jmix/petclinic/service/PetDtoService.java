package io.jmix.petclinic.service;


import io.jmix.core.FileRef;
import io.jmix.petclinic.dto.PetDto;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@GraphQLApi
public class PetDtoService {

    @GraphQLQuery
    public List<PetDto> petDtoList() {
        PetDto petDto = new PetDto();
        petDto.setId(UUID.randomUUID());
        petDto.setPhoto(new FileRef("main", "path", "file1"));
        return Collections.singletonList(petDto);
    }


    /* Usage of createPetDto mutation

    mutation {
        createPetDto(petDto: {
            id: "b9b4ec8b-98ac-4b02-aff9-71c641fe883f"
            photo: {
                storageName: "main"
                path: "/"
                fileName: "X"
            }
        }) {
            id
        }
    }

    */

    @GraphQLMutation
    public PetDto createPetDto(PetDto petDto) {
        return petDto;
    }

}

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
        petDto.setPhotoFileRef("fs://2022/02/02/ec03cf64-948f-3f1e-b06e-d9293a400802?name=");
        return Collections.singletonList(petDto);
    }


    /* Usage of createPetDto mutation

        mutation {
            createPetDto(petDto: {
                id: "b9b4ec8b-98ac-4b02-aff9-71c641fe883f"
                photoFileRef: "fs://2022/02/02/ec03cf64-948f-3f1e-b06e-d9293a400802?name="
          }) {
            id
          }
        }

    */

    @GraphQLMutation
    public PetDto createPetDto(PetDto petDto) {
        FileRef photoFile = FileRef.fromString(petDto.getPhotoFileRef());
        System.out.println("photoFile = " + photoFile);
        return petDto;
    }

}

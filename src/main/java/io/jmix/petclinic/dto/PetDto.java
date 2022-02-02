package io.jmix.petclinic.dto;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;

import java.util.UUID;

@JmixEntity(name = "petclinic_PetDto")
public class PetDto {

    @JmixGeneratedValue
    @JmixId
    @JmixProperty(mandatory = true)
    private UUID id;


    @JmixProperty
    private String photoFileRef;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPhotoFileRef() {
        return photoFileRef;
    }

    public void setPhotoFileRef(String photoFileRef) {
        this.photoFileRef = photoFileRef;
    }
}
package io.jmix.petclinic.dto;

import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import io.jmix.core.metamodel.annotation.PropertyDatatype;

import java.util.UUID;

@JmixEntity(name = "petclinic_PetDto")
public class PetDto {

    @JmixGeneratedValue
    @JmixId
    @JmixProperty(mandatory = true)
    private UUID id;


    @PropertyDatatype("fileRef")
    private FileRef photo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public FileRef getPhoto() {
        return photo;
    }

    public void setPhoto(FileRef photo) {
        this.photo = photo;
    }
}
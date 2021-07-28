package io.jmix.petclinic.app.visit;

import io.jmix.petclinic.entity.User;
import io.jmix.petclinic.entity.pet.Pet;
import io.jmix.petclinic.entity.pet.PetType;

import java.util.UUID;

/**
 * PetclinicData represents an API abstraction for creating different entities for test purposes
 */
public class PetclinicData {


  public Pet pet(String name) {
    Pet pet = new Pet();
    pet.setId(UUID.randomUUID());
    pet.setName(name);
    return pet;
  }

  public PetType electricType() {
    PetType type = new PetType();
    type.setId(UUID.randomUUID());
    type.setName("Electric");
    return type;
  }
  public PetType waterType() {
    PetType type = new PetType();
    type.setId(UUID.randomUUID());
    type.setName("Water");
    return type;
  }

  public PetType fireType() {
    PetType type = new PetType();
    type.setId(UUID.randomUUID());
    type.setName("Fire");
    return type;
  }

  public User nurse(String name) {
    User nurse = new User();
    nurse.setId(UUID.randomUUID());
    nurse.setFirstName(name);
    return nurse;
  }
}
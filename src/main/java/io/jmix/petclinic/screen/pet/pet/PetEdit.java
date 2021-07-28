package io.jmix.petclinic.screen.pet.pet;

import io.jmix.ui.screen.*;
import io.jmix.petclinic.entity.pet.Pet;

@UiController("petclinic_Pet.edit")
@UiDescriptor("pet-edit.xml")
@EditedEntityContainer("petDc")
public class PetEdit extends StandardEditor<Pet> {
}
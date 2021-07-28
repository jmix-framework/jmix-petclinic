package io.jmix.petclinic.screen.veterinarian.specialty;

import io.jmix.ui.screen.*;
import io.jmix.petclinic.entity.veterinarian.Specialty;

@UiController("petclinic_Specialty.lookup")
@UiDescriptor("specialty-lookup.xml")
@LookupComponent("specialtiesTable")
public class SpecialtyLookup extends StandardLookup<Specialty> {
}
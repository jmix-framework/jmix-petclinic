package io.jmix.petclinic.screen.veterinarian.specialty;

import io.jmix.petclinic.entity.veterinarian.Specialty;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@UiController("petclinic_Specialty.lookup")
@UiDescriptor("specialty-lookup.xml")
@LookupComponent("specialtiesTable")
@Route(value = "specialities-selection")
public class SpecialtyLookup extends StandardLookup<Specialty> {
}
package io.jmix.petclinic.screen.veterinarian.specialty;

import io.jmix.petclinic.entity.veterinarian.Specialty;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.MasterDetailScreen;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@UiController("petclinic_Specialty.browse")
@UiDescriptor("specialty-browse.xml")
@LookupComponent("table")
@Route(value = "specialities")
public class SpecialtyBrowse extends MasterDetailScreen<Specialty> {
}
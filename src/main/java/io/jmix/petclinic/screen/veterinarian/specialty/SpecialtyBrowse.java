package io.jmix.petclinic.screen.veterinarian.specialty;

import io.jmix.ui.screen.*;
import io.jmix.petclinic.entity.veterinarian.Specialty;

@UiController("petclinic_Specialty.browse")
@UiDescriptor("specialty-browse.xml")
@LookupComponent("table")
public class SpecialtyBrowse extends MasterDetailScreen<Specialty> {
}
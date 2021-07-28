package io.jmix.petclinic.screen.veterinarian.veterinarian;

import io.jmix.ui.screen.*;
import io.jmix.petclinic.entity.veterinarian.Veterinarian;

@UiController("petclinic_Veterinarian.browse")
@UiDescriptor("veterinarian-browse.xml")
@LookupComponent("veterinariansTable")
public class VeterinarianBrowse extends StandardLookup<Veterinarian> {
}
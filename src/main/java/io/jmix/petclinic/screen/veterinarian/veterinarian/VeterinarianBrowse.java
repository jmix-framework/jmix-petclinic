package io.jmix.petclinic.screen.veterinarian.veterinarian;

import io.jmix.petclinic.entity.veterinarian.Veterinarian;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@UiController("petclinic_Veterinarian.browse")
@UiDescriptor("veterinarian-browse.xml")
@LookupComponent("veterinariansTable")
@Route(value = "veterinarians")
public class VeterinarianBrowse extends StandardLookup<Veterinarian> {
}
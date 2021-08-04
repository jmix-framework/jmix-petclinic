package io.jmix.petclinic.screen.owner;

import io.jmix.petclinic.entity.owner.Owner;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@UiController("petclinic_Owner.browse")
@UiDescriptor("owner-browse.xml")
@LookupComponent("ownersTable")
@Route(value = "owners")
public class OwnerBrowse extends StandardLookup<Owner> {
}
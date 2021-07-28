package io.jmix.petclinic.screen.owner;

import io.jmix.ui.screen.*;
import io.jmix.petclinic.entity.owner.Owner;

@UiController("petclinic_Owner.browse")
@UiDescriptor("owner-browse.xml")
@LookupComponent("ownersTable")
public class OwnerBrowse extends StandardLookup<Owner> {
}
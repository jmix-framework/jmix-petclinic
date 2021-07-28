package io.jmix.petclinic.screen.pet.pettype;

import io.jmix.ui.UiComponents;
import io.jmix.ui.component.ColorPicker;
import io.jmix.ui.component.Component;
import io.jmix.ui.screen.*;
import io.jmix.petclinic.entity.pet.PetType;

import javax.inject.Inject;

import static io.jmix.petclinic.screen.pet.pettype.ColorGeneration.randomColor;

@UiController("petclinic_PetType.browse")
@UiDescriptor("pet-type-browse.xml")
@LookupComponent("table")
public class PetTypeBrowse extends MasterDetailScreen<PetType> {

    @Inject
    protected UiComponents uiComponents;

    @Subscribe
    protected void onInitEntity(InitEntityEvent<PetType> event) {
        event.getEntity().setColor(randomColor());
    }

    @Install(to = "table.color", subject = "columnGenerator")
    protected Component tableColorColumnGenerator(PetType petType) {
        if (petType.getColor() != null) {
            return colorPicker(petType.getColor());
        }

        return null;
    }


    private Component colorPicker(String color) {
        ColorPicker component = uiComponents.create(ColorPicker.class);
        component.setValue(color);
        component.setEditable(false);
        return component;
    }
}
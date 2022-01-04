package io.jmix.petclinic.screen.pet.pettype;

import io.jmix.petclinic.entity.pet.PetType;
import io.jmix.ui.UiComponents;
import io.jmix.ui.component.ColorPicker;
import io.jmix.ui.component.Component;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.Install;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.MasterDetailScreen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

import static io.jmix.petclinic.screen.pet.pettype.ColorGeneration.randomColor;

@UiController("petclinic_PetType.browse")
@UiDescriptor("pet-type-browse.xml")
@LookupComponent("table")
@Route(value = "pettypes")
public class PetTypeBrowse extends MasterDetailScreen<PetType> {

    @Autowired
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
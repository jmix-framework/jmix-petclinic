package io.jmix.petclinic.screen.veterinarian.veterinarian;

import io.jmix.core.EntityStates;
import io.jmix.petclinic.entity.veterinarian.Veterinarian;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.EditedEntityContainer;
import io.jmix.ui.screen.MessageBundle;
import io.jmix.ui.screen.StandardEditor;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("petclinic_Veterinarian.edit")
@UiDescriptor("veterinarian-edit.xml")
@EditedEntityContainer("veterinarianDc")
@Route(value = "veterinarians/edit", parentPrefix = "veterinarians")
public class VeterinarianEdit extends StandardEditor<Veterinarian> {

    @Autowired
    protected MessageBundle messageBundle;
    @Autowired
    private EntityStates entityStates;

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        getWindow().setCaption(editorTitleLabel());
    }

    private String editorTitleLabel() {
        if (entityStates.isNew(getEditedEntity())) {
            return messageBundle.getMessage("newCaption");
        }
        else {
            return messageBundle.formatMessage("editCaption", getEditedEntity().getName());
        }
    }

}
package io.jmix.petclinic.screen.owner;

import io.jmix.core.EntityStates;
import io.jmix.petclinic.entity.owner.Owner;
import io.jmix.ui.component.Label;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.EditedEntityContainer;
import io.jmix.ui.screen.MessageBundle;
import io.jmix.ui.screen.StandardEditor;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;



@UiController("petclinic_Owner.edit")
@UiDescriptor("owner-edit.xml")
@EditedEntityContainer("ownerDc")
@Route(value = "owners/edit", parentPrefix = "owners")
public class OwnerEdit extends StandardEditor<Owner> {

    @Autowired
    protected Label<String> titleLabel;
    @Autowired
    protected MessageBundle messageBundle;
    @Autowired
    private EntityStates entityStates;

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        titleLabel.setValue(editorTitleLabel());
    }

    private String editorTitleLabel() {
        if (entityStates.isNew(getEditedEntity())) {
            return messageBundle.getMessage("newOwnerTitle");
        }
        else {
            return messageBundle.formatMessage("ownerTitle", getEditedEntity().getName());
        }
    }
}
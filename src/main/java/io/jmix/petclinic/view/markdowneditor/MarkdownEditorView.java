package io.jmix.petclinic.view.markdowneditor;


import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.markdowneditor.MarkdownEditor;
import io.jmix.flowui.view.*;
import io.jmix.petclinic.view.main.MainView;

@Route(value = "markdown-editor", layout = MainView.class)
@ViewController(id = "petclinic_MarkdownEditorView")
@ViewDescriptor(path = "markdown-editor-view.xml")
public class MarkdownEditorView extends StandardView {

    @ViewComponent
    protected MarkdownEditor markdownEditor;

    @Subscribe
    protected void onInit(InitEvent event) {
        markdownEditor.setValue("""
                # Markdown Editor
                
                Use the **toolbar** or _keyboard shortcuts_ to format text.
                
                - Bulleted list item
                - [Link to jmix.io](https://www.jmix.io)
                - `inline code`
                
                > Switch to the **Preview** tab to see the rendered result.
                """);
    }
}
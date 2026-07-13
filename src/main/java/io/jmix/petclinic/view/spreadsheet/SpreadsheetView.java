package io.jmix.petclinic.view.spreadsheet;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.spreadsheet.Spreadsheet;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.upload.FileUploadField;
import io.jmix.flowui.download.DownloadFormat;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.view.*;
import io.jmix.petclinic.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Route(value = "spreadsheet", layout = MainView.class)
@ViewController(id = "petclinic_SpreadsheetView")
@ViewDescriptor(path = "spreadsheet-view.xml")
public class SpreadsheetView extends StandardView {

    @Autowired
    private Downloader downloader;

    @ViewComponent
    private Spreadsheet spreadsheet;

    @Subscribe("downloadBtn")
    public void onDownloadBtnClick(ClickEvent<JmixButton> event) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            spreadsheet.write(byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error while downloading XLSX", e);
        }

        downloader.download(byteArrayOutputStream.toByteArray(), "spreadsheet", DownloadFormat.XLSX);
    }

    @Subscribe("uploadField")
    public void onUploadFieldFileUploadSucceeded(final FileUploadSucceededEvent<FileUploadField, byte[]> event) {
        try {
            spreadsheet.read(new ByteArrayInputStream(event.getData()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
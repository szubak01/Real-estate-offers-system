package com.example.application.views.myoffers;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiUploadForm extends FormLayout {

  // Upload area
   MultiFileMemoryBuffer multiFileMemoryBuffer = new MultiFileMemoryBuffer();
  Upload multiFileUpload = new Upload(multiFileMemoryBuffer);

  public MultiUploadForm() {

    multiFileUpload.addClassNames("box-border");
    multiFileUpload.setAcceptedFileTypes("image/*");
    multiFileUpload.addFileRejectedListener(event -> {
      String errorMessage = event.getErrorMessage();

      Notification notification = Notification.show(
          errorMessage,
          3500,
          Notification.Position.MIDDLE
      );
      notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    });

    Paragraph hint = new Paragraph("Accepted file formats: (.png), (.jpg/.jpeg)");
    hint.getStyle().set("color", "var(--lumo-secondary-text-color)");
    hint.getStyle().set("margin-top", "15px");

    setColspan(multiFileUpload, 4);
    setColspan(hint, 4);
    setResponsiveSteps(
        new ResponsiveStep("0", 1),
        new ResponsiveStep("500px", 4)
    );

     add(hint, multiFileUpload);
  }

}

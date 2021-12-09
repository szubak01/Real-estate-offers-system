package com.example.application.views.signup;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import lombok.Getter;

@Getter
public class UploadImageForm extends Div {

  public UploadImageForm(){

    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setAcceptedFileTypes("image/*");

    upload.addFileRejectedListener(event -> {
      String errorMessage = event.getErrorMessage();

      Notification notification = Notification.show(
          errorMessage,
          5000,
          Notification.Position.MIDDLE
      );
      notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    });

    upload.setMaxWidth("466px");
    upload.getStyle().set("margin-top", "5px");

    Paragraph hint = new Paragraph("Accepted file formats: (.png), (.jpg/.jpeg)");
    hint.getStyle().set("color", "var(--lumo-secondary-text-color)");
    hint.getStyle().set("margin-top", "15px");

    setVisible(false);
    add(hint, upload);
  }

}

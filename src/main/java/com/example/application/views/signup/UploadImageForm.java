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

  MemoryBuffer buffer = new MemoryBuffer();
  Upload upload = new Upload(buffer);

  public UploadImageForm(){

    int maxFileSizeInBytes = 10 * 1024 * 1024; // 10MB
    upload.setMaxFileSize(maxFileSizeInBytes);
    upload.setAcceptedFileTypes("image/*");

    upload.setMaxWidth("466px");
    upload.getStyle().set("margin-top", "5px");

    Paragraph hint = new Paragraph("Accepted file formats: (.png), (.jpg/.jpeg)");
    hint.getStyle().set("color", "var(--lumo-secondary-text-color)");
    hint.getStyle().set("margin-top", "15px");

    uploadListeners();
    setVisible(false);
    add(hint, upload);
  }

  private void uploadListeners(){

    upload.addFileRejectedListener(event -> {
      String errorMessage = event.getErrorMessage();

      Notification notification = Notification.show(
          errorMessage,
          5000,
          Notification.Position.MIDDLE
      );
      notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    });

  }

}

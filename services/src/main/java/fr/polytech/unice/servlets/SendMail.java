package fr.polytech.unice.servlets;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by ThiernoMamadouCellou on 11/4/2017.
 */
@SuppressWarnings("serial")
public class SendMail extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String type = req.getParameter("type");
    if (type != null && type.equals("multipart")) {
      resp.getWriter().print("Sending HTML email with attachment.");
      System.out.println("Envoi de mail avec attachement");
      //sendMultipartMail();
    } else {
      resp.getWriter().print("Sending simple email.");
      sendSimpleMail();
    }
  }

  private void sendSimpleMail() {
    // [START simple_example]
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress("celloukansily@gmail.com", "Example.com Admin"));
      msg.addRecipient(Message.RecipientType.TO,
        new InternetAddress("baldecellou@hotmail.fr", "Mr. Balde"));
      msg.setSubject("Votre demande de conversion à été bien prise en compte");
      msg.setText("This is a test");
      Transport.send(msg);
    } catch (AddressException e) {
      // ...
    } catch (MessagingException e) {
      // ...
    } catch (UnsupportedEncodingException e) {
      // ...
    }
    // [END simple_example]
  }


}

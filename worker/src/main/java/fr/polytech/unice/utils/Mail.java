package fr.polytech.unice.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Mail
{
  public void sendSimpleMail(String user,String mail,String video) {
    // [START simple_example]
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress("celloukansily@gmail.com", "CAAS"));
      msg.addRecipient(Message.RecipientType.TO,
        new InternetAddress(mail, user));
      msg.setSubject("la conversion de la video est finie avec succés");
      msg.setText("voici votre video " + video);
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

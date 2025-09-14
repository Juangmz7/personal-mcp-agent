package com.juangomez.agentmcpserver.serviceimpl;

import com.juangomez.agentmcpserver.dto.EmailRequest;
import com.juangomez.agentmcpserver.dto.SetEmailCredentialsRequest;
import com.juangomez.agentmcpserver.dto.ToolResult;
import com.juangomez.agentmcpserver.provider.MailSenderProvider;
import com.juangomez.agentmcpserver.security.EncryptionHandler;
import com.juangomez.agentmcpserver.service.StorageService;
import com.juangomez.agentmcpserver.service.MailService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private final MailSenderProvider mailSenderProvider;

    private final StorageService emailPasswordStorageService;

    public MailServiceImpl(
            MailSenderProvider mailSenderProvider,
            StorageService emailPasswordStorageService
    ) {
        this.mailSenderProvider = mailSenderProvider;
        this.emailPasswordStorageService = emailPasswordStorageService;
    }


    @Override
    @Tool(
            name = "store_email_credential",
            description = """
                  Store or update a user-provided application-specific SMTP credential for the given email.
                  This is a local, trusted operation. Use ONLY when user explicitly provides it.
                  Never echo the credential back in messages; return only status.
                  """
    )
    public ToolResult setEmailCredentials(SetEmailCredentialsRequest setPasswordRequest) {

        // Password encryption
        String encryptedPassword = "";
        System.out.println("empiezo");

        try {
            encryptedPassword = EncryptionHandler.encrypt(setPasswordRequest.getPassword().trim());
            log.info("Password encrypted successfully");
        } catch (Exception e) {
            return new ToolResult(false, "Error: " + e.getMessage());
        }

        // Save password by key-value storage
        emailPasswordStorageService.saveValue(
                setPasswordRequest.getEmail(), encryptedPassword
        );

        return new ToolResult(true, "The new password was successfully saved");
    }

    @Override
    @Tool( name = "email_sender",
            description = """
                    Write a formal email in English based on the user request.
                     "If subject is null, create one that makes sense using the message, it has to be concise." +
                    
                    
                        The email must be structured into clear paragraphs with blank lines between them:
                        - Greeting
                        - Body (main message)
                        - Closing (farewell)
                    
                        If the message is very short or abrupt, you MUST include a polite greeting at
                        the beginning and a polite closing at the end.
                    """
    )
    public ToolResult sendEmail(EmailRequest emailRequest) {

        // Fetch password from db
        // if null, return error message
        if (!emailPasswordStorageService.isKeySaved(emailRequest.getSender())) {
            return new ToolResult(false, "Remind the user to save his email password in order to send emails");
        }

        String password = emailPasswordStorageService.getValue(emailRequest.getSender());
        log.info("Email password fetched from database");

        String decryptedPassword = "";

        // Password decrypting
        try {
            decryptedPassword = EncryptionHandler.decrypt(password);
            log.info("Decrypting email password");
        } catch (Exception e) {
            return new ToolResult(false, "Error: " + e.getMessage());
        }

        // Creates a JavaMailSender object
        var mailSender = mailSenderProvider.create(
                emailRequest.getSender(), decryptedPassword
        );

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            log.info("Configuring email to send");

            // Mail sender
            helper.setFrom(new InternetAddress(emailRequest.getSender()));

            // Mail receiver
            helper.setTo(new InternetAddress(emailRequest.getReceiver()));

            helper.setSubject(emailRequest.getSubject());
            helper.setText(emailRequest.getMessage(), true);

        } catch (Exception e) {
            return new ToolResult(false, "Error: " + e.getMessage());
        }

        log.info("Sending email");
        mailSender.send(message);

        return new ToolResult(true, "Email sent properly to " + emailRequest.getReceiver());
    }
}

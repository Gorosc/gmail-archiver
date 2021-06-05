package org.cgoro.gmailarchiver.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.apache.camel.CamelContext;
import org.apache.camel.component.google.mail.stream.GoogleMailStreamComponent;
import org.apache.camel.component.google.mail.stream.GoogleMailStreamConfiguration;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.cgoro.gmailarchiver.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class CamelConfig {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    GoogleMailStreamComponent googleMail(AuthenticationService authenticationService) throws IOException {
        try (GoogleMailStreamComponent googleMailComponent = new GoogleMailStreamComponent()) {
            GoogleMailStreamConfiguration config = new GoogleMailStreamConfiguration();

            NetHttpTransport httpTransport = getNetHttpTransport();

            Credential credential = authenticationService.getCredentials(httpTransport);

            config.setClientId(authenticationService.getSecrets().getDetails().getClientId());
            config.setClientSecret(authenticationService.getSecrets().getDetails().getClientSecret());
            config.setAccessToken(credential.getAccessToken());
            config.setRefreshToken(credential.getRefreshToken());
            config.setQuery("older_than:1y");
            config.setMarkAsRead(false);
            googleMailComponent.setConfiguration(config);
            return googleMailComponent;
        }
    }

    private NetHttpTransport getNetHttpTransport() {
        NetHttpTransport httpTransport = null;

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Could not establish Transport", e);
        }
        return httpTransport;
    }

    @Bean
    CamelContextConfiguration contextConfiguration(GoogleMailStreamComponent googleMail) {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                context.addComponent("google-mail-stream", googleMail);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                //Nothing done here!
            }
        };
    }
}

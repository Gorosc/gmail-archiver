package org.cgoro.gmailarchiver.route;

import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GmailRouteBuilder extends RouteBuilder {

    //TODO: Check why userId=me is not working
    @Value("${gmail.archiver.email}")
    String userId;

    @Override
    public void configure() throws Exception {

        from("google-mail:messages/list?userId=" + userId + "&repeatCount=1")
                .process(exchange -> exchange.getIn().setBody(exchange.getMessage().getBody(ListMessagesResponse.class).getMessages()))
                .split().body().transform(simple("${body.id}"))
                .toD("google-mail:messages/get?userId="+ userId + "&id=${body}")
                .process(exchange -> exchange.getMessage().getBody(Message.class)
                        .getPayload()
                        .getHeaders().stream()
                        .filter(header -> header.getName().equals("Subject"))
                        .findFirst()
                        .ifPresentOrElse(
                                messagePartHeader -> exchange.getIn().setBody("Subject: " + messagePartHeader.getValue()),
                                () -> exchange.getIn().setBody("Subject: -No Subject-")
                        ))
                .log("${body}");
    }
}

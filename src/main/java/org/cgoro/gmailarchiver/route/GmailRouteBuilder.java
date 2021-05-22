package org.cgoro.gmailarchiver.route;

import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class GmailRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("google-mail:messages/list?userId=chr.goros@gmail.com&repeatCount=1")
                .process(exchange -> {
                    ListMessagesResponse list = exchange.getMessage().getBody(ListMessagesResponse.class);
                    exchange.getIn().setBody(list.getMessages());
                })
                .split().body().transform(simple("${body.id}"))
                .toD("google-mail:messages/get?userId=chr.goros@gmail.com&id=${body}")
                .process(exchange -> {
                    exchange.getMessage().getBody(Message.class)
                            .getPayload()
                            .getHeaders().stream()
                            .filter(header -> header.getName().equals("Subject"))
                            .findFirst()
                            .ifPresentOrElse(
                                    messagePartHeader -> exchange.getIn().setBody("Subject: " + messagePartHeader.getValue()),
                                    () -> exchange.getIn().setBody("Subject: -No Subject-")
                            );
                })
                .log("${body}");
    }
}

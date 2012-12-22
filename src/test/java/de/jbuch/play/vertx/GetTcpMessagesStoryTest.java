package de.jbuch.play.vertx;

import net.thucydides.core.annotations.*;
import net.thucydides.core.pages.Pages;
import net.thucydides.junit.runners.ThucydidesRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@Story(Application.MessagePage.SubscribeToMessages.class)
@RunWith(ThucydidesRunner.class)
public class GetTcpMessagesStoryTest {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @ManagedPages(defaultUrl = "http://localhost:8080/")
    public Pages pages;

    @Steps
    public EndUserSteps endUser;

    @Issue("#WIKI-1")
    @Test
    public void subscribing_to_messages_should_print_messages() {
        endUser.is_on_the_message_page();
        endUser.subscribes_to_address("tcp.messages");
        endUser.should_see_some_messages();

    }
}
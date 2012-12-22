package de.jbuch.play.vertx;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class EndUserSteps extends ScenarioSteps {

    public EndUserSteps(Pages pages) {
        super(pages);
    }

    @Step
    public void subscribes_to_address(String address) {
        onHomePage().subscribeToAddress(address);
    }

    @Step
    public void should_see_some_messages() {
        List<String> messages = onHomePage().getMessages();

        assertNotNull(messages);
        assertTrue("Should contain at least one message", messages.size() > 0);
    }

    private MessagePage onHomePage() {
        return getPages().currentPageAt(MessagePage.class);
    }

    @Step
    public void is_on_the_message_page() {
        onHomePage().open();
    }

    @Step
    public void connects_to_messagebus() {
        onHomePage().connect();
        assertThat(onHomePage().isConnected(), is(true));
    }

    public void sends_message(String address, String message) {
        onHomePage().sendMessage(address, message);
    }

    public void should_see_his_message(String address, String message) {
        List<String> messages = onHomePage().getMessagesByAddress(address);

        boolean found = false;
        for (String m : messages) {
            if (!found) {
                found = m.contains(message);
            }
        }

        assertTrue("We should find our own message", found);
    }
}

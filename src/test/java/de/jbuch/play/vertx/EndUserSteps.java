package de.jbuch.play.vertx;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;


public class EndUserSteps extends ScenarioSteps {

    public EndUserSteps(Pages pages) {
        super(pages);
    }

    @Step
    public void subscribes_to_address(String address) {
        onHomePage().subscribeToAdress(address);
    }

    @Step
    public void should_see_some_messages() {
        assertThat(onHomePage().getMessages(), is(not(null)));
    }

    private MessagePage onHomePage() {
        return getPages().currentPageAt(MessagePage.class);
    }

    @Step
    public void is_on_the_message_page() {
        onHomePage().open();
    }
}

package de.jbuch.play.vertx;

import ch.lambdaj.function.convert.Converter;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static ch.lambdaj.Lambda.convert;

@DefaultUrl("http://localhost:8080/")
public class MessagePage extends PageObject {

    @FindBy(id = "connectButton")
    private WebElement connectButton;

    @FindBy(id = "subscribeAddressInput")
    private WebElement subscribeAddressInput;

    @FindBy(id = "subscribeButton")
    private WebElement subscribeButton;

    @FindBy(id = "subscribed")
    private WebElement subscribedAdresses;


    public MessagePage(WebDriver driver) {
        super(driver);
    }

    public void connect() {
        connectButton.click();
    }

    public void subscribeToAdress(String address) {
        subscribeAddressInput.sendKeys(address);
        subscribeButton.click();
    }

    public List<String> getMessages() {
        WebElement messageList = getDriver().findElement(By.id("received"));
        List<WebElement> results = messageList.findElements(By.tagName("code"));

        return convert(results, new ExtractDefinition());
    }

    class ExtractDefinition implements Converter<WebElement, String> {
        public String convert(WebElement from) {
            return from.getText();
        }
    }
}
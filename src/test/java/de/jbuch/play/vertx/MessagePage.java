package de.jbuch.play.vertx;

import ch.lambdaj.function.convert.Converter;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.convert;

@DefaultUrl("http://localhost:8080/")
public class MessagePage extends PageObject {

    @FindBy(id = "connectButton")
    private WebElement connectButton;

    @FindBy(id = "subscribeAddress")
    private WebElement subscribeAddressInput;

    @FindBy(id = "subscribeButton")
    private WebElement subscribeButton;

    @FindBy(id = "subscribed")
    private WebElement subscribedAdresses;

    @FindBy(id = "received")
    private WebElement receivedMessagesField;

    @FindBy(id = "status_info")
    private WebElement connectionInfo;

    @FindBy(id = "sendButton")
    private WebElement sendButton;

    @FindBy(id = "sendAddress")
    private WebElement sendAddress;

    @FindBy(id = "sendMessage")
    private WebElement sendMessage;


    public MessagePage(WebDriver driver) {
        super(driver);
        setWaitForTimeout(10 * 1000);
    }

    public void connect() {
        connectButton.click();

        waitForTextToDisappear("Not connected");
    }

    public boolean isConnected() {
        return connectionInfo.getText().equals("Connected");
    }

    public void subscribeToAddress(String address) {
        clearSubscribeField();
        subscribeAddressInput.sendKeys(address);
        subscribeButton.click();
    }

    public void clearSubscribeField() {
        subscribeAddressInput.clear();
    }

    public void sendMessage(String address, String message) {
        sendAddress.sendKeys(address);
        sendMessage.sendKeys(message);
        sendButton.click();
    }

    public List<String> getMessagesByAddress(String address) {
        List<String> messages = new ArrayList<>();

        for (String message : getMessages()) {
            if (message.contains(address)) {
                messages.add(message);
            }
        }

        return messages;
    }

    public List<String> getMessages() {

        waitFor(new MessageFieldNonEmptyCondition());

        String[] lines = receivedMessagesField.getText().split("\\n");

        return convert(Arrays.asList(lines), new ExtractDefinition());
    }

    class ExtractDefinition implements Converter<String, String> {
        public String convert(String from) {
            return from.trim();
        }
    }

    class MessageFieldNonEmptyCondition implements ExpectedCondition<Boolean> {
        @Override
        public Boolean apply(WebDriver webDriver) {
            List<WebElement> results = receivedMessagesField.findElements(By.tagName("br"));

            return results.size() > 0 ? Boolean.TRUE : Boolean.FALSE;
        }
    }
}

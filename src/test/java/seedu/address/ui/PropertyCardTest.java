package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.property.Property;
import seedu.address.testutil.PropertyBuilder;

public class PropertyCardTest {

    private static boolean fxReady = false;

    @BeforeAll
    public static void initJavaFxToolkit() {
        boolean ok = true;
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ex) {
            ok = true;
        } catch (UnsupportedOperationException ex) {
            ok = false;
        }
        fxReady = ok;
    }

    @Test
    public void propertyCard_rendersAllFields_correctly() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        
        Property property = new PropertyBuilder()
                .withName("The Pinnacle")
                .withAddress("123 Example St")
                .withPrice(1000000)
                .build();

        PropertyCard card = runOnFxAndGet(() -> new PropertyCard(property, 1));
        
        Label idLabel = getPrivateField(card, "id");
        Label nameLabel = getPrivateField(card, "propertyName");
        Label addressLabel = getPrivateField(card, "address");
        Label priceLabel = getPrivateField(card, "price");
        HBox cardPane = getPrivateField(card, "cardPane");

        assertNotNull(cardPane);
        assertEquals("1. ", idLabel.getText());
        assertEquals("The Pinnacle", nameLabel.getText());
        assertEquals("Address: 123 Example St", addressLabel.getText());
        assertEquals("Price: $1000000", priceLabel.getText());
    }

    @Test
    public void propertyCard_withHighIndex_displaysCorrectId() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        
        Property property = new PropertyBuilder()
                .withName("Test Property")
                .withAddress("456 Main Rd")
                .withPrice(2000000)
                .build();

        PropertyCard card = runOnFxAndGet(() -> new PropertyCard(property, 42));
        
        Label idLabel = getPrivateField(card, "id");
        assertEquals("42. ", idLabel.getText());
    }

    @Test
    public void propertyCard_withComplexPropertyName_rendersCorrectly() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        
        Property property = new PropertyBuilder()
                .withName("The Pinnacle at Duxton")
                .withAddress("1 Cantonment Road, #48-10")
                .withPrice(1450000)
                .build();

        PropertyCard card = runOnFxAndGet(() -> new PropertyCard(property, 1));
        
        Label nameLabel = getPrivateField(card, "propertyName");
        Label addressLabel = getPrivateField(card, "address");
        Label priceLabel = getPrivateField(card, "price");

        assertEquals("The Pinnacle at Duxton", nameLabel.getText());
        assertEquals("Address: 1 Cantonment Road, #48-10", addressLabel.getText());
        assertEquals("Price: $1450000", priceLabel.getText());
    }

    @Test
    public void propertyCard_withVeryHighPrice_rendersCorrectly() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        
        Property property = new PropertyBuilder()
                .withName("Luxury Villa")
                .withAddress("999 Sentosa Cove")
                .withPrice(15000000)
                .build();

        PropertyCard card = runOnFxAndGet(() -> new PropertyCard(property, 1));
        
        Label priceLabel = getPrivateField(card, "price");
        assertEquals("Price: $15000000", priceLabel.getText());
    }

    @Test
    public void propertyCard_getsCorrectProperty() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        
        Property property = new PropertyBuilder()
                .withName("Test Property")
                .withAddress("123 Test St")
                .withPrice(100000)
                .build();

        PropertyCard card = runOnFxAndGet(() -> new PropertyCard(property, 1));
        
        assertEquals(property, card.property);
    }

    private static <T> T runOnFxAndGet(java.util.concurrent.Callable<T> callable) throws Exception {
        java.util.concurrent.FutureTask<T> task = new java.util.concurrent.FutureTask<>(callable);
        Platform.runLater(task);
        return task.get();
    }

    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(PropertyCard card, String fieldName) throws Exception {
        java.lang.reflect.Field f = PropertyCard.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(card);
    }
}


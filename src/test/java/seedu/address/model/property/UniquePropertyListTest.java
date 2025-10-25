package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.property.exceptions.DuplicatePropertyException;
import seedu.address.model.property.exceptions.PropertyNotFoundException;

public class UniquePropertyListTest {

    private UniquePropertyList uniqueList;
    private Property pA;
    private Property pB;
    private Property pASameNameDifferentFields;

    @BeforeEach
    public void setUp() {
        uniqueList = new UniquePropertyList();
        pA = new Property(new Address("Addr A"), new Price(100), new PropertyName("NameA"));
        pB = new Property(new Address("Addr B"), new Price(200), new PropertyName("NameB"));
        pASameNameDifferentFields = new Property(new Address("Addr A2"), new Price(300), new PropertyName("NameA"));
    }

    @Test
    public void add_contains_success() {
        uniqueList.add(pA);
        assertTrue(uniqueList.contains(pA));
    }

    @Test
    public void add_duplicate_throwsDuplicatePropertyException() {
        uniqueList.add(pA);
        assertThrows(DuplicatePropertyException.class, () -> uniqueList.add(pASameNameDifferentFields));
    }

    @Test
    public void remove_nonExisting_throwsPropertyNotFoundException() {
        assertThrows(PropertyNotFoundException.class, () -> uniqueList.remove(pA));
    }

    @Test
    public void setProperty_targetNotFound_throwsPropertyNotFoundException() {
        assertThrows(PropertyNotFoundException.class, () -> uniqueList.setProperty(pA, pB));
    }

    @Test
    public void setProperty_replaceWithDuplicate_throwsDuplicatePropertyException() {
        uniqueList.add(pA);
        uniqueList.add(pB);
        assertThrows(DuplicatePropertyException.class, () -> uniqueList.setProperty(pB, pASameNameDifferentFields));
    }

    @Test
    public void setProperties_withDuplicateInList_throwsDuplicatePropertyException() {
        List<Property> listWithDup = Arrays.asList(pA, pASameNameDifferentFields);
        assertThrows(DuplicatePropertyException.class, () -> uniqueList.setProperties(listWithDup));
    }

    @Test
    public void setProperties_replacesSuccessfully() {
        uniqueList.add(pA);
        UniquePropertyList replacement = new UniquePropertyList();
        replacement.add(pB);
        uniqueList.setProperties(replacement);
        assertFalse(uniqueList.contains(pA));
        assertTrue(uniqueList.contains(pB));
    }

    @Test
    public void asUnmodifiableObservableList_modify_throws() {
        uniqueList.add(pA);
        assertThrows(UnsupportedOperationException.class, () -> uniqueList.asUnmodifiableObservableList().remove(0));
    }
}

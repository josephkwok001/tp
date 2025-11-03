package seedu.address.logic.commands.property;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PROPERTIES;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;


/**
 * Edits the details of an existing property in the address book.
 */
public class EditPropertyCommand extends Command {

    public static final String COMMAND_WORD = "editp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the property identified "
            + "by the index number used in the displayed property list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "PROPERTY NAME] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_PRICE + "PRICE]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ADDRESS + " 123, New Road, #01-01, S123456 "
            + PREFIX_PRICE + " 1000000 ";

    public static final String MESSAGE_EDIT_PROPERTY_SUCCESS = "Edited Property: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PROPERTY = "This property already exists in the address book.";

    private final Index index;
    private final EditPropertyDescriptor editPropertyDescriptor;

    /**
     * @param index of the property in the filtered property list to edit
     * @param editPropertyDescriptor details to edit the property with
     */
    public EditPropertyCommand(Index index, EditPropertyDescriptor editPropertyDescriptor) {
        requireNonNull(index);
        requireNonNull(editPropertyDescriptor);

        this.index = index;
        this.editPropertyDescriptor = new EditPropertyDescriptor(editPropertyDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Property> lastShownList = model.getFilteredPropertyList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
        }

        Property propertyToEdit = lastShownList.get(index.getZeroBased());
        Property editedProperty = createEditedProperty(propertyToEdit, editPropertyDescriptor);

        if (!propertyToEdit.isSameProperty(editedProperty) && model.hasProperty(editedProperty)) {
            throw new CommandException(MESSAGE_DUPLICATE_PROPERTY);
        }

        String newKey = PropertyName.canonicalLoose(editedProperty.getPropertyName().toString());
        Optional<Property> similar = model.getAddressBook().getPropertyList().stream()
                .filter(p -> p != propertyToEdit)
                .filter(p -> PropertyName.canonicalLoose(p.getPropertyName().toString()).equals(newKey))
                .filter(p -> !p.getPropertyName().equals(editedProperty.getPropertyName()))
                .findFirst();

        model.setProperty(propertyToEdit, editedProperty);
        model.updatePropertyInAllPersons(propertyToEdit, editedProperty);

        String msg = String.format(MESSAGE_EDIT_PROPERTY_SUCCESS, Messages.formatProperty(editedProperty));
        if (similar.isPresent()) {
            msg += String.format(
                    "\nWarning: A similar property name already exists: \"%s\" (differs only by spacing/case).",
                    similar.get().getPropertyName().toString());
        }

        return new CommandResult(msg);
    }

    /**
     * Creates and returns a {@code Property} with the details of {@code propertyToEdit}
     * edited with {@code editPropertyDescriptor}.
     */
    private static Property createEditedProperty(Property propertyToEdit,
        EditPropertyDescriptor editPropertyDescriptor) {
        assert propertyToEdit != null;

        PropertyName updatedName = editPropertyDescriptor.getPropertyName().orElse(propertyToEdit.getPropertyName());
        Price updatedPrice = editPropertyDescriptor.getPrice().orElse(propertyToEdit.getPrice());
        Address updatedAddress = editPropertyDescriptor.getAddress().orElse(propertyToEdit.getAddress());

        return new Property(updatedAddress, updatedPrice, updatedName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditPropertyCommand)) {
            return false;
        }

        EditPropertyCommand otherEditPropertyCommand = (EditPropertyCommand) other;
        return index.equals(otherEditPropertyCommand.index)
                && editPropertyDescriptor.equals(otherEditPropertyCommand.editPropertyDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPropertyDescriptor", editPropertyDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the property with. Each non-empty field value will replace the
     * corresponding field value of the property.
     */
    public static class EditPropertyDescriptor {
        private PropertyName propertyname;
        private Address address;
        private Price price;

        public EditPropertyDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPropertyDescriptor(EditPropertyDescriptor toCopy) {
            setPropertyName(toCopy.propertyname);
            setPrice(toCopy.price);
            setAddress(toCopy.address);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(propertyname, address, price);
        }

        public void setPropertyName(PropertyName propertyname) {
            this.propertyname = propertyname;
        }

        public Optional<PropertyName> getPropertyName() {
            return Optional.ofNullable(propertyname);
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        public Optional<Price> getPrice() {
            return Optional.ofNullable(price);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditPropertyDescriptor)) {
                return false;
            }

            EditPropertyDescriptor otherEditPropertyDescriptor = (EditPropertyDescriptor) other;
            return Objects.equals(propertyname, otherEditPropertyDescriptor.propertyname)
                    && Objects.equals(price, otherEditPropertyDescriptor.price)
                    && Objects.equals(address, otherEditPropertyDescriptor.address);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", propertyname)
                    .add("address", address)
                    .add("price", price)
                    .toString();
        }
    }
}

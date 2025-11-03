---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# EstateSearch Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This application, EstateSearch, is built on top of the Address-Book Level 3 (AB3) project, which is developed by the
[SE-EDU Initiative](https://se-education.org/).

The libraries and resources that were used include the following:
1. [AB3](https://se-education.org/addressbook-level3/)
2. [JavaFX](https://openjfx.io/)
3. [JUnit](https://junit.org/)
4. [Jackson](https://github.com/FasterXML/jackson)
--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103T-W12-4/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S1-CS2103T-W12-4/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Client` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info">

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `EstateSearchParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a client).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `EstateSearchParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `EstateSearchParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the `Person` list data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the `Property` list data i.e., all `Property` objects (which are contained in a `UniquePropertyList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the currently 'selected' `Property` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Property>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info">

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Client` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------


## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Add Property Feature

#### Implementation

The Add Property feature allows real estate agents to add new properties to EstateSearch. It is implemented through the `AddPropertyCommand` class located in `seedu.address.logic.commands.property`.

**Key Components:**
* `AddPropertyCommand` — Handles the addition of new properties to the system
* `Model#hasProperty(Property)` — Checks if a property already exists
* `Model#addProperty(Property)` — Adds the property to the property list

**How it works:**

When an agent executes `addp a/ADDRESS pr/PRICE n/NAME`, the following sequence occurs:

1. The `EstateSearchParser` parses the command and creates an `AddPropertyCommandParser`
2. The parser validates all required fields (address, price, name) and creates a `Property` object
3. The parser returns an `AddPropertyCommand` containing the property to add
4. `LogicManager` executes the command by calling `AddPropertyCommand#execute(Model)`
5. The command checks for duplicate properties using `Model#hasProperty(Property)`
    - If a duplicate exists (same property name), a `CommandException` is thrown
    - Otherwise, the property is added using `Model#addProperty(Property)`
6. A success message is returned to the user

**Duplicate Detection:**

Properties are considered duplicates if they have the same property name (case-sensitive). This prevents agents from accidentally adding the same property multiple times, maintaining data integrity.

**Example Usage:**
```
addp a/311, Clementi Ave 2, #02-25 pr/1000000 n/Hillside Villa
```

**Design Considerations:**

**Aspect: Duplicate Detection Strategy**

* **Current Implementation:** Compare by property name only
    * Pros: Simple and efficient; property names are typically unique identifiers in real estate
    * Cons: Two different properties with identical names would be rejected

* **Alternative:** Compare by multiple fields (name + address)
    * Pros: More precise duplicate detection
    * Cons: Same property at same address with different names could slip through; more complex comparison logic

---

### Delete Property Feature with Cascading Deletion

#### Implementation

The Delete Property feature allows agents to remove properties from EstateSearch. A critical aspect of this feature is **cascading deletion** — when a property is deleted, all references to it are automatically removed from clients' owned and interested property lists. This maintains referential integrity across the system.

**Key Components:**
* `DeletePropertyCommand` — Handles property deletion
* `Model#removePropertyFromAllPersons(Property)` — Removes property references from all clients
* `Model#deleteProperty(Property)` — Deletes the property from the property list

**How it works:**

When an agent executes `deletep INDEX`, the following sequence occurs:

<puml src="diagrams/DeletePropertySequenceDiagram.puml" alt="Delete Property Sequence Diagram" />

**Step-by-step execution:**

1. The `EstateSearchParser` parses the command and creates a `DeletePropertyCommandParser`
2. The parser validates the index and returns a `DeletePropertyCommand`
3. `LogicManager` executes the command by calling `DeletePropertyCommand#execute(Model)`
4. The command retrieves the property from the filtered property list using the index
5. **Cascading deletion step:** `Model#removePropertyFromAllPersons(Property)` is called
    - This method iterates through all clients in the system
    - For each client, it checks both owned and interested property lists
    - If the property is found in either list, it is removed
    - The client is updated in the address book
6. After all references are cleaned up, `Model#deleteProperty(Property)` removes the property itself
7. A success message is returned to the user

**Cascading Deletion Implementation (ModelManager.java:124-145):**

```java
public void removePropertyFromAllPersons(Property propertyToDelete) {
    requireNonNull(propertyToDelete);

    List<Person> allPersons = addressBook.getPersonList();
    for (Person person : allPersons) {
        boolean changed = false;

        if (person.getOwnedProperties().contains(propertyToDelete)) {
            person.removeOwnedProperty(propertyToDelete);
            changed = true;
        }

        if (person.getInterestedProperties().contains(propertyToDelete)) {
            person.removeInterestedProperty(propertyToDelete);
            changed = true;
        }

        if (changed) {
            addressBook.setPerson(person, person);
        }
    }
}
```

**Why Cascading Deletion Matters:**

Without cascading deletion, deleting a property would leave "dangling references" — clients would still have references to a property that no longer exists. This would cause:
- Data inconsistency
- Potential null pointer exceptions
- Confusion for users seeing references to non-existent properties

**Design Considerations:**

**Aspect: When to remove property references**

* **Current Implementation:** Remove references before deleting the property
    * Pros: Ensures referential integrity; no dangling references possible
    * Cons: Requires iterating through all clients (O(n) complexity)

* **Alternative:** Delete property first, remove references on-demand when accessed
    * Pros: Faster deletion operation
    * Cons: Dangling references persist; requires null checks throughout codebase; poor user experience

**Aspect: Should users be warned before cascading deletion?**

* **Current Implementation:** No warning, automatic cleanup
    * Pros: Seamless user experience; maintains data integrity automatically
    * Cons: Users might not realize client-property links are being removed

* **Alternative:** Show confirmation dialog listing affected clients
    * Pros: Transparency; users aware of side effects
    * Cons: Extra step slows down workflow; most users expect automatic cleanup


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Real estate agents
* Can type fast
* Requires fast access to large number of contacts
* Prefers typing to mouse interactions
* Is reasonably comfortable using CLI apps

**Value proposition**: A streamlined real estate tool that centralizes client management, property listings, appointments, tasks, and negotiations. It enhances efficiency with quick data access, timely reminders, and intuitive scheduling. Agents benefit from centralized notes, and a typing-optimized interface, enabling smarter decisions, improved productivity, and higher closing rates.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                   | I want to …​                 | So that I can…​                                               |
|-------|-------------------------------------------|------------------------------|---------------------------------------------------------------|
| `* * *` | real estate agent                         | add new client contacts with their details | keep track of potential buyers and sellers                    |
| `* * *` | real estate agent                         | delete clients that I no longer work with | keep my contact list clean and up-to-date                     |
| `* * *` | real estate agent                         | view a list of all my clients | see all the people I'm working with at a glance               |
| `* * *` | real estate agent                         | add properties to my listing database | track all properties I'm managing                             |
| `* * *` | real estate agent                         | delete properties that are no longer available | keep my property listings current                             |
| `* * *` | real estate agent                         | view all properties I'm managing | see my complete inventory of listings                         |
| `* * *` | real estate agent                         | exit the application through CLI | close the app efficiently without using the mouse             |
| `* *` | real estate agent                         | edit client information | update contact details when they change                       |
| `* *` | real estate agent                         | edit property details | update prices and information when they change                |
| `* *` | real estate agent                         | find clients by name or tags | quickly locate specific people I'm working with               |
| `* *` | real estate agent                         | find properties by name | quickly locate specific properties in my listings             |
| `* *` | real estate agent                         | get help on available commands | learn how to use the application effectively                  |
| `* *` | real estate agent                         | link properties that clients own | track who owns which properties                               |
| `* *` | real estate agent                         | link properties that clients are interested in | match buyers with suitable properties                         |
| `* *` | real estate agent                         | remove property ownership links from clients | update records when properties are sold                       |
| `* *` | real estate agent                         | remove property interest links from clients | update records when clients lose interest                     |
| `* *` | real estate agent                         | tag clients with labels like "buyer" or "seller" | categorize and organize my contacts                           |
| `* *` | real estate agent                         | export my client and property data to CSV | use the data in other applications like Excel                 |
| `* *` | real estate agent                         | see clients with their owned and interested properties | understand each client's property portfolio and interests     |
| `* *` | real estate agent managing many properties | search for properties by keywords | quickly find properties matching their property name          |
| `* *` | real estate agent with many contacts | filter clients by tags | view only buyers, sellers, or other specific categories       |
| `* *` | organized agent                          | clear all data at once | start fresh when beginning a new business cycle               |
| `* *` | real estate agent                         | view which properties a client owns | avoid showing them properties they already own                |
| `* *` | real estate agent                         | track which properties a client is interested in | follow up with relevant property updates                      |
| `*`   | new user                                  | see sample data when I first start the app | understand what the app can do without entering data manually |
| `*`   | real estate agent                         | have property relationships automatically removed when I delete a property | avoid manual cleanup of stale references                      |
| `*`   | real estate agent                         | prevent duplicate clients from being added | maintain data integrity in my contact list                    |
| `*`   | real estate agent                         | prevent duplicate properties from being added | avoid confusion with identical property names                 |
| `*`   | real estate agent                         | have my exported files saved to a consistent location | easily find my exported data                                  |


### Use cases

(For all use cases below, the **System** is the `EstateSearch` and the **Actor** is the `real estate agents`, unless specified otherwise)

**Use case UC01: View help**

**Preconditions:**
* The system is running and responsive

**Guarantees:**
* Help information is displayed to the agent
* System state remains unchanged

**MSS**
1. Agent requests to view help
2. System shows a summary of available commands
   Use case ends.

**Extensions**
* 1a. Agent requests help for a specific command.
    * 1a1. System shows detailed usage for that command.
    * Use case ends.

---

**Use case UC02: Add a client**

**Preconditions:**
* The system is running and responsive
* No client with identical unique identifiers (e.g., phone/email) exists in the system

**Guarantees:**
* If successful, a new client with valid details is added to the system
* If unsuccessful, the system state remains unchanged

**MSS**
1. Agent requests to add a client
2. System requests required details (name, phone, email, address)
3. Agent enters the requested details
4. System validates and saves the client, and shows confirmation
   Use case ends.

**Extensions**
* 3a. One or more fields are missing/invalid.
    * 3a1. System shows an error message
    * 3a2. Agent re-enters details.
    * Use case resumes at step 4.
* 3b. A duplicate client is detected (based on unique fields).
    * 3b1. System warns about duplication and rejects the add.
    * Use case ends.

---

**Use case UC03: Add a property**

**Preconditions:**
* The system is running and responsive
* No property with the same property name exists in the system

**Guarantees:**
* If successful, a new property with valid details is added to the system
* If unsuccessful, the system state remains unchanged

**MSS**
1. Agent requests to add a property
2. System requests required details (address, price, property name)
3. Agent enters the requested details
4. System validates and saves the property, and shows confirmation
   Use case ends.

**Extensions**
* 3a. One or more fields are missing/invalid.
    * 3a1. System shows an error message
    * 3a2. Agent re-enters details.
    * Use case resumes at step 4.
* 3b. A duplicate property is detected (based on property name).
    * 3b1. System warns about duplication and rejects the add.
    * Use case ends.

---

**Use case UC04: Edit a client**

**Preconditions:**
* The system is running and responsive
* At least one client exists in the system

**Guarantees:**
* If successful, the specified client's details are updated with valid values
* If unsuccessful, the system state remains unchanged
* Client relationships (owned/interested properties) are preserved

**MSS**
1. Agent requests to edit a specific client
2. System requests the fields to update
3. Agent provides new values
4. System validates and updates the client, then shows confirmation
   Use case ends.

**Extensions**
* 1a. The specified client does not exist.
    * 1a1. System shows an error message.
    * Use case ends.
* 3a. New values are invalid
    * 3a1. System indicates invalid fields.
    * 3a2. Agent corrects and resubmits.
    * Use case resumes at step 4.
* 3b. Update would create a duplicate with another client.
    * 3b1. System warns and rejects the update.
    * Use case ends.

---

**Use case UC05: Edit a property**

**Preconditions:**
* The system is running and responsive
* At least one property exists in the system

**Guarantees:**
* If successful, the specified property's details are updated with valid values
* If unsuccessful, the system state remains unchanged
* Property relationships with clients are preserved

**MSS**
1. Agent requests to edit a specific property
2. System requests the fields to update
3. Agent provides new values
4. System validates and updates the property, then shows confirmation
   Use case ends.

**Extensions**
* 1a. The specified property does not exist.
    * 1a1. System shows an error message.
    * Use case ends.
* 3a. New values are invalid
    * 3a1. System indicates invalid fields.
    * 3a2. Agent corrects and resubmits.
    * Use case resumes at step 4.
* 3b. Update would create a duplicate with another property.
    * 3b1. System warns and rejects the update.
    * Use case ends.
---

**Use case UC06: Delete a client**

**Preconditions:**
* The system is running and responsive
* The client list has been loaded

**Guarantees:**
* If successful, the specified client is permanently removed from the system
* If unsuccessful, the system state remains unchanged

**MSS**
1. Agent requests to list clients
2. System shows a list of clients
3. Agent requests to delete a specific client in the list
4. System deletes the client and shows confirmation
   Use case ends.

**Extensions**
* 2a. The list is empty.
    * Use case ends.
* 3a. The given index is invalid.
    * 3a1. System shows an error message.
    * Use case resumes at step 2.
* 3b. The given email is invalid.
    * 3b1. System shows an error message.
    * Use case resumes at step 2.

---

**Use case UC07: Delete a property**

**Preconditions:**
* The system is running and responsive
* The property list has been loaded

**Guarantees:**
* If successful, the specified property is permanently removed from the system
* All references to the property are removed from associated clients' owned and interested property lists
* If unsuccessful, the system state remains unchanged

**MSS**
1. Agent requests to list properties
2. System shows a list of properties
3. Agent requests to delete a specific property in the list
4. System deletes the property (and removes it from all associated clients) and shows confirmation
   Use case ends.

**Extensions**
* 2a. The list is empty.
    * Use case ends.
* 3a. The given index is invalid.
    * 3a1. System shows an error message.
    * Use case resumes at step 2.

---

**Use case UC08: Find clients by keywords**

**Preconditions:**
* The system is running and responsive
* Client list has been loaded

**Guarantees:**
* The displayed client list is filtered to show only clients matching the keywords
* The underlying data remains unchanged
* If no matches found, an empty list is displayed with appropriate message

**MSS**
1. Agent requests to find clients by one or more keywords in client's name or tag
2. System filters and shows matching clients
   Use case ends.

**Extensions**
* 1a. Keywords are invalid
    * 1a1. System shows usage guidance.
    * Use case ends.
* 2a. No clients match the keywords.
    * 2a1. System shows "no results found".
    * Use case ends.

---

**Use case UC09: Find properties by keywords**

**Preconditions:**
* The system is running and responsive
* Property list has been loaded

**Guarantees:**
* The displayed property list is filtered to show only properties matching the keywords
* The underlying data remains unchanged
* If no matches found, an empty list is displayed with appropriate message

**MSS**
1. Agent requests to find properties by one or more keywords in property name
2. System filters and shows matching properties
   Use case ends.

**Extensions**
* 1a. Keywords are invalid (e.g., empty/whitespace only).
    * 1a1. System shows usage guidance.
    * Use case ends.
* 2a. No properties match the keywords.
    * 2a1. System shows "no results found".
    * Use case ends.

---

**Use case UC10: List clients**

**Preconditions:**
* The system is running and responsive

**Guarantees:**
* All clients in the system are displayed
* Any previously applied filters are cleared
* System state remains unchanged

**MSS**
1. Agent requests to list clients
2. System shows all clients
   Use case ends.

**Extensions**
* 1a. There are no clients stored.
    * 1a1. System shows an empty list message.
    * Use case ends.

---

**Use case UC11: List properties**

**Preconditions:**
* The system is running and responsive

**Guarantees:**
* All properties in the system are displayed
* Any previously applied filters are cleared
* System state remains unchanged

**MSS**
1. Agent requests to list properties
2. System shows all properties with count
   Use case ends.

**Extensions**
* 1a. There are no properties stored.
    * 1a1. System shows an empty list message with count of 0.
    * Use case ends.

---

---

**Use case UC12: Set owned property for a client**

**Preconditions:**
* The system is running and responsive
* At least one client exists in the system
* At least one property exists in the system

**Guarantees:**
* If successful, the specified property is added to the client's owned properties list
* If unsuccessful, the system state remains unchanged
* No duplicate property ownership is created

**MSS**
1. Agent requests to list clients
2. System shows a list of clients
3. Agent requests to set an owned property for a specific client by providing client index and property name
4. System validates that the client exists, the property exists, and the client doesn't already own it
5. System adds the property to the client's owned properties and shows confirmation
   Use case ends.

**Extensions**
* 2a. The client list is empty.
    * Use case ends.
* 3a. The given client index is invalid.
    * 3a1. System shows an error message.
    * Use case resumes at step 2.
* 4a. The specified property does not exist in the property list.
    * 4a1. System shows "Property not found" error.
    * Use case ends.
* 4b. The client already owns the specified property.
    * 4b1. System shows "duplicate property" error.
    * Use case ends.

---

**Use case UC13: Set interested property for a client**

**Preconditions:**
* The system is running and responsive
* At least one client exists in the system
* At least one property exists in the system

**Guarantees:**
* If successful, the specified property is added to the client's interested properties list
* If unsuccessful, the system state remains unchanged
* No duplicate property interest is created

**MSS**
1. Agent requests to list clients
2. System shows a list of clients
3. Agent requests to set an interested property for a specific client by providing client index and property name
4. System validates that the client exists, the property exists, and the client isn't already interested in it
5. System adds the property to the client's interested properties and shows confirmation
   Use case ends.

**Extensions**
* 2a. The client list is empty.
    * Use case ends.
* 3a. The given client index is invalid.
    * 3a1. System shows an error message.
    * Use case resumes at step 2.
* 4a. The specified property does not exist in the property list.
    * 4a1. System shows "Property not found" error.
    * Use case ends.
* 4b. The client is already interested in the specified property.
    * 4b1. System shows "duplicate link" error.
    * Use case ends.

---

**Use case UC14: Delete owned property from a client**

**Preconditions:**
* The system is running and responsive
* At least one client exists in the system
* The specified client has at least one owned property

**Guarantees:**
* If successful, the specified property is removed from the client's owned properties list
* If unsuccessful, the system state remains unchanged
* The property itself remains in the system (only the relationship is removed)

**MSS**
1. Agent requests to list clients
2. System shows a list of clients
3. Agent requests to delete an owned property from a specific client by providing client index and property name
4. System validates that the client exists, the property exists, and the client owns it
5. System removes the property from the client's owned properties and shows confirmation
   Use case ends.

**Extensions**
* 2a. The client list is empty.
    * Use case ends.
* 3a. The given client index is invalid.
    * 3a1. System shows an error message.
    * Use case resumes at step 2.
* 4a. The specified property does not exist in the property list.
    * 4a1. System shows "Property not found" error.
    * Use case ends.
* 4b. The client does not own the specified property.
    * 4b1. System shows error that the property is not in the client's owned list.
    * Use case ends.

---

**Use case UC15: Delete interested property from a client**

**Preconditions:**
* The system is running and responsive
* At least one client exists in the system
* The specified client has at least one interested property

**Guarantees:**
* If successful, the specified property is removed from the client's interested properties list
* If unsuccessful, the system state remains unchanged
* The property itself remains in the system (only the relationship is removed)

**MSS**
1. Agent requests to list clients
2. System shows a list of clients
3. Agent requests to delete an interested property from a specific client by providing client index and property name
4. System validates that the client exists, the property exists, and the client is interested in it
5. System removes the property from the client's interested properties and shows confirmation
   Use case ends.

**Extensions**
* 2a. The client list is empty.
    * Use case ends.
* 3a. The given client index is invalid.
    * 3a1. System shows an error message.
    * Use case resumes at step 2.
* 4a. The specified property does not exist in the property list.
    * 4a1. System shows "Property not found" error.
    * Use case ends.
* 4b. The client is not interested in the specified property.
    * 4b1. System shows error that the property is not in the client's interested list.
    * Use case ends.

---

**Use case UC16: Clear all clients (dangerous operation)**

**Preconditions:**
* The system is running and responsive

**Guarantees:**
* If successful, all clients are permanently removed from the system
* If cancelled, the system state remains unchanged
* All property relationships with deleted clients are removed

**MSS**
1. Agent requests to clear all clients
2. System requests confirmation
3. Agent confirms the clear operation
4. System deletes all clients and shows confirmation
   Use case ends.

**Extensions**
* 2a. Agent cancels at the confirmation step.
    * 2a1. System aborts the operation.
    * Use case ends.

### Non-Functional Requirements

1. Technical Requirements
    1. The system must avoid OS-dependent features and be portable across Windows, Linux, and macOS without requiring code changes.
    2. The app must run exclusively on Java 17, and shall not require features from higher versions.
    3. The data should be stored in a single human-editable text file in JSON format.
    4. The app should not rely on external database software.
    5. The app should only support offline usage with no server component.
    6. The app must maintain separate storage structures for clients and properties while preserving their relationships.

2. Usability & Quality Requirements
    1. Any user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
    2. The app should be usable by people with visual disabilities (e.g. colour blindness).
    3. All error messages must provide clear, actionable guidance without technical jargon, including specific messages for relationship operations (e.g., property not found, duplicate property link).
    4. The product is offered as a free offline service.
    5. The user interface should be intuitive enough for users who are not IT-savvy.
    6. The application should not require the user to have advanced IT knowledge to operate.
    7. The app shall not require an installer; it should be deliverable as a standalone package (e.g. single JAR) that can run without setup steps.
    8. The product should be for a single user.
    9. The UI should clearly distinguish between owned properties and interested properties for each client.
    10. The dual-pane interface should provide clear visual separation between client list and property list views.

3. Performance Requirements
    1. All command-based operations (add, delete, update, search) for both clients and properties must complete within 1 second under normal usage.
    2. The GUI must support standard resolutions (e.g. 1920×1080 and above) without layout issues, and degrade gracefully (no broken layouts) down to lower resolutions (e.g. 1280×720) or scaled UI modes.
    3. The system must support at least 1,000 clients and 1,000 properties without exceeding 1s for add/delete/update/search operations.
    4. The dual-pane UI must render and switch between client and property views without noticeable lag (< 500ms).

## Glossary

| Term | Meaning |
|------|---------|
| **AB3** | AddressBook Level 3 — the base codebase on which EstateSearch is built. |
| **Architecture Diagram** | A UML diagram showing the high-level relationships among components (UI, Logic, Model, Storage, Commons). |
| **Command** | Represents an executable user action (e.g., `AddCommand`, `DeleteCommand`). Each command encapsulates its own logic. |
| **CommandResult** | The object returned after a command is executed, containing feedback to display in the UI. |
| **Component** | A self-contained module of the application (e.g., `Logic`, `Model`, `UI`, `Storage`). |
| **Logic** | Component that interprets user input, parses commands, executes them, and returns results. |
| **Model** | Component that manages in-memory data (e.g., persons, properties, user preferences). |
| **Storage** | Component responsible for reading/writing JSON files to persist data. |
| **UI (User Interface)** | Component that manages the visible JavaFX interface. |
| **Parser** | A class that converts raw text input (like `"add n/John"`) into a structured `Command` object. |
| **ObservableList** | A JavaFX collection that notifies the UI when its contents change, allowing auto-updates. |
| **Filtered List** | A view of data after applying search filters (e.g., results of `find`). |
| **Property** | A new entity type in EstateSearch, representing a real estate listing with attributes like address and price. |
| **Client** | A person (buyer/seller) entry in EstateSearch, adapted from AB3's Person entity. |
| **Relationship Command** | Commands that link clients and properties (e.g., `setop`, `setip`, `deleteop`, `deleteip`). |
| **VersionedAddressBook** | A proposed data structure for implementing undo/redo functionality. |
| **FXML** | XML-based layout files defining UI components in JavaFX. |
| **Dual-pane Interface** | EstateSearch's UI layout that displays both client and property lists simultaneously. |

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

**Team Size: 5**

1. **Handle long property names and addresses in UI**: Currently, very long property names or addresses may be truncated or cause display issues in the property list panel. We plan to implement proper text wrapping and tooltips to show full details on hover.
    - Current behavior: Property names longer than 50 characters are cut off with "..."
    - Planned behavior: Long property names wrap to multiple lines, and hovering shows the full name in a tooltip

2. **Enhance phone number validation**: Currently, phone number validation only checks for digits and minimum length. We plan to support international phone number formats and provide more specific validation feedback.
    - Current behavior: `add n/John p/12 e/john@example.com a/123 St` shows generic "Phone numbers should only contain numbers..."
    - Planned behavior: `add n/John p/12 e/john@example.com a/123 St` shows "Phone number must be at least 3 digits long. Examples: 91234567, +65 9123 4567"

3. **Improve GUI separation between client and property panels**: Currently, the dual-pane interface shows both client and property lists in similar-looking panels, which can cause confusion about which panel is active. We plan to add clearer visual distinction with headers, different background colors, and active panel highlighting.
    - Current behavior: Both panels look identical with no clear indication of which list is being displayed
    - Planned behavior: Client panel will have a blue header labeled "Clients", property panel will have a green header labeled "Properties", and the active panel will have a highlighted border

4. **Add visual indicators for clients with property relationships**: Currently, users cannot tell at a glance which clients have owned or interested properties without viewing the details. We plan to add icons/badges to the client list cards showing property relationship status.
    - Current behavior: Client card shows only basic information (name, phone, email, tags)
    - Planned behavior: Client card displays small icons: "🏠" badge showing number of owned properties, "⭐" badge showing number of interested properties (e.g., "🏠 2" means owns 2 properties)

5. **Allow filtering clients by property ownership or interest**: Currently, users cannot filter clients based on their property relationships. We plan to add filter options to show only clients who own properties, only clients interested in properties, or clients with specific property associations.
    - Current behavior: `find n/John` only searches by client name or tags
    - Planned behavior: `find owns/Hillside Villa` shows all clients who own "Hillside Villa", `find interested/` shows all clients with at least one interested property

6. **Improve error messages for duplicate entries**: Currently, when adding duplicate clients or properties, the error message is generic ("This person already exists"). We plan to show which specific field caused the duplicate (e.g., same phone number, same email, same property name).
    - Current behavior: `add n/Alice p/91234567 e/alice@example.com a/123 St` when Alice exists shows "This person already exists in the address book"
    - Planned behavior: Shows "Duplicate client detected: A client with phone number 91234567 (Alice Tan) already exists"

7. **Add confirmation prompt for clear command**: Currently, the `clear` command immediately deletes all clients and properties without confirmation, which can lead to accidental data loss. We plan to add a confirmation step for destructive operations.
    - Current behavior: Typing `clear` immediately deletes all data
    - Planned behavior: `clear` prompts "Are you sure you want to delete ALL clients and properties? Type 'clear confirm' to proceed" requiring `clear confirm` to actually execute the deletion

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**


Given below are instructions to test the app manually.

<box type="info">


**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more _exploratory_ testing.


</box>


### Launch and shutdown
#### Initial Launch
1. Download the jar file and copy into an empty folder
2. Double-click the jar file<br>Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

#### Saving window preferences
1. Resize the window to an optimum size. Move the window to a different location. Close the window.
2. Re-launch the app by double-clicking the jar file.<br>
   Expected: The most recent window size and location is retained.

### Managing Persons
#### Adding a person
1. Adding a client with all fields specified
    1. Test case: `add n/Jason Doe p/98765432 e/jasond@example.com a/311, Clementi Ave 2, #02-25 t/buyer`<br>
       Expected: Client is added to the list. Details of the new contact shown in the status message.

2. Adding a duplicate client
    1. Test case: `add n/Duplicate Person p/82345719 e/duplicate@mail.com a/123 Jurong West Avenue 1 #12-18 t/seller`
    2. Repeat with the same command.
       Expected: Client is added the first time. On second time, no person is added and error showing duplicate person.

3. Adding a client with missing fields
    1. Test case: `add n/Missing Fields p/91234567`<br>
       Expected: No client is added. Shows invalid command.
    2. This can be repeated with other fields, eg. `add n/Missing Fields e/field@mail.com`<br>
       Expected: No client is added. Shows invalid command.

#### Listing all client
1. Listing all persons
    1. Test case: `list`<br>
       Expected: All persons in the address book are listed.

#### Editing a client
1. Editing a client while all clients are being shown
    1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
    2. Test case: `edit 1 n/Hannah`<br>
       Expected: First client is edited. Details of the edited client shown in the status message.
    3. Test case: `edit 0 n/Hannah`<br>
       Expected: No client is edited. Error details shown in the status message. Status bar remains the same.
    4. Other incorrect delete commands to try: `edit `, `edit x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

#### Deleting a client
1. Deleting a client while all clients are being shown
    1. Prerequisites: List all clients using the `list` command. Multiple persons in the list.
    2. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message.
    3. Test case: `delete 100`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.
    4. Other incorrect delete commands to try: `delete`, `delete 0`, `delete x`, `...`
       (where x is larger than the list size)<br>
       Expected: Similar to previous.

#### Finding a client
1. Finding a client by name keyword
    1. Prerequisites: List all clients using the `list` command.
    2. Test case: `find n/Alice`<br>
       Expected: Only clients whose names contain "Alice" are listed. Shows "X persons listed!" in the status message.

2. Finding a client by tag keyword
    1. Prerequisites: List all clients using the `list` command.
    2. Test case: `find t/Buyer`<br>
       Expected: Only clients whose tags contain "Buyer" are listed. Shows "X persons listed!" in the status message.

3. Finding no clients of a keyword
    1. Prerequisites: List all clients using the `list` command.
    2. Test case: `find n/NonExistingName`<br>
       Expected: No clients are listed. Shows "0 persons listed!" in the status message.

### Managing Property
#### Adding a property
1. Adding a property with all fields specified
    1. Test case: `addp n/Clementi Villa a/41 Clementi Ave 2, #02-25 pr/700000`<br>
       Expected: Property is added to the list. Details of the new property shown in the status message.

2. Adding a duplicate property
    1. Test case: `addp n/Duplicate Property a/321 Yishun Avenue 1 #12-18 pr/468810`
    2. Repeat with the same command.
       Expected: Property is added the first time. On second time, no property is added and error showing duplicate person.

3. Adding a property with missing fields
    1. Test case: `addp n/Missing property a/127 Bishan Avenue 4 #18-19`<br>
       Expected: No property is added. Shows invalid command.
    2. This can be repeated with other fields, eg. `add n/Missing property pr/214`<br>
       Expected: No property is added. Shows invalid command.

#### Editing a property
1. Editing a property while all properties are being shown
    1. Prerequisites: List all properties using the `listp` command. Multiple properties in the list.
    2. Test case: `editp 1 n/Hannah Mansion`<br>
       Expected: First property is edited. Details of the edited property shown in the status message.
    3. Test case: `editp 0 n/Hannah Mansion`<br>
       Expected: No property is deleted. Error details shown in the status message. Status bar remains the same.
    4. Other incorrect delete commands to try: `editp `, `editp x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

#### Listing all properties
1. Listing all property
    1. Test case: `listp`<br>
       Expected: All properties in the address book are listed.

#### Deleting a property
1. Deleting a property while all property are being shown
    1. Prerequisites: List all properties using the `listp` command. Multiple persons in the list.
    2. Test case: `deletep 1`<br>
       Expected: First property is deleted from the list. Details of the deleted contact shown in the status message.
    3. Test case: `deletep 100`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.
    4. Other incorrect delete commands to try: `deletep`, `deletep 0`, `deletep x`, `...`
       (where x is larger than the list size)<br>
       Expected: Similar to previous.

#### Finding a property
1. Finding a property by name keyword
    1. Prerequisites: List all properties using the `listp` command.
    2. Test case: `findp n/HDB`<br>
       Expected: All properties contain "HDB" are listed. Shows "X properties listed!" in the status message.

2. No keyword found
    1. Prerequisites: List all properties using the `listp` command.
    2. Test case: `findp n/Name`<br>
       Expected: No properties are listed. Shows "0 properties listed!" in the status message.

### Linking Properties to Clients
#### Setting owned property
1. Setting client's owned properties while all clients are being shown
    1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
    2. Test case: `setop 1 n/Hannah Mansion`<br>
       Expected: If properties with the specification exists, property will be added into client's owned properties.
       Client's and property detail in the status message.
    3. Test case: `setop n/Hannah`<br>
       Expected: Nothing happens. Error details shown in the status message. Status bar remains the same.

#### Setting interested property
1. Setting client's interested properties while all clients are being shown
    1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
    2. Test case: `setip 1 n/Hannah Mansion`<br>
       Expected: If properties with the specification exists, property will be added into client's interested properties.
       Client's and property detail in the status message.
    3. Test case: `setip n/Hannah`<br>
       Expected: Nothing happens. Error details shown in the status message. Status bar remains the same.

#### Deleting owned property
1. Deleting client's owned properties while all clients are being shown
    1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
    2. Test case: `deleteop 1 n/Hannah Mansion`<br>
       Expected: If properties with the specification exists, property will be deleted from client's owned properties.
       Client's and property detail in the status message.
    3. Test case: `deleteop n/Hannah`<br>
       Expected: Nothing happens. Error details shown in the status message. Status bar remains the same.


#### Deleting interested property
1. Deleting client's interested properties while all clients are being shown
    1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
    2. Test case: `deleteip 1 n/Hannah Mansion`<br>
       Expected: If properties with the specification exists, property will be deleted from client's interested properties.
       Client's and property detail in the status message.
    3. Test case: `deleteip n/Hannah`<br>
       Expected: Nothing happens. Error details shown in the status message. Status bar remains the same.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Effort**

### Difficulty Level and Challenges

EstateSearch presented significantly greater complexity compared to AB3 due to the introduction of multiple entity types and their inter-relationships. While AB3 manages a single entity type (Person), our application handles two distinct entity types—Client and Property—with bidirectional associations between them.

**Key Challenges:**

1. **Multiple Entity Management**: Implementing operations for Client and Property entities required careful design to maintain consistency across the codebase. Each entity needed its own set of commands, parsers, and UI components, effectively doubling the implementation effort for core features.

2. **Entity Relationships**: The most challenging aspect was managing relationships between clients and properties. Clients can own properties or be interested in properties, creating a many-to-many relationship. This required:
    - Careful synchronization when properties are deleted (automatically removing references from all associated clients)
    - Complex validation to ensure referential integrity
    - Additional commands for managing these associations (e.g., `setop`, `setip`, `deleteop`, `deleteip`)

3. **Data Model Complexity**: Properties have distinct attributes (address, price, property name) that differ significantly from Client attributes. This necessitated separate validation logic, storage adapters, and display components for each entity type.

4. **UI Complexity**: Unlike AB3's single list view, EstateSearch required implementing a dual-pane interface to display both clients and properties simultaneously, with dynamic switching between views based on user commands. This involved significant modifications to the UI component architecture.

### Effort Required

The project required approximately similar effort to the individual project, due to:
- **Dual Entity Implementation**: Every feature implemented for clients needed a parallel implementation for properties, including commands, parsers, storage, and UI components.
- **Extended Testing**: Test coverage needed to account for entity interactions, edge cases in relationships, and state consistency across both entity types.

### Achievements

Despite the increased complexity, the team successfully delivered:

1. **Comprehensive Feature Set**: Full CRUD operations for both Client and Property entities, including add, edit, delete, find, and list commands for each.

2. **Robust Relationship System**: A reliable mechanism for associating properties with clients, with automatic cleanup to maintain data integrity when entities are deleted.

3. **Enhanced User Experience**: A property-focused interface tailored for real estate agents, with features like property price tracking and filtered property listings.

4. **Maintained Code Quality**: Despite the expanded codebase, we maintained high test coverage and adhered to software engineering best practices throughout development.

### Reuse and Efficiency

The project benefited from the AB3 foundation, which provided:

- **Core Architecture**: The Logic-Model-Storage-UI architecture was reused and extended for property management
- **Client class**: The AB3's person class was reused and adapted for client management, alongside its features such as add, edit, delete, etc.
- **Command Pattern**: The existing command execution framework was adapted for property commands with minimal modifications (~10% effort saved)
- **Testing Framework**: AB3's testing utilities and patterns were reused, saving significant effort in test setup

However, substantial new code was required for:
- Property entity model and all associated operations (implemented in `seedu.address.model.property` package)
- Property-specific commands and parsers (implemented in `seedu.address.logic.commands.property` and `seedu.address.logic.parser.property` packages)
- Relationship management logic (implemented across `Model` and command classes like `DeletePropertyCommand`, which handles cascading updates)
- Dual-view UI components (implemented in `seedu.address.ui.property` package)


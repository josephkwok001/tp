---
  layout: default.md
    title: "Developer Guide"
    pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

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

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
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

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a client).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Client` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Clinet` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

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

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `EstateSearch` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th client in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new client. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the client was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the client being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


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

| Priority | As a …​                                   | I want to …​                 | So that I can…​                                                      |
|----------|-------------------------------------------|------------------------------|----------------------------------------------------------------------|
| `* * *`  | agent using the app                       | add contacts to my contact list       | see my contacts                |
| `* * *`  | long-time user                            | delete contacts that I am no longer interested in or require             |  clean my contacts list                                                                    |
| `* * *`  | user  | exit the application through the cli              | be efficient in my use of the cli without having to use the gui                                 |
| `* * *`  | real estate agent                                      | see all the contacts that I have        | better organise my contacts based on their purposes such as buyers, sellers, rentals etc. |

*{More to be added}*


### Use cases

(For all use cases below, the **System** is the `EstateSearch` and the **Actor** is the `real estate agents`, unless specified otherwise)

**Use case UC01: View help**

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

**MSS**
1. Agent requests to add a client
2. System requests required details (name, phone, email, address)
3. Agent enters the requested details
4. System validates and saves the client, and shows confirmation
   Use case ends.

**Extensions**
* 3a. One or more fields are missing/invalid.
  * 3a1. System indicates the problematic fields.
  * 3a2. Agent re-enters details.
  * Use case resumes at step 4.
* 3b. A duplicate client is detected (based on unique fields).
  * 3b1. System warns about duplication and rejects the add.
  * Use case ends.

---

**Use case UC03: Add a property**

**MSS**
1. Agent requests to add a property
2. System requests required details (address, price, property name)
3. Agent enters the requested details
4. System validates and saves the property, and shows confirmation
   Use case ends.

**Extensions**
* 3a. One or more fields are missing/invalid.
  * 3a1. System indicates the problematic fields.
  * 3a2. Agent re-enters details.
  * Use case resumes at step 4.
* 3b. A duplicate property is detected (based on property name).
  * 3b1. System warns about duplication and rejects the add.
  * Use case ends.

---

**Use case UC04: Edit a client**

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
* 3a. New values are invalid (e.g., phone/email format).
  * 3a1. System indicates invalid fields.
  * 3a2. Agent corrects and resubmits.
  * Use case resumes at step 4.
* 3b. Update would create a duplicate with another client.
  * 3b1. System warns and rejects the update.
  * Use case ends.

---

**Use case UC05: Edit a property**

**MSS**
1. Agent requests to list properties
2. System shows a list of properties
3. Agent requests to edit a specific property in the list
4. System requests the fields to update
5. Agent provides new values for one or more fields (name, address, price)
6. System validates and updates the property, then shows confirmation
   Use case ends.

**Extensions**
* 2a. The list is empty.
  * Use case ends.
* 3a. The specified index is invalid.
  * 3a1. System shows an error message.
  * Use case resumes at step 2.
* 5a. No fields are provided for update.
  * 5a1. System indicates that at least one field must be provided.
  * Use case resumes at step 4.
* 5b. New values are invalid (e.g., price format).
  * 5b1. System indicates invalid fields.
  * 5b2. Agent corrects and resubmits.
  * Use case resumes at step 6.
* 5c. Update would create a duplicate with another property.
  * 5c1. System warns and rejects the update.
  * Use case ends.

---

**Use case UC06: Delete a client**

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

---

**Use case UC07: Delete a property**

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

**MSS**
1. Agent requests to find clients by one or more keywords
2. System filters and shows matching clients
   Use case ends.

**Extensions**
* 1a. Keywords are invalid (e.g., empty/whitespace only).
  * 1a1. System shows usage guidance.
  * Use case ends.
* 2a. No clients match the keywords.
  * 2a1. System shows "no results found".
  * Use case ends.

---

**Use case UC09: Find properties by keywords**

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

**Use case UC10: Add a tag to a client (find-then-act)**

**MSS**
1. Agent requests to find clients by keyword(s)
2. System shows matching clients
3. Agent selects a specific client from the results
4. System requests the tag to add
5. Agent provides the tag
6. System adds the tag to the client and shows confirmation
   Use case ends.

**Extensions**
* 2a. No clients match the keyword(s).
  * 2a1. System shows "no results found".
  * Use case ends.
* 3a. The selected index is invalid.
  * 3a1. System shows an error message.
  * Use case resumes at step 2.
* 5a. The tag already exists on this client.
  * 5a1. System informs duplication and rejects the add.
  * Use case ends.
* 5b. Tag value is invalid (e.g., length/characters).
  * 5b1. System shows validation error.
  * 5b2. Agent re-enters a valid tag.
  * Use case resumes at step 6.

---

**Use case UC11: List clients**

**MSS**
1. Agent requests to list clients
2. System shows all clients
   Use case ends.

**Extensions**
* 1a. There are no clients stored.
  * 1a1. System shows an empty list message.
  * Use case ends.

---

**Use case UC12: List properties**

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

**Use case UC13: Set owned property for a client**

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

**Use case UC14: Set interested property for a client**

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

**Use case UC15: Delete owned property from a client**

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

**Use case UC16: Delete interested property from a client**

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

**Use case UC17: Clear all clients (dangerous operation)**

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

### Glossary

* **Real estate agent**: A professional who helps clients buy, sell, or rent properties. They link between buyers and sellers, providing advice, handling negotiations, and managing paperwork.
* **Command-Line Interface (CLI):** A text-based interface where users interact with the program by typing.
* **AB3 (AddressBook Level 3):** A desktop application for managing contacts.
* **GUI (Graphical User Interface):** An interface that allows users to interact with software through visual elements.
* **Interface:** Allows users to interact with the program.


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**


Given below are instructions to test the app manually.


<box type="info" seamless>


**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more _exploratory_ testing.


</box>


### Launch and shutdown


1. Initial launch


   1. Download the jar file and copy into an empty folder


   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.


1. Saving window preferences


   1. Resize the window to an optimum size. Move the window to a different location. Close the window.


   1. Re-launch the app by double-clicking the jar file.<br>
      Expected: The most recent window size and location is retained.


1. _{ more test cases …​ }_


### Adding a client


1. Adding a client while all clients are being shown


   1. Test case: `add n/Hannah p/89098891 e/hannah@gmail.com a/123 Bridge t/client t/buyer`<br>
      Expected: client added onto the list. Details of the added client shown in the status message.


   1. Test case: `add n/Hannah p/89098891 e/hannah@gmail.com a/123 Bridge t/client buyer`<br>
      Expected: No client is added. Error details shown in the status message. Status bar remains the same.


   1. Other incorrect add client commands to try: `add p/89098891 e/hannah@gmail.com a/123 Bridge t/client buyer`,<br>
      Expected: Similar to previous.


### Adding a property


1. Adding a property while all properties are being shown


   1. Test case: `addp n/123 Bridge pr/11200 a/123 Bridge`<br>
      Expected: Property added onto the list. Details of the added property shown in the status message.


   1. Test case: `addp n/123 Bridge pr/1ooo a/1233 Bridge`<br>
      Expected: No property is added. Error details shown in the status message. Status bar remains the same.


   1. Other incorrect add property commands to try: `addp n/123 Bridge pr/qwerty a/1233 Bridge`,<br>
      Expected: Similar to previous.


### Deleting a client


1. Deleting a client while all clients are being shown


   1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.


   1. Test case: `delete 1`<br>
      Expected: First client is deleted from the list. Details of the deleted client shown in the status message.


   1. Test case: `delete 0`<br>
      Expected: No client is deleted. Error details shown in the status message. Status bar remains the same.


   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.


### Deleting a property


1. Deleting a property while all properties are being shown


   1. Prerequisites: List all properties using the `listp` command. Multiple properties in the list.


   1. Test case: `deletep 1`<br>
      Expected: First property is deleted from the list. Details of the deleted property shown in the status message.


   1. Test case: `deletep 0`<br>
      Expected: No property is deleted. Error details shown in the status message. Status bar remains the same.


   1. Other incorrect delete commands to try: `deletep`, `deletep x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.


### Editing a client


1. Editing a client while all clients are being shown


   1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.


   1. Test case: `edit 1 n/Hannah`<br>
      Expected: First client is edited. Details of the edited client shown in the status message.


   1. Test case: `edit 0 n/Hannah`<br>
      Expected: No client is deleted. Error details shown in the status message. Status bar remains the same.


   1. Other incorrect delete commands to try: `edit `, `edit x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.


### Editing a property


1. Editing a property while all properties are being shown


   1. Prerequisites: List all properties using the `listp` command. Multiple properties in the list.


   1. Test case: `editp 1 n/Hannah Mansion`<br>
      Expected: First property is edited. Details of the edited property shown in the status message.


   1. Test case: `editp 0 n/Hannah's Mansion`<br>
      Expected: No property is deleted. Error details shown in the status message. Status bar remains the same.


   1. Other incorrect delete commands to try: `editp `, `editp x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.


### Finding a client


1. Finding a client while all clients are being shown


   1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.


   1. Test case: `find n/Hannah`<br>
      Expected: If clients with the specification exists, clients will be shown. Number of clients in the status message.


   1. Test caseL `find t/friend`<br>
      Expected: If client with the specificatino exists, client will be shown. Number of clients in the status message.


   1. Test case: `find n/Hannah`<br>
      Expected: No property is deleted. Error details shown in the status message. Status bar remains the same.


### Finding a property


1. Finding a property while all properties are being shown


   1. Prerequisites: List all properties using the `listp` command. Multiple properties in the list.


   1. Test case: `findp n/Hannah Mansion`<br>
      Expected: If properties with the specification exists, properties will be shown. Number of properties in the status message.


   1. Test case: `findp n/Hannah'`<br>
      Expected: No property is deleted. Error details shown in the status message. Status bar remains the same.


### Setting owned property


1. Setting client's owned properties while all clients are being shown


   1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.


   1. Test case: `setop i/1 n/Hannah Mansion`<br>
      Expected: If properties with the specification exists, property will be added into client's owned properties. Client's and property detail in the status message.


   1. Test case: `setop n/Hannah'`<br>'
      Expected: Nothing happens. Error details shown in the status message. Status bar remains the same.


### Setting interested property


1. Setting client's interested properties while all clients are being shown


   1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.


   1. Test case: `setip i/1 n/Hannah Mansion`<br>
      Expected: If properties with the specification exists, property will be added into client's interested properties. Client's and property detail in the status message.


   1. Test case: `setip n/Hannah'`<br>'
      Expected: Nothing happens. Error details shown in the status message. Status bar remains the same.


### Deleting owned property


1. Deleting client's owned properties while all clients are being shown


   1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.


   1. Test case: `deleteop i/1 n/Hannah Mansion`<br>
      Expected: If properties with the specification exists, property will be deleted from client's owned properties. Client's and property detail in the status message.


   1. Test case: `deleteop n/Hannah'`<br>'
      Expected: Nothing happens. Error details shown in the status message. Status bar remains the same.


### Deleting interested property


1. Deleting client's interested properties while all clients are being shown


   1. Prerequisites: List all clients using the `list` command. Multiple clients in the list.


   1. Test case: `deleteip i/1 n/Hannah Mansion`<br>
      Expected: If properties with the specification exists, property will be deleted from client's interested properties. Client's and property detail in the status message.


   1. Test case: `deleteip n/Hannah'`<br>'
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
   - Additional commands for managing these associations (e.g., `setownedp`, `setinterestedp`)

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


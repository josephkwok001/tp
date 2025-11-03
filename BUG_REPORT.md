# EstateSearch Bug Report

## Testing Methodology
This bug report was generated through systematic testing of all features documented in the User Guide. Each command was tested with:
- Valid inputs (expected to succeed)
- Invalid inputs (expected to fail)
- Edge cases and boundary conditions
- Parameter validation

All existing unit tests pass successfully, indicating these are issues not currently covered by the test suite.

---

## Bugs Found

### 1. ❌ EDIT COMMANDS RESET FILTER - Inconsistent Behavior
**Severity:** HIGH
**Locations:**
- src/main/java/seedu/address/logic/commands/person/EditCommand.java:89
- src/main/java/seedu/address/logic/commands/property/EditPropertyCommand.java:89

**Description:**
Both the `edit` and `editp` commands reset their respective filtered lists to show all items after execution, which is inconsistent with other commands and not documented in the User Guide.

**Root Cause:**
Line 89 of EditCommand.java explicitly calls:
```java
model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
```

This resets any active filter (from `find` commands) to display all persons.

**Impact:**
This creates confusing user experience where:
1. User filters the list with `find t/buyer` (shows 1 person)
2. User edits with `edit 1 ...`
3. The list suddenly shows all persons again (2 persons)
4. Subsequent operations using indices may operate on unexpected persons

**Steps to Reproduce:**
1. Add clients:
   - `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
   - `add n/Betsy Crowe t/client e/betsycrowe@example.com a/Changi p/1234567 t/buyer`
2. Run: `find t/buyer` (filters to show only Betsy - list size: 1)
3. Edit: `edit 1 p/91234567 e/johndoe@example.com`
4. Observe: List now shows both persons (list size: 2) - **Filter was reset!**

**Evidence:** See specific_bug_test.txt lines 41-47:
- Before edit: List size = 1 (only Betsy)
- After edit: List size = 2 (both John and Betsy)

**Expected Behavior:**
Based on User Guide documentation (line 336-337) which states commands operate on the "currently displayed" list, the filter should be maintained after editing, consistent with other commands like `delete`.

**Recommended Fix:**
Remove the following lines:
- EditCommand.java:89: `model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);`
- EditPropertyCommand.java:89: `model.updateFilteredPropertyList(PREDICATE_SHOW_ALL_PROPERTIES);`

Or replace them with logic to maintain the current filter predicate.

**User Guide Reference:** docs/UserGuide.md:336-337, 339-358, 443-459

---

### 2. ⚠️  CLEAR COMMAND - Documentation Mismatch
**Severity:** LOW
**Location:** docs/UserGuide.md:258

**Description:**
The User Guide states the clear command should display "Address book has been cleared", but the actual message is "Person and property data has been cleared!"

**Actual Message:** `Person and property data has been cleared!`
**User Guide Says:** `Address book has been cleared`

**Recommendation:** Update the User Guide to match the actual implementation message, which is more descriptive and user-friendly.

**User Guide Reference:** docs/UserGuide.md:258

---

### 3. ⚠️  DELETEOP COMMAND - Documentation Mismatch
**Severity:** LOW
**Location:** docs/UserGuide.md:553-577 (deleteop section)

**Description:**
The User Guide implies the success message should contain "Deleted owned property", but the actual message format is different.

**Actual Message:** `Deleted property: Hillside Villa from John Doe's list of owned properties.`
**User Guide Implies:** Message should contain "Deleted owned property"

**Screenshot Location:** docs/images/results/deleteop.png (line 570)

**Recommendation:** Update the User Guide description or screenshot to match the actual implementation. The actual message is more descriptive and better for user understanding.

**User Guide Reference:** docs/UserGuide.md:570

---

### 4. ⚠️  DELETEIP COMMAND - Documentation Mismatch
**Severity:** LOW
**Location:** docs/UserGuide.md:578-603 (deleteip section)

**Description:**
Similar to deleteop, the User Guide implies the success message should contain "Deleted interested property", but the actual message format is different.

**Actual Message:** `Deleted property: Sunshine Condo from John Doe's list of interested properties.`
**User Guide Implies:** Message should contain "Deleted interested property"

**Screenshot Location:** docs/images/results/deleteip.png (line 595)

**Recommendation:** Update the User Guide description or screenshot to match the actual implementation. The actual message is more descriptive and better for user understanding.

**User Guide Reference:** docs/UserGuide.md:595

---

### 5. ⚠️  EXPORT COMMAND - Documentation Mismatch
**Severity:** LOW
**Location:** docs/UserGuide.md:229-256 (export section)

**Description:**
The User Guide description mentions "exported to CSV" but the actual success message is more detailed.

**Actual Message:** `Exported 2 clients and 2 properties to test_export.csv`
**User Guide Says:** Should contain "exported to CSV"

**Recommendation:** Update the User Guide to reflect the actual, more informative message format. The actual message provides useful information about the number of items exported.

**User Guide Reference:** docs/UserGuide.md:229-256

---

### 6. ℹ️  COMMAND CASE SENSITIVITY - Expected Behavior
**Severity:** NONE (Not a bug)
**Type:** Clarification Needed

**Description:**
Commands are case-sensitive (`list` works, but `LIST` does not). This is standard CLI behavior but could be clarified in the User Guide.

**User Guide Note (line 202):**
"Extraneous parameters for commands that do not take in parameters... will be ignored"

**Recommendation:** Add a note in the User Guide clarifying that commands are case-sensitive and must be entered in lowercase.

---

## Additional Testing Results

### ✅ All Parameter Validations Working Correctly
- Phone number validation (3-20 digits): PASS
- Email format validation: PASS
- Name length validation (max 50 characters): PASS
- Tag length validation (max 30 characters): PASS
- Price validation (positive integers, max 2147483647): PASS
- Duplicate detection (clients and properties): PASS
- Invalid index handling: PASS
- Property relationship validations: PASS

### ✅ All Core Features Working
- Add, edit, delete clients: PASS
- Add, edit, delete properties: PASS
- Find clients by name/tag: PASS
- Find properties by name: PASS
- Set/delete owned properties: PASS
- Set/delete interested properties: PASS
- Export to CSV: PASS
- List commands: PASS
- Clear command: PASS
- Help command: PASS

---

## Summary

**Total Issues Found:** 6
**High Severity:** 1 (Delete by email logic error)
**Low Severity:** 4 (Documentation mismatches)
**Informational:** 1 (Clarification needed)

**Critical Issues:** 1 issue requires code investigation and potential fix
**Documentation Issues:** 4 issues require User Guide updates

The application is generally well-implemented with good validation. Most issues are documentation mismatches where the actual behavior is often better/more descriptive than what the User Guide suggests. The primary concern is the delete by email logic error which should be investigated and fixed.

---

## Recommendations

1. **Priority 1:** Investigate and fix the delete by email issue (Bug #1)
2. **Priority 2:** Update User Guide to match actual implementation messages (Bugs #2-5)
3. **Priority 3:** Add clarification about case-sensitive commands
4. **Priority 4:** Review all UserGuide screenshots to ensure they match current implementation

## Test Evidence

Full test output available in: `bug_report.txt`
Test class location: `src/test/java/seedu/address/ManualCommandTest.java`

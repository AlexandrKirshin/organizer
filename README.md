# organizer

* The simple console organizer has the following fields: tableNumber(numeric value), name, position, organization, 
mail, phoneNumber
    * Import file - /setFieldPath pathToFile
    * Add contact - /add tableNumber(numeric value), "name", "position", "organization", "mail", "phoneNumber"
    * Update contact - /update "searchField", "searchFieldValue", "updatingField", "newValue"
    * Delete contact - /delete "searchField", "searchFieldValue"
    * Show list contact - /get
    * Find contact - /find "searchField", "searchFieldValue"

* XML storage organizer template /organizer.xml
### Show a list with sections which are single item choice menus.
#### Selection  of an item in a section can cause disabling of certain items in other sections. An item may need be disbaled due to multiple possible selections.
* **WeirdAdapter** Shows a list of items which can be subsection titles or subsection choices. Choices can be enabled/disabled and checked/unchecked.
* **MainActivity** Implements _onVariationClicked_ to calculate changes required in checked and enabled states.
* **VariantSelectionModel** Has the backing data for subsections and the rules of blocking choices. It returns index of items in the list which need to be unchecked or set enabled/disabled. 
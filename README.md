JS Toolbox
==========

An Intellij / Webstorm plugin that provides several tools for javascript development.

Here is a list of available actions:

* Go to test [Alt T], [Ctrl Command T] on a Mac
* Go to view [Alt V], [Ctrl Command V] on a Mac
* Go to constructor [Alt G], [Ctrl Command G] on a mac
* Join multi-line strings and variable declarations [Ctrl SHIFT Alt J], [Ctrl Command J] on a Mac
* Open current file in browser [Ctrl SHIFT Alt G], [SHIFT Option Command G] on a Mac
* Sort the selection using a user-defined splitter [SHIFT alt 1], [Command SHIFT 1] on Mac.
* Toggle from dash case to camel case and back
* Generate a method for the current javascript class
* Override a method from the parent javascript class

See all the JS Toolbox actions from two locations:

* From Tools > JS Toolbox
* From the editor, right click > JS Toolbox

### Go to test [Alt T], [Ctrl Command T] on a Mac

Jump between a javascript file and its unit test. You can configure the
file name patterns under:

Settings > JS Toolbox

And set values for **Unit test suffix** and **File suffix**

If you need multiple patterns use comma. For example:

Unit test suffix: ``Spec.js,-spec.js``
File suffix: ``-controller.js,.js``

With this configuration the action will take you from:

``my-component.js`` or ``my-component-controller.js``

to

``my-componentSpec.js`` or ``my-component-spec.js``

### Go to view [Alt V], [Ctrl Command V] on a Mac

Jump between a javascript file and its associated view. You can configure the
file name patterns under:

Settings > JS Toolbox

And set values for **View suffix** and **File suffix**

If you need multiple patterns use comma. For example:

File suffix: ``-controller.js,.js``
View suffix: ``-view.html,.html``

With this configuration the action will take you from:

``my-component.js`` or ``my-component-controller.js``

to

``my-component.html`` or ``my-component-view.html``

### Go to constructor [Alt G], [Ctrl Command G] on a mac

Jump to the constructor of the current javascript file.

### Join multi-line strings and variable declarations [Ctrl SHIFT Alt J], [Ctrl Command J] on a Mac

Join strings and variable declarations

Turn a multi-line string into a single string. Press Ctrl SHIFT Alt J on any
of the string lines or select a block of text and join.

```javascript
var s = 'one ' +
    'two ' +
    'three';
```

Into a single string:
```javascript
var s = 'one two three';
```

Join multiple vars into a single var:
```javascript
var foo = 1;
var bar = 2;
```

Into a single var declaration:
```javascript
var foo = 1,
    bar = 2;
```

### Open current file in browser [Ctrl SHIFT Alt G], [SHIFT Option Command G] on a Mac

Open the current file in the browser. Configure the URL that you want to
use when opening the browser under Settings > JS Toolbox.

### Sort the selection using a user-defined splitter [SHIFT alt 1], [Command SHIFT 1] on Mac.

Select a block of code that you want to sort, choose sort, and enter a separator (like comma or new line) to sort.

### Generate a method for the current javascript class (Under generate menu)

Add a new method to the current javascript class.

You need to use the @constructor annotation to mark the constructor of the
class.

### Override a method from the parent javascript class (Under generate menu)

Override a parent method in the current javascript class.

You need to annotate the classes with the @constructor and @extends
annotations, otherwise the plugin cannot find the class hierarchy.

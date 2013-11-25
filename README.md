JS Toolbox
==========

An Intellij / Webstrom plugin that provides several tools for javascript development.

Here is a list of available actions:

* Go to test (Alt T)
* Go to view (Alt V)
* Go to constructor (Alt G)
* Join string or multiple vars into a single var (Ctrl SHIFT Alt J)
* Open current file in browser (Ctrl SHIFT Alt G)
* Generate a method for the current javascript class.
* Override a method from a parent javascript class

### Go to test (Alt T)

Jump between the javascript file and the unit test. You can configure the patterns under:

Settings > JS Tollbox

And set values for **Unit test suffix** and **File suffix**

If you need multiple patterns use comma. For example: **Spec.js,-spec.js**

### Go to view (Alt V)

Jump between the javascript file and the view. You can configura the patterns under:

Settings > JS Tollbox

And set values for **View suffix** and **File suffix**

### Go to constructor (Alt G)

Jump to the constructor of the current javascript file.

### Join multi-line strings and variable declarations (Ctrl SHIFT Alt J)

Join strings and variable declarations

Turn a mulit-line string into a single string. Press Ctrl SHIFT Alt J on any of the 
string lines or select a block of text and join.

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

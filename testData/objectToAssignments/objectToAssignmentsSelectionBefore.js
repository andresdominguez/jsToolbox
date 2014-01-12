/**
 * The const.
 * @constructor
 */
TheConstructor = function () {
    this.hey = 123;
};

/**
 * The js doc.
 * @param left
 * @param right
 */
TheConstructor.prototype.someFn = function (left, right) {
    return left + right;
};

<selection>

var foo = {
    one: 'xxx',
    two: 123,<caret>
    'three': 'three',
    'person': {
        name: 'John',
        lastName: 'Doe'
    }
};

</selection>

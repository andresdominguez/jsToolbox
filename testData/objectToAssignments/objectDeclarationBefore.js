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

var foo = <caret>{
    one: 'xxx',
    two: 123,
    'three': 'three',
    'person': {
        name: 'John',
        lastName: 'Doe'
    }
};

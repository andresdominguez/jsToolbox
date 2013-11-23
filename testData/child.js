/**
 * @constructor
 * @extends Yo.parent
 */
Yo.child = function () {
};

/**
 * @override
 */
Yo.child.prototype.anotherMEth = function() {
    Yo.parent.prototype.anotherMEth.call(this);
};

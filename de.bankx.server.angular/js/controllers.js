/**
 * INSPINIA - Responsive Admin Theme
 *
 */

/**
 * MainCtrl - controller
 */
function MainCtrl() {

    this.userName = 'Administrator';

};


angular
    .module('inspinia')
    .controller('MainCtrl', MainCtrl)

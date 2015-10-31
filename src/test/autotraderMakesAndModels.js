var data = "";


var makeSelect = $("#j_id_ay-searchform-col-wrapper-col1-listingsSearch-makemodelSEVO-make1-selectOneMenu");
var makeOptions = $("#j_id_ay-searchform-col-wrapper-col1-listingsSearch-makemodelSEVO-make1-selectOneMenu option");

var prevFirstOptValue = null;

var processMake = function(index) {
    if (index === makeOptions.length - 1) {
        return;
    }

    console.log("Current index: " + index);

    $(makeSelect).val(makeOptions[index].value);

    setTimeout(function() {
        $(makeSelect).change();
        var waitForLoad;
        waitForLoad = function() {
            setTimeout(function() {
                console.log("waited");
                var modelOpts = $("#j_id_ay-searchform-col-wrapper-col1-listingsSearch-makemodelSEVO-model1-selectOneMenu option");
                var shouldWaitMore = false;
                if(modelOpts.length > 1){
                    console.log("checking");
                    var firstVal = modelOpts[1].value;
                    shouldWaitMore = prevFirstOptValue === firstVal;
                }

                console.log("checking2");
                if (shouldWaitMore) {
                    console.log("Waiting more");
                    waitForLoad();
                } else {
                    modelOpts.each(function(index, elem) {
                        data += 'modelsMap.put("' + elem.innerHTML.toLowerCase() + '", "' + elem.value + '");';
                        data += '\n';
                    });

                    if(modelOpts.length > 1) {
                        prevFirstOptValue = modelOpts[1].value;
                    }
                    processMake(index + 1);
                }

            }, 7000);

        };
        waitForLoad();



    }, 2000);
};

processMake(1);

var data = "";


var makeSelect = $($(".select.maker-select-dropdown.btn-small")[0]);
var makeOptions = makeSelect.find("option");

var prevFirstOptValue = null;

var processMake = function (index) {
    if (index === makeOptions.length - 1) {
        return;
    }

    console.log("Current index: " + index);

    $(makeSelect).val(makeOptions[index].value);

    setTimeout(function () {
        $(makeSelect).change();
        var waitForLoad;
        waitForLoad = function () {
            setTimeout(function () {
                console.log("waited");
                var modelOpts = $(".select.model-select-dropdown.btn-small option");
                var shouldWaitMore = false;
                if (modelOpts.length > 1) {
                    console.log("checking");
                    var firstVal = modelOpts[1].value;
                    shouldWaitMore = prevFirstOptValue === firstVal;
                }

                console.log("checking2");
                if (shouldWaitMore) {
                    console.log("Waiting more");
                    waitForLoad();
                } else {
                    modelOpts.each(function (index, elem) {
                        data += 'modelsMap.put("' + elem.innerHTML.toLowerCase() + '", "' + elem.value + '");';
                        data += '\n';
                    });

                    if (modelOpts.length > 1) {
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


///get makes script

var data = "";


var makeSelect = $($(".select.maker-select-dropdown.btn-small")[0]);
var makeOptions = makeSelect.find("option");

for (var i = 0; i < makeOptions.length; i++) {
    var elem = makeOptions[i];
    data += 'makesMap.put("' + elem.innerHTML.toLowerCase() + '", "' + elem.value + '");';
    data += '\n';
}

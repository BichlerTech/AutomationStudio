function enable_driver_edit() {
    $("input.dc_inputs").removeAttr("readonly");
    $("input.dc_inputs").css({"background-color":"white"});
    $("input.dc_checks").removeAttr("disabled");
    $("input.dc_buttons").remove();
    $("input.dc_buttons_hidden").show();
}

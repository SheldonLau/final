$(document).ready(function() {
    console.log("JS loaded");

    $(".searchButton").click(function() {
        ev.preventDefault();
        var $self=$(this).parent();
        $self.children().animate({left:'1000px'},function() {
            document.location= $self.attr('href');
        });
    });

    $("input[type=text]").click(function() {
        $(".searchHelp").animate({opacity:'1'})
    })
})

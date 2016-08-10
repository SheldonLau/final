$.fn.loopingAnimation = function(props, dur, eas)
{
    if (this.data('loop') == true)
    {
       this.animate( props, dur, eas, function() {
           if( $(this).data('loop') == true ) $(this).loopingAnimation(props, dur, eas);
       });
    }
    return this; // Don't break the chain
}

$(document).ready(function() {
    console.log("JS loaded");

    $(".searchButton").click(function() {
        ev.preventDefault();
        var $self=$(this).parent();
        $self.children().animate({'left':'1000px'},function() {
            document.location= $self.attr('href');
        });
    });

    $(":text").mouseover(function() {
        $(".searchHelp").animate({'opacity':'1'},1000);
    });

    $(":submit").mouseover(function() {
        this.animate({'margin-left':'5px'},)
    });
})

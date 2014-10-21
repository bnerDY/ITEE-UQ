(function($) {
  var isWindowLoaded= false;

  $(window).load(function(){
    isWindowLoaded= true;
  });


  $.fn.gallery = function(options) {

    var _this = $(this),
        opts =  $.extend({}, $.fn.gallery.defaults, options),
        context = $.fn.extend(opts, {
          imgs: _this.find('img'),//images array
          cnv_width: _this.width(), //canvas width
          cnv_height: _this.height(), //canvas height
          canvas_context: _this[0].getContext('2d'),
          act_index: 0,
          actual_step: 0,
          white_space: 0, // white space between images
          start_pos_x: 0,
          pos_y: 0,
          moving: 0,
          util_image: new Array(),
          pause: 0,
          next_step: 0,
          back_step: 0,
          slideshow_interval: null
        });

    context.white_space = Math.floor((context.cnv_width - (context.img_width+2*context.border_size)*context.num_elem)/(context.num_elem));
    context.start_pos_x = context.white_space -10; //so we don't see shadow
    context.pos_y = 2*context.border_size;

    _this.children().hide();

    var loader_opacity = 0,
        inverter = 1,
        loader_step = 5,
        loader_interval = setInterval(function(){
            var opacity = 255 * loader_opacity / 100;
            context.canvas_context.fillStyle= "rgb(" +opacity+ "," +opacity+ "," +opacity+ ")";
            context.canvas_context.fillText(context.loader_text, (context.cnv_width / 2) - (context.canvas_context.measureText(context.loader_text).width / 2), 15);

            loader_opacity= loader_opacity - inverter * loader_step;
            if(loader_opacity <= 0) {
              inverter= -1;
            }
            else if(loader_opacity >= 100){
              inverter= 1;
            }
        }, 20);


    if (context.show_controls == true) {
      prev_img = new Image();
      prev_img.src = 'images/prev.png';
      context.util_image[0] = prev_img;
      play_img = new Image();
      play_img.src = 'images/play.png';
      context.util_image[1] = play_img;
      pause_img = new Image();
      pause_img.src = 'images/pause.png';
      context.util_image[2] = pause_img;
      next_img = new Image();
      next_img.src = 'images/next.png';
      context.util_image[3] = next_img;
    };

    _this.click(function(e) {
      offset = _this.offset();
      cord_x =  e.pageX - offset.left;
      cord_y =  e.pageY - offset.top;

      if (( (Math.floor(context.cnv_width/2)+16) > cord_x && cord_x > (Math.floor(context.cnv_width/2)-16) ) && (context.cnv_height-5 > cord_y && cord_y > context.cnv_height-41)) {
        context.pause = (context.pause == 0) ? 1 : 0;
      }
      else if (( 41 > cord_x && cord_x > 5 ) && (context.cnv_height-5 > cord_y && cord_y > context.cnv_height-41) && (context.back_step == 0)) {
        context.back_step = 1;
      }
      else if (( context.cnv_width-5 > cord_x && cord_x > context.cnv_width-41 ) && (context.cnv_height-5 > cord_y && cord_y > context.cnv_height-41)) {
        context.next_step = 1;
      }
      else if (context.lightbox == true){
        cont_inc = 0;
        space=(context.start_pos_x+context.img_width);
        while (space < (context.cnv_width + (context.white_space + context.img_width))) {
          if (cord_x < space) {
            var light_img = new Image();
            light_img.src = context.imgs[context.act_index + cont_inc].src ;
            light_img.alt = context.imgs[context.act_index + cont_inc].alt ;
            $(light_img).lightbox().trigger('click');
            e.stopPropagation();
            return
          }
          space += (context.white_space + context.img_width);
          cont_inc++;
        }
      }
      e.stopPropagation();
    });

    var load_canvas_slideshow = function() {
      _this.bind('canvas_slideshow.start', function(){
        if (context.pause == 1) {
          $.fn.draw(context);
          context.pause = 2;
        }
        if (context.pause==0) {
          context.actual_step++;
          if ((context.next_step == 1 || context.back_step == 1) && context.moving == 0) {
            context.actual_step = context.speed;
            context.next_step = 0;
            if (context.back_step == 1) context.back_step = 2;
          }

          if (context.actual_step >= (context.speed/context.step)) {
            if (context.back_step == 2) context.start_pos_x += 4;
            else context.start_pos_x -= 4;
            $.fn.draw(context);
            context.moving = 1;
          }

          if ((-context.start_pos_x >= context.img_width+15) || (context.start_pos_x >= context.img_width+2*context.white_space-15)) {
            context.actual_step = 0;
            context.start_pos_x = context.white_space -10;
            if (context.back_step == 2) context.act_index--;
            else context.act_index++;
            context.back_step = 0;
            context.moving = 0;
            if (context.act_index == context.imgs.length) context.act_index = 0;
            if (context.act_index < 0) context.act_index = context.imgs.length + context.act_index;
          }
        }
      });

      if(loader_interval) {
        context.canvas_context.clearRect(0, 0, context.cnv_width, context.cnv_height);
        clearInterval(loader_interval);
        $.fn.draw(context);
      }
      context.slideshow_interval = setInterval(function(){_this.trigger('canvas_slideshow.start');},context.step);
    };

    if(isWindowLoaded) {
      load_canvas_slideshow();
    }
    else {
      $(window).load(load_canvas_slideshow);
    }

  };

  $.fn.draw = function(context) {
    context.canvas_context.clearRect(0, 0, context.cnv_width, context.cnv_height);
    context.pos_x = context.start_pos_x;

    if (context.back_step == 2) {
      context.pos_x -= (context.white_space + context.img_width);
      for (i=context.act_index-1;i<=(context.act_index+context.num_elem+1);i++) {
        if (i >= context.imgs.length)
          current_i = i - context.imgs.length;
        else if (i < 0)
          current_i = context.imgs.length + i;
        else
          current_i = i;
        if (context.pos_x < context.cnv_width) {
          if (context.show_box == true) $.fn.draw_box(context);
          context.canvas_context.drawImage(context.imgs[current_i], context.pos_x, context.pos_y, context.img_width, context.img_height);
          if (context.show_description == true) $.fn.draw_text(context);
        }
        context.pos_x +=  (context.white_space + context.img_width);
      }
    }
    else {
      for (i=context.act_index;i<=(context.act_index+context.num_elem+1);i++) {
        current_i = (i >= context.imgs.length) ? i-context.imgs.length : i;
        if (context.pos_x < context.cnv_width) {
          if (context.show_box == true) $.fn.draw_box(context);
          context.canvas_context.drawImage(context.imgs[current_i], context.pos_x, context.pos_y, context.img_width, context.img_height);
          if (context.show_description == true) $.fn.draw_text(context);
        }
        context.pos_x += (context.white_space + context.img_width);
      }
    }
    if (context.show_controls == true) $.fn.draw_control(context);
  };


  $.fn.draw_box = function(context) {
    context.canvas_context.shadowBlur = 10;
    context.canvas_context.shadowColor = "#cccccc";
    context.canvas_context.shadowOffsetX = context.shadow_size;
    context.canvas_context.shadowOffsetY = context.shadow_size;
    context.canvas_context.fillStyle = '#000000';
    context.canvas_context.fillRect(context.pos_x-context.border_size, context.pos_y-context.border_size, (context.img_width + 2*context.border_size), (context.img_height + 2*context.border_size + ((context.show_description == true) ? 40 : 0)));
    context.canvas_context.shadowBlur = 0;
    context.canvas_context.shadowOffsetX = 0;
    context.canvas_context.shadowOffsetY = 0;
  };

  $.fn.draw_text = function(context) {
    context.canvas_context.shadowBlur = 20;
    context.canvas_context.shadowColor = "#ffffff";
    context.canvas_context.shadowOffsetX = 2;
    context.canvas_context.shadowOffsetY = 1;

    description_text = context.imgs[current_i].alt;
    context.canvas_context.font = "Times new roman";
    context.canvas_context.textAlign = "center";
    context.canvas_context.textBaseline = "middle";
    context.canvas_context.fillStyle = '#ffffff';
    metrics = context.canvas_context.measureText(description_text);

    if (metrics.width > context.img_width) {
      context.canvas_context.fillText(description_text.slice(0,Math.floor((description_text.length)/2)), context.pos_x+Math.floor(context.img_width/2), (context.pos_y+context.img_height+10), context.img_width);
      context.canvas_context.fillText(description_text.slice(Math.floor((description_text.length)/2)), context.pos_x+Math.floor(context.img_width/2), (context.pos_y+context.img_height+30), context.img_width);
    }
    else
      context.canvas_context.fillText(description_text, context.pos_x+Math.floor(context.img_width/2), (context.pos_y+context.img_height+10), context.img_width);

    context.canvas_context.shadowBlur = 0;
    context.canvas_context.shadowOffsetX = 0;
    context.canvas_context.shadowOffsetY = 0;
  };
  // control panel
  $.fn.draw_control = function(context) {
    context.canvas_context.drawImage(context.util_image[0], 5, context.cnv_height-41, 36, 36);
    if (context.pause == 1) {
      context.canvas_context.drawImage(context.util_image[1], (Math.floor(context.cnv_width/2)-16), context.cnv_height-41, 36, 36);
    }
    else {
      context.canvas_context.drawImage(context.util_image[2], (Math.floor(context.cnv_width/2)-16), context.cnv_height-41, 36, 36);
    }
    context.canvas_context.drawImage(context.util_image[3], context.cnv_width-41, context.cnv_height-41, 36, 36);
  };

  // defaults
  $.fn.gallery.defaults = {
      img_width: 200, //image width
      img_height: 150, //image height
      border_size: 5, //border size
      shadow_size: 5, //shadow size
      num_elem: 3, //number of element to show in 1 screen
      show_description: true, // show the alt tag as description?
      show_box: true, // show the outer box?
      show_controls: true, // show the controls
      lightbox: true, // use lightbox on click?
      speed: 2000, //time distance between each step
      step: 20, //step time
      loader_text: "Loading, please wait!"
  };
})(jQuery);

;(function() {
var bjcPlayer_function_getEnv = {}, bjcPlayer_function_getProEnv = {}, cc_function_split = {}, cc_function_offsetSecond = {}, bjcPlayer_function_base64 = {}, bjcPlayer_function_log = {}, bjcPlayer_function_getBrowser, bjcPlayer_function_insertStyleStr = {}, bjcPlayer_function_camelCase, bjcPlayer_config = {}, common_videoConfig = {}, cc_function_supportLocalStorage = {}, bjcPlayer_plugin_videojs_definition = {}, bjcPlayer_plugin_videojs_logo = {}, bjcPlayer_plugin_videojs_rolling = {}, bjcPlayer_plugin_jquery_marquee, bjcPlayer_plugin_videojs_loop = {}, cc_function_supportWebSocket = {}, cc_function_supportFlash = {}, cc_function_supportCanvas = {}, cc_function_supportPlaceholder = {}, cc_function_supportInput = {}, cc_util_instance = {}, bjcPlayer_plugin_videojs_eyes_protection = {}, bjcPlayer_plugin_videojs_notice = {}, cc_function_offsetMinute = {}, bjcPlayer_function_decodeUrl = {}, bjcPlayer_function_camelCaseObject = {}, cc_util_localStorage = {}, bjcPlayer_plugin_videojs_marquee = {}, cc_function_offsetHour = {}, cc_function_offsetDate = {}, cc_util_support = {}, common_support = {}, cc_util_cookie = {}, bjcPlayer_function_getUuid, common_storage = {}, bjcPlayer_function_bjcPlayerStatisticReport = {}, bjcPlayer_plugin_videojs_subtitles = {}, bjcPlayer_plugin_videojs_ads, bjcPlayer_index = {};
bjcPlayer_function_getEnv = function getEnv() {
  var apiUrlPrefix = 'www';
  if (apiUrlPrefix.indexOf('test-') != -1) {
    return 'test';
  }
  if (apiUrlPrefix.indexOf('beta-') != -1) {
    return 'beta';
  }
  return 'prod';
};
bjcPlayer_function_getProEnv = function getProEnv(privateDomainPrefix, doaminSuffix) {
  var apiUrlPrefix = 'www';
  if (apiUrlPrefix.indexOf('test-') != -1) {
    return '//' + privateDomainPrefix + '.test-at.' + doaminSuffix;
  }
  if (apiUrlPrefix.indexOf('beta-') != -1) {
    return '//' + privateDomainPrefix + '.beta-at.' + doaminSuffix;
  }
  return '//' + privateDomainPrefix + '.at.' + doaminSuffix;
};
cc_function_split = function (str, sep) {
  var result = [];
  if ($.type(str) === 'number') {
    str = '' + str;
  }
  if (str && $.type(str) === 'string') {
    $.each(str.split(sep), function (index, part) {
      part = $.trim(part);
      if (part) {
        result.push(part);
      }
    });
  }
  return result;
};
cc_function_offsetSecond = function (date, offset) {
  if ($.type(date) === 'date') {
    date = date.getTime();
  }
  return new Date(date + offset * 1000);
};
bjcPlayer_function_base64 = function (exports) {
  var _PADCHAR = '=', _ALPHA = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/', _VERSION = '1.0';
  function _getbyte64(s, i) {
    var idx = _ALPHA.indexOf(s.charAt(i));
    if (idx === -1) {
      throw 'Cannot decode base64';
    }
    return idx;
  }
  function _decode(s) {
    var pads = 0, i, b10, imax = s.length, x = [];
    s = String(s);
    if (imax === 0) {
      return s;
    }
    if (imax % 4 !== 0) {
      throw 'Cannot decode base64';
    }
    if (s.charAt(imax - 1) === _PADCHAR) {
      pads = 1;
      if (s.charAt(imax - 2) === _PADCHAR) {
        pads = 2;
      }
      imax -= 4;
    }
    for (i = 0; i < imax; i += 4) {
      b10 = _getbyte64(s, i) << 18 | _getbyte64(s, i + 1) << 12 | _getbyte64(s, i + 2) << 6 | _getbyte64(s, i + 3);
      x.push(String.fromCharCode(b10 >> 16, b10 >> 8 & 255, b10 & 255));
    }
    switch (pads) {
    case 1:
      b10 = _getbyte64(s, i) << 18 | _getbyte64(s, i + 1) << 12 | _getbyte64(s, i + 2) << 6;
      x.push(String.fromCharCode(b10 >> 16, b10 >> 8 & 255));
      break;
    case 2:
      b10 = _getbyte64(s, i) << 18 | _getbyte64(s, i + 1) << 12;
      x.push(String.fromCharCode(b10 >> 16));
      break;
    }
    return x.join('');
  }
  function _getbyte(s, i) {
    var x = s.charCodeAt(i);
    if (x > 255) {
      throw 'INVALID_CHARACTER_ERR: DOM Exception 5';
    }
    return x;
  }
  function _encode(s) {
    if (arguments.length !== 1) {
      throw 'SyntaxError: exactly one argument required';
    }
    s = String(s);
    var i, b10, x = [], imax = s.length - s.length % 3;
    if (s.length === 0) {
      return s;
    }
    for (i = 0; i < imax; i += 3) {
      b10 = _getbyte(s, i) << 16 | _getbyte(s, i + 1) << 8 | _getbyte(s, i + 2);
      x.push(_ALPHA.charAt(b10 >> 18));
      x.push(_ALPHA.charAt(b10 >> 12 & 63));
      x.push(_ALPHA.charAt(b10 >> 6 & 63));
      x.push(_ALPHA.charAt(b10 & 63));
    }
    switch (s.length - imax) {
    case 1:
      b10 = _getbyte(s, i) << 16;
      x.push(_ALPHA.charAt(b10 >> 18) + _ALPHA.charAt(b10 >> 12 & 63) + _PADCHAR + _PADCHAR);
      break;
    case 2:
      b10 = _getbyte(s, i) << 16 | _getbyte(s, i + 1) << 8;
      x.push(_ALPHA.charAt(b10 >> 18) + _ALPHA.charAt(b10 >> 12 & 63) + _ALPHA.charAt(b10 >> 6 & 63) + _PADCHAR);
      break;
    }
    return x.join('');
  }
  exports.encode = _encode;
  exports.decode = _decode;
  return exports;
}(bjcPlayer_function_base64);
bjcPlayer_function_log = function (exports) {
  var imageList = [];
  var send = function (url, data) {
    var params = data || {};
    var target = (url || liveUrl) + '?' + $.param(params);
    var img = new Image();
    var index = imageList.push(img) - 1;
    var complete = function () {
      img = img.onload = img.onerror = null;
      delete imageList[index];
    };
    img.onload = complete;
    img.onerror = function () {
      complete();
      setTimeout(function () {
        send(url, data);
      }, 1000);
    };
    img.src = target;
  };
  return send;
}(bjcPlayer_function_log);
bjcPlayer_function_getBrowser = function getBrowser() {
  var userAgent = navigator.userAgent.toLowerCase();
  var isOpera = userAgent.indexOf('opera') > -1;
  if (isOpera) {
    return 1;
  }
  if (userAgent.indexOf('firefox') > -1) {
    return 5;
  }
  if (userAgent.indexOf('chrome') > -1) {
    return 3;
  }
  if (userAgent.indexOf('safari') > -1) {
    return 6;
  }
  if (userAgent.indexOf('compatible') > -1 && userAgent.indexOf('msie') > -1 && !isOpera) {
    return 2;
  }
  if (/rv:(\S)+\) like gecko/.test(userAgent)) {
    return 2;
  } else {
    return 7;
  }
};
bjcPlayer_function_insertStyleStr = function (styleStr) {
  if (document.all) {
    window.style = styleStr;
    document.createStyleSheet('javascript:style');
  } else {
    var style = document.createElement('style');
    style.type = 'text/css';
    style.innerHTML = styleStr;
    document.getElementsByTagName('HEAD').item(0).appendChild(style);
  }
};
bjcPlayer_function_camelCase = function (str) {
  if ($.type(str) !== 'string') {
    str = '' + str;
  }
  if (str.indexOf('_') >= 0) {
    str = str.replace(/_/g, '-');
    return $.camelCase(str);
  }
  return str;
};
bjcPlayer_config = {
  videoTypes: {
    ev1: { type: 'video/x-flv' },
    flv: { type: 'video/x-flv' },
    mp4: { type: 'video/mp4' }
  }
};
common_videoConfig = function (exports) {
  exports.VIDEO_OPTIONS_PLAY = 'play';
  exports.VIDEO_OPTIONS_PAUSE = 'pause';
  exports.VIDEO_OPTIONS_VOLUME = 'changeVolume';
  exports.VIDEO_DEFINITION_STANDARD = 'low';
  exports.VIDEO_DEFINITION_HIGH = 'high';
  exports.VIDEO_DEFINITION_SUPER = 'super';
  exports.VIDEO_DEFINITION_SUPERHD = 'superHD';
  exports.VIDEO_DEFINITION_720 = '720p';
  exports.VIDEO_DEFINITION_1080 = '1080p';
  exports.VIDEO_RATE_0_5 = 0.5;
  exports.VIDEO_RATE_1 = 1;
  exports.VIDEO_RATE_1_5 = 1.5;
  exports.VIDEO_RATE_2 = 2;
  exports.VIDEO_RATE_3 = 3;
  exports.VIDEO_RATE_4 = 4;
  return exports;
}(common_videoConfig);
cc_function_supportLocalStorage = function () {
  return typeof window.localStorage !== 'undefined';
};
bjcPlayer_plugin_videojs_definition = function (exports) {
  videojs.registerPlugin('definition', function (options) {
    var el = this.el();
    var me = this;
    var defaultDefinition = options.defaultDefinition || 'low';
    var definitionData = options.definitionData || [];
    var defaultDefinitionObj = {};
    var menuHtml = '<ul class="vjs-menu-content" role="menu">';
    definitionData.forEach(function (item) {
      var isCurrent = item.type == defaultDefinition;
      if (isCurrent) {
        defaultDefinitionObj = item;
      }
      menuHtml += '<li class="vjs-menu-item  vjs-hd-menu-item ' + (isCurrent ? 'vjs-selected' : '') + '" type="' + item.type + '" role="menuitemcheckbox">' + item.name + '</li>';
    });
    menuHtml += '</ul>';
    var videoPanelMenu = $('.vjs-fullscreen-control', el);
    if (!$(el).find('.vjs-definition-control')[0]) {
      videoPanelMenu.after('' + '<div class="vjs-definition-control vjs-menu-button vjs-menu-button-popup vjs-control vjs-button"  aria-live="polite" aria-expanded="false" aria-haspopup="true">' + '     <div class="vjs-menu" role="presentation">' + (definitionData.length > 1 ? menuHtml : '') + '     </div>' + '     <button class="vjs-subs-caps-button vjs-control vjs-button btn-switch-definition" type="button" title="清晰度切换">' + '         <span>' + defaultDefinitionObj.name + '</span>' + '     </button>' + '</div>');
    }
    $(el).on('click', '.vjs-hd-menu-item', function () {
      $('.vjs-menu-item', el).removeClass('vjs-selected');
      $(this).addClass('vjs-selected');
      var type = $(this).attr('type');
      var text = $(this).text();
      me.trigger('switchdefinition', type);
      $('.btn-switch-definition span', el).text(text);
    });
    return me;
  });
  return exports;
}(bjcPlayer_plugin_videojs_definition);
bjcPlayer_plugin_videojs_logo = function (exports) {
  videojs.registerPlugin('setLogo', function (options) {
    var el = this.el();
    var url = options.url;
    var homeUrl = options.homeUrl;
    var videoPanelMenu = $('.vjs-fullscreen-control', el);
    var tpl = homeUrl ? '<a target="_blank" href="' + homeUrl + '" style="background:url(' + url + '); ' : '<a style="background:url(' + url + '); ';
    tpl += 'background-size: contain;' + 'background-repeat: no-repeat; z-index: 10;' + 'background-position: center center; width:45px; margin: 0 5px;">&nbsp;</a>';
    videoPanelMenu.after(tpl);
  });
  return exports;
}(bjcPlayer_plugin_videojs_logo);
bjcPlayer_plugin_videojs_rolling = function (exports) {
  videojs.registerPlugin('rolling', function (options) {
    var source = options.source;
    var player = this;
    player.src({
      type: options.type,
      src: options.url
    });
    player.controlBar.hide();
    player.isRolling = true;
    player.loop(false);
    player.one('ended', function () {
      player.controlBar.show();
      options.callback && options.callback();
    });
    player.play();
  });
  return exports;
}(bjcPlayer_plugin_videojs_rolling);
bjcPlayer_plugin_jquery_marquee = undefined;
(function ($) {
  $.fn.marquee = function (options) {
    return this.each(function () {
      var o = $.extend({}, $.fn.marquee.defaults, options), $this = $(this), $marqueeWrapper, containerWidth, animationCss, verticalDir, elWidth, loopCount = 3, playState = 'animation-play-state', css3AnimationIsSupported = false, _prefixedEvent = function (element, type, callback) {
          var pfx = [
            'webkit',
            'moz',
            'MS',
            'o',
            ''
          ];
          for (var p = 0; p < pfx.length; p++) {
            if (!pfx[p])
              type = type.toLowerCase();
            element.addEventListener(pfx[p] + type, callback, false);
          }
        }, _objToString = function (obj) {
          var tabjson = [];
          for (var p in obj) {
            if (obj.hasOwnProperty(p)) {
              tabjson.push(p + ':' + obj[p]);
            }
          }
          tabjson.push();
          return '{' + tabjson.join(',') + '}';
        }, _startAnimationWithDelay = function () {
          $this.timer = setTimeout(animate, o.delayBeforeStart);
        }, methods = {
          pause: function () {
            if (css3AnimationIsSupported && o.allowCss3Support) {
              $marqueeWrapper.css(playState, 'paused');
            } else {
              if ($.fn.pause) {
                $marqueeWrapper.pause();
              }
            }
            $this.data('runningStatus', 'paused');
            $this.trigger('paused');
          },
          resume: function () {
            if (css3AnimationIsSupported && o.allowCss3Support) {
              $marqueeWrapper.css(playState, 'running');
            } else {
              if ($.fn.resume) {
                $marqueeWrapper.resume();
              }
            }
            $this.data('runningStatus', 'resumed');
            $this.trigger('resumed');
          },
          toggle: function () {
            methods[$this.data('runningStatus') == 'resumed' ? 'pause' : 'resume']();
          },
          destroy: function () {
            clearTimeout($this.timer);
            $this.find('*').addBack().unbind();
            $this.html($this.find('.js-marquee:first').html());
          }
        };
      if (typeof options === 'string') {
        if ($.isFunction(methods[options])) {
          if (!$marqueeWrapper) {
            $marqueeWrapper = $this.find('.js-marquee-wrapper');
          }
          if ($this.data('css3AnimationIsSupported') === true) {
            css3AnimationIsSupported = true;
          }
          methods[options]();
        }
        return;
      }
      var dataAttributes = {}, attr;
      $.each(o, function (key, value) {
        attr = $this.attr('data-' + key);
        if (typeof attr !== 'undefined') {
          switch (attr) {
          case 'true':
            attr = true;
            break;
          case 'false':
            attr = false;
            break;
          }
          o[key] = attr;
        }
      });
      if (o.speed) {
        o.duration = parseInt($this.width(), 10) / o.speed * 1000;
      }
      verticalDir = o.direction == 'up' || o.direction == 'down';
      o.gap = o.duplicated ? parseInt(o.gap) : 0;
      $this.wrapInner('<div class="js-marquee"></div>');
      var $el = $this.find('.js-marquee').css({
        'margin-right': o.gap,
        'float': 'left'
      });
      if (o.duplicated) {
        $el.clone(true).appendTo($this);
      }
      $this.wrapInner('<div style="width:100000px" class="js-marquee-wrapper"></div>');
      $marqueeWrapper = $this.find('.js-marquee-wrapper');
      if (verticalDir) {
        var containerHeight = $this.height();
        $marqueeWrapper.removeAttr('style');
        $this.height(containerHeight);
        $this.find('.js-marquee').css({
          'float': 'none',
          'margin-bottom': o.gap,
          'margin-right': 0
        });
        if (o.duplicated)
          $this.find('.js-marquee:last').css({ 'margin-bottom': 0 });
        var elHeight = $this.find('.js-marquee:first').height() + o.gap;
        if (o.startVisible && !o.duplicated) {
          o._completeDuration = (parseInt(elHeight, 10) + parseInt(containerHeight, 10)) / parseInt(containerHeight, 10) * o.duration;
          o.duration = parseInt(elHeight, 10) / parseInt(containerHeight, 10) * o.duration;
        } else {
          o.duration = (parseInt(elHeight, 10) + parseInt(containerHeight, 10)) / parseInt(containerHeight, 10) * o.duration;
        }
      } else {
        elWidth = $this.find('.js-marquee:first').width() + o.gap;
        containerWidth = $this.width();
        if (o.startVisible && !o.duplicated) {
          o._completeDuration = (parseInt(elWidth, 10) + parseInt(containerWidth, 10)) / parseInt(containerWidth, 10) * o.duration;
          o.duration = parseInt(elWidth, 10) / parseInt(containerWidth, 10) * o.duration;
        } else {
          o.duration = (parseInt(elWidth, 10) + parseInt(containerWidth, 10)) / parseInt(containerWidth, 10) * o.duration;
        }
      }
      if (o.duplicated) {
        o.duration = o.duration / 2;
      }
      if (o.allowCss3Support) {
        var elm = document.body || document.createElement('div'), animationName = 'marqueeAnimation-' + Math.floor(Math.random() * 10000000), domPrefixes = 'Webkit Moz O ms Khtml'.split(' '), animationString = 'animation', animationCss3Str = '', keyframeString = '';
        if (elm.style.animation) {
          keyframeString = '@keyframes ' + animationName + ' ';
          css3AnimationIsSupported = true;
        }
        if (css3AnimationIsSupported === false) {
          for (var i = 0; i < domPrefixes.length; i++) {
            if (elm.style[domPrefixes[i] + 'AnimationName'] !== undefined) {
              var prefix = '-' + domPrefixes[i].toLowerCase() + '-';
              animationString = prefix + animationString;
              playState = prefix + playState;
              keyframeString = '@' + prefix + 'keyframes ' + animationName + ' ';
              css3AnimationIsSupported = true;
              break;
            }
          }
        }
        if (css3AnimationIsSupported) {
          animationCss3Str = animationName + ' ' + o.duration / 1000 + 's ' + o.delayBeforeStart / 1000 + 's infinite ' + o.css3easing;
          $this.data('css3AnimationIsSupported', true);
        }
      }
      var _rePositionVertically = function () {
          $marqueeWrapper.css('transform', 'translateY(' + (o.direction == 'up' ? containerHeight + 'px' : '-' + elHeight + 'px') + ')');
        }, _rePositionHorizontally = function () {
          $marqueeWrapper.css('transform', 'translateX(' + (o.direction == 'left' ? containerWidth + 'px' : '-' + elWidth + 'px') + ')');
        };
      if (o.duplicated) {
        if (verticalDir) {
          if (o.startVisible) {
            $marqueeWrapper.css('transform', 'translateY(0)');
          } else {
            $marqueeWrapper.css('transform', 'translateY(' + (o.direction == 'up' ? containerHeight + 'px' : '-' + (elHeight * 2 - o.gap) + 'px') + ')');
          }
        } else {
          if (o.startVisible) {
            $marqueeWrapper.css('transform', 'translateX(0)');
          } else {
            $marqueeWrapper.css('transform', 'translateX(' + (o.direction == 'left' ? containerWidth + 'px' : '-' + (elWidth * 2 - o.gap) + 'px') + ')');
          }
        }
        if (!o.startVisible) {
          loopCount = 1;
        }
      } else if (o.startVisible) {
        loopCount = 2;
      } else {
        if (verticalDir) {
          _rePositionVertically();
        } else {
          _rePositionHorizontally();
        }
      }
      var animate = function () {
        if (o.duplicated) {
          if (loopCount === 1) {
            o._originalDuration = o.duration;
            if (verticalDir) {
              o.duration = o.direction == 'up' ? o.duration + containerHeight / (elHeight / o.duration) : o.duration * 2;
            } else {
              o.duration = o.direction == 'left' ? o.duration + containerWidth / (elWidth / o.duration) : o.duration * 2;
            }
            if (animationCss3Str) {
              animationCss3Str = animationName + ' ' + o.duration / 1000 + 's ' + o.delayBeforeStart / 1000 + 's ' + o.css3easing;
            }
            loopCount++;
          } else if (loopCount === 2) {
            o.duration = o._originalDuration;
            if (animationCss3Str) {
              animationName = animationName + '0';
              keyframeString = $.trim(keyframeString) + '0 ';
              animationCss3Str = animationName + ' ' + o.duration / 1000 + 's 0s infinite ' + o.css3easing;
            }
            loopCount++;
          }
        }
        if (verticalDir) {
          if (o.duplicated) {
            if (loopCount > 2) {
              $marqueeWrapper.css('transform', 'translateY(' + (o.direction == 'up' ? 0 : '-' + elHeight + 'px') + ')');
            }
            animationCss = { 'transform': 'translateY(' + (o.direction == 'up' ? '-' + elHeight + 'px' : 0) + ')' };
          } else if (o.startVisible) {
            if (loopCount === 2) {
              if (animationCss3Str) {
                animationCss3Str = animationName + ' ' + o.duration / 1000 + 's ' + o.delayBeforeStart / 1000 + 's ' + o.css3easing;
              }
              animationCss = { 'transform': 'translateY(' + (o.direction == 'up' ? '-' + elHeight + 'px' : containerHeight + 'px') + ')' };
              loopCount++;
            } else if (loopCount === 3) {
              o.duration = o._completeDuration;
              if (animationCss3Str) {
                animationName = animationName + '0';
                keyframeString = $.trim(keyframeString) + '0 ';
                animationCss3Str = animationName + ' ' + o.duration / 1000 + 's 0s infinite ' + o.css3easing;
              }
              _rePositionVertically();
            }
          } else {
            _rePositionVertically();
            animationCss = { 'transform': 'translateY(' + (o.direction == 'up' ? '-' + $marqueeWrapper.height() + 'px' : containerHeight + 'px') + ')' };
          }
        } else {
          if (o.duplicated) {
            if (loopCount > 2) {
              $marqueeWrapper.css('transform', 'translateX(' + (o.direction == 'left' ? 0 : '-' + elWidth + 'px') + ')');
            }
            animationCss = { 'transform': 'translateX(' + (o.direction == 'left' ? '-' + elWidth + 'px' : 0) + ')' };
          } else if (o.startVisible) {
            if (loopCount === 2) {
              if (animationCss3Str) {
                animationCss3Str = animationName + ' ' + o.duration / 1000 + 's ' + o.delayBeforeStart / 1000 + 's ' + o.css3easing;
              }
              animationCss = { 'transform': 'translateX(' + (o.direction == 'left' ? '-' + elWidth + 'px' : containerWidth + 'px') + ')' };
              loopCount++;
            } else if (loopCount === 3) {
              o.duration = o._completeDuration;
              if (animationCss3Str) {
                animationName = animationName + '0';
                keyframeString = $.trim(keyframeString) + '0 ';
                animationCss3Str = animationName + ' ' + o.duration / 1000 + 's 0s infinite ' + o.css3easing;
              }
              _rePositionHorizontally();
            }
          } else {
            _rePositionHorizontally();
            animationCss = { 'transform': 'translateX(' + (o.direction == 'left' ? '-' + elWidth + 'px' : containerWidth + 'px') + ')' };
          }
        }
        $this.trigger('beforeStarting');
        if (css3AnimationIsSupported) {
          $marqueeWrapper.css(animationString, animationCss3Str);
          var keyframeCss = keyframeString + ' { 100%  ' + _objToString(animationCss) + '}', $styles = $marqueeWrapper.find('style');
          if ($styles.length !== 0) {
            $styles.filter(':last').html(keyframeCss);
          } else {
            $('head').append('<style>' + keyframeCss + '</style>');
          }
          _prefixedEvent($marqueeWrapper[0], 'AnimationIteration', function () {
            $this.trigger('finished');
          });
          _prefixedEvent($marqueeWrapper[0], 'AnimationEnd', function () {
            animate();
            $this.trigger('finished');
          });
        } else {
          $marqueeWrapper.animate(animationCss, o.duration, o.easing, function () {
            $this.trigger('finished');
            if (o.pauseOnCycle) {
              _startAnimationWithDelay();
            } else {
              animate();
            }
          });
        }
        $this.data('runningStatus', 'resumed');
      };
      $this.bind('pause', methods.pause);
      $this.bind('resume', methods.resume);
      if (o.pauseOnHover) {
        $this.bind('mouseenter', methods.pause);
        $this.bind('mouseleave', methods.resume);
      }
      if (css3AnimationIsSupported && o.allowCss3Support) {
        animate();
      } else {
        _startAnimationWithDelay();
      }
    });
  };
  $.fn.marquee.defaults = {
    allowCss3Support: true,
    css3easing: 'linear',
    easing: 'linear',
    delayBeforeStart: 1000,
    direction: 'left',
    duplicated: false,
    duration: 5000,
    gap: 20,
    pauseOnCycle: false,
    pauseOnHover: false,
    startVisible: false
  };
}(jQuery));
bjcPlayer_plugin_videojs_loop = function (exports) {
  videojs.registerPlugin('setLoop', function (options) {
    var el = this.el();
    var me = this;
    var defaultValue = typeof options.defaultLoopValue == 'undefined' ? false : options.defaultLoopValue;
    var defaultDefinitionObj = {};
    me.loop(defaultValue);
    var iconClass = 'icon-loop';
    if (!defaultValue) {
      iconClass = 'icon-loopclose';
    }
    var menuHtml = '' + '<button class="vjs-loop-control vjs-control vjs-button" type="button" aria-live="polite"  aria-disabled="false">' + '<span aria-hidden="true" class="iconfont ' + iconClass + ' control-icon">' + '</span>' + '</button>';
    var videoPanelMenu = $('.vjs-fullscreen-control', el);
    if (!$(el).find('.vjs-loop-control')[0]) {
      videoPanelMenu.before(menuHtml);
    }
    $(el).on('click', '.vjs-loop-control', function () {
      var dom = $('.control-icon', this);
      var text = $('.vjs-control-text', this);
      var loop;
      if (dom.hasClass('icon-loop')) {
        loop = false;
        dom.removeClass('icon-loop');
        dom.addClass('icon-loopclose');
        dom.attr('title', '关闭循环');
      } else {
        loop = true;
        dom.addClass('icon-loop');
        dom.removeClass('icon-loopclose');
        dom.attr('title', '循环播放');
      }
      me.trigger('loopvaluechange', loop);
    });
    return me;
  });
  return exports;
}(bjcPlayer_plugin_videojs_loop);
cc_function_supportWebSocket = function () {
  return typeof window.WebSocket !== 'undefined';
};
cc_function_supportFlash = function () {
  var swf;
  var plugins = navigator.plugins;
  if (plugins && plugins.length > 0) {
    swf = plugins['Shockwave Flash'];
  } else if (document.all) {
    try {
      swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
    } catch (e) {
    }
  }
  return !!swf;
};
cc_function_supportCanvas = function () {
  var canvas = document.createElement('canvas');
  return canvas && canvas.getContext ? true : false;
};
cc_function_supportPlaceholder = function () {
  var element = $('<input type="text" />')[0];
  return 'placeholder' in element;
};
cc_function_supportInput = function () {
  var element = $('<input type="text" />')[0];
  return 'oninput' in element;
};
cc_util_instance = function (exports) {
  exports.window = $(window);
  exports.document = $(document);
  exports.html = $(document.documentElement);
  exports.body = $(document.body);
  return exports;
}(cc_util_instance);
bjcPlayer_plugin_videojs_eyes_protection = function (exports) {
  videojs.registerPlugin('setEyesProtection', function (options) {
    var el = this.el();
    if (!$(el).find('.bjy-eyes-icon')[0]) {
      $(el).append('<div class="bjy-eyes-icon"><div class="bjy-eyes-tooltip">护眼模式</div></div></div>');
      $(el).on('click', '.bjy-eyes-icon', function () {
        $('.bjy-eyes-icon').toggleClass('bjy-eyes-icon-active');
        $(el).toggleClass('bjy-video-eyes-protection');
      });
    }
  });
  return exports;
}(bjcPlayer_plugin_videojs_eyes_protection);
bjcPlayer_plugin_videojs_notice = function (exports) {
  function colorRgbFormula(originColor, opacity) {
    var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
    if (!originColor) {
      return '';
    }
    var color = originColor.toLowerCase();
    if (reg.test(color)) {
      if (color.length === 4) {
        var colorNew = '#';
        for (var i = 1; i < 4; i += 1) {
          colorNew += color.slice(i, i + 1).concat(color.slice(i, i + 1));
        }
        color = colorNew;
      }
      var colorChange = [];
      for (var i = 1; i < 7; i += 2) {
        colorChange.push(parseInt('0x' + color.slice(i, i + 2)));
      }
      colorChange.push(opacity / 100);
      return 'rgba(' + colorChange.join(',') + ')';
    } else {
      return color;
    }
  }
  videojs.registerPlugin('setNotice', function (options) {
    var el = this.el();
    var maxWidth = el.offsetWidth;
    var maxHeight = el.offsetHeight;
    var notice = options.notice;
    var setting = options.settings;
    var bgColor = colorRgbFormula(setting.bg_color, setting.bg_transparency);
    var fontColor = colorRgbFormula(setting.font_color, setting.font_transparency);
    var borderColor = colorRgbFormula(setting.border_color, setting.border_transparency);
    var fontSize = setting.font_size + 'px';
    var interval = setting.roll_interval;
    var animationDuration = maxWidth / 320 * 5;
    var contentIndex = 0;
    var dragging = false;
    var originCoordinate = {};
    var originTouchCoordinate = {};
    var yScale = setting.y_scale;
    var topDistance = yScale + '%';
    var cssText = 'position:absolute;left:0;right:0;top: ' + topDistance + ';z-index: 11;' + 'background: ' + bgColor + ';overflow:hidden; text-align:left;padding: 9px 0;';
    var childCssText = 'font-size: ' + fontSize + ';color: ' + fontColor + '; -webkit-text-stroke:1px ' + borderColor + ';' + 'white-space:nowrap;' + 'transform: translateX(' + maxWidth + 'px);display:inline-block;transition: transform ' + animationDuration + 's linear;' + 'text-decoration: none;';
    var html = '<div class="bjy-notice-text-container" style="' + cssText + '">' + '<a class="bjy-notice-text" style="' + childCssText + '">国</a>' + '</div>';
    el.insertAdjacentHTML('beforeEnd', html);
    var container = document.querySelector('.bjy-notice-text-container');
    var text = document.querySelector('.bjy-notice-text');
    if (text && container) {
      move();
      var maxHeightPercent = Math.floor(100 - (container.offsetHeight + 40) * 100 / maxHeight);
      yScale = yScale > maxHeightPercent ? maxHeightPercent : yScale;
      container.style.top = yScale + '%';
      maxWidth = maxWidth - container.offsetWidth;
      maxHeight = maxHeight - container.offsetHeight - 40;
      initEvent();
    }
    function move() {
      var fatherWidth = el.offsetWidth;
      container.style.display = '';
      var noticeData = notice[contentIndex];
      var content = noticeData.content;
      var href = noticeData.link;
      text.innerText = content;
      if (href) {
        text.href = href;
        text.target = '_blank';
      } else {
        text.href = 'javascript:void(0);';
        text.target = '';
      }
      var textWidth = text.offsetWidth;
      var time = (1 + textWidth / fatherWidth) * animationDuration;
      text.style.transition = 'transform ' + time + 's linear';
      text.style.transform = 'translateX(-100%)';
      setTimeout(function () {
        container.style.display = 'none';
      }, time * 1000);
      setTimeout(function () {
        text.style.transition = 'transform 0s';
        text.style.transform = 'translateX(' + fatherWidth + 'px)';
        contentIndex++;
        contentIndex = contentIndex >= notice.length ? 0 : contentIndex;
        move();
      }, (time + interval) * 1000);
    }
    function initEvent() {
      container.addEventListener('mousedown', pointerDownHandler);
      el.addEventListener('mouseup', pointerUpHandler);
      el.addEventListener('mousemove', pointerMoveHandler);
    }
    function pointerDownHandler(e) {
      dragging = true;
      var target = e.target;
      while (target.parentNode !== el) {
        target = target.parentNode;
      }
      originCoordinate = {
        x: target.offsetLeft,
        y: target.offsetTop
      };
      originTouchCoordinate = {
        x: e.pageX,
        y: e.pageY
      };
    }
    function pointerUpHandler() {
      dragging = false;
    }
    function pointerMoveHandler(e) {
      if (dragging) {
        var y = originCoordinate.y + e.pageY - originTouchCoordinate.y;
        y = y <= 0 ? 0 : y;
        y = y >= maxHeight ? maxHeight : y;
        container.style.top = y + 'px';
      }
      e.preventDefault();
    }
  });
  return exports;
}(bjcPlayer_plugin_videojs_notice);
cc_function_offsetMinute = function (exports) {
  var offsetSecond = cc_function_offsetSecond;
  return function (date, offset) {
    return offsetSecond(date, offset * 60);
  };
}(cc_function_offsetMinute);
bjcPlayer_function_decodeUrl = function (exports) {
  var base64 = bjcPlayer_function_base64;
  return function decodeUrl(url) {
    var prefix = 'bjcloudvod://';
    if ('' == url || url.indexOf(prefix) !== 0) {
      return;
    }
    url = url.slice(prefix.length, url.length).replace(/-/g, '+').replace(/_/g, '/');
    var pad = url.length % 4;
    if (pad == 2) {
      url += '==';
    } else if (pad == 3) {
      url += '=';
    }
    url = base64.decode(url);
    var factor = url.charCodeAt(0);
    var c = factor % 8;
    url = url.slice(1, url.length);
    var result = [];
    for (var i = 0, char; char = url[i]; i++) {
      var step = i % 4 * c + i % 3 + 1;
      result.push(String.fromCharCode(char.charCodeAt(0) - step));
    }
    return result.join('');
  };
}(bjcPlayer_function_decodeUrl);
bjcPlayer_function_camelCaseObject = function (exports) {
  var camelCase = bjcPlayer_function_camelCase;
  function camelCaseObject(obj) {
    var result = $.isArray(obj) ? [] : {};
    $.each(obj, function (key, value) {
      if ($.isPlainObject(value) || $.isArray(value)) {
        value = camelCaseObject(value);
      }
      result[camelCase(key)] = value;
    });
    return result;
  }
  return camelCaseObject;
}(bjcPlayer_function_camelCaseObject);
cc_util_localStorage = function (exports) {
  var support = cc_function_supportLocalStorage();
  function set(key, value) {
    if ($.isPlainObject(key)) {
      $.each(key, set);
    } else {
      try {
        localStorage[key] = value;
      } catch (e) {
      }
    }
  }
  function get(key) {
    var result;
    try {
      result = localStorage[key];
    } catch (e) {
    }
    return result;
  }
  function remove(key) {
    try {
      localStorage.removeItem(key);
    } catch (e) {
    }
  }
  exports.support = support;
  if (support) {
    exports.set = set;
    exports.get = get;
    exports.remove = remove;
  } else {
    exports.set = exports.get = exports.remove = $.noop;
  }
  return exports;
}(cc_util_localStorage);
bjcPlayer_plugin_videojs_marquee = function (exports) {
  bjcPlayer_plugin_jquery_marquee;
  var defaults = {
    content: '跑马灯示例文本\uFF0C2分钟跑一次',
    position: 'top',
    direction: 'left',
    pauseOnHover: true,
    duration: 10000,
    backgroundcolor: 'tranpsarent',
    color: '#fff'
  };
  var dom = videojs.dom || videojs;
  var onPlayerReady = function (player, options) {
    player.addClass('vjs-marquee-overlay');
    var el = dom.createEl('div', { className: 'vjs-emre-marquee' });
    el.innerHTML = options.content;
    player.el().appendChild(el);
    if (options.position == 'bottom') {
      el.style.bottom = 0;
    } else {
      el.style.top = 0;
    }
    el.style.backgroundColor = options.backgroundcolor;
    el.style.color = options.color;
    player.on('timeupdate', function () {
      if (player.userActive()) {
        if (options.position == 'bottom') {
          el.style.bottom = '39px';
        }
      } else {
        if (options.position == 'bottom') {
          el.style.bottom = 0;
        }
      }
    });
    $(function () {
      $('.vjs-emre-marquee').marquee({
        duration: options.duration,
        gap: 50,
        pauseOnHover: options.pauseOnHover,
        delayBeforeStart: 0,
        direction: options.direction,
        duplicated: false
      }).bind('finished', function (event) {
        var target = $(event.target);
        target.trigger('paused');
        target.fadeToggle('fast');
        setTimeout(function () {
          target.trigger('resume');
          var isPaused = player.paused();
          var isEnded = player.ended();
          if (!player.isRolling && !isPaused && !isEnded) {
            target.fadeToggle('fast');
          }
        }, 1000 * 120);
      });
    });
  };
  var marqueeOverlay = function (options) {
    this.ready(function () {
      onPlayerReady(this, videojs.mergeOptions(defaults, options));
    });
    marqueeOverlay.changeString = function (scrollingText) {
      $('.js-marquee').text(scrollingText);
    };
  };
  videojs.registerPlugin('marquee', marqueeOverlay);
  return exports;
}(bjcPlayer_plugin_videojs_marquee);
cc_function_offsetHour = function (exports) {
  var offsetMinute = cc_function_offsetMinute;
  return function (date, offset) {
    return offsetMinute(date, offset * 60);
  };
}(cc_function_offsetHour);
cc_function_offsetDate = function (exports) {
  var offsetHour = cc_function_offsetHour;
  return function (date, offset) {
    return offsetHour(date, offset * 24);
  };
}(cc_function_offsetDate);
cc_util_support = function (exports) {
  var customElement = document.createElement('musicode');
  var customElementStyle = customElement.style;
  var cache = {};
  var prefixs = [
    'Webkit',
    'Moz',
    'O',
    'ms'
  ];
  function testCSS(property) {
    var upperCase = property.charAt(0).toUpperCase() + property.slice(1);
    var list = (property + ' ' + prefixs.join(upperCase + ' ') + upperCase).split(' ');
    var result = false;
    $.each(list, function (index, name) {
      if (name in customElementStyle) {
        result = true;
        return false;
      }
    });
    return result;
  }
  exports.animation = function () {
    return testCSS('animationName');
  };
  exports.boxShadow = function () {
    return testCSS('boxShadow');
  };
  exports.flexbox = function () {
    return testCSS('flexWrap');
  };
  exports.transform = function () {
    return testCSS('transform');
  };
  exports.webp = function () {
    if (cache.webp) {
      return cache.webp;
    }
    var promise = $.Deferred();
    var image = new Image();
    image.onload = image.onerror = function () {
      if (image.height === 2) {
        promise.resolve();
      } else {
        promise.reject();
      }
    };
    image.src = 'data:image/webp;base64,UklGRjoAAABXRUJQVlA4IC4AAACyAgCdASoCAAIALmk0mk0iIiIiIgBoSygABc6WWgAA/veff/0PP8bA//LwYAAA';
    cache.webp = promise;
    return promise;
  };
  exports.webSocket = cc_function_supportWebSocket;
  exports.localStorage = cc_function_supportLocalStorage;
  exports.flash = cc_function_supportFlash;
  exports.canvas = cc_function_supportCanvas;
  exports.placeholder = cc_function_supportPlaceholder;
  exports.input = cc_function_supportInput;
  return exports;
}(cc_util_support);
common_support = function (exports) {
  var support = cc_util_support;
  var body = cc_util_instance.body;
  exports.init = function () {
    var classList = [];
    if (support.flexbox()) {
      exports.flex = true;
      classList.push('flex');
    } else {
      classList.push('no-flex');
    }
    if (support.animation()) {
      exports.animation = true;
      classList.push('animation');
    } else {
      classList.push('no-animation');
    }
    if (support.boxShadow()) {
      exports.boxShadow = true;
      classList.push('box-shadow');
    } else {
      classList.push('no-box-shadow');
    }
    if (support.flash()) {
      exports.flash = true;
    }
    if (support.localStorage()) {
      exports.localStorage = true;
    }
    body.addClass(classList.join(' '));
  };
  return exports;
}(common_support);
cc_util_cookie = function (exports) {
  var split = cc_function_split;
  var offsetHour = cc_function_offsetDate;
  function parse(cookieStr) {
    if (cookieStr.indexOf('"') === 0) {
      cookieStr = cookieStr.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
    }
    var result = {};
    try {
      cookieStr = decodeURIComponent(cookieStr.replace(/\+/g, ' '));
      $.each(split(cookieStr, ';'), function (index, part) {
        var terms = split(part, '=');
        var key = terms[0];
        var value = terms[1];
        if (key) {
          result[key] = value;
        }
      });
    } catch (e) {
    }
    return result;
  }
  function setCookie(key, value, options) {
    var expires = options.expires;
    if ($.isNumeric(expires)) {
      expires = offsetHour(new Date(), expires);
    }
    document.cookie = [
      encodeURIComponent(key),
      '=',
      encodeURIComponent(value),
      expires ? ';expires=' + expires.toUTCString() : '',
      options.path ? ';path=' + options.path : '',
      options.domain ? ';domain=' + options.domain : '',
      options.secure ? ';secure' : ''
    ].join('');
  }
  exports.get = function (key) {
    var result = parse(document.cookie);
    return $.type(key) === 'string' ? result[key] : result;
  };
  exports.set = function (key, value, options) {
    if ($.isPlainObject(key)) {
      options = value;
      value = null;
    }
    options = $.extend({}, exports.defaultOptions, options);
    if (value === null) {
      $.each(key, function (key, value) {
        setCookie(key, value, options);
      });
    } else {
      setCookie(key, value, options);
    }
  };
  exports.remove = function (key, options) {
    options = options || {};
    options.expires = -1;
    setCookie(key, '', $.extend({}, exports.defaultOptions, options));
  };
  exports.defaultOptions = {};
  return exports;
}(cc_util_cookie);
bjcPlayer_function_getUuid = function (require) {
  var cookie = cc_util_cookie;
  function s4() {
    return ((1 + Math.random()) * 65536 | 0).toString(16).substring(1);
  }
  function guid() {
    return [
      s4() + s4(),
      s4(),
      s4(),
      s4(),
      s4() + s4() + s4()
    ].join('-');
  }
  return function getUuid() {
    if (!cookie.get('uuid')) {
      var uuid = guid();
      cookie.set('uuid', 'uuid-' + uuid);
    }
    return cookie.get('uuid');
  };
}({});
common_storage = function (exports) {
  var support = common_support;
  var localStorage = cc_util_localStorage;
  var cookie = cc_util_cookie;
  var instance;
  exports.init = function (callback) {
    callback = callback || $.noop;
    instance = support.localStorage ? localStorage : cookie;
    setTimeout(callback);
  };
  exports.set = function (key, value) {
    instance.set(key, value);
  };
  exports.get = function (key) {
    return instance.get(key);
  };
  exports.remove = function (key) {
    instance.remove(key);
  };
  return exports;
}(common_storage);
bjcPlayer_function_bjcPlayerStatisticReport = function (exports) {
  var log = bjcPlayer_function_log;
  var getUuid = bjcPlayer_function_getUuid;
  var getBrowser = bjcPlayer_function_getBrowser;
  var getEnv = bjcPlayer_function_getEnv;
  return function bjcPlayerStatisticReport(data, domainSuffix) {
    var uuid = getUuid();
    var webData = {
      clientype: 5,
      net: 1,
      uuid: uuid,
      browser: getBrowser(),
      version: '0.0.7',
      type: 'video_vod',
      env: getEnv(),
      customstr: window.bjcCustomReportString || '',
      user_name: data.userName,
      user_number: data.userNumber
    };
    $.extend(true, data, webData);
    var env = getEnv() == 'test' ? getEnv() + '-' : '';
    log('//' + env + 'click.' + domainSuffix + '/gs.gif', data);
    data.APIVersion = '0.6.0';
    log('//bjy-log.cn-beijing.log.aliyuncs.com/logstores/' + env + 'frontend-video/track_ua.gif', data);
  };
}(bjcPlayer_function_bjcPlayerStatisticReport);
bjcPlayer_plugin_videojs_subtitles = function (exports) {
  var storage = common_storage;
  storage.init();
  var BJY_SUBTITLES_MEMORY = 'bjy_subtitles_memory';
  var memorySubtitles = storage.get(BJY_SUBTITLES_MEMORY);
  if (memorySubtitles) {
    memorySubtitles = JSON.parse(memorySubtitles);
    var keys = Object.keys(memorySubtitles);
    if (keys.length > 100) {
      storage.set(BJY_SUBTITLES_MEMORY, {});
    }
  } else {
    memorySubtitles = {};
  }
  videojs.registerPlugin('subtitles', function (options) {
    var player = this;
    var trackList = {};
    var subtitles = options.subtitles;
    var memorySubtitleId = memorySubtitles[options.vid];
    if (!subtitles || !subtitles.length) {
      return false;
    }
    var el = this.el();
    var videoPanelMenu = $('.vjs-fullscreen-control', el);
    var subtitlesMenuHtml = '<ul class="vjs-subtitles-list">';
    subtitles.forEach(function (item) {
      subtitlesMenuHtml += '<li class="vjs-subtitles-item ' + '" data-id="' + item.id + '" data-checked="0">' + item.name + '</li>';
    });
    subtitlesMenuHtml += '</ul>';
    var menuHtml = '<ul class="vjs-menu-content" role="menu">' + '<li class="vjs-menu-item vjs-subtitles-menu-item">字幕切换<div class="vjs-subtitles-item-checked"><span></span>' + subtitlesMenuHtml + '</div></li>' + '<li class="vjs-menu-item vjs-subtitles-menu-item">字幕开关<div class="vjs-subtitles-switch" data-switch="1"></div></li>' + '</ul>';
    if (!$(el).find('.vjs-subtitles-control')[0]) {
      videoPanelMenu.before('' + '<div class="vjs-subtitles-control vjs-menu-button vjs-menu-button-popup vjs-control vjs-button"  aria-live="polite" aria-expanded="false" aria-haspopup="true">' + '     <div class="vjs-menu" role="presentation">' + menuHtml + '     </div>' + '     <button class="vjs-subs-caps-button vjs-menu-button vjs-menu-button-popup vjs-button" type="button" title="字幕">' + '         <span class="vjs-icon-subtitles vjs-icon-placeholder"></span>' + '         <span class="vjs-control-text">字幕</span>' + '     </button>' + '</div>');
    }
    var index;
    var defaultId;
    for (index in subtitles) {
      var item = subtitles[index];
      item.url = item.url.replace('https:', '').replace('http:', '');
      var trackItem = player.addRemoteTextTrack({
        src: item.url,
        label: item.name,
        kind: 'subtitles',
        default: false
      }, true);
      trackItem.id = item.id;
      trackItem.name = item.name;
      trackList[item.id] = trackItem;
      trackItem.track.mode = 'disabled';
      if (memorySubtitleId && memorySubtitleId === item.id) {
        trackItem.track.mode = 'showing';
        checkItem(item.id);
      } else if (!memorySubtitleId && item.is_default) {
        trackItem.track.mode = 'showing';
        checkItem(item.id);
      } else if (memorySubtitleId && item.is_default) {
        defaultId = item.id;
      }
    }
    if (defaultId && !trackList[memorySubtitleId]) {
      checkItem(defaultId);
    }
    $('.vjs-subtitles-item ').click(function () {
      checkItem($(this).attr('data-id'));
    });
    $('.vjs-subtitles-switch').click(function () {
      var switchVal = +$(this).attr('data-switch');
      $('.vjs-subtitles-switch').attr('data-switch', switchVal ? 0 : 1);
      switchSubtitles(switchVal);
    });
    function checkItem(id) {
      $('.vjs-subtitles-item-checked span').html(trackList[id].name);
      for (var subtitleId in trackList) {
        if (subtitleId === id) {
          if (+$('.vjs-subtitles-switch').attr('data-switch')) {
            trackList[subtitleId].track.mode = 'showing';
          }
          $('.vjs-subtitles-item[data-id="' + subtitleId + '"]').attr('data-checked', 1);
          memorySubtitles[options.vid] = subtitleId;
          storage.set(BJY_SUBTITLES_MEMORY, JSON.stringify(memorySubtitles));
        } else {
          trackList[subtitleId].track.mode = 'disabled';
          $('.vjs-subtitles-item[data-id="' + subtitleId + '"]').attr('data-checked', 0);
        }
      }
    }
    function switchSubtitles(val) {
      if (!val) {
        if (memorySubtitles[options.vid]) {
          checkItem(memorySubtitles[options.vid]);
        }
      } else {
        for (var subtitleId in trackList) {
          trackList[subtitleId].track.mode = 'disabled';
        }
      }
    }
  });
  return exports;
}(bjcPlayer_plugin_videojs_subtitles);
bjcPlayer_plugin_videojs_ads = function (require) {
  var statisticReport = bjcPlayer_function_bjcPlayerStatisticReport;
  var REPORT_TYPE_SHOW = 0;
  var REPORT_TYPE_CLICK = 1;
  function Advert(container, options) {
    var defaultValue = {
      showTimer: true,
      duration: 10,
      width: 100,
      height: 100,
      canSkip: true,
      canSkipSeconds: 5
    };
    this.container = container;
    this.options = $.extend({}, defaultValue, options);
  }
  Advert.prototype.init = function () {
    var me = this;
    var options = me.options;
    var container = me.container;
    me.content = $(me.getRenderTpl(options));
    me.setSize();
    $(window).resize(function () {
      me.setSize();
    });
    container.append(this.content);
    me.container.addClass('bjc-ad-show');
    if (options.showTimer) {
      container.find('.bjc-timer').removeClass('bjc-hide');
    }
    me.beginTimer();
    container.find('.bjc-ad-more').click(function () {
      me.log(REPORT_TYPE_CLICK);
    });
    me.log(REPORT_TYPE_SHOW);
  };
  Advert.prototype.getRenderTpl = function (options) {
    var tpl = '' + '<div class="bjc-player-ad">' + '   <img class="bjc-ad-img" src="' + options.imgUrl + '">' + '   <div class="bjc-ad-opt">' + '     <span class="bjc-timer bjc-hide">' + '       &nbsp;<span class="bjc-timer-count">' + options.duration + '        </span> 秒&nbsp' + '     </span>' + '     <span class="bjc-skip bjc-hide">跳过广告</span>' + '   </div>';
    if (options.jumpUrl) {
      tpl += '' + '<a class="bjc-ad-more" href="' + options.jumpUrl + '" target="_blank">' + ' 了解详情 &gt;' + '</a>';
    }
    tpl += '</div>';
    return tpl;
  };
  Advert.prototype.setSize = function () {
    var size = this.computeContentSize();
    this.content.find('.bjc-ad-img').css({
      width: size.width,
      height: size.height,
      left: size.left,
      top: size.top
    });
  };
  Advert.prototype.beginTimer = function () {
    var me = this;
    var options = me.options;
    var duration = options.duration;
    var ele = me.container.find('.bjc-timer-count');
    var container = me.container;
    me.timer = setInterval(function () {
      duration--;
      var watched = me.options.duration - duration;
      if (options.canSkip && watched == options.canSkipSeconds) {
        var skipEle = container.find('.bjc-skip');
        skipEle.removeClass('bjc-hide');
        skipEle.click(function () {
          me.finished();
        });
      }
      if (duration == 0) {
        me.finished();
      }
      ele.html(duration);
    }, 1000);
  };
  Advert.prototype.finished = function () {
    var me = this;
    me.content.remove();
    me.container.removeClass('bjc-ad-show');
    clearInterval(me.timer);
    if (me.options.onfinish) {
      me.options.onfinish();
    }
  };
  Advert.prototype.computeContentSize = function () {
    var me = this;
    var container = me.container;
    var options = me.options;
    var containerWidth = container.width();
    var containerHeight = container.height() - 20;
    var resWidth = containerWidth;
    var resHeight = Math.ceil(containerWidth * options.height / options.width);
    var top = (containerHeight - resHeight) / 2;
    var left = 0;
    if (resHeight > containerHeight) {
      resWidth = Math.ceil(containerHeight * options.width / options.height);
      resHeight = containerHeight;
      top = 10;
      left = (containerWidth - resWidth) / 2;
    }
    return {
      width: resWidth,
      height: resHeight,
      left: left + 'px',
      top: top + 'px'
    };
  };
  Advert.prototype.log = function (reportType) {
    var me = this;
    var options = me.options;
    var videoInfo = options.videoInfo;
    var data = {
      type: 'baijiayun_ad',
      guid: videoInfo.guid,
      fid: videoInfo.video_id,
      partner_id: videoInfo.video_info.partner_id,
      ad_id: options.id,
      userName: options.user_name,
      userNumber: options.user_number
    };
    data.report_type = reportType;
    statisticReport(data, options.domainSuffix);
  };
  videojs.registerPlugin('ads', function (dom, adValues, callback, videoInfo, domainSuffix) {
    var adInstance = new Advert(dom, {
      id: adValues.adId,
      jumpUrl: adValues.clickJumpUrl,
      imgUrl: adValues.imgUrl,
      showTimer: adValues.showTimer,
      duration: adValues.duration,
      width: adValues.width,
      height: adValues.height,
      canSkip: adValues.skipAd,
      canSkipSeconds: adValues.skipAdSeconds,
      onfinish: callback,
      videoInfo: videoInfo,
      domainSuffix: domainSuffix
    });
    adInstance.init();
    return adInstance;
  });
}({});
bjcPlayer_index = function (exports) {
  var getEnv = bjcPlayer_function_getEnv;
  var getProEnv = bjcPlayer_function_getProEnv;
  var getUuid = bjcPlayer_function_getUuid;
  var decodeUrl = bjcPlayer_function_decodeUrl;
  var bjcPlayerStatisticReport = bjcPlayer_function_bjcPlayerStatisticReport;
  var insertStyleStr = bjcPlayer_function_insertStyleStr;
  var camelCaseObject = bjcPlayer_function_camelCaseObject;
  var config = bjcPlayer_config;
  var AD_TYPE_IMG = 0;
  var videoConfig = common_videoConfig;
  var domainSuffix = 'baijiayun.com';
  var localStorage = cc_util_localStorage;
  var REPORT_RESOLUTION_TYPES = {};
  REPORT_RESOLUTION_TYPES[videoConfig.VIDEO_DEFINITION_STANDARD] = 1;
  REPORT_RESOLUTION_TYPES[videoConfig.VIDEO_DEFINITION_HIGH] = 2;
  REPORT_RESOLUTION_TYPES[videoConfig.VIDEO_DEFINITION_SUPER] = 3;
  REPORT_RESOLUTION_TYPES[videoConfig.VIDEO_DEFINITION_SUPERHD] = 4;
  REPORT_RESOLUTION_TYPES[videoConfig.VIDEO_DEFINITION_720] = 5;
  REPORT_RESOLUTION_TYPES[videoConfig.VIDEO_DEFINITION_1080] = 6;
  var REPORT_EVENTS_ACCORDING_TO_PLAYER_EVENT = {
    play: 'play',
    playing: 'play',
    pause: 'pause',
    waiting: 'block',
    seeking: 'seek',
    seeked: 'seek',
    ratechange: 'speedup',
    error: 'playerror',
    normal: 'normal',
    ended: 'endplay',
    changecdn: 'changecdn',
    resolution: 'resolution',
    firstplaywait: 'firstplaywait'
  };
  bjcPlayer_plugin_videojs_definition;
  bjcPlayer_plugin_videojs_logo;
  bjcPlayer_plugin_videojs_ads;
  bjcPlayer_plugin_videojs_rolling;
  bjcPlayer_plugin_videojs_marquee;
  bjcPlayer_plugin_videojs_loop;
  bjcPlayer_plugin_videojs_subtitles;
  bjcPlayer_plugin_videojs_eyes_protection;
  bjcPlayer_plugin_videojs_notice;
  function BjcPlayer(dom, options) {
    var me = this;
    var mergedOptions = $.extend({
      autoplay: true,
      controls: true,
      preload: 'none',
      sid: '',
      fluid: false,
      muted: false,
      width: '100%',
      height: '100%',
      language: 'zh-CN',
      lastReportTime: 0,
      currentTime: 0,
      sources: [],
      extraData: { video_info: {} },
      showWatermark: false,
      showLogo: false,
      playbackRates: [
        0.7,
        1,
        1.2,
        1.5,
        2
      ],
      html5: { nativeTextTracks: false },
      children: {
        loadingSpinner: true,
        textTrackDisplay: true,
        bigPlayButton: true,
        textTrackSettings: false,
        controlBar: {
          volumePanel: { inline: false },
          currentTimeDisplay: options.showCurrentTimeDisplay || false,
          durationDisplay: options.showDurationDisplay || false,
          fullscreenToggle: true,
          progressControl: !options.hideProgress
        },
        posterImage: true
      },
      techOrder: [
        'html5',
        'flvjs',
        'fmp4'
      ]
    }, options);
    this.el = dom;
    this.options = mergedOptions;
    this.reportOptions = {
      firstPlay: true,
      canBlock: false,
      waitTime: 0,
      loadStartTime: 0,
      blockStartTime: 0,
      isBlocked: false,
      isSeeking: false,
      isResolutionChanged: false,
      seekfrom: 0
    };
    this.sourceIndex = 0;
    this.reportInterval = undefined;
    this.defaultReportInterval = 120;
    domainSuffix = options.domainSuffix || domainSuffix;
    window.bjcCustomReportString = options.customReportString;
    if (options.user_number) {
      window.bjcCustomReportString = window.bjcCustomReportString ? window.bjcCustomReportString + '&user_number=' + options.user_number : 'user_number=' + options.user_number;
    }
    if (options.user_name) {
      window.bjcCustomReportString = window.bjcCustomReportString ? window.bjcCustomReportString + '&user_name=' + options.user_name : 'user_name=' + options.user_name;
    }
    $(dom).attr('data-vid', options.vid);
    me.init(dom, mergedOptions);
  }
  BjcPlayer.prototype.bindEvent = function () {
    var me = this;
    var player = this.player;
    var lastCurrentTime = 0;
    var isSeeking = false;
    if (this.options.extraData.subtitles) {
      var subtitlesPlugin = player.subtitles({
        subtitles: this.options.extraData.subtitles,
        vid: this.options.vid
      });
    }
    var events = [
      'loadstart',
      'loadeddata',
      'canplay',
      'canplaythrough',
      'play',
      'pause',
      'waiting',
      'playing',
      'seeking',
      'seeked',
      'ended',
      'timeupdate',
      'ratechange',
      'error'
    ];
    var reportEvents = [
      'playing',
      'pause',
      'seeked',
      'waiting',
      'ended',
      'ratechange',
      'error'
    ];
    for (var i = 0; i < events.length; i++) {
      (function (event) {
        player.on(event, function (env) {
          if (player.isRolling) {
            if (event === 'ended') {
              player.isRolling = false;
            }
            return false;
          }
          var currentTime = player.currentTime();
          me.options.currentTime = Math.round(currentTime * 1000);
          if ($.inArray(event, reportEvents) > -1) {
            me.clearReportTimer();
            if (event === 'playing' && me.reportOptions.canBlock && me.reportOptions.isBlocked) {
              me.reportOptions.isBlocked = false;
              me.reportOptions.waitTime = new Date().getTime() - me.reportOptions.blockStartTime;
              me.statisticReport('blockend');
              me.statisticReport(event);
            }
            if (event === 'seeked') {
              if (me.reportOptions.firstPlay) {
                return;
              }
              me.reportOptions.isSeeking = false;
              if (!me.reportOptions.isResolutionChanged) {
                me.statisticReport(event);
              }
              me.reportOptions.seekfrom = currentTime;
            } else if (event === 'waiting') {
              if (!me.reportOptions.firstPlay && me.reportOptions.canBlock && !me.reportOptions.isSeeking) {
                me.reportOptions.isBlocked = true;
                me.reportOptions.blockStartTime = new Date().getTime();
                me.statisticReport(event);
              }
            } else if (event === 'pause' || event === 'ended' || event === 'error') {
              if (!me.reportOptions.isSeeking) {
                me.statisticReport(event);
              }
            } else {
              me.statisticReport(event);
            }
            if (event === 'playing') {
              me.beginReportTimer();
            }
          }
          if (event === 'loadstart') {
            me.reportOptions.loadStartTime = new Date().getTime();
          }
          if (event === 'canplay') {
            if (me.reportOptions.firstPlay) {
              me.reportOptions.firstPlay = false;
              var nowTime = new Date().getTime();
              me.reportOptions.waitTime = nowTime - me.reportOptions.loadStartTime;
              me.statisticReport('firstplaywait');
            }
          }
          if (event === 'timeupdate') {
            if (Math.abs(currentTime - lastCurrentTime) >= 3) {
              me.reportOptions.seekfrom = lastCurrentTime;
              me.reportOptions.isSeeking = true;
            }
            lastCurrentTime = currentTime;
            var currentPlayerDom = $('[data-vid=' + me.options.vid + ']');
            if (!currentPlayerDom[0]) {
              me.player.dispose();
              return false;
            }
          }
          if (event == 'seeking' && !me.reportOptions.isSeeking) {
            me.reportOptions.isSeeking = true;
          }
          if (event == 'ratechange') {
            var rate = player.playbackRate();
            localStorage.set('bjyPlaybackRate', rate);
          }
          var callback = me.options['on' + event];
          if ($.type(callback) === 'function' && event !== 'error') {
            env.currentTime = currentTime;
            callback.call(null, env);
          }
          if (event === 'error' || event === 'hls-error-reload') {
            me.tryPlayNextSource();
          }
        });
      }(events[i]));
    }
  };
  BjcPlayer.prototype.beginReportTimer = function () {
    var me = this;
    if (me.reportInterval) {
      return;
    }
    var interval = (me.options.extraData.report_interval || me.defaultReportInterval) * 1000;
    me.reportInterval = window.setInterval(function () {
      me.statisticReport('normal');
    }, interval);
  };
  BjcPlayer.prototype.clearReportTimer = function () {
    this.reportInterval && window.clearInterval(this.reportInterval);
    this.reportInterval = null;
  };
  BjcPlayer.prototype.statisticReport = function (event) {
    if (!this.options.vid || !this.player || !this.player.el_) {
      return;
    }
    if (this.reportOptions.isResolutionChanged && event === 'playing') {
      this.reportOptions.isResolutionChanged = false;
      return;
    }
    var extraData = this.options.extraData;
    var video_info = extraData.video_info;
    var play_info = extraData.all_format_play_info;
    var player = this.player;
    var source = this.options.sources[this.sourceIndex];
    var buffered = player.buffered();
    var bufferedLength = buffered.length;
    var bufbegintime = '';
    var bufendtime = '';
    var currentRate = this.player.playbackRate();
    if (buffered && bufferedLength) {
      bufbegintime = buffered.start(bufferedLength - 1);
      bufendtime = buffered.end(bufferedLength - 1);
    }
    var fileSize = play_info && source.playfiletype && this.defaultDefinition && play_info[source.playfiletype] && play_info[source.playfiletype][this.defaultDefinition] && play_info[source.playfiletype][this.defaultDefinition].size;
    this.options.currentTime = Math.round(player.currentTime() * 1000);
    var data = {
      fid: video_info.video_id,
      guid: extraData.guid,
      playendtime: this.options.currentTime,
      playbegintime: this.options.lastReportTime,
      partner_id: video_info.partner_id,
      totaltime: player.duration() * 1000,
      event: REPORT_EVENTS_ACCORDING_TO_PLAYER_EVENT[event] || 'normal',
      playfiletype: source.playfiletype,
      bufendtime: bufendtime,
      filesize: fileSize || 0,
      bufbegintime: bufbegintime,
      cdn: source.cdn,
      resolution: this.defaultDefinition,
      speedup: currentRate,
      userName: this.options.user_name,
      userNumber: this.options.user_number
    };
    if (event == 'seeked') {
      data.playendtime = Math.round(this.reportOptions.seekfrom * 1000);
      data.seekto = this.options.currentTime;
      this.reportOptions.seekfrom = player.currentTime();
    }
    if (data.event == 'play') {
      data.playbegintime = data.playendtime;
      this.reportOptions.canBlock = true;
    } else if (data.event != 'normal') {
      this.reportOptions.canBlock = false;
    }
    if (this.reportOptions.waitTime && (data.event == 'firstplaywait' || data.event == 'blockend')) {
      data.waittime = this.reportOptions.waitTime;
      this.reportOptions.waitTime = 0;
    }
    data.duration = data.playendtime - data.playbegintime;
    bjcPlayerStatisticReport(data, domainSuffix);
    this.options.lastReportTime = this.options.currentTime;
  };
  BjcPlayer.prototype.showBigPlayButton = function () {
    var player = this.player;
    var me = this;
    var bigPlayButton = player.bigPlayButton;
    if (bigPlayButton) {
      bigPlayButton.show();
    } else {
      player.bigPlayButton = player.addChild('bigPlayButton');
    }
  };
  BjcPlayer.prototype.switchDefinition = function (definitionType, currentTime) {
    var sources = [];
    var me = this;
    var player = this.player;
    !currentTime && (currentTime = player.currentTime());
    var data = this.options.extraData;
    var play_info = data.all_format_play_info;
    var videoWatermark = data.video_watermark;
    var videoTypes = config.videoTypes;
    var isPaused = player.paused();
    this.defaultDefinition = definitionType;
    this.sourceIndex = 0;
    if (play_info) {
      for (var type in videoTypes) {
        var item = videoTypes[type];
        if (this.player.canPlayType(item.type) && play_info[type]) {
          play_info[type][definitionType].cdn_list.forEach(function (m) {
            var url = decodeUrl(m.enc_url);
            var uuid = getUuid();
            if (url.indexOf('?') > -1) {
              url += '&uuid=' + uuid;
            } else if (url.length > 0) {
              url += '?uuid=' + uuid;
            }
            me.ratio = m.width / m.height;
            sources.push({
              src: url,
              cdn: m.cdn,
              type: item.type,
              playfiletype: type
            });
          });
        }
      }
      this.options.sources = sources;
    }
    player.src(this.options.sources[this.sourceIndex]);
    var handleSeekEvent = 'loadedmetadata';
    if (player.techName_ !== 'Youtube' && player.preload() === 'none' && player.techName_ !== 'Flash') {
      handleSeekEvent = 'timeupdate';
    }
    player.one(handleSeekEvent, function () {
      player.currentTime(currentTime);
      if (!isPaused || me.options.autoplay) {
        player.play();
      }
      var playRate = localStorage.get('bjyPlaybackRate') || me.options.currentRate;
      if (playRate && !player.isRolling) {
        player.playbackRate(playRate);
      }
    });
    player.one('play', function () {
      if (!data.disable_hourse_lamp && me.options.customHourseLamp) {
        me.player.marquee({ content: me.options.customHourseLamp });
      }
      if (videoWatermark && videoWatermark.url && me.options.showWatermark) {
        var pos = +videoWatermark.pos;
        player.watermark({
          file: videoWatermark.url,
          xpos: [
            1,
            4
          ].indexOf(pos) > -1 ? 0 : 100,
          ypos: [
            1,
            2
          ].indexOf(pos) > -1 ? 0 : 100,
          xrepeat: 0,
          ratio: me.ratio,
          opacity: 0.5
        });
      }
    });
    player.one('error', function (env, error) {
      if (error) {
        me.options.onerror && me.options.onerror({ msg: error.message });
      }
    });
    player.one('ended', function () {
      var currentRate = player.playbackRate();
      if (!player.isRolling) {
        me.options.currentRate = currentRate;
      }
      var url = player.src();
      function onfinish() {
        if (data.pre_load_logo) {
          me.player.hasStarted(false);
          me.player.poster(data.pre_load_logo);
        }
        me.firstTimePlay = false;
        if (me.needSetLoopAfterRoll) {
          player.loop(true);
        }
        var loop = player.loop();
        if (!loop) {
          player.autoplay(false);
          player.src(me.options.sources[me.sourceIndex]);
        } else {
          player.src(me.options.sources[me.sourceIndex]);
        }
      }
      if (data.end_video) {
        player.rolling({
          url: data.end_video,
          type: 'video/mp4',
          callback: onfinish
        });
      } else {
        var ad = data.ad;
        if (me.hasEndAds(data) && !me.endAd) {
          var adValues = ad.end[0];
          me.endAd = me.player.ads($('.bjc-player-wrapper'), adValues, onfinish.bind(me), data);
        } else {
          onfinish();
        }
      }
    });
  };
  BjcPlayer.prototype.hasEndAds = function (data) {
    var ad = data.ad;
    return ad && ad.end.length > 0 && ad.end[0].adType == AD_TYPE_IMG;
  };
  BjcPlayer.prototype.getPlayInfoRequest = function (url, callback) {
    var me = this;
    var options = me.options;
    $.ajax({
      type: 'get',
      async: false,
      url: url,
      dataType: 'jsonp',
      jsonp: 'callback',
      success: function (res) {
        if (res.code == 0) {
          if (res.data.ad) {
            res.data.ad = camelCaseObject(res.data.ad);
          }
          callback.call(me, res);
        } else if (res.code == 5300) {
          window.location.href = res.data.url;
        } else {
          res.msg && me.showErrorTip(res.msg);
          options.onplayfail && options.onplayfail({
            error: {
              code: res.code,
              msg: res.msg
            },
            vid: options.vid
          });
        }
      },
      error: function () {
        me.showErrorTip('出错啦~');
        options.onplayfail && options.onplayfail({
          error: {
            code: 301,
            msg: '服务器出错'
          },
          vid: options.vid
        });
      }
    });
  };
  BjcPlayer.prototype.getVideoInfo = function (vid, sid) {
    var options = this.options;
    var videoInfoJsonp = '//' + 'www.' + domainSuffix + '/vod/video/getAllFormatPlayUrl';
    if (options.privateDomainPrefix) {
      videoInfoJsonp = getProEnv(options.privateDomainPrefix, domainSuffix) + '/vod/video/getAllFormatPlayUrl';
    }
    var reqParam = [
      'vid=' + vid,
      'sid=' + sid,
      'render=jsonp',
      'client_type=flash',
      'ver=2'
    ];
    if (options.token == 'wannengtoken') {
      reqParam.push('skip_verify=1');
    } else {
      reqParam.push('token=' + options.token);
    }
    if (options.pid) {
      reqParam.push('pid=' + options.pid);
    }
    if (options.access_key) {
      reqParam.push('access_key=' + options.access_key);
    }
    this.getPlayInfoRequest(videoInfoJsonp + '?' + reqParam.join('&'), this.getVideoInfoHandler);
  };
  BjcPlayer.prototype.getVideoInfoHandler = function (res) {
    var data = res.data;
    var player = this.player;
    this.options.extraData = data;
    player.setEyesProtection();
    if (data.start_video) {
      player.rolling({
        url: data.start_video,
        type: 'video/mp4',
        callback: this.tryPlay.bind(this)
      });
    } else {
      var ad = data.ad;
      if (ad && ad.start.length > 0 && ad.start[0].adType == AD_TYPE_IMG) {
        var adValues = ad.start[0];
        if (!this.startAd) {
          var adValues = ad.start[0];
          this.startAd = this.player.ads($('.bjc-player-wrapper'), adValues, this.tryPlay.bind(this), data, domainSuffix);
        }
      } else {
        this.tryPlay();
      }
    }
    if (data.notice && data.notice.extend) {
      player.setNotice(data.notice.extend);
    }
  };
  BjcPlayer.prototype.init = function (dom, options) {
    var me = this;
    var playerOptions = me.options;
    videojs.Hls.xhr.beforeRequest = function (options) {
      var protocol = window.location.protocol;
      var uri = options.uri;
      if (uri.indexOf('http:') > -1 && protocol === 'https:') {
        options.uri = options.uri.replace('http:', 'https:');
      }
      return options;
    };
    dom = $('[data-vid=' + options.vid + ']')[0];
    this.player = window.videojs(dom, options, function () {
      me.bindEvent();
    });
    if (!playerOptions.sources || !(playerOptions.vid && playerOptions.token)) {
      me.showErrorTip('找不到视频源\uFF0C请传入source或vid及token');
      return false;
    }
    if (options.disableSeek) {
      $('.vjs-control-bar').append('<div class="progress-modal"></div>');
    }
    this.play(playerOptions.vid, playerOptions.sid);
    var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
    if (MutationObserver) {
      var mutationObserver = new MutationObserver(function () {
        $('video').remove();
        me.showErrorTip('视频禁止在小窗口播放\uFF0C请关闭小窗口并刷新页面重试');
        mutationObserver.disconnect();
      });
      mutationObserver.observe($('.vjs-loading-spinner')[0], { attributes: true });
    }
  };
  BjcPlayer.prototype.tryPlay = function () {
    var options = this.options;
    var data = options.extraData;
    var player = this.player;
    var init_pic = data.init_pic;
    var vod_default_definition = data.vod_default_definition;
    var localDefinition = localStorage.get('bjyDefinition');
    var play_info = data.all_format_play_info;
    var videoTypes = config.videoTypes;
    if (play_info) {
      for (var type in videoTypes) {
        var item = videoTypes[type];
        if (this.player.canPlayType(item.type) && play_info[type] && play_info[type][localDefinition]) {
          vod_default_definition = localDefinition;
          break;
        }
      }
    }
    if (vod_default_definition !== localDefinition) {
      localStorage.set('bjyDefinition', vod_default_definition);
    }
    var video_snapshot = data.video_info.video_snapshot;
    var me = this;
    if (!this.options.autoplay) {
      this.showBigPlayButton();
    }
    var loopPlugin = player.setLoop({ defaultLoopValue: options.loop });
    loopPlugin.on('loopvaluechange', function (event, value) {
      var data = me.options.extraData;
      if (value) {
        if ((data.end_video || me.hasEndAds(data)) && me.firstTimePlay) {
          me.needSetLoopAfterRoll = true;
        } else {
          player.loop(true);
        }
      } else {
        player.loop(false);
        me.needSetLoopAfterRoll = false;
      }
    });
    if (init_pic) {
      player.poster(init_pic);
    }
    if (data.logo_url && options.showLogo) {
      player.setLogo({
        url: data.logo_url,
        homeUrl: data.home_url
      });
    }
    if (vod_default_definition) {
      var definition = player.definition({
        defaultDefinition: vod_default_definition,
        definitionData: data.definition
      });
      definition.on('switchdefinition', function (event, type) {
        player.poster('');
        player.bigPlayButton && player.bigPlayButton.hide();
        if (type !== me.defaultDefinition) {
          localStorage.set('bjyDefinition', type);
          me.options.currentRate = player.playbackRate();
          me.switchDefinition(type);
          me.reportOptions.isResolutionChanged = true;
        }
      });
    }
    if (data.player_skin) {
      insertStyleStr('.video-js .vjs-play-progress{background-color:' + data.player_skin + '}');
    }
    this.switchDefinition(vod_default_definition || options.defaultDefinition, options.currentTime);
    if (video_snapshot && video_snapshot.length) {
      player.markers({
        markers: video_snapshot.map(function (item) {
          return {
            time: item.pos,
            text: item.name
          };
        })
      });
    }
  };
  BjcPlayer.prototype.tryPlayNextSource = function () {
    var sources = this.options.sources;
    var lastSourcesIndex = sources.length - 1;
    if (this.sourceIndex == lastSourcesIndex) {
      return;
    }
    var index = ++this.sourceIndex;
    this.statisticReport('changecdn');
    this.player.src(sources[index]);
  };
  BjcPlayer.prototype.showErrorTip = function (msg) {
    var dialog = document.createElement('div');
    var player = this.player;
    dialog.className = 'vjs-error-dialog';
    dialog.textContent = msg;
    player.el().appendChild(dialog);
    player.bigPlayButton && player.bigPlayButton.hide();
    this.options.onerror && this.options.onerror({ msg: msg });
  };
  BjcPlayer.prototype.dispose = function () {
    this.player && this.player.dispose();
  };
  BjcPlayer.prototype.pause = function () {
    this.player.pause();
  };
  BjcPlayer.prototype.resume = function () {
    this.player.play();
  };
  BjcPlayer.prototype.seek = function (time) {
    this.player.currentTime(time);
  };
  BjcPlayer.prototype.getCurrentTime = function () {
    return this.player.currentTime();
  };
  BjcPlayer.prototype.play = function (vid, sid) {
    this.firstTimePlay = true;
    if (vid) {
      this.getVideoInfo(vid, sid);
    } else {
      this.tryPlay();
    }
    this.clearReportTimer();
  };
  window.BjcPlayer = window.bjcPlayer = BjcPlayer;
  return BjcPlayer;
}(bjcPlayer_index);
}());
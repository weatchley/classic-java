// Copyright (c) 2006 S??bastien Gruhier (http://xilinus.com, http://itseb.com)
// 
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
// 
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// VERSION 0.99

var Window = Class.create();
Window.keepMultiModalWindow = false;
Window.prototype = {
  // Constructor
  // Available parameters : className, title, minWidth, minHeight, maxWidth, maxHeight, width, height, top, left, bottom, right, resizable, zIndex, opacity, recenterAuto, wiredDrag
  //                        hideEffect, showEffect, showEffectOptions, hideEffectOptions, effectOptions, url, draggable, closable, minimizable, maximizable, parent, onload
  initialize: function(id) {
  
  debug("Window.initialize: "+id);
	  
	  if ($(id))
      //alert("Window " + id + " is already register is the DOM!!, be sure to use setDestroyOnClose()")
      
    this.hasEffectLib = String.prototype.parseColor != null;
    this.options = Object.extend({
      className:         "dialog",
      minWidth:          100, 
      minHeight:         20,
      resizable:         true,
      closable:          true,
      minimizable:       true,
      maximizable:       true,
      draggable:         true,
      userData:          null,
      showEffect:        (this.hasEffectLib ? Effect.Appear : Element.show),
      hideEffect:        (this.hasEffectLib ? Effect.Fade : Element.hide),
      showEffectOptions: {},
      hideEffectOptions: {},
      effectOptions:     null,
      parent:            document.getElementsByTagName("body").item(0),
      title:             "&nbsp;",
      url:               null,
      onload:            Prototype.emptyFunction,
      width:             200,
      height:            300,
      opacity:           1,
      recenterAuto:      true,
      wiredDrag:         false,
	  winState:			 "reg"
    }, arguments[1] || {});
        
    if (this.options.effectOptions) {
      Object.extend(this.options.hideEffectOptions, this.options.effectOptions);
      Object.extend(this.options.showEffectOptions, this.options.effectOptions);
    }
    if (this.options.hideEffect == Element.hide)
      this.options.hideEffect = function(){ Element.hide(this.element); if (this.destroyOnClose) this.destroy(); }.bind(this)
      
    this.element = this._createWindow(id);
    
    // Bind event listener
    this.eventMouseDown = this._initDrag.bindAsEventListener(this);
    this.eventMouseUp   = this._endDrag.bindAsEventListener(this);
    this.eventMouseMove = this._updateDrag.bindAsEventListener(this);
    this.eventOnLoad    = this._getWindowBorderSize.bindAsEventListener(this);
    this.eventMouseDownContent = this.toFront.bindAsEventListener(this);
    this.eventResize = this._recenter.bindAsEventListener(this);
 
    this.topbar = $(this.element.id + "_top");
    this.bottombar = $(this.element.id + "_bottom");
    this.content = $(this.element.id + "_content");
    
    Event.observe(this.topbar, "mousedown", this.eventMouseDown);
    Event.observe(this.bottombar, "mousedown", this.eventMouseDown);
    Event.observe(this.content, "mousedown", this.eventMouseDownContent);
    Event.observe(window, "load", this.eventOnLoad);
    Event.observe(window, "resize", this.eventResize);
    Event.observe(window, "scroll", this.eventResize);
    
    if (this.options.draggable)  {
      this.bottombar.addClassName("bottom_draggable");
      this.topbar.addClassName("top_draggable");
    }    
    
    if (this.options.resizable) {
      this.sizer = $(this.element.id + "_sizer");
      Event.observe(this.sizer, "mousedown", this.eventMouseDown);
    }  
    
	//debug("window, options:"+arguments[1].title+", "+arguments[1].url);
	
	//winState setup
	/*
	if (arguments[1].wState != null) {
	this.wState = arguments[1].wState;
	} 
	*/
	
	this.url = null;
    this.title = null;
    if (arguments[1].url != null) {
      this.url = arguments[1].url;
    }
	if (arguments[1].title != null) {
      this.title = arguments[1].title;
    }
	
    this.useLeft = null;
    this.useTop = null;
    if (arguments[1].left != null) {
      this.element.setStyle({left: parseFloat(arguments[1].left) + 'px'});
      this.useLeft = true;
    }
    if (arguments[1].right != null) {
      this.element.setStyle({right: parseFloat(arguments[1].right) + 'px'});
      this.useLeft = false;
    }
    if (this.useLeft == null) {
      this.element.setStyle({left: "0px"});
      this.useLeft = true;
    }
    
    if (arguments[1].top != null) {
      this.element.setStyle({top: parseFloat(arguments[1].top) + 'px'});
      this.useTop = true;
    }
    if (arguments[1].bottom != null) {
      this.element.setStyle({bottom: parseFloat(arguments[1].bottom) + 'px'});      
      this.useTop = false;
    }
    if (this.useTop == null) {
      this.element.setStyle({top: "0px"});
      this.useTop = true;
    }

    this.storedLocation = null;
    
    this.setOpacity(this.options.opacity);
    if (this.options.zIndex)
      this.setZIndex(this.options.zIndex)

    this.destroyOnClose = false;

    this._getWindowBorderSize();
    this.width = this.options.width;
	this.height = this.options.height;
    this.visible = false;
    
    if (this.width && this.height)
      this.setSize(this.options.width, this.options.height);
    this.setTitle(this.options.title)
    Windows.register(this);    
  },
  
  // Destructor
   destroy: function() {
    Windows.notify("onDestroy", this);
    Event.stopObserving(this.topbar, "mousedown", this.eventMouseDown);
    Event.stopObserving(this.bottombar, "mousedown", this.eventMouseDown);
    Event.stopObserving(this.content, "mousedown", this.eventMouseDownContent);
    
    Event.stopObserving(window, "load", this.eventOnLoad);
    Event.stopObserving(window, "resize", this.eventResize);
    Event.stopObserving(window, "scroll", this.eventResize);
    
    Event.stopObserving(this.content, "load", this.options.onload);

    if (this._oldParent) {
      var content = this.getContent();
      var originalContent = null;
      for(var i = 0; i < content.childNodes.length; i++) {
        originalContent = content.childNodes[i];
        if (originalContent.nodeType == 1) 
          break;
        originalContent = null;
      }
      if (originalContent)
        this._oldParent.appendChild(originalContent);
      this._oldParent = null;
    }

    if (this.sizer)
        Event.stopObserving(this.sizer, "mousedown", this.eventMouseDown);

    if (this.options.url)
      this.content.src = null

     if(this.iefix) 
      Element.remove(this.iefix);

    Element.remove(this.element);
    Windows.unregister(this);      
  },
    
  // Sets window deleagte, should have functions: "canClose(window)" 
  setDelegate: function(delegate) {
    this.delegate = delegate
  },
  
  // Gets current window delegate
  getDelegate: function() {
    return this.delegate;
  },
  
  // Gets window content
  getContent: function () {
    return this.content;
  },
  
  // Sets the content with an element id
  setContent: function(id, autoresize, autoposition) {
    var element = $(id);
    if (null == element) throw "Unable to find element '" + id + "' in DOM";
    this._oldParent = element.parentNode;

    var d = null;
    var p = null;

    if (autoresize) 
      d = Element.getDimensions(element);
    if (autoposition) 
      p = Position.cumulativeOffset(element);

    var content = this.getContent()
    content.appendChild(element);
    element.show();
    if (autoresize) 
      this.setSize(d.width, d.height);
    if (autoposition) 
      this.setLocation(p[1] - this.heightN, p[0] - this.widthW);    
  },
  
  setAjaxContent: function(url, options, showCentered, showModal) {
    this.showFunction = showCentered ? "showCenter" : "show";
    this.showModal = showModal || false;
  
    if (options == null)
      options = {}  
    this.onComplete = options.onComplete;
    options.onComplete = this._setAjaxContent.bind(this);
    
    new Ajax.Request(url, options);
  },
  
  _setAjaxContent: function(originalRequest) {
    Element.update(this.getContent(), originalRequest.responseText);
    if (this.onComplete)
      this.onComplete(originalRequest);
    this[this.showFunction](this.showModal)
  },
  
  // Stores position/size in a cookie, by default named with window id
  setCookie: function(name, expires, path, domain, secure) {
  /*
	  //name = name || this.element.id;
	name = this.element.id;
    this.cookie = [name, expires, path, domain, secure];
    
    debug("setCookie: name="+name+", expires="+expires+", path="+path+", domain="+domain+", secure="+secure);
    
    // Get cookie
    var value = null;
    	//var value = WindowUtilities.getCookie(name)
    // If exists
		
    if (value) {
      var values = value.split(',');
      var x = values[0].split(':');
      var y = values[1].split(':');

      var w = parseFloat(values[2]), h = parseFloat(values[3]);
      var mini = values[4];
      var maxi = values[5];

      this.setSize(w, h);
      if (mini == "true")
        this.doMinimize = true; // Minimize will be done at onload window event
      else if (maxi == "true")
        this.doMaximize = true; // Maximize will be done at onload window event

      this.useLeft = x[0] == "l";
      this.useTop = y[0] == "t";

      this.element.setStyle(this.useLeft ? {left: x[1]} : {right: x[1]});
      this.element.setStyle(this.useTop ? {top: y[1]} : {bottom: y[1]});
    }
  */
  },
  
  // Gets window ID
  getId: function() {
    return this.element.id;
  },
  
  // Detroys itself when closing 
  setDestroyOnClose: function() {
    var destroyFunc = this.destroy.bind(this);
    if (this.options.hideEffectOptions.afterFinish) {
      var func = this.options.hideEffectOptions.afterFinish;
      this.options.hideEffectOptions.afterFinish = function() {func();destroyFunc() }
    }
    else 
      this.options.hideEffectOptions.afterFinish = function() {destroyFunc() }
    this.destroyOnClose = true;
  },
  
  // initDrag event
  _initDrag: function(event) {
    // Get pointer X,Y
    this.pointer = [Event.pointerX(event), Event.pointerY(event)];
    
    if (this.options.wiredDrag) 
      this.currentDrag = this._createWiredElement();
    else
      this.currentDrag = this.element;
      
    // Resize
    if (Event.element(event) == this.sizer) {
      this.doResize = true;
      this.widthOrg = this.width;
      this.heightOrg = this.height;
      this.bottomOrg = parseFloat(this.element.getStyle('bottom'));
      this.rightOrg = parseFloat(this.element.getStyle('right'));
      Windows.notify("onStartResize", this);
    }
    else {
      this.doResize = false;

      // Check if click on close button, 
      var closeButton = $(this.getId() + '_close');
      if (closeButton && Position.within(closeButton, this.pointer[0], this.pointer[1])) {
        this.currentDrag = null;
        return;
      }

      this.toFront();

      if (! this.options.draggable) 
        return;
      Windows.notify("onStartMove", this);
    }    
    // Register global event to capture mouseUp and mouseMove
    Event.observe(document, "mouseup", this.eventMouseUp, false);
    Event.observe(document, "mousemove", this.eventMouseMove, false);
    
    // Add an invisible div to keep catching mouse event over iframes
    WindowUtilities.disableScreen('__invisible__', '__invisible__');

    // Stop selection while dragging
    document.body.ondrag = function () { return false; };
    document.body.onselectstart = function () { return false; };
    
    this.currentDrag.show();
    Event.stop(event);
  },

  // updateDrag event
  _updateDrag: function(event) {
     var pointer = [Event.pointerX(event), Event.pointerY(event)];    
    var dx = pointer[0] - this.pointer[0];
    var dy = pointer[1] - this.pointer[1];
    
    // Resize case, update width/height
    if (this.doResize) {
      this.setSize(this.widthOrg + dx , this.heightOrg + dy);
      
      dx = this.width - this.widthOrg
      dy = this.height - this.heightOrg
      
      // Check if it's a right position, update it to keep upper-left corner at the same position
      if (! this.useLeft) 
        this.currentDrag.setStyle({right: (this.rightOrg -dx) + 'px'});
      // Check if it's a bottom position, update it to keep upper-left corner at the same position
      if (! this.useTop) 
        this.currentDrag.setStyle({bottom: (this.bottomOrg -dy) + 'px'});
    }
    // Move case, update top/left
    else {
      this.pointer = pointer;
      
      if (this.useLeft) 
        this.currentDrag.setStyle({left: parseFloat(this.currentDrag.getStyle('left')) + dx + 'px'});
      else 
        this.currentDrag.setStyle({right: parseFloat(this.currentDrag.getStyle('right')) - dx + 'px'});
      
      if (this.useTop) 
        this.currentDrag.setStyle({top: parseFloat(this.currentDrag.getStyle('top')) + dy + 'px'});
      else 
        this.currentDrag.setStyle({bottom: parseFloat(this.currentDrag.getStyle('bottom')) - dy + 'px'});
    }
    if (this.iefix) 
      this._fixIEOverlapping(); 
      
    this._removeStoreLocation();
    Event.stop(event);
  },

   // endDrag callback
   _endDrag: function(event) {
    // Remove temporary div over iframes
     WindowUtilities.enableScreen('__invisible__');
    
    if (this.doResize)
      Windows.notify("onEndResize", this);
    else
      Windows.notify("onEndMove", this);
    
    // Release event observing
    Event.stopObserving(document, "mouseup", this.eventMouseUp,false);
    Event.stopObserving(document, "mousemove", this.eventMouseMove, false);

    Event.stop(event);
    
    this._hideWiredElement();

    // Store new location/size if need be
    this._saveCookie()
	//add my cookie function here (_endDrag) - sh
    
      
    // Restore selection
    document.body.ondrag = null;
    document.body.onselectstart = null;
  },

  // Creates HTML window code
  _createWindow: function(id) {
    var className = this.options.className;
    var win = document.createElement("div");
    win.setAttribute('id', id);
    win.className = "dialog";

    var content;
    if (this.options.url)
    
      content= "<iframe ALLOWTRANSPARENCY=true  frameborder=\"0\" name=\"" + id + "_content\"  id=\"" + id + "_content\" src=\"" + this.options.url + "\"> </iframe>";
    else
      content ="<div id=\"" + id + "_content\" class=\"" +className + "_content\"> </div>";

    var closeDiv = this.options.closable ? "<div class='"+ className +"_close' id='"+ id +"_close' onclick='Windows.close(\""+ id +"\", event)'> </div>" : "";
    var minDiv = this.options.minimizable ? "<div class='"+ className + "_minimize' id='"+ id +"_minimize' onclick='Windows.minimize(\""+ id +"\", event)'> </div>" : "";
    var maxDiv = this.options.maximizable ? "<div class='"+ className + "_maximize' id='"+ id +"_maximize' onclick='Windows.maximize(\""+ id +"\", event)'> </div>" : "";
    var seAttributes = this.options.resizable ? "class='" + className + "_sizer' id='" + id + "_sizer'" : "class='"  + className + "_se'";
    
    win.innerHTML = closeDiv + minDiv + maxDiv + "\
      <table id='"+ id +"_row1' class=\"top table_window\">\
        <tr>\
          <td class='"+ className +"_nw'>&nbsp;</td>\
          <td class='"+ className +"_n'><div id='"+ id +"_top' class='"+ className +"_title title_window'>"+ this.options.title +"</div></td>\
          <td class='"+ className +"_ne'>&nbsp;</td>\
        </tr>\
      </table>\
      <table id='"+ id +"_row2' class=\"mid table_window\">\
        <tr>\
          <td class='"+ className +"_w'></td>\
            <td id='"+ id +"_table_content' class='"+ className +"_content' valign='top'>" + content + "</td>\
          <td class='"+ className +"_e'></td>\
        </tr>\
      </table>\
        <table id='"+ id +"_row3' class=\"bot table_window\">\
        <tr>\
          <td class='"+ className +"_sw'>&nbsp;</td>\
            <td class='"+ className +"_s'><div id='"+ id +"_bottom' class='status_bar'>&nbsp;</div></td>\
            <td " + seAttributes + ">&nbsp;</td>\
        </tr>\
      </table>\
    ";
    Element.hide(win);
    this.options.parent.insertBefore(win, this.options.parent.firstChild);
    Event.observe($(id + "_content"), "load", this.options.onload);
    return win;
        
  },
  
  // Sets window location
  setLocation: function(top, left) {
    if (top < 0)
      top = 0;
    if (left < 0)
      left= 0
    this.element.setStyle({top: top + 'px'});
    this.element.setStyle({left: left + 'px'});
    this.useLeft = true;
    this.useTop = true;
  },
    
  getLocation: function() {
    var location = {};
    if (this.useTop)
      location = Object.extend(location, {top: this.element.getStyle("top")});
    else
      location = Object.extend(location, {bottom: this.element.getStyle("bottom")});
    if (this.useLeft)
      location = Object.extend(location, {left: this.element.getStyle("left")});
    else
      location = Object.extend(location, {right: this.element.getStyle("right")});
    
    return location;
  },
  
  // Gets window size
  getSize: function() {
    return {width: this.width, height: this.height};
  },
    
  // Sets window size
  setSize: function(width, height) {    
    width = parseFloat(width);
    height = parseFloat(height);
    
    // Check min and max size
    if (width < this.options.minWidth)
      width = this.options.minWidth;

    if (height < this.options.minHeight)
      height = this.options.minHeight;
      
    if (this.options. maxHeight && height > this.options. maxHeight)
      height = this.options. maxHeight;

    if (this.options. maxWidth && width > this.options. maxWidth)
      width = this.options. maxWidth;

    this.width = width;
    this.height = height;
    var e = this.currentDrag ? this.currentDrag : this.element;
    e.setStyle({width: width + this.widthW + this.widthE + "px"})
    e.setStyle({height: height  + this.heightN + this.heightS + "px"})

    // Update content height
    if (!this.currentDrag || this.currentDrag == this.element) {
      var content = $(this.element.id + '_content');
      content.setStyle({height: height  + 'px'});
      content.setStyle({width: width  + 'px'});
    }
  },
  
  updateHeight: function() {
    this.setSize(this.width, this.content.scrollHeight)
  },
  
  updateWidth: function() {
    this.setSize(this.content.scrollWidth, this.height)
  },
  
  // Brings window to front
  toFront: function() {
    if (Windows.focusedWindow == this) 
      return;
    this.setZIndex(Windows.maxZIndex + 20);
    Windows.notify("onFocus", this);
  },
  
  // Displays window modal state or not
  show: function(modal) {
    if (modal) {
      Windows.addModalWindow(this);
      
      this.modal = true;      
      this.setZIndex(Windows.maxZIndex + 20);
      Windows.unsetOverflow(this);
    }
    
    // To restore overflow if need be
    if (this.oldStyle)
      this.getContent().setStyle({overflow: this.oldStyle});
      
    if (! this.width || !this.height) {
      var size = WindowUtilities._computeSize(this.content.innerHTML, this.content.id, this.width, this.height, 0)
      if (this.height)
        this.width = size + 5
      else
        this.height = size + 5
    }

    this.setSize(this.width, this.height);
    if (this.centered)
      this._center(this.centerTop, this.centerLeft);    
      
    if (this.options.showEffect != Element.show && this.options.showEffectOptions )
      this.options.showEffect(this.element, this.options.showEffectOptions);  
    else
      this.options.showEffect(this.element);  
      
    this._checkIEOverlapping();
    this.visible = true;
    WindowUtilities.focusedWindow = this
    Windows.notify("onShow", this);    
  },
  
  // Displays window modal state or not at the center of the page
  showCenter: function(modal, top, left) {
    this.centered = true;
    this.centerTop = top;
    this.centerLeft = left;

    this.show(modal);
  },
  
  isVisible: function() {
    return this.visible;
  },
  
  _center: function(top, left) {
    var windowScroll = WindowUtilities.getWindowScroll();    
    var pageSize = WindowUtilities.getPageSize();    

    if (!top)
      top = (pageSize.windowHeight - (this.height + this.heightN + this.heightS))/2;
    top += windowScroll.top
    
    if (!left)
      left = (pageSize.windowWidth - (this.width + this.widthW + this.widthE))/2;
    left += windowScroll.left 
    
    this.setLocation(top, left);
    this.toFront();
  },
  
  _recenter: function(event) {
    if (this.modal && this.centered) {
      var pageSize = WindowUtilities.getPageSize();
      // Check for this stupid IE that sends dumb events
      if (this.pageSize && this.pageSize.pageWidth == pageSize.windowWidth && this.pageSize.pageHeight == pageSize.windowHeight) 
        return;
      
      this.pageSize = pageSize;
      // set height of Overlay to take up whole page and show
      if ($('overlay_modal')) {
        $('overlay_modal').style.height = (pageSize.pageHeight + 'px');
        $('overlay_modal').style.width = (pageSize.pageWidth + 'px');
      }    
      if (this.options.recenterAuto)
        this._center(this.centerTop, this.centerLeft);    
    }
  },
  
  // Hides window
  hide: function() {
    this.visible = false;
    if (this.modal) {
      Windows.removeModalWindow(this);
      Windows.resetOverflow();
    }
    // To avoid bug on scrolling bar
    this.oldStyle = this.getContent().getStyle('overflow') || "auto"
    this.getContent().setStyle({overflow: "hidden"});

    this.options.hideEffect(this.element, this.options.hideEffectOptions);  

     if(this.iefix) 
      this.iefix.hide();
    Windows.notify("onHide", this);
  },

  minimize: function() {
    var r2 = $(this.getId() + "_row2");
    var dh = r2.getDimensions().height;
    
    if (r2.visible()) {
      var h  = this.element.getHeight() - dh
      r2.hide()
      this.element.setStyle({height: h + "px"})
      if (! this.useTop) {
        var bottom = parseFloat(this.element.getStyle('bottom'));
        this.element.setStyle({bottom: (bottom + dh) + 'px'});
      }
    } 
    else {
      var h  = this.element.getHeight() + dh;
      this.element.setStyle({height: h + "px"})
      if (! this.useTop) {
        var bottom = parseFloat(this.element.getStyle('bottom'));
        this.element.setStyle({bottom: (bottom - dh) + 'px'});
      }
      r2.show();
      
      this.toFront();
    }
    Windows.notify("onMinimize", this);
    
    // Store new location/size if need be
    this._saveCookie()
	//add my cookie function here (minimize) - sh
  },
  
  maximize: function() {
    if (this.storedLocation != null) {
      this._restoreLocation();
      if(this.iefix) 
        this.iefix.hide();
    }
    else {
      this._storeLocation();
      Windows.unsetOverflow(this);
      
      var windowScroll = WindowUtilities.getWindowScroll();
      var pageSize = WindowUtilities.getPageSize();    

      this.element.setStyle(this.useLeft ? {left: windowScroll.left} : {right: windowScroll.left});
      this.element.setStyle(this.useTop ? {top: windowScroll.top} : {bottom: windowScroll.top});

      this.setSize(pageSize.windowWidth - this.widthW - this.widthE, pageSize.windowHeight - this.heightN - this.heightS)
      this.toFront();
      if (this.iefix) 
        this._fixIEOverlapping(); 
    }
    Windows.notify("onMaximize", this);

    // Store new location/size if need be
    this._saveCookie()
	//add my cookie function here (maximize) - sh
	
  },
  
  isMinimized: function() {
    var r2 = $(this.getId() + "_row2");
    return !r2.visible();
  },
  
  isMaximized: function() {
    return (this.storedLocation != null);
  },
  
  setOpacity: function(opacity) {
    if (Element.setOpacity)
      Element.setOpacity(this.element, opacity);
  },
  
  setZIndex: function(zindex) {
    this.element.setStyle({zIndex: zindex});
    Windows.updateZindex(zindex, this);
  },

  setTitle: function(newTitle) {
    if (!newTitle || newTitle == "") 
      newTitle = "&nbsp;";
      
    Element.update(this.element.id + '_top', newTitle);
  },

  setStatusBar: function(element) {
    var statusBar = $(this.getId() + "_bottom");

    if (typeof(element) == "object") {
      if (this.bottombar.firstChild)
        this.bottombar.replaceChild(element, this.bottombar.firstChild);
      else
        this.bottombar.appendChild(element);
    }
    else
      this.bottombar.innerHTML = element;
  },

  _checkIEOverlapping: function() {
    if(!this.iefix && (navigator.appVersion.indexOf('MSIE')>0) && (navigator.userAgent.indexOf('Opera')<0) && (this.element.getStyle('position')=='absolute')) {
        new Insertion.After(this.element.id, '<iframe ALLOWTRANSPARENCY=true id="' + this.element.id + '_iefix" '+ 'style="display:none;position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" ' + 'src="javascript:false;" frameborder="0" scrolling="no"></iframe>');
        this.iefix = $(this.element.id+'_iefix');
    }
    if(this.iefix) 
      setTimeout(this._fixIEOverlapping.bind(this), 50);
  },

  _fixIEOverlapping: function() {
      Position.clone(this.element, this.iefix);
      this.iefix.style.zIndex = this.element.style.zIndex - 1;
      this.iefix.show();
  },
  
  _getWindowBorderSize: function(event) {
    // Hack to get real window border size!!
    var div = this._createHiddenDiv(this.options.className + "_n")
    this.heightN = Element.getDimensions(div).height;    
    div.parentNode.removeChild(div)

    var div = this._createHiddenDiv(this.options.className + "_s")
    this.heightS = Element.getDimensions(div).height;    
    div.parentNode.removeChild(div)

    var div = this._createHiddenDiv(this.options.className + "_e")
    this.widthE = Element.getDimensions(div).width;    
    div.parentNode.removeChild(div)

    var div = this._createHiddenDiv(this.options.className + "_w")
    this.widthW = Element.getDimensions(div).width;
    div.parentNode.removeChild(div);

    // Workaround for IE!!
    if (isIE) {
      this.heightS = $(this.getId() +"_row3").getDimensions().height;
      this.heightN = $(this.getId() +"_row1").getDimensions().height;
    }

    // Safari size fix
    if (/Konqueror|Safari|KHTML/.test(navigator.userAgent))
      this.setSize(this.width, this.height);
    if (this.doMaximize)
      this.maximize();
    if (this.doMinimize)
      this.minimize();
  },
 
  _createHiddenDiv: function(className) {
    var objBody = document.getElementsByTagName("body").item(0);
    var win = document.createElement("div");
    win.setAttribute('id', this.element.id+ "_tmp");
    win.className = className;
    win.style.display = 'none';
    win.innerHTML = '';
    objBody.insertBefore(win, objBody.firstChild);
    return win;
  },
  
  _storeLocation: function() {
    if (this.storedLocation == null) {
      this.storedLocation = {useTop: this.useTop, useLeft: this.useLeft, 
                             top: this.element.getStyle('top'), bottom: this.element.getStyle('bottom'),
                             left: this.element.getStyle('left'), right: this.element.getStyle('right'),
                             width: this.width, height: this.height };
    }
  },
  
  _restoreLocation: function() {
    if (this.storedLocation != null) {
      this.useLeft = this.storedLocation.useLeft;
      this.useTop = this.storedLocation.useTop;
      
      this.element.setStyle(this.useLeft ? {left: this.storedLocation.left} : {right: this.storedLocation.right});
      this.element.setStyle(this.useTop ? {top: this.storedLocation.top} : {bottom: this.storedLocation.bottom});
      this.setSize(this.storedLocation.width, this.storedLocation.height);
      
      Windows.resetOverflow();
      this._removeStoreLocation();
    }
  },
  
  _removeStoreLocation: function() {
    this.storedLocation = null;
  },
  
  _saveCookie: function() {
  //main _saveCookie function here... sh note	  
	
	/*  
    if (this.cookie) {
      var value = "";
      if (this.useLeft)
        value += "l:" +  (this.storedLocation ? this.storedLocation.left : this.element.getStyle('left'))
      else
        value += "r:" + (this.storedLocation ? this.storedLocation.right : this.element.getStyle('right'))
      if (this.useTop)
        value += ",t:" + (this.storedLocation ? this.storedLocation.top : this.element.getStyle('top'))
      else
        value += ",b:" + (this.storedLocation ? this.storedLocation.bottom :this.element.getStyle('bottom'))
        
      value += "," + (this.storedLocation ? this.storedLocation.width : this.width);
      value += "," + (this.storedLocation ? this.storedLocation.height : this.height);
      value += "," + this.isMinimized();
      value += "," + this.isMaximized();
      
	  //debug('_saveCookie: '+value);
	  
      WindowUtilities.setCookie(value, this.cookie)
    }
	*/
	
	//custom cookie function by sh 2/5/7
	/*
	wState = 'reg';
	if(this.isMinimized()) wState = 'min';
	if(this.isMaximized()) wState = 'max';
	ttop = new String (""); 
	tleft = new String (""); 	
	ttop = this.element.getStyle('top');
	tleft = this.element.getStyle('left');
	sttop = ttop.split("px"); 
	stleft = tleft.split("px");
	tpid = new String("");
	tpid = this.element.id;
	stpid = tpid.split("dialog");
	sstpid = null;
	if(stpid[1]) sstpid = (parseInt(stpid[1])+1);
	//debug('zIndex: '+this.element.getStyle('zIndex'))
	setPortletCookie(
		this.title,
		this.url,
		sttop[0],
		stleft[0],
		this.width,
		this.height,
		parseInt(this.element.getStyle('zIndex')),
		sstpid,
		wState
	);
	*/
  },
  
  _createWiredElement: function() {
    if (! this.wiredElement) {
      if (isIE)
        this._getWindowBorderSize();
      var div = document.createElement("div");
      div.className = "wired_frame " + this.options.className + "_wired_frame";
      
      //div.style.display = "none";
      div.style.position = 'absolute';
      document.body.insertBefore(div, document.body.firstChild);
      div = $(div);

      var dim = this.element.getDimensions();
      div.setStyle({width: dim.width + "px", height: dim.height +"px"});

      if (this.useLeft) 
        div.setStyle({left: this.element.getStyle('left')});
      else 
        div.setStyle({right: this.element.getStyle('right')});
        
      if (this.useTop) 
        div.setStyle({top: this.element.getStyle('top')});
      else 
        div.setStyle({bottom: this.element.getStyle('bottom')});
      this.wiredElement = div
    }
    this.wiredElement.setStyle({zIndex: Windows.maxZIndex+30});
    return this.wiredElement;
  },
  
  _hideWiredElement: function() {
    if (this.currentDrag == this.element) 
      this.currentDrag = null;
    else {
      if (this.useLeft) 
        this.element.setStyle({left: this.currentDrag.getStyle('left')});
      else 
        this.element.setStyle({right: this.currentDrag.getStyle('right')});

      if (this.useTop) 
        this.element.setStyle({top: this.currentDrag.getStyle('top')});
      else 
        this.element.setStyle({bottom: this.currentDrag.getStyle('bottom')});

      this.currentDrag.hide();
      this.currentDrag = null;
      this.setSize(this.width, this.height);
    } 
  }
};

// Windows containers, register all page windows
var Windows = {
  windows: [],
  modalWindows: [],
  observers: [],
  focusedWindow: null,
  maxZIndex: 0,

  addObserver: function(observer) {
    this.removeObserver(observer);
    this.observers.push(observer);
  },
  
  removeObserver: function(observer) {  
    this.observers = this.observers.reject( function(o) { return o==observer });
  },
  
  notify: function(eventName, win) {  //  onStartResize(), onEndResize(), onStartMove(), onEndMove(), onClose(), onDestroy(), onMinimize(), onMaximize(), onHide(), onShow(), onFocus()
    this.observers.each( function(o) {if(o[eventName]) o[eventName](eventName, win);});
  },

  // Gets window from its id
  getWindow: function(id) {
    return this.windows.detect(function(d) { return d.getId() ==id });
  },

  // Gets the last focused window
  getFocusedWindow: function() {
    return this.focusedWindow;
  },

  // Registers a new window (called by Windows constructor)
  register: function(win) {
    this.windows.push(win);
  },
    
  // Add a modal window in the stack
  addModalWindow: function(win) {
    // Disable screen if first modal window
    if (this.modalWindows.length == 0)
      WindowUtilities.disableScreen(win.options.className, 'overlay_modal', win.getId());
    else {
      // Move overlay over all windows
      if (Window.keepMultiModalWindow) {
        $('overlay_modal').style.zIndex = Windows.maxZIndex + 20;
        Windows.maxZIndex += 20;
        WindowUtilities._hideSelect(this.modalWindows.last().getId());
      }
      // Hide current modal window
      else
        this.modalWindows.last().element.hide();
      // Fucking IE select issue
      WindowUtilities._showSelect(win.getId());
    }      
    this.modalWindows.push(win);    
  },
  
  removeModalWindow: function(win) {
    this.modalWindows.pop();
    
    // No more modal windows
    if (this.modalWindows.length == 0)
      WindowUtilities.enableScreen();     
    else {
      if (Window.keepMultiModalWindow) {
        this.modalWindows.last().toFront();
        WindowUtilities._showSelect(this.modalWindows.last().getId());        
      }
      else
        this.modalWindows.last().element.show();
    }
  },
  
  // Registers a new window (called by Windows constructor)
  register: function(win) {
    this.windows.push(win);
  },
  
  // Unregisters a window (called by Windows destructor)
  unregister: function(win) {
    this.windows = this.windows.reject(function(d) { return d==win });
  }, 

  // Closes a window with its id
  close: function(id, event) {
  top.debug('window.close: '+id+', '+event);
    var win = this.getWindow(id);
    // Asks delegate if exists
    if (win && win.visible) {
      if (win.getDelegate() && ! win.getDelegate().canClose(win)) 
        return;
      this.focusedWindow = this.windows.length >=2 ? this.windows[this.windows.length-2] : null;
      this.notify("onClose", win);
      win.hide();
    }
    if (event)
      Event.stop(event);
  },
  
  // Closes all windows
  closeAll: function() {  
    this.windows.each( function(w) {Windows.close(w.getId())} );
  },
  
  closeAllModalWindows: function() {
    WindowUtilities.enableScreen();     
    
    this.modalWindows.each( function(win) {win.hide()});    
  },
  // Minimizes a window with its id
  minimize: function(id, event) {
    var win = this.getWindow(id)
    if (win && win.visible)
      win.minimize();
    Event.stop(event);
  },
  
  // Maximizes a window with its id
  maximize: function(id, event) {
    var win = this.getWindow(id)
    if (win && win.visible)
      win.maximize();
    Event.stop(event);
  },
  
  unsetOverflow: function(except) {    
    this.windows.each(function(d) { d.oldOverflow = d.getContent().getStyle("overflow") || "auto" ; d.getContent().setStyle({overflow: "hidden"}) });
    if (except && except.oldOverflow)
      except.getContent().setStyle({overflow: except.oldOverflow});
  },

  resetOverflow: function() {
    this.windows.each(function(d) { if (d.oldOverflow) d.getContent().setStyle({overflow: d.oldOverflow}) });
  },

  updateZindex: function(zindex, win) {
    if (zindex > this.maxZIndex)
      this.maxZIndex = zindex;
    this.focusedWindow = win;
  }
};

var Dialog = {
  dialogId: null,
  onCompleteFunc: null,
  callFunc: null, 
  parameters: null, 
    
  confirm: function(content, parameters) {
    // Get Ajax return before
    if (typeof content != "string") {
      Dialog._runAjaxRequest(content, parameters, Dialog.confirm);
      return 
    }
    
    parameters = parameters || {};
    var okLabel = parameters.okLabel ? parameters.okLabel : "Ok";
    var cancelLabel = parameters.cancelLabel ? parameters.cancelLabel : "Cancel";

    var windowParam = parameters.windowParameters || {};
    windowParam.className = windowParam.className || "alert";

    var okButtonClass = "class ='" + (parameters.buttonClass ? parameters.buttonClass + " " : "") + " ok_button'" 
    var cancelButtonClass = "class ='" + (parameters.buttonClass ? parameters.buttonClass + " " : "") + " cancel_button'" 
    var content = "\
      <div class='" + windowParam.className + "_message'>" + content  + "</div>\
        <div class='" + windowParam.className + "_buttons'>\
          <input type='button' value='" + okLabel + "' onclick='Dialog.okCallback()'" + okButtonClass + "/>\
          <input type='button' value='" + cancelLabel + "' onclick='Dialog.cancelCallback()' " + cancelButtonClass + "/>\
        </div>\
    ";
    return this._openDialog(content, parameters)
  },
  
  alert: function(content, parameters) {
    // Get Ajax return before
    if (typeof content != "string") {
      Dialog._runAjaxRequest(content, parameters, Dialog.alert);
      return 
    }
    
    parameters = parameters || {};
    var okLabel = parameters.okLabel ? parameters.okLabel : "Ok";

    var windowParam = parameters.windowParameters || {};
    windowParam.className = windowParam.className || "alert";

    var okButtonClass = "class ='" + (parameters.buttonClass ? parameters.buttonClass + " " : "") + " ok_button'" 
    var content = "\
      <div class='" + windowParam.className + "_message'>" + content  + "</div>\
        <div class='" + windowParam.className + "_buttons'>\
          <input type='button' value='" + okLabel + "' onclick='Dialog.okCallback()'" + okButtonClass + "/>\
        </div>";
    return this._openDialog(content, parameters)
  },
  
  info: function(content, parameters) {   
    // Get Ajax return before
    if (typeof content != "string") {
      Dialog._runAjaxRequest(content, parameters, Dialog.info);
      return 
    }
     
    parameters = parameters || {};
    parameters.windowParameters = parameters.windowParameters || {};
    
    var className = parameters.windowParameters.className || "alert";
  
    //alert("Values of parameters "  + parameters);
       
    //alert("What is the value of parameters.showProgress ???? " + parameters.showProgress );
   
  
    var content = "<div id='modal_dialog_message' class='" + className + "_message'>" + content  + "</div>";
    if (parameters.showProgress)
      content += "<div id='modal_dialog_progress' class='" + className + "_progress'>  </div>";

    parameters.windowParameters.ok = null;
    parameters.windowParameters.cancel = null;
    parameters.windowParameters.className = className;
    
    return this._openDialog(content, parameters)
  },
  
  setInfoMessage: function(message) {
    $('modal_dialog_message').update(message);
  },
  
  closeInfo: function() {
    Windows.close(this.dialogId);
  },
  
  _openDialog: function(content, parameters) {
    if (! parameters.windowParameters.height && ! parameters.windowParameters.width) {
      parameters.windowParameters.width = WindowUtilities.getPageSize().pageWidth / 2;
    }
    if (parameters.id)
      this.dialogId = parameters.id;
    else { 
      var t = new Date();
      this.dialogId = 'modal_dialog_' + t.getTime();
    }

    // compute height or width if need be
    if (! parameters.windowParameters.height || ! parameters.windowParameters.width) {
      var size = WindowUtilities._computeSize(content, this.dialogId, parameters.windowParameters.width, parameters.windowParameters.height)
      if (parameters.windowParameters.height)
        parameters.windowParameters.width = size + 5
      else
        parameters.windowParameters.height = size + 5
    }
    var windowParam = parameters && parameters.windowParameters ? parameters.windowParameters : {};
    windowParam.resizable = windowParam.resizable || false;
    
    windowParam.effectOptions = windowParam.effectOptions || {duration: 1};
    windowParam.minimizable = false;
    windowParam.maximizable = false;
    windowParam.closable = false;
    
    var win = new Window(this.dialogId, windowParam);
    win.getContent().innerHTML = content;
    win.showCenter(true, parameters.top, parameters.left);  
    win.setDestroyOnClose();
    
    win.cancelCallback = parameters.cancel;
    win.okCallback = parameters.ok;
    
    return win;    
  },
  
  _getAjaxContent: function(originalRequest)  {
      Dialog.callFunc(originalRequest.responseText, Dialog.parameters)
  },
  
  _runAjaxRequest: function(message, parameters, callFunc) {
    if (message.options == null)
      message.options ={}  
    Dialog.onCompleteFunc = message.options.onComplete;
    Dialog.parameters = parameters;
    Dialog.callFunc = callFunc;
    
    message.options.onComplete = Dialog._getAjaxContent;
    new Ajax.Request(message.url, message.options);
  },
  
  okCallback: function() {
    var win = Windows.focusedWindow;
    if (!win.okCallback || win.okCallback(win)) {
      // Remove onclick on button
      $$("#" + win.getId()+" input").each(function(element) {element.onclick=null;})
      win.hide();
    }
  },

  cancelCallback: function() {
    var win = Windows.focusedWindow;
    // Remove onclick on button
    $$("#" + win.getId()+" input").each(function(element) {element.onclick=null})
    win.hide();
    if (win.cancelCallback)
      win.cancelCallback(win);
  }
}
/*
  Based on Lightbox JS: Fullsize Image Overlays 
  by Lokesh Dhakar - http://www.huddletogether.com

  For more information on this script, visit:
  http://huddletogether.com/projects/lightbox/

  Licensed under the Creative Commons Attribution 2.5 License - http://creativecommons.org/licenses/by/2.5/
  (basically, do anything you want, just leave my name and link)
*/

var isIE = navigator.appVersion.match(/MSIE/) == "MSIE";

var WindowUtilities = {
  // From script.aculo.us
  getWindowScroll: function() {
    var w = window;
      var T, L, W, H;
      with (w.document) {
        if (w.document.documentElement && documentElement.scrollTop) {
          T = documentElement.scrollTop;
          L = documentElement.scrollLeft;
        } else if (w.document.body) {
          T = body.scrollTop;
          L = body.scrollLeft;
        }
        if (w.innerWidth) {
          W = w.innerWidth;
          H = w.innerHeight;
        } else if (w.document.documentElement && documentElement.clientWidth) {
          W = documentElement.clientWidth;
          H = documentElement.clientHeight;
        } else {
          W = body.offsetWidth;
          H = body.offsetHeight
        }
      }
      return { top: T, left: L, width: W, height: H };
    
  }, 
  //
  // getPageSize()
  // Returns array with page width, height and window width, height
  // Core code from - quirksmode.org
  // Edit for Firefox by pHaez
  //
  getPageSize: function(){
    var xScroll, yScroll;

    if (window.innerHeight && window.scrollMaxY) {  
      xScroll = document.body.scrollWidth;
      yScroll = window.innerHeight + window.scrollMaxY;
    } else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
      xScroll = document.body.scrollWidth;
      yScroll = document.body.scrollHeight;
    } else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
      xScroll = document.body.offsetWidth;
      yScroll = document.body.offsetHeight;
    }

    var windowWidth, windowHeight;

    if (self.innerHeight) {  // all except Explorer
      windowWidth = self.innerWidth;
      windowHeight = self.innerHeight;
    } else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
      windowWidth = document.documentElement.clientWidth;
      windowHeight = document.documentElement.clientHeight;
    } else if (document.body) { // other Explorers
      windowWidth = document.body.clientWidth;
      windowHeight = document.body.clientHeight;
    }  
    var pageHeight, pageWidth;

    // for small pages with total height less then height of the viewport
    if(yScroll < windowHeight){
      pageHeight = windowHeight;
    } else { 
      pageHeight = yScroll;
    }

    // for small pages with total width less then width of the viewport
    if(xScroll < windowWidth){  
      pageWidth = windowWidth;
    } else {
      pageWidth = xScroll;
    }

    return {pageWidth: pageWidth ,pageHeight: pageHeight , windowWidth: windowWidth, windowHeight: windowHeight};
  },

   disableScreen: function(className, overlayId, contentId) {
    WindowUtilities.initLightbox(overlayId, className);
    var objBody = document.getElementsByTagName("body").item(0);

    // prep objects
     var objOverlay = $(overlayId);

    var pageSize = WindowUtilities.getPageSize();

    // Hide select boxes as they will 'peek' through the image in IE, store old value
    if (contentId && isIE) {
      WindowUtilities._hideSelect();
      WindowUtilities._showSelect(contentId);
    }  
  
    // set height of Overlay to take up whole page and show
    objOverlay.style.height = (pageSize.pageHeight + 'px');
    objOverlay.style.width = (pageSize.windowWidth + 'px');
    objOverlay.style.display = 'block';  
  },

   enableScreen: function(id) {
     id = id || 'overlay_modal';
     var objOverlay =  $(id);
    if (objOverlay) {
      // hide lightbox and overlay
      objOverlay.style.display = 'none';

      // make select boxes visible using old value
      if (id != "__invisible__") 
        WindowUtilities._showSelect();
      
      objOverlay.parentNode.removeChild(objOverlay);
    }
  },

  _hideSelect: function(id) {
    if (isIE) {
      id = id ==  null ? "" : "#" + id + " ";
      $$(id + 'select').each(function(element) {
        if (! WindowUtilities.isDefined(element.oldVisibility)) {
          element.oldVisibility = element.style.visibility ? element.style.visibility : "visible";
          element.style.visibility = "hidden";
        }
      });
    }
  },
  
  _showSelect: function(id) {
    if (isIE) {
      id = id ==  null ? "" : "#" + id + " ";
      $$(id + 'select').each(function(element) {
        if (WindowUtilities.isDefined(element.oldVisibility)) {
          // Why?? Ask IE
          try {
            element.style.visibility = element.oldVisibility;
          } catch(e) {
            element.style.visibility = "visible";
          }
          element.oldVisibility = null;
        }
        else {
          if (element.style.visibility)
            element.style.visibility = "visible";
        }
      });
    }
  },

  isDefined: function(object) {
    return typeof(object) != "undefined" && object != null;
  },
  
  // initLightbox()
  // Function runs on window load, going through link tags looking for rel="lightbox".
  // These links receive onclick events that enable the lightbox display for their targets.
  // The function also inserts html markup at the top of the page which will be used as a
  // container for the overlay pattern and the inline image.
  initLightbox: function(id, className) {
    // Already done, just update zIndex
    if ($(id)) {
      Element.setStyle(id, {zIndex: Windows.maxZIndex + 10});
    }
    // create overlay div and hardcode some functional styles (aesthetic styles are in CSS file)
    else {
      var objBody = document.getElementsByTagName("body").item(0);
      var objOverlay = document.createElement("div");
      objOverlay.setAttribute('id', id);
      objOverlay.className = "overlay_" + className
      objOverlay.style.display = 'none';
      objOverlay.style.position = 'absolute';
      objOverlay.style.top = '0';
      objOverlay.style.left = '0';
      objOverlay.style.zIndex = Windows.maxZIndex + 10;
       objOverlay.style.width = '100%';
      objBody.insertBefore(objOverlay, objBody.firstChild);
    }
  },
  
  setCookie: function(value, parameters) {
    document.cookie= parameters[0] + "=" + escape(value) +
      ((parameters[1]) ? "; expires=" + parameters[1].toGMTString() : "") +
      ((parameters[2]) ? "; path=" + parameters[2] : "") +
      ((parameters[3]) ? "; domain=" + parameters[3] : "") +
      ((parameters[4]) ? "; secure" : "");
  },

  getCookie: function(name) {
  
  //debug("getCookie(): "+name);
  
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1) {
      begin = dc.indexOf(prefix);
      if (begin != 0) return null;
    } else {
      begin += 2;
    }
    var end = document.cookie.indexOf(";", begin);
    if (end == -1) {
      end = dc.length;
    }
    return unescape(dc.substring(begin + prefix.length, end));
  },
    
  _computeSize: function(content, id, width, height, margin) {
    if (margin == null)
      margin = 5;

    var objBody = document.getElementsByTagName("body").item(0);
    var tmpObj = document.createElement("div");
    tmpObj.setAttribute('id', id);
  
    if (height)
      tmpObj.style.height = height + "px"
    else
      tmpObj.style.width = width + "px"
  
    tmpObj.style.position = 'absolute';
    tmpObj.style.top = '0';
    tmpObj.style.left = '0';
    tmpObj.style.display = 'none';

    tmpObj.innerHTML = content;
    objBody.insertBefore(tmpObj, objBody.firstChild);
    
    var size;
    if (height)
      size = $(id).getDimensions().width + margin;
    else
      size = $(id).getDimensions().height + margin;
      
    objBody.removeChild(tmpObj);
    return size;
  }  
}


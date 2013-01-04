// Copyright (c) 2006 Gabriel Lanzani (http://www.glanzani.com.ar)
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
// SEE CHANGELOG FOR A COMPLETE CHANGES OVERVIEW
// VERSION 0.2

Autocompleter.SelectBox = Class.create();
Autocompleter.SelectBox.prototype = Object.extend(new Autocompleter.Base(), {
  initialize: function(select, options) {
	this.element = document.createElement("input");
	this.element.type = "text";
	this.element.id = $(select).id + "_combo";
    Element.addClassName(this.element, 'combo');
	$(select).parentNode.appendChild(this.element);
  	
	this.update = document.createElement("div");
	this.update.id = $(select).id + "_options";
	Element.addClassName(this.update, 'autocomplete');
	$(select).parentNode.appendChild(this.update);
			
    this.baseInitialize(this.element, this.update, options);
    this.select = select;
	this.selectOptions = [];
			
	this.element.setAttribute('readonly','readonly');
	this.element.readOnly = true;
	if(this.options.debug)alert('input ' + this.element.id + ' and div '+ this.update.id + ' created, Autocompleter.Base() initialized');
	if(!this.options.debug)Element.hide(select);

	var optionList = $(this.select).getElementsByTagName('option');
	var nodes = $A(optionList);

	for(i=0; i<nodes.length;i++){
		this.selectOptions.push("<li id=\"" + nodes[i].value + "\">" + nodes[i].innerHTML + '</li>');
		if (nodes[i].getAttribute("selected")) this.element.value = nodes[i].innerHTML;
		if(this.options.debug)alert('option ' + nodes[i].innerHTML + ' added to '+ this.update.id);
	}
	
	Event.observe(this.element, "click", this.activate.bindAsEventListener(this));
	
	//Element.setStyle(this.update,{width : this.options.width + 'px'});	
	
	this.options.afterUpdateElement = function(text, li) {
		var optionList = $(select).getElementsByTagName('option');
		var nodes = $A(optionList);

		var opt = nodes.find( function(node){
			return (node.value == li.id);
		});
		$(select).selectedIndex=opt.index;
			//debug($(select).options[$(select).selectedIndex].value);
			if($(select).options[$(select).selectedIndex].value>0){
				closeAll();
				setTimeout("callAjax('retrieve',"+$(select).options[$(select).selectedIndex].value+")",1000);
				//callAjax('retrieve',$(select).options[$(select).selectedIndex].value);
			}
	}
  },

  getUpdatedChoices: function() {
  		this.updateChoices(this.setValues());
  },

  setValues : function(){
		return ("<ul>" + this.selectOptions.join('') + "</ul>");
  },
  
  setOptions: function(options) {
    this.options = Object.extend({
		//MORE OPTIONS TO EXTEND THIS CLASS
		inputClass	: 'combo',
		url			: '',
		width		: 0,
		debug		: false,
		autoSubmit	: false,
		inputClass	: ''	
	}, options || {});
  }
});
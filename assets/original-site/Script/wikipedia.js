//
//  wikipedia.js
//  ExplorableExplanations
//
//  Created by Bret Victor on 3/14/11.
//  (c) 2011 Bret Victor.  MIT open-source license.
//
var gPages;

(function () {

var isSearching = false;
var searchTerm = "";
var hoverSpan = null;

var container;
var searchBox;
var wikiBox;

var requestTimer;
var request;
var expandUrl;


//----------------------------------------------------------
//
//  init
//

initWikipediaExample = function () {
	container = $("wikipediaExample");
	wrapWordsInSpans();
	$(document).addEvent("keydown", keyDidGoDown);
}


//----------------------------------------------------------
//
//  which word is pointed at?
//

function wrapWordsInSpans () {
	container.getChildren().each( function (el) {
		var text = el.get("text");
		var words = text.split(/\s+/);
		var spans = words.map( function (word) { return "<span>" + word + "</span>"; });
		var html = spans.join(" ");
		el.set("html", html);
		
		el.getChildren().each( function (span) {
			span.addEvent("mouseenter", mouseDidEnterSpan);
			span.addEvent("mouseleave", mouseDidLeaveSpan);
		});
	});
}

function mouseDidEnterSpan (event) {
	hoverSpan = event.target;
}

function mouseDidLeaveSpan (event) {
	if (hoverSpan === event.target) { hoverSpan = null; }
}


//----------------------------------------------------------
//
//  trigger
//

function keyDidGoDown (event) {
	if (isSearching) {
    	if (event.key == "esc") { stopSearching(); event.stop(); }
    	return;
    }

    var shouldTrigger = (event.key == "w") && !event.shift && !event.control && !event.alt && !event.meta;
    if (!shouldTrigger) { return; }
    
    if (!hoverSpan) { return; }

	event.stop();
	addSearchBoxWithSpan(hoverSpan);
}


//----------------------------------------------------------
//
//  search box
//

function addSearchBoxWithSpan (span) {
	var position = span.getPosition(container);
	var string = span.get("text");

	string = string.replace(/\W+$/, "").replace(/\W+$/, "").replace(/\'s$/, "");  // get rid of leading and trailing punctuation
    if (!string.match(/\w/)) { return; }  // ignore if there are no letters left

	isSearching = true;
	if (searchBox) { searchBox.destroy(); searchBox = null; }

	var useSearchStyle = Browser.Platform.mac && (Browser.safari || Browser.chrome);
	if (useSearchStyle) {
		position.y -= 3;
		position.x -= 11;
	}
	else {
		position.y -= 2;
		position.x -= 2;
	}

	searchBox = new Element("input", {
		type:useSearchStyle ? "search" : "text", 
		value:string + " ",
		"class":"wikipediaSearchBox", 
		size:string.length + 15,
		style:"left:" + position.x + "px; top:" + position.y + "px;"
	});
	searchBox.addEvent("blur", stopSearching);
	searchBox.addEvent("keyup", function (event) {
		if (event.key == "esc") { stopSearching(); return; }
		searchBoxDidChange(true);
	});

	container.grab(searchBox, "bottom");
	searchBox.focus();
	
	searchBoxDidChange(false);
}

function searchBoxDidChange (shouldDelay) {
	var term = searchBox.get("value");
	if (searchTerm == term) { return; }
	searchTerm = term;

	performSearchRequest(searchTerm,shouldDelay);
}


//----------------------------------------------------------
//
//  request via XMLHTTPRequest
//

function performSearchRequest (searchTerm, shouldDelay)  {
	if (requestTimer) { clearTimeout(requestTimer); requestTimer = null; }
	if (request) { request.cancel(); request = null; }
	
	requestTimer = (function () {
		requestTimer = null;
		if (request) { request.cancel(); request = null; }

		if (!searchTerm) { return ; }
		var query = escape(searchTerm);

		var search_url = "https://api.wikimedia.org/core/v1/wikipedia/en/search/page?limit=1&origin=*&q=" + query;
		var search_xhr = new XMLHttpRequest();
		search_xhr.open('GET', search_url, true);
		search_xhr.onload = function() {
			var search_result = JSON.parse(this.response);
			var page = search_result.pages[0];
			var title = page.title;

			var text_url = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&exintro&explaintext&origin=*&titles=" + page.key;
			var text_xhr = new XMLHttpRequest();
			text_xhr.open('GET', text_url, true);
			text_xhr.onload = function() {
				var text_result = JSON.parse(this.response);
				var text = Object.values(text_result.query.pages)[0].extract;
				
				addWikiBoxWithHTML(text, title)
			}
			text_xhr.send();
		};
		search_xhr.send();
	}).delay(shouldDelay ? 250 : 0);
}


//----------------------------------------------------------
//
//  wiki box
//

function addWikiBoxWithHTML (html,title) {
	if (!searchBox) { return; }
	var position = searchBox.getPosition($("everything"));
	position.x -= 2;
	position.y += 24;

	if (wikiBox == null) {
		wikiBox = new Element("div", { "class":"wikipediaWikiBox" });
		var headerBox = new Element("div", { "class":"wikipediaWikiBoxHeader" }).inject(wikiBox, "bottom");
		var contentBox = new Element("div", { "class":"wikipediaWikiBoxContent" }).inject(wikiBox, "bottom");
		$("everything").grab(wikiBox, "bottom");
	}

	wikiBox.setStyles({ left:position.x, top:position.y });
	updateWikiBoxWithHTML(html,title);
}

function updateWikiBoxWithHTML (html,title) {	
	var headerBox = wikiBox.getElement(".wikipediaWikiBoxHeader");
	headerBox.set("text",title);
	
	var contentBox = wikiBox.getElement(".wikipediaWikiBoxContent");
	contentBox.set("html", html);
}


//----------------------------------------------------------
//
//  stop
//

function stopSearching () {
	if (searchBox) { searchBox.destroy(); searchBox = null; }
	if (wikiBox) { wikiBox.destroy(); wikiBox = null; }
	if (requestTimer) { clearTimeout(requestTimer); requestTimer = null; }
	if (request) { request.cancel(); request = null; }
	if (request) { request.cancel(); request = null; }
	
	isSearching = false;
	searchTerm = "";
}

	
//----------------------------------------------------------

})();



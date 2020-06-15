$(function(){
	// Bind events.
	// $('#createNewUnit').click(showDialogCreateNewUnit);
	$('#sendCreateMapUnit').click(sendCreateMapUnit);
	
	// Init Map-units
	initMapUnits();
	
	var map = new ol.Map({
		target: 'content',
		layers: [
			new ol.layer.Tile({
				source: new ol.source.OSM()
			})
		],
		view: new ol.View({
			center: ol.proj.fromLonLat([37.41, 8.82]),
			zoom: 4
		})
	});
$(window).on('hashchange', windowOnHashChange);

if (window.location.hash) {
    $(window).trigger('hashchange')
}});

function initMapUnits() {
	$.ajax('/api/map-units',
			{
				method : 'GET'
			})
		.done(function(data){
			for (const p of data) {
				$('<div />', {}).appendTo('#sidebar')
					.append($('<a />', {text : p.name, href : '#' + p.id}));
			}
		});
}//sidebar

function showDialogCreateNewUnit() {
	var name = prompt('New name?');
	if (name != null && name != '') {
		$.ajax('/api/map-units',
			{
				async : false,
				data : { name : name },
				method : 'POST'
			})
		.done(function(data){
			alert(data);
		});
	}
}

function sendCreateMapUnit() {
	$('#form-post-map').submit();
	$('#dialogCreateMapUnit').modal('hide');
}

function parseParms(str) {
    var pieces = str.split("&"), data = {}, i, parts;
    // process each query pair
    for (i = 0; i < pieces.length; i++) {
        parts = pieces[i].split("=");
        if (parts.length < 2) {
            parts.push("");
        }
        data[decodeURIComponent(parts[0])] = decodeURIComponent(parts[1]);
    }
    return data;
}

function windowOnHashChange(e) {
    alert(parseParms(location.hash.substring(1)));
}

function parseParms(str) {
    var pieces = str.split("&"), data = {}, i, parts;
    // process each query pair
    for (i = 0; i < pieces.length; i++) {
        parts = pieces[i].split("=");
        if (parts.length < 2) {
            parts.push("");
        }
        data[decodeURIComponent(parts[0])] = decodeURIComponent(parts[1]);
    }
    return data;
}
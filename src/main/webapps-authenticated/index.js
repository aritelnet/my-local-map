var map;
$(function(){
	// Bind events.
	// $('#createNewUnit').click(showDialogCreateNewUnit);
	$('#sendCreateMapUnit').click(sendCreateMapUnit);
	
	// Init Map-units
	initMapUnits();
	
	map = new ol.Map({
		target: 'map',
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
				$('<div />', {class : 'row'}).appendTo('#sidebar')
					.append($('<a />', {class: 'bd-toc-link', text : p.name, href : '#id=' + p.id}));
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

var source;
function windowOnHashChange(e) {
    var param = parseParms(location.hash.substring(1));
    var layers = map.getLayers();
    layers.forEach(function(e,i,a){
    	map.removeLayer(e);
    });
    if (param.id){
    	var extent = [0, 0, 1024, 968];
    	var projection = new ol.proj.Projection({
		  code: 'xkcd-image',
		  units: 'pixels',
		  extent: extent
		});
	    map.addLayer(new ol.layer.Image({
	    	source : new ol.source.ImageStatic({
	    		url : '/api/map-units/' + param.id + '/image',
	    		projection: projection,
	    		imageExtent: extent
	    	})
	    }));
	    source = new ol.source.Vector({wrapX: false});
	    map.addLayer(new ol.layer.Vector({
  source: source
}));
		resetInteraction(source);
	    map.setView(new ol.View({
		    projection: projection,
		    center: ol.extent.getCenter(extent),
		    zoom: 2,
		    maxZoom: 8
		  }));
	}
}

var draw; // global so we can remove it later
function resetInteraction(source) {
  if (draw) {
    map.removeInteraction(draw);
  }
    draw = new ol.interaction.Draw({
      source: source,
      type: 'LineString'
    });
    map.addInteraction(draw);
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
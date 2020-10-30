<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ePark</title>
<style>
#map {
	position: absolute;
	top: 50px;
	left: 0;
	bottom: 0;
	right: 0;
}
</style>
<script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script>

</head>
<body>

	<a href="/logout">Logout</a>

	<div id="map"></div>
	<script>
		function initMap() {
			var map = new google.maps.Map(document.getElementById("map"), {
				center : {
					lat : -34.397,
					lng : 150.644
				},
				zoom : 7,
			});
		}
	</script>
	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAqmLfX-y8ALh9UU0d48ddAKL3WNpK7ti4&callback=initMap"
		async defer></script>


</body>
</html>
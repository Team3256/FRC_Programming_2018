
var ctx;
var ftToPixelsScale = 14;
var fieldWidth = 74; //in feet
var fieldHeight = 30; //in feet
var width = fieldWidth * ftToPixelsScale; //in pixels
var height = fieldHeight * ftToPixelsScale; //in pixels
var robotWidth = 40/12; //in feet
var robotHeight = 35/12; //in feet
var pointRadius = 5; //in pixels
var kEpsilon = 1E-9; 
var image;
var imageFlipped;
var points = [];

class Point {
	constructor(x, y, vel, desc) {
		this.x = x;
		this.y = y;
		this.vel = vel;
		this.desc = desc;
	}

	norm() {
		return Math.sqrt(Translation2d.dot(this, this));
	}

	scale(s) {
		return new Translation2d(this.x * s, this.y * s);
	}

	translate(t) {
		return new Translation2d(this.x + t.x, this.y + t.y);
	}

	invert() {
		return new Translation2d(-this.x, -this.y);
	}

	perp() {
		return new Translation2d(-this.y, this.x);
	}

    getX() {
		return this.x*ftToPixelsScale;
	}

	getY() {
		return height - this.y*ftToPixelsScale;
	}

	getAngle() {
		return Math.atan2(-this.y, this.x);
	}

	draw(color) {
		color = color || "#f72c1c";
		ctx.beginPath();
		ctx.arc(this.getX(), this.getY(), pointRadius, 0, 2*Math.PI, false);
		ctx.fillStyle = color;
		ctx.strokeStyle = color;
		ctx.fill();
		ctx.lineWidth = 0;
		ctx.stroke();
	}



	static diff(a, b) {
		return new Translation2d(b.x - a.x, b.y - a.y);
	}

	static cross(a, b) {
		return a.x * b.y - a.y * b.x;
	}

	static dot(a, b) {
		return a.x * b.x + a.y * b.y;
	}

	static angle(a, b) {
		return Math.acos(Translation2d.dot(a,b) / (a.norm() * b.norm()));
	}
}

class Line {

}

class Arc {

}

function addPoint() {
    prevPoint = points[points.length-1];
	$('tbody').append('<tr>'
		+'<td><input class="x" value="'+(parseInt(prevPoint.x)+5)+'"></td>'
		+'<td><input class="y" value="'+(parseInt(prevPoint.y)+5)+'"></td>'
		+'<td><input class="vel" value="10"></td>'
		+'<td><input class="desc" placeholder="Description"></td>'
		+'<td><button class="delete">Delete</button></td>'
		+'</tr>'
		);
	addChanger();
    update();
}

function addChanger() {
    $($('tbody').children('tr')[points.length]).find('td').on('input', function(e) {
        update();
    });
}

function update() {
    ctx.clearRect(0, 0, width, height);
    ctx.drawImage(image, 0, 0, width, height);
    points = [];
    $('tbody').children('tr').each(function() {
        var row = $(this);
        var x = $(row.find('.x')).val();
        var y = $(row.find('.y')).val();
        var vel = $(row.find('.vel')).val();
        var desc = $(row.find('.desc')).val();
        points.push(new Point(x, y, vel, desc));
    })
    for (var point in points) {
        points[point].draw("#f72c1c");
    }

}

function init() {
	$('#field').css("width", width);
	ctx = document.getElementById('field').getContext('2d');
    ctx.canvas.width = width;
    ctx.canvas.height = height;
    ctx.clearRect(0, 0, width, height);
    ctx.fillStyle="#FF0000";
    image = new Image();
    image.src = 'PowerUpField.PNG';
    image.onload = function(){
        ctx.drawImage(image, 0, 0, width, height);
        update();
    }
	addChanger();
}
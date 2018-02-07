
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
	constructor(x, y, vel, radius, desc) {
		this.x = x;
		this.y = y;
		this.vel = vel;
		this.radius = radius;
		this.desc = desc;
	}

	norm() {
		return Math.sqrt(Point.dot(this, this));
	}

	scale(s) {
		return new Point(this.x * s, this.y * s);
	}

	translate(t) {
		return new Point(this.x + t.x, this.y + t.y);
	}

	invert() {
		return new Point(-this.x, -this.y);
	}

	perp() {
		return new Point(-this.y, this.x);
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

	draw() {
		var color = "#f72c1c";
		ctx.beginPath();
		ctx.arc(this.getX(), this.getY(), pointRadius, 0, 2*Math.PI, false);
		ctx.fillStyle = color;
		ctx.strokeStyle = color;
		ctx.fill();
		ctx.lineWidth = 0;
		ctx.stroke();
	}



	static diff(a, b) {
		return new Point(b.x - a.x, b.y - a.y);
	}

	static cross(a, b) {
		return a.x * b.y - a.y * b.x;
	}

	static dot(a, b) {
		return a.x * b.x + a.y * b.y;
	}

	static angle(a, b) {
		return Math.acos(Point.dot(a,b) / (a.norm() * b.norm()));
	}
}

class Line {
    constructor(pointA, pointB) {
        this.start = pointA;
		this.end = pointB;
		this.slope = Point.diff(pointA, pointB);
    }

    draw() {
		var color = "#2dc65b";
        ctx.beginPath();
        ctx.moveTo(this.start.getX(), this.start.getY());
        ctx.lineTo(this.end.getX(), this.end.getY());
        ctx.strokeStyle = color;
		ctx.lineWidth = 2;
        ctx.stroke();
    }
}

class Arc {

}

function deletePoint(index) {
    $('tr')[index].remove();
}

function addPoint() {
    prevPoint = points[points.length-1];
	$('tbody').append('<tr>'
		+'<td class="hoverable"><input class="x number_cell" value="'+(parseInt(prevPoint.x)+5)+'"></td>'
		+'<td class="hoverable"><input class="y number_cell" value="'+(parseInt(prevPoint.y)+5)+'"></td>'
		+'<td class="hoverable"><input class="vel number_cell" value="10"></td>'
		+'<td class="hoverable"><input class="radius number_cell" value="0"></td>'
		+'<td class="hoverable"><input class="desc" placeholder="Description"></td>'
		+'<td><button class="delete">Delete</button></td>'
		+'</tr>'
		);
	addEventListeners();
    update();
}

function fitSizeToText(input) {
    var maxWidth = input.width() - 2;
    var val = input.val();
    if (val == 0) {return};
    $('#text_width_calc').text(val);
    var textWidth = $('#text_width_calc').width();
    var width = textWidth < maxWidth ? textWidth : maxWidth;
    input.css('width', width);
}

function addEventListeners() {
    row = $($('tbody').children('tr')[points.length]);

    row.find('td').keyup(function() {
        update();
    });

    row.find('.desc').focus(function() {
        $(this).css('width','100%');
    }).focusout(function() {
        fitSizeToText($(this));
    });

    row.find('.delete').click(function() {
        $(this).parent().parent().remove();
        update();
    });

    row.find('.hoverable').hover(
        function() {
            $(this).find('.hoverable').css("background-color", "#f5f5f5");
        },
        function() {
            $(this).find('.hoverable').css("background-color", "white");
        }
    )
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
        var radius = $(row.find('.radius')).val();
        points.push(new Point(x, y, vel, radius, desc));
    })
    for (var point in points) {
        points[point].draw();
        if (point > 0) {
            var line = new Line(points[point-1], points[point]);
            line.draw();
<<<<<<< Updated upstream
=======
            } else {

            }
>>>>>>> Stashed changes
        }
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
	addEventListeners();
}
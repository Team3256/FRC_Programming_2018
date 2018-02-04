var ftToPixelsScale = 14;

var fieldWidth = 74; //in feet
var fieldHeight = 30; //in feet
var robotWidth = 40/12; //in feet
var robotHeight = 35/12; //in feet
var width = fieldWidth * ftToPixelsScale; //in pixels
var height = fieldHeight * ftToPixelsScale; //in pixels
var pointRadius = 4; //in pixels

var pointRadius = 5; //in pixels

var ctx;
var image;
var imageFlipped;
var isFlipped = false;

var startPositions = {
    "center": [10, 14],
    "left": [10, 24],
    "right": [10, 6],
}


var kEpsilon = 1E-9;
var waypoints = [];

function chooseStart(position) {
    startPos = startPositions[position];
    startX = startPos[0];
    startY = startPos[1];
    waypoints.push(new WayPoint(startX, startY, 0, 0, ""));
    deletePoint(1);
    addPoint(startX,startY);
    $($('tbody').children('tr')[0]).find('.x').val(startX);
    $($('tbody').children('tr')[0]).find('.y').val(startY);
    addPoint(startX, startY);
    update();
}



class Translation {
	constructor(x, y) {
		this.x = x;
		this.y = y;
	}

	norm() {
		return Math.sqrt(Translation.dot(this, this));
	}

	scale(s) {
		return new Translation(this.x * s, this.y * s);
	}

	translate(t) {
		return new Translation(this.x + t.x, this.y + t.y);
	}

	invert() {
		return new Translation(-this.x, -this.y);
	}

	perp() {
		return new Translation(-this.y, this.x);
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
		var color = color || "#f72c1c";
		ctx.beginPath();
		ctx.arc(this.getX(), this.getY(), pointRadius, 0, 2*Math.PI, false);
		ctx.fillStyle = color;
		ctx.strokeStyle = color;
		ctx.fill();
		ctx.lineWidth = 0;
		ctx.stroke();
	}

	static diff(a, b) {
		return new Translation(b.x - a.x, b.y - a.y);
	}

	static cross(a, b) {
		return a.x * b.y - a.y * b.x;
	}

	static dot(a, b) {
		return a.x * b.x + a.y * b.y;
	}

	static angle(a, b) {
		return Math.acos(Translation.dot(a,b) / (a.norm() * b.norm()));
	}
}

class Waypoint {
    constructor(x, y, vel, radius, desc) {
        this.coordinates = new Translation(x, y);
        this.vel = vel;
        this.radius = radius;
        this.desc = desc;
    }

    draw() {
        this.coordinates.draw();
    }


    /*toString() {
        return "sWaywaypoints.add(new Waypoint("+this.coordinates.x+","+this.coordinates.y+","+this.radius+","+this.velocity+"));";
    }*/
}

class Line {
    constructor(pointA, pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.slope = Translation.diff(pointA.coordinates, pointB.coordinates);


        var scaledA = this.slope.scale(pointA.radius/this.slope.norm() );
        var scaledB = this.slope.scale(pointB.radius/this.slope.norm()).invert();

        this.start = pointA.coordinates.translate(scaledA);
        this.end = pointB.coordinates.translate(scaledB);
    }

    draw() {
		var color = "#2dc65b";
        ctx.strokeStyle = color;
        ctx.beginPath();
        ctx.moveTo(this.start.getX(), this.start.getY());
        ctx.lineTo(this.end.getX(), this.end.getY());

        ctx.strokeStyle = color;
		ctx.lineWidth = 3;

        ctx.stroke();
    }

    static intersect(a, b, c, d) {
        var diffA = Translation.diff(a, b);
        var diffC = Translation.diff(c, d);
        var slopeA = diffA.y/diffA.x;
        var slopeC = diffC.y/diffC.x;
        var y_intA = -slopeA * a.x + a.y;
        var y_intC = -slopeC * c.x + c.y;
        var x = (y_intC - y_intA)/(slopeA - slopeC);
        var y = (slopeA * x) + y_intA;
        return new Translation(x,y);
    }

}

class Arc {
    constructor (lineA, lineB) {
        this.lineA = lineA;
        this.lineB = lineB;
        var perpA = lineA.end.translate(lineA.slope.perp());
        var perpB = lineB.start.translate(lineB.slope.perp());
        this.center = Line.intersect(lineA.end, perpA, lineB.start, perpB);
        this.radius = Translation.diff(lineA.end, this.center).norm();
    }

    draw() {
        var sTrans = Translation.diff(this.center, this.lineA.end);
        var eTrans = Translation.diff(this.center, this.lineB.start);
        var sAngle, eAngle;

/*      draws the center of arcs
        if(Translation.diff(this.lineA.end, this.lineB.start).norm() > 0) {
            this.center.draw("blue");
        }
        */

        if(Translation.cross(sTrans, eTrans) > 0) {
            eAngle = -Math.atan2(sTrans.y, sTrans.x);
            sAngle = -Math.atan2(eTrans.y, eTrans.x);
        } else {
            sAngle = -Math.atan2(sTrans.y, sTrans.x);
            eAngle = -Math.atan2(eTrans.y, eTrans.x);
        }

        ctx.strokeStyle="white";
        ctx.beginPath();
        ctx.arc(this.center.getX(), this.center.getY(), this.radius*ftToPixelsScale, sAngle, eAngle);

        ctx.stroke();
    }
/*
    fill() {
        this.lineA.fill();
        this.lineB.fill();
        var sTrans = Translation.diff(this.center, this.lineA.end);
        var eTrans = Translation.diff(this.center, this.lineB.start);
        var sAngle = (Translation.cross(sTrans, eTrans) > 0) ? sTrans.angle : eTrans.angle;
        var angle = Translation.angle(sTrans, eTrans);
        var length = angle * this.radius;
        for(var i=0; i<length; i+=this.radius/100) {
            //drawRotatedRect(this.center.translate(new Translation(this.radius*Math.cos(sAngle-i/length*angle),-this.radius*Math.sin(sAngle-i/length*angle))), robotHeight, robotWidth, sAngle-i/length*angle+Math.PI/2, null, pathFillColor, true);
        }
    }
    */

    getTurnAngle() {    //?
        return sAngle - eAngle;
    }
}

function deletePoint(index) {
    $('tr')[index].remove();
}

function addPoint(cx, cy) {
    prevPoint = waypoints[waypoints.length-1].coordinates;
	$('tbody').append('<tr>'
		+'<td class="hoverable"><input class="x number_cell" value="'+(cx)+'"></td>'
		+'<td class="hoverable"><input class="y number_cell" value="'+(cy)+'"></td>'
		+'<td class="hoverable"><input class="vel number_cell" value="10"></td>'
		+'<td class="hoverable"><input class="radius number_cell" value="0"></td>'
		+'<td class="hoverable"><input class="desc" placeholder="Description"></td>'
		+'<td><button class="delete">Delete</button></td>'
		+'</tr>'
		);
	addEventListeners();
    update();
}

function invertField() {
    if (!isFlipped) {
        ctx.drawImage(imageFlipped, 0, 0, width, height);
        isFlipped = true;
    }
    else{
        ctx.drawImage(image, 0, 0, width, height);
        isFlipped = false;
    }
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

    row = $($('tbody').children('tr')[waypoints.length]);

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
            $(this).parent().find('.hoverable').css("background-color", "#f5f5f5");
        },
        function() {
            $(this).parent().find('.hoverable').css("background-color", "white");
        }
    )
}

function update() {
    ctx.clearRect(0, 0, width, height);
    if(isFlipped) {
        ctx.drawImage(imageFlipped, 0, 0, width, height);
    } else {
        ctx.drawImage(image, 0, 0, width, height);
    }
    waypoints = [];
    $('tbody').children('tr').each(function() {
        var row = $(this);
        var x = parseFloat($(row.find('.x')).val());
        var y = parseFloat($(row.find('.y')).val());
        var vel = parseFloat($(row.find('.vel')).val()) || 0;
        var desc = $(row.find('.desc')).val();
        var radius = parseFloat($(row.find('.radius')).val()) || 0;
        waypoints.push(new Waypoint(x, y, vel, radius, desc));
    })
    for (var point in waypoints) {
        waypoints[point].coordinates.draw();
        if (point > 0) {
            var line = new Line(waypoints[point], waypoints[point - 1]);
            line.draw();
            if (point > 1) {
                var line1 = new Line(waypoints[point], waypoints[point - 1]);
                var line2 = new Line(waypoints[point - 1], waypoints[point - 2]);
                var arc = new Arc(line1, line2);
                arc.draw();
            }
        }
    }
}

function fieldClicked(event){
    cx = Math.round((event.offsetX/ftToPixelsScale)*10)/10;
    cy = Math.round((30-event.offsetY/ftToPixelsScale)*10)/10;
    addPoint(cx, cy);
}

function chooseStart(position) {
    startPos = startPositions[position];
    startX = startPos[0];
    startY = startPos[1];
    waypoints.push(new Waypoint(startX, startY, 0, 0, ""));
    $($('tbody').children('tr')[0]).find('.x').val(startX);
    $($('tbody').children('tr')[0]).find('.y').val(startY);
    update();
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
    imageFlipped = new Image();
    imageFlipped.src = 'PowerUpFieldReversed.PNG';
    image.onload = function(){
        ctx.drawImage(image, 0, 0, width, height);
        var field = document.getElementById('field');
        field.addEventListener('mousedown', fieldClicked, false);
        update();
    }
	addEventListeners();
}
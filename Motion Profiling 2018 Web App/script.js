var inchesToPixelsScale = 14 / 12;
var fieldWidth = 74 * 12;
var fieldHeight = 30 * 12;
var robotWidth = 34;
var robotHeight = 55.008;
var width = fieldWidth * inchesToPixelsScale; //in pixels
var height = fieldHeight * inchesToPixelsScale; //in pixels
var pointRadius = 10; //in pixels
var modalText = "";
var modalPurePursuitText = "";
var count = 0;
var countToString = "";
var ctx;
var image;
var imageFlipped;
var isFlipped = false;
var modalTitle = "";

var startPositions = {
    "center": [10 * 12, 14 * 12],
    "left": [10 * 12, 24 * 12],
    "right": [10 * 12, 6 * 12],
}

var kEpsilon = 1E-9;
var waypoints = [];

$(function() {
    $('.modal').fadeOut(0);
    $('body').fadeOut(0);
});

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
		return this.x*inchesToPixelsScale;
	}

	getY() {
		return height - this.y*inchesToPixelsScale;
	}

	getAngle() {
		return Math.atan2(-this.y, this.x);
	}

	draw(color) {
		var color = color || "#f72c1c";
		ctx.beginPath();
		ctx.lineWidth = 0;
		ctx.arc(this.getX(), this.getY(), pointRadius, 0, 2*Math.PI, false);
		ctx.fillStyle = color;
		ctx.strokeStyle = color;
		ctx.fill();
		//ctx.stroke();
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

    draw(color) {
        this.coordinates.draw(color);
    }
}

class Line {
    constructor(pointA, pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.slope = Translation.diff(pointA.coordinates, pointB.coordinates);

        var scaledA = this.slope.scale(pointA.radius/this.slope.norm());
        var scaledB = this.slope.scale(pointB.radius/this.slope.norm()).invert();

        this.start = pointA.coordinates.translate(scaledA);
        this.end = pointB.coordinates.translate(scaledB);
        this.length = Translation.diff(this.start, this.end).norm();
    }

    checkValid() {
        if(Translation.diff(this.end, this.pointB.coordinates).norm() > this.slope.norm()){
            return false;
        }
        else if(Translation.diff(this.pointA.coordinates, this.start).norm() > this.slope.norm()){
            return false;
        }
        return true;
    }

    draw(color) {
		var color = color || "#2dc65b";
        ctx.strokeStyle = color;
        ctx.beginPath();
        ctx.moveTo(this.start.getX(), this.start.getY());
        ctx.lineTo(this.end.getX(), this.end.getY());

        ctx.strokeStyle = color;
		ctx.lineWidth = robotWidth;

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
        var diffAC = Translation.diff(a, c);
        if (diffC.x == 0) {
            x = c.x;
            y = a.y + slopeA * diffAC.x;
        }
        if (diffA.x == 0) {
            x = a.x;
            y = c.y - slopeC * diffAC.x;
        }
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

        this.sTrans = Translation.diff(this.center, this.lineA.end);
        this.eTrans = Translation.diff(this.center, this.lineB.start);
        this.sAngle, this.eAngle;
        this.clockwise;

        if(Translation.cross(this.sTrans, this.eTrans) > 0) {
            this.eAngle = -Math.atan2(this.sTrans.y, this.sTrans.x);
            this.sAngle = -Math.atan2(this.eTrans.y, this.eTrans.x);
            this.clockwise = -1;
        } else {
            this.sAngle = -Math.atan2(this.sTrans.y, this.sTrans.x);
            this.eAngle = -Math.atan2(this.eTrans.y, this.eTrans.x);
            this.clockwise = 1;
        }
    }

    draw(color) {
        ctx.strokeStyle="white" || color;
        ctx.beginPath();
        ctx.arc(this.center.getX(), this.center.getY(), this.radius*inchesToPixelsScale, this.sAngle, this.eAngle);
        ctx.lineWidth = robotWidth;
        ctx.stroke();
    }

    getDirection() {
        return this.clockwise;
    }

    getTurnAngle() {
        var angle = ((this.eAngle - this.sAngle)*180)/Math.PI * this.getDirection();
        if (angle < -180) {
            angle += 360;
        } else if (angle > 180) {
            angle -= 360;
        }
        return angle;
    }

    length() {
        return Math.PI * this.radius * (this.getTurnAngle()/180);
    }

}

function deletePoint(index) {
    $('tr')[index].remove();
}

function getGradient(min, max, curr) {
    var startColor = [255, 255, 0];
    var endColor = [255, 0, 0];
    var currColor = [0, 0, 0];
    var spot = (curr - min)/(max - min);
    for (var i in startColor) {
        currColor[i] = startColor[i] + (endColor[i]-startColor[i]) * spot;
    }
    return "rgb("+currColor.toString()+")";
}

function addPoint(cx, cy) {

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

    $(".trajectoryName").keyup(function() {
        modalTitle = $(this).val();
    })

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
    modalText = "double currVel = 0.0; \n";
    modalPurePursuitText = "";
    waypointsString = "";
    for (var point in waypoints) {
        var angle = 0;
        waypoints[point].coordinates.draw(getGradient(0, 100, waypoints[point].vel));
        if (point > 1) {
            var line1 = new Line(waypoints[point], waypoints[point - 1]);
            var line2 = new Line(waypoints[point - 1], waypoints[point - 2]);
            var arc = new Arc(line1, line2);
            arc.draw();
            angle = arc.getTurnAngle();
            var radius = arc.radius;
            modalText += "runAction(new FollowArcTrajectoryAction(";
            modalText += "currVel, " + waypoints[point].vel + ", ";
            modalText += (Math.round(radius)) + ", " + (Math.round(angle)) + "));";
            if(waypoints[point-1].desc != ""){
                modalText += ("   //" + waypoints[point-1].desc);
            }
            modalText += "\ncurrVel = DriveTrain.getInstance().getAverageVelocity();"
            modalText += "\n";
        }
        var updateCount = true;
        if (point > 0) {
            var line = new Line(waypoints[point - 1], waypoints[point]);
            line.draw();
            var distance = line.length;
            modalText += "runAction(new FollowTrajectoryAction(";
            modalText += "currVel, " + waypoints[point].vel + ", ";
            modalText += (Math.round(distance)) + ", " + (Math.round(angle)) + "));"
            if(point < waypoints.length - 1) {
                modalText += "\ncurrVel = DriveTrain.getInstance().getAverageVelocity();"
            }
            modalText += "\n";
            if (!line.checkValid()) {
                $($('tbody').children('tr')[point - 1]).addClass('redBox');
            } else {
                $($('tbody').children('tr')[point - 1]).removeClass('redBox');
            }
        }
        var countToString = String.fromCharCode(97+parseInt(point));
        var coordinates = waypoints[point].coordinates;
        modalPurePursuitText += "PathGenerator.Waypoint " + countToString + " = new PathGenerator.Waypoint(";
        modalPurePursuitText += coordinates.x + ", " + coordinates.y + ", " + (Math.round(waypoints[point].radius)) + ", " + waypoints[point].vel + ");";
        if(waypoints[point].desc != ""){
            modalPurePursuitText += ("   //" + waypoints[point].desc);
        }
        modalPurePursuitText += "\n";
        waypointsString += countToString;
        if (point < waypoints.length - 1) {
            waypointsString += ", ";
        }
    }
    modalPurePursuitText += "Path path = PathGenerator.generate(Arrays.asList(" + waypointsString;
    modalPurePursuitText += ")); \n";
    modalPurePursuitText += "runAction(new PurePursuitAction(path));";
}

function fieldClicked(event){
    cx = Math.round((event.offsetX/inchesToPixelsScale));
    cy = Math.round((fieldHeight-event.offsetY/inchesToPixelsScale));
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

function displayConfiguration() {
    $('.modal').fadeIn(500);
    $("#modalTitle").html(modalTitle);
    $("#pathCode").val(modalText);
}

function displayPurePursuitConfiguration() {
    $('.modal').fadeIn(500);
    $("#modalTitle").html(modalTitle);
    $("#pathCode").val(modalPurePursuitText);
}

function hideConfiguration() {
    $('.modal').fadeOut(500);
}

function copyToClipboard() {
    $('#pathCode').select();
    document.execCommand("Copy");
}

function init() {
	$('#field').css("width", width);
	hideConfiguration();
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
        $('body').fadeIn(0);
    }
	addEventListeners();
}
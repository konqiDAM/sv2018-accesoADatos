//Jairo Pérez Sánchez

use alquiler;

db.vehiculo.save(
	[
		{matricula:"1111 AAA",
		marca:"Fiat",
		modelo:"500",
		grupo:"turismo",
		precio:21.58,
		extras:[
			{extra:"Turbo"},
			{extra:"Alerones"}
		]},
		{matricula:"2222 BBB",
		marca:"Fiat",
		modelo:"Ducato",
		grupo:"furgoneta",
		precio:53.22,
		volumen:7500},
		{matricula:"3333 CCC",
		marca:"KIA",
		modelo:"Sportage",
		grupo:"todoterreno",
		precio:46.28}
	]
);

db.vehiculo.find(
	{precio:{$lt:40}}
);

db.cliente.save(
	[
		{dni:"1111A",
		nombre:"Jairo",
		apellidos:"Perez Sanchez",
		direccion:[
			{linea:"Lillo Juan, 128"},
			{linea:"03690 San Vicente del Raspeig"}
		]},
		{dni:"2222B",
		nombre:"Nacho",
		apellidos:"Dios del universo",
		direccion:[
			{linea:"C/ 13, 23"},
			{linea:"piso 3, puerta izquierda"},
			{linea:"03690 San Vicente del Raspeig"}
		]}
	]
);

db.cliente.find(
	{"direccion.linea":"03690 San Vicente del Raspeig"}
);

db.cliente.save(
	{dni:"3333C",
	nombre:"Pedro",
	apellidos:"El de al lado",
	direccion:[]}
);

db.cliente.find(
	{direccion:{$size:0}}
);

db.cliente.remove(
	{dni:"3333C"}
);

db.prestamos.save(
	[
		{matricula:"1111 AAA",
		dni:"1111A",
		fechaSalida:ISODate("2019-02-07T10:30:00Z"),
		fechaDevolucion:ISODate("2019-02-12T10:23:00Z"),
		kmIniciales:2500,
		kmFinales:3243,
		importe:200.87,
		incidencias:[]},
		{matricula:"1111 AAA",
		dni:"2222A",
		fechaSalida:ISODate("2019-03-07T10:30:00Z"),
		importe:300.87,
		incidencias:[
			{incidencia:"Daño puerta 3"}
		]},
		{matricula:"2222 BBB",
		dni:"1111A",
		fechaSalida:ISODate("2019-02-07T10:30:00Z"),
		fechaDevolucion:ISODate("2019-02-12T10:23:00Z"),
		kmIniciales:4500,
		kmFinales:6243,
		importe:500.87,
		incidencias:[
			{incidencia:"Rueda repuesto pinchada"}
		]}
	]
);

